import java.util.Collection;
import java.util.Random;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SuperAgent implements Agent
{
	private Stack<String> actions;
	
	public class State
	{
		short dirts;
		short posx;
		short posy;
		char orientation; //from 0-3 use modulo 4 when turning
		boolean on;
	}
	private State environment;
	private int homex;
	private int homey;
	private int sizex;
	private int sizey;
	private char[] field;
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
    	environment = new State();
    	environment.dirts = 0;
		Pattern perceptNamePattern = Pattern.compile("\\(\\s*([^\\s]+).*");
		for (String percept:percepts) {
			Matcher perceptNameMatcher = perceptNamePattern.matcher(percept);
			if (perceptNameMatcher.matches()) {
				String perceptName = perceptNameMatcher.group(1);
				if (perceptName.equals("HOME")) {
					Matcher m = Pattern.compile("\\(\\s*HOME\\s+([0-9]+)\\s+([0-9]+)\\s*\\)").matcher(percept);
					if (m.matches()) {
						System.out.println("robot is at " + m.group(1) + "," + m.group(2));
						this.homex = Integer.parseInt(m.group(1));
						this.homey = Integer.parseInt(m.group(2));
						this.environment.posx = Short.parseShort(m.group(1));
						this.environment.posy = Short.parseShort(m.group(2));
					}
				}
				else if (perceptName.equals("ORIENTATION")) {
					Matcher m = Pattern.compile("\\(\\s*ORIENTATION\\s+(\\w)\\)").matcher(percept);
					if (m.matches()) {
						System.out.println("Orientation is: " + m.group(1));
						this.environment.orientation = m.group(1).charAt(0);
					}
				}
				else if (perceptName.equals("SIZE")) {
					Matcher m = Pattern.compile("\\(\\s*SIZE\\s+([0-9]+)\\s+([0-9]+)\\s*\\)").matcher(percept);
					if (m.matches()) {
						System.out.println("Size is " + m.group(1) + "," + m.group(2));
						this.sizex = Integer.parseInt(m.group(1));
						this.sizey = Integer.parseInt(m.group(2));
						this.field = new char[sizex * sizey];
					}
				} else {
					System.out.println("other percept:" + percept);
				}
				
			} else {
				System.err.println("strange percept that does not match pattern: " + percept);
			}
		}
		for (String percept:percepts) {
			Matcher perceptNameMatcher = perceptNamePattern.matcher(percept);
			if (perceptNameMatcher.matches()) {
				String perceptName = perceptNameMatcher.group(1);
				if (perceptName.equals("AT")) {
					Matcher m = Pattern.compile("\\(\\s*AT DIRT\\s+([0-9]+)\\s+([0-9]+)\\s*\\)").matcher(percept);
					if (m.matches()) {
						System.out.println("Dirt is at " + m.group(1) + "," + m.group(2));
						this.environment.dirts++;
						this.field[(Integer.parseInt(m.group(1)) - 1) + (Integer.parseInt(m.group(2)) - 1) * sizey] = 'D';
					}
					m = Pattern.compile("\\(\\s*AT OBSTACLE\\s+([0-9]+)\\s+([0-9]+)\\s*\\)").matcher(percept);
					if (m.matches()) {
						System.out.println("Obstacle is at " + m.group(1) + "," + m.group(2));
						this.field[(Integer.parseInt(m.group(1)) - 1) + (Integer.parseInt(m.group(2)) - 1) * sizey] = 'O';
					}
				} else {
					System.out.println("other percept:" + percept);
				}
				
			} else {
				System.err.println("strange percept that does not match pattern: " + percept);
			}
		}
		System.out.println("home: " + this.homex + "," + this.homey + "\nsizex: " + this.sizex + "," + this.sizey);
		System.out.println(environment.dirts);
		System.out.println(environment.posx);
		System.out.println(environment.posy);
		System.out.println(environment.orientation);
		System.out.println(environment.on);
		field[(homey - 1)*sizey + homex - 1] = 'H';
		for (int i = 0; i < sizey; i++)
		{
			for (int j = 0; j < sizex; j++)
			{
				System.out.print(field[i*sizey + j] + " ");
				
			}
			System.out.print("\n");
		}
    }

    public String nextAction(Collection<String> percepts) {
		return "";
	}
    private void countDirt()
    {
    	
    }
}
