import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;

public class HeuristicSearch {

	private Node root;
	private int sizeOfFrontier;
	private int expansionCount;
	private HashMap<Integer, Integer> visitedStates;
	
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
			return new int[] { N, W + 1, E + 1};
		} 
		else if(state.orientation == 'E' ) 
		{
			return new int[] { E, N + 1, S + 1};
		} 
		else if(state.orientation == 'S' ) 
		{
			return new int[] { S, E + 1, W + 1};
		} 
		else 
		{
			return new int[] { W, S + 1, N + 1};
		} 
	}
	
	/**
	 * Function used to determine if a dirt is reachable 
	 * @param dirtPoints
	 * @param dirts
	 * @param obstacles
	 * @param size
	 * @param home
	 */
	public void reachableDirt(Point[] dirtPoints, boolean[] dirts, boolean[][] obstacles, Point size, Point home)
	{
		Node[] reachableDirt = new Node[dirtPoints.length];
		for(int i = 0; i < dirtPoints.length; i++)
		{
			reachableDirt[i] = null;
			Queue<Node> que = new ArrayDeque<Node>();
			que.add(root);
			visitedStates = new HashMap<Integer, Integer>();
			Node currNode;
			dirts[i] = true;
			while(!que.isEmpty())
			{
				Node node = que.remove();
				if(node.state.dirtGoalState(node.state, dirtPoints[i]))
				{
					dirts[i] = false;
					reachableDirt[i] = node;
					break;
				}
				node.state.listOfActions(node, dirtPoints, obstacles, size);	// void function
				for(String string:node.state.actions)
				{
					if(string.equals(""))
					{
						continue;
					}
					currNode = new Node(node.state.act(string), node);
					int hash = node.state.hashState(currNode.state);
					if(visitedStates.containsKey(hash))
					{
						continue;
					}
					visitedStates.put(hash, 0);
					que.add(currNode);
				}
			}
			
		}
	}
	
	
	// **************************************************************************************************************************
	// *********************************************** A* Search ****************************************************************
	// **************************************************************************************************************************
	public Node AstarSearch(Point[] dirtPoints, boolean[][] obstacles, Point size, Point home)
	{
		reachableDirt(dirtPoints, root.state.dirts, obstacles, size, home);
		PriorityQueue<Node> pq = new PriorityQueue<Node>( new Comparator<Node>() {
		// over writes the compare function for Node to include the heuristic cost
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
		visitedStates = new HashMap<Integer, Integer>();
		Node currNode;
		while(!pq.isEmpty())
		{
			Node node = pq.poll();
			if(node.state.searchGoalState(node.state, home))
			{
				System.out.println("Shit's done  " + sizeOfFrontier);
				System.out.println("expansion count: " + expansionCount);
				return node;
			}
			int closestDist = 10000;
			Point closestDirt = null;
			expansionCount++;
			for(int i = 0; i < dirtPoints.length; i++)
			{
				if(!node.state.dirts[i])
				{
					int dist = dirtPoints[i].manhatanDist(new Point(node.state.posx, node.state.posy));
					if(dist < closestDist)
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
				int hash = node.state.hashState(currNode.state);
				if(visitedStates.containsKey(hash))
				{
					continue;
				}
				visitedStates.put(hash, currNode.state.pathCost + currNode.state.heuristicCost);
				pq.add(currNode);
				if(sizeOfFrontier < pq.size())
				{
					sizeOfFrontier = pq.size();
				}
				if(string == "SUCK")
				{
					expansionCount--;
					break;
				}
			}
		}
		return null;
	}
	
	public int[] dirtRemainingHeuristics(State state) 
	{
		int dirtCount = 0;
		for(boolean b:state.dirts) 
		{
			if(!b) dirtCount++;
		}
		return new int[] {dirtCount, dirtCount, dirtCount};
	}
}
