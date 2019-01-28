import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

public class BlindSearch {
	
	private Node root;
	private int sizeOfFrontier;							// used for measurements
	private int expansionCount;							// used for measurements
	private HashMap<Integer, Integer> visitedStates;	// Hash map with the state hashed as the key and the (pathCost + heuristicCost) as the value
	
	public BlindSearch(State state)
	{
		sizeOfFrontier = 0;
		expansionCount = 0;
		root = new Node(state);
	}
	
	@SuppressWarnings("unused")
	private boolean checkVisited(Node currNode)
	{
		int hash = currNode.state.hashState(currNode.state);
		if(visitedStates.containsKey(hash))
		{
			return true;
		}
		visitedStates.put(hash, 0);
		return false;
	}
	
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
	// *********************************************** Breadth First Search *****************************************************
	// **************************************************************************************************************************
	public Node BFS(Point[] dirtPoints, boolean[][] obstacles, Point size, Point home)
	{
		reachableDirt(dirtPoints, root.state.dirts, obstacles, size, home);
		Queue<Node> que = new ArrayDeque<Node>();
		que.add(root);
		visitedStates = new HashMap<Integer, Integer>();
		Node currNode;
		while(!que.isEmpty())
		{
			Node node = que.remove();
			if(node.state.searchGoalState(node.state, home))
			{
				System.out.println("Size of frontier: " + sizeOfFrontier);
				System.out.println("expansion count: " + expansionCount);
				return node;
			}
			expansionCount++;
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
				if(sizeOfFrontier < que.size())
				{
					sizeOfFrontier = que.size();
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
	
	
	
	// **************************************************************************************************************************
	// *********************************************** Depth First Search *******************************************************
	// **************************************************************************************************************************
	public Node DFS(Point[] dirtPoints, boolean[][] obstacles, Point size, Point home)
	{
		reachableDirt(dirtPoints, root.state.dirts, obstacles, size, home);
		Stack<Node> stack = new Stack<Node>();
		stack.push(root);
		visitedStates = new HashMap<Integer, Integer>();
		Node currNode = null;
		while(!stack.isEmpty())
		{
			Node node = stack.pop();
			if(node.state.searchGoalState(node.state, home))
			{
				System.out.println("Shit's done  " + sizeOfFrontier);
				return node;
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
				stack.add(currNode);
				if(sizeOfFrontier < stack.size())
				{
					sizeOfFrontier = stack.size();
				}
				if(string == "SUCK")
				{
					break;
				}
			}
		}
		return currNode;
	}
	
	
	
	// **************************************************************************************************************************
	// *********************************************** Uniform Cost Search ******************************************************
	// **************************************************************************************************************************
	public Node UCS(Point[] dirtPoints, boolean[][] obstacles, Point size, Point home)
	{
		reachableDirt(dirtPoints, root.state.dirts, obstacles, size, home);
		PriorityQueue<Node> pq = new PriorityQueue<Node>( new Comparator<Node>() {
		    public int compare(Node n1, Node n2) {
		        if(n1.state.pathCost < n2.state.pathCost)
		        {
		        	return -1;
		        }
		        if(n1.state.pathCost == n2.state.pathCost)
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
				return node;
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
}
