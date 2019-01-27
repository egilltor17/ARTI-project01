import java.util.Collection;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SuperAgent implements Agent
{
	private Stack<String> actions;
	private State environment;
	public Point home;
	public Point size; // the greatest point in the field
	public Point[] dirtPoints;
	public boolean[][] obstacles;
	public int dirtsCount;
	public char[][] field;
	Stack<Point> locations;
	Stack<Character> orientation;
	Point lastField;
	/*
		init(Collection<String> percepts) is called once before you have to select the first action. Use it to find a plan. Store the plan and just execute it step by step in nextAction.
	*/

    public void init(Collection<String> percepts) {
		/*
			Possible percepts are:
			- "(SIZE x y)" denoting the size of the environment, where x,y are integers
			- "(HOME x y)" with x,y >= 1 denoting the initial position of the robot
			- "(ORIENTATION o)" with o in {"NORTH", "SOUTH", "EAST", "WEST"} denoting the initial orientation of the robot
			- "(AT o x y)" with o being "DIRT" or "OBSTACLE" denoting the position of a dirt or an obstacle
			Moving north increases the y coordinate and moving east increases the x coordinate of the robots position.
			The robot is turned off initially, so don't forget to turn it on.
		*/
    	dirtsCount = 0;
    	environment = new State();
		Pattern perceptNamePattern = Pattern.compile("\\(\\s*([^\\s]+).*");
		for (String percept:percepts) {
			Matcher perceptNameMatcher = perceptNamePattern.matcher(percept);
			if (perceptNameMatcher.matches()) {
				String perceptName = perceptNameMatcher.group(1);
				if (perceptName.equals("HOME")) {
					Matcher m = Pattern.compile("\\(\\s*HOME\\s+([0-9]+)\\s+([0-9]+)\\s*\\)").matcher(percept);
					if (m.matches()) {
						this.obstacles = new boolean[Integer.parseInt(m.group(2))][Integer.parseInt(m.group(1))];
						this.environment.posx = Short.parseShort(m.group(1));
						this.environment.posy = Short.parseShort(m.group(2));
						this.home = new Point(Integer.parseInt(m.group(1)),Integer.parseInt(m.group(2)));
					}
				}
				else if (perceptName.equals("ORIENTATION")) {
					Matcher m = Pattern.compile("\\(\\s*ORIENTATION\\s+(\\w+)\\)").matcher(percept);
					if (m.matches()) {
						this.environment.orientation = m.group(1).charAt(0);
					}
				}
				else if (perceptName.equals("SIZE")) {
					Matcher m = Pattern.compile("\\(\\s*SIZE\\s+([0-9]+)\\s+([0-9]+)\\s*\\)").matcher(percept);
					if (m.matches()) {
						this.size = new Point(Integer.parseInt(m.group(1)),Integer.parseInt(m.group(2)));
						this.obstacles = new boolean[size.y][size.x];
						this.field = new char[size.y][size.x];
						lastField = new Point(home.x, home.y);
					}
				}
				else if (perceptName.equals("AT")) {
					Matcher m = Pattern.compile("\\(\\s*AT DIRT\\s+([0-9]+)\\s+([0-9]+)\\s*\\)").matcher(percept);
					if (m.matches()) {
						this.dirtsCount++;
					}
				}
			} else {
				System.err.println("strange percept that does not match pattern: " + percept);
			}
		}
		this.dirtPoints = new Point[this.dirtsCount];
		int i = 0;
		for (String percept:percepts) {
			Matcher perceptNameMatcher = perceptNamePattern.matcher(percept);
			if (perceptNameMatcher.matches()) {
				String perceptName = perceptNameMatcher.group(1);
				if (perceptName.equals("AT")) {
					Matcher m = Pattern.compile("\\(\\s*AT DIRT\\s+([0-9]+)\\s+([0-9]+)\\s*\\)").matcher(percept);
					if (m.matches()) {
						//System.out.print(dirtPoints);
						this.dirtPoints[i] = new Point(Integer.parseInt(m.group(1)),Integer.parseInt(m.group(2)));
						field[dirtPoints[i].y - 1][dirtPoints[i].x - 1] = 'D';
						i++;
					}
					m = Pattern.compile("\\(\\s*AT OBSTACLE\\s+([0-9]+)\\s+([0-9]+)\\s*\\)").matcher(percept);
					if (m.matches()) {
						this.obstacles[Integer.parseInt(m.group(2)) - 1][Integer.parseInt(m.group(1)) - 1] = true;
						field[Integer.parseInt(m.group(2)) - 1][Integer.parseInt(m.group(1)) - 1] = 'X';
					}
				}
			} else {
				System.err.println("strange percept that does not match pattern: " + percept);
			}
		}
		field[home.y - 1][home.x - 1] = environment.orientation;
		System.out.println();
		printField();
		this.environment.dirts = new boolean[dirtsCount];
		this.environment.lastAction = "TURN_ON";
		this.environment.pathCost = 0;
		/*System.out.println("height: "+ size.x);
		System.out.println("with: " + size.y);
		System.out.println("xPos: " + environment.posx);
		System.out.println("yPos: " + environment.posy);
		System.out.println("Ort: " + environment.orientation);
		for(int k = 0; k < dirtsCount; k++)
		{
			System.out.println(environment.dirts[k]);
			//System.out.println(dirt[k].x + " " + dirt[k].y);
		}*/
		BlindSearch search = new BlindSearch(environment);
		Node node = search.BFS(dirtPoints, obstacles, size, home);
		//HeuristicSearch search = new HeuristicSearch(environment);
		//Node node = search.AstarSearch(dirtPoints, obstacles, size, home);
		System.out.println("node: " + node);
		actions = new Stack<String>();
		orientation = new Stack<Character>();
		locations = new Stack<Point>();
		while(node != null)
		{
			actions.push(node.state.lastAction);
			orientation.push(node.state.orientation);
			locations.push(new Point(node.state.posx, node.state.posy));
			node = node.parent;
		}
    }
    private void printField()
    {
		System.out.println();
    	for(int y = size.y - 1; 0 <= y; y--)
		{
			for(int k = 0; k < size.x; k++)
			{
				System.out.print(field[y][k]);
			}
			System.out.println();
		}
		System.out.println();
    }
    public String nextAction(Collection<String> percepts) {
    	if(actions == null || actions.isEmpty())
    	{
    		System.out.print("Stack is empty");
    		return "TURN_OFF";
    	}
    	Point location = locations.pop();
    	field[lastField.y - 1][lastField.x - 1] = ' ';
    	field[location.y - 1][location.x - 1] = orientation.pop();
    	lastField = location;
    	printField();
    	return actions.pop();
	}
}
