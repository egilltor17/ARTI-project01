import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public class HeuristicSearch {

	Node root;
	int sizeOfFrontier;
	//HashMap<String, Integer> visitedStates;
	HashMap<Integer, Integer> visitedStates;
	public HeuristicSearch(State state)
	{
		root = new Node(state);
	}
	/**
	 * Returns an array with the weighted heuristics for turning left, going straight and turning right
	 * @param state   -  Initial state
	 * @param target  -  Target's location 
	 * @return int[3] -  {LeftWeight, GoWeight, RightWeight}
	 */
	public int[] singleManhatanHeuristic(State state, Point target)
	{	
		int N = target.manhatanDist(new Point(state.posx, state.posy + 1));
		int E = target.manhatanDist(new Point(state.posx + 1, state.posy));
		int S = target.manhatanDist(new Point(state.posx, state.posy - 1));
		int W = target.manhatanDist(new Point(state.posx - 1, state.posy));
		
		if(state.orientation == 'N' ) 
		{
			return new int[] {W + 1, N, E + 1};
		} 
		else if(state.orientation == 'E' ) 
		{
			return new int[] {N + 1, E, S + 1};
		} 
		else if(state.orientation == 'S' ) 
		{
			return new int[] {E + 1, S, W + 1};
		} 
		else 
		{
			return new int[] {S + 1, W, N + 1};
		} 
	}
	
	
	/**
	 * Returns an array with the weighted heuristics for turning left, going straight and turning right
	 * @param state	  -  Current state
	 * @param dirtPos -  Dirt position array from superAgent
	 * @return int[3] -  {LeftWeight, GoWeight, RightWeight}
	 */
	public int[] manhatanHeuristic(State state, Point[] dirtPos, Point homePos)
	{	
		int distFront[] = new int[state.dirts.length];
		int distLeft[]  = new int[state.dirts.length];
		int distRight[] = new int[state.dirts.length];
		boolean noDirtsLeft = true;
		for(boolean b:state.dirts)
		{
			if(!b)
			{
				noDirtsLeft = !b;
				break;
			}
		}
		if(noDirtsLeft)
		{	// Gives heuristic for going home
			int N = homePos.manhatanDist(new Point(state.posx, state.posy + 1));
			int E = homePos.manhatanDist(new Point(state.posx + 1, state.posy));
			int S = homePos.manhatanDist(new Point(state.posx, state.posy - 1));
			int W = homePos.manhatanDist(new Point(state.posx - 1, state.posy));
			if(state.orientation == 'N' ) 
			{
				return new int[] {W + 1, N, E + 1};
			} 
			else if(state.orientation == 'E' ) 
			{
				return new int[] {N + 1, E, S + 1};
			} 
			else if(state.orientation == 'S' ) 
			{
				return new int[] {E + 1, S, W + 1};
			} 
			else 
			{
				return new int[] {S + 1, W, N + 1};
			}
		}
		else
		{	// Gives heuristic to closes non cleaned dirt
			for(int i = 0; i < state.dirts.length; i++) 
			{
				if(state.dirts[i]) 
				{
					distFront[i] = -1;
					distLeft[i]  = -1;
					distRight[i] = -1;
				} 
				else 
				{
					int N = dirtPos[i].manhatanDist(new Point(state.posx, state.posy - 1));
					int E = dirtPos[i].manhatanDist(new Point(state.posx + 1, state.posy));
					int S = dirtPos[i].manhatanDist(new Point(state.posx, state.posy + 1));
					int W = dirtPos[i].manhatanDist(new Point(state.posx - 1, state.posy));
					if(state.orientation == 'N' ) 
					{
						distFront[i] = N;
						distLeft[i]  = W + 1;
						distRight[i] = E + 1;
					} 
					else if(state.orientation == 'E' ) 
					{
						distFront[i] = E;
						distLeft[i]  = N + 1;
						distRight[i] = S + 1;
					} 
					else if(state.orientation == 'S' ) 
					{
						distFront[i] = S;
						distLeft[i]  = E + 1;
						distRight[i] = W + 1;
					} 
					else 
					{
						distFront[i] = W;
						distLeft[i]  = S + 1;
						distRight[i] = N + 1;
					}
				}
			}
			Arrays.sort(distFront);
			Arrays.sort(distLeft);
			Arrays.sort(distRight);
			return new int[] {distLeft[distLeft.length - 1], distFront[distFront.length - 1], distRight[distRight.length - 1]};  // Is not admissible for multiple objectives.
			//return new int[] {distLeft[0], distFront[0], distRight[0]};  // Is not complete for multiple objectives. 
		}
	}
	public Node AstarSearch(Point[] dirtPoints, boolean[][] obstacles, Point size, Point home)
	{
		PriorityQueue<Node> pq = new PriorityQueue<Node>( new Comparator<Node>() {
		public int compare(Node n1, Node n2) {
	        if(n1.state.pathCost + n1.state.heuristicCost < n2.state.pathCost + n2.state.heuristicCost)
	        {
	        	return -1;
	        }
	        if(n1.state.pathCost + n1.state.heuristicCost == n2.state.pathCost + n2.state.heuristicCost)
	        {
	        	return 0;
	        }
	        return 1;
	    }
	});
	pq.add(root);
	//visitedStates = new HashMap<String, Integer>();
	visitedStates = new HashMap<Integer, Integer>();
	Node currNode;
	while(!pq.isEmpty())
	{
		Node node = pq.poll();
		if(node.state.searchGoalState(node.state, home))
		{
			System.out.println("Shit's done  " + sizeOfFrontier);
			return node;
		}
		int closestDist = 1;
		Point closestDirt = null;
		for(int i = 0; i < dirtPoints.length; i++)
		{
			if(!node.state.dirts[i])
			{
				int dist = dirtPoints[i].manhatanDist(new Point(node.state.posx, node.state.posy));
				if(dist > closestDist)
				{
					closestDist = dist;
					closestDirt = dirtPoints[i];
				}
			}
		}
		if(closestDirt == null)
		{
			closestDirt = home;
		}
		int[] moves = singleManhatanHeuristic(node.state, closestDirt);
		//int[] moves = manhatanHeuristic(node.state, dirtPoints, home);
		node.state.listOfActions(node, dirtPoints, obstacles, size);	// void function
		int i = -1;
		for(String string:node.state.actions)
		{
			i++;
			if(string.equals(""))
			{
				continue;
			}
			currNode = new Node(node.state.act(string), node);
			currNode.state.heuristicCost = moves[i];
			//String str = hashState(currNode.state);
			int str = hashState(currNode.state);
			if(visitedStates.containsKey(str))
			{
				continue;
			}
			visitedStates.put(str, currNode.state.pathCost + currNode.state.heuristicCost);
			pq.add(currNode);
			if(sizeOfFrontier < pq.size())
			{
				sizeOfFrontier = pq.size();
			}
			if(string == "SUCK")
			{
				break;
			}
		}
	}
	return null;
	}
	
	public int dirtRemainingHeuristics(State state) 
	{
		int dirtCount = 0;
		for(boolean b:state.dirts) 
		{
			if(!b) dirtCount++;
		}
		return dirtCount;
	}
	/*public String hashState(State state)
	{
		String str = "";
		for(boolean dirt:state.dirts)
		{
			if(dirt)
			{
				str += "1";
			}
			else
			{
				str += "0";
			}
		}
		str += state.posx + "" + state.posy + "" + (int)state.orientation;
		return str;
	}*/
	public int hashState(State state)
	{
		// Hash is now an integer with one bit for each dirt (max 16 dirt),
		// 7 bits for x & y coordinates (max 128 x 128 board) and 2 bits for orientation
		// hash = DDDD DDDD DDDD DDDD  XXXX XXXY YYYY YYOO
		int hash = 0;
		for(int i = 0; i < state.dirts.length; i++) 
		{
			if(state.dirts[i]) hash += 1 << (31 - i);
		}
		hash += state.posx << 9;
		hash += state.posy << 2;
		if(state.orientation == 'N') hash += 0;
		if(state.orientation == 'E') hash += 1;
		if(state.orientation == 'S') hash += 2;
		if(state.orientation == 'W') hash += 3;
		return hash;
	}
	
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub

	}

}
