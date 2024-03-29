import java.util.Collection;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SuperAgent implements Agent
{
	private Stack<String> actions;				// the path found to the goal state
	private State environment;					// the starting state
	public Point home;							// the starting point 
	public Point size; 							// the greatest point in the field
	public Point[] dirtPoints;					// an array of all dirt points in the field 
	public boolean[][] obstacles;				// an array of all the obstacles in the field 
	public int dirtsCount;						// the number of dirt points
	
	/*
	public char[][] field;						// used to print 
	private Stack<Point> locations;				// used to print	
	private Stack<Character> orientation;		// used to print
	private Point lastField;					// used to print
	*/


    public void init(Collection<String> percepts) {
		/*
			Possible precepts are:
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
						/*	// un-comment to use printField()
						this.field = new char[size.y][size.x];
						lastField = new Point(home.x, home.y);
						*/
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
						this.dirtPoints[i] = new Point(Integer.parseInt(m.group(1)),Integer.parseInt(m.group(2)));
						/*  // un-comment to use printField()
						field[dirtPoints[i].y - 1][dirtPoints[i].x - 1] = 'D';
						*/
						i++;
					}
					m = Pattern.compile("\\(\\s*AT OBSTACLE\\s+([0-9]+)\\s+([0-9]+)\\s*\\)").matcher(percept);
					if (m.matches()) {
						this.obstacles[Integer.parseInt(m.group(2)) - 1][Integer.parseInt(m.group(1)) - 1] = true;
						/* // un-comment to use printField()
						field[Integer.parseInt(m.group(2)) - 1][Integer.parseInt(m.group(1)) - 1] = 'X';
						*/
					}
				}
			} else {
				System.err.println("strange percept that does not match pattern: " + percept);
			}
		}
		/*  // un-comment to use printField()
		field[home.y - 1][home.x - 1] = environment.orientation;
		printField();
		*/
		this.environment.dirts = new boolean[dirtsCount];
		this.environment.lastAction = "TURN_ON";
		this.environment.pathCost = 0;
		
		BlindSearch search = new BlindSearch(environment);
		//Node node = search.BFS(dirtPoints, obstacles, size, home);
		//Node node = search.DFS(dirtPoints, obstacles, size, home);
		Node node = search.UCS(dirtPoints, obstacles, size, home);
		
		//HeuristicSearch search = new HeuristicSearch(environment);
		//Node node = search.AstarSearch(dirtPoints, obstacles, size, home);
		actions = new Stack<String>();
		
		/*  // un-comment to use printFeild()
		orientation = new Stack<Character>();
		locations = new Stack<Point>();
		*/
		
		while(node != null)
		{
			actions.push(node.state.lastAction);
			/*  // un-comment to use printFeild()
			orientation.push(node.state.orientation);
			locations.push(new Point(node.state.posx, node.state.posy));
			*/
			node = node.parent;
		}
		
    }
    
    /*
    // Prints the state of the field
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
    */
    
    // Returns the next action 
    public String nextAction(Collection<String> percepts) {
    	if(actions == null || actions.isEmpty())
    	{
    		return "TURN_OFF";
    	}
    	/*  // un-comment to use printFeild() 
    	Point location = locations.pop();
    	field[lastField.y - 1][lastField.x - 1] = ' ';
    	field[location.y - 1][location.x - 1] = orientation.pop();
    	lastField = location;
    	printField();
    	*/
    	return actions.pop();
	}
}
