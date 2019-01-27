import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;
//import java.util.Stack;

public class BlindSearch {
	
	Node root;
	int sizeOfFrontier;
	//HashMap<String, Integer> visitedStates;
	HashMap<Integer, Integer> visitedStates;
	public BlindSearch(State state)
	{
		sizeOfFrontier = 0;
		root = new Node(state);
	}
	private boolean checkVisited(Node currNode)
	{
		//String str = hashState(currNode.state);
		int str = hashState(currNode.state);
		if(visitedStates.containsKey(str))
		{
			return true;
		}
		visitedStates.put(str, 0);
		return false;
	}
	public Node[] reachableDirt(Point[] dirtPoints, boolean[][] obstacles, Point size, Point home)
	{
		Node[] reachableDirt = new Node[dirtPoints.length];
		for(int i = 0; i < dirtPoints.length; i++)
		{
			reachableDirt[i] = null;
			Queue<Node> que = new ArrayDeque<Node>();
			que.add(root);
			//visitedStates = new HashMap<String, Integer>();
			visitedStates = new HashMap<Integer, Integer>();
			Node currNode;
			while(!que.isEmpty())
			{
				Node node = que.remove();
				if(node.state.dirtGoalState(node.state, dirtPoints[i]))
				{
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
					//String str = hashState(currNode.state);
					int str = hashState(currNode.state);
					if(visitedStates.containsKey(str))
					{
						continue;
					}
					visitedStates.put(str, 0);
					que.add(currNode);
					if(string == "SUCK")
					{
						break;
					}
				}
			}
		}
		return reachableDirt;
	}
	public Node BFS(Point[] dirtPoints, boolean[][] obstacles, Point size, Point home)
	{
		Node[] dirt = reachableDirt(dirtPoints, obstacles, size, home);
		for(int i = 0; i < dirt.length; i++)
		{
			if(dirt[i] == null)
			{
				System.out.println("dirt is unreachable" + dirtPoints[i].x + " " + dirtPoints[i].y);
				//this dirt is unreachable dont make the bot get it
				root.state.dirts[i] = true;
			}
		}
		Queue<Node> que = new ArrayDeque<Node>();
		que.add(root);
		//visitedStates = new HashMap<String, Integer>();
		visitedStates = new HashMap<Integer, Integer>();
		Node currNode;
		while(!que.isEmpty())
		{
			Node node = que.remove();
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
				//String str = hashState(currNode.state);
				int str = hashState(currNode.state);
				if(visitedStates.containsKey(str))
				{
					continue;
				}
				visitedStates.put(str, 0);
				que.add(currNode);
				if(sizeOfFrontier < que.size())
				{
					sizeOfFrontier = que.size();
				}
				if(string == "SUCK")
				{
					break;
				}
			}
		}
		return null;
	}
	public Node UCS(Point[] dirtPoints, boolean[][] obstacles, Point size, Point home)
	{
		Node[] dirt = reachableDirt(dirtPoints, obstacles, size, home);
		for(int i = 0; i < dirt.length; i++)
		{
			if(dirt[i] == null)
			{
				System.out.println("dirt is unreachable" + dirtPoints[i].x + " " + dirtPoints[i].y);
				//this dirt is unreachable dont make the bot get it
				root.state.dirts[i] = true;
			}
		}
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
			node.state.listOfActions(node, dirtPoints, obstacles, size);	// void function
			for(String string:node.state.actions)
			{
				if(string.equals(""))
				{
					continue;
				}
				currNode = new Node(node.state.act(string), node);
				//String str = hashState(currNode.state);
				int str = hashState(currNode.state);
				if(visitedStates.containsKey(str))
				{
					continue;
				}
				visitedStates.put(str, 0);
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
	public Node DFS(Point[] dirtPoints, boolean[][] obstacles, Point size, Point home)
	{
		Node[] dirt = reachableDirt(dirtPoints, obstacles, size, home);
		for(int i = 0; i < dirt.length; i++)
		{
			if(dirt[i] == null)
			{
				System.out.println("dirt is unreachable" + dirtPoints[i].x + " " + dirtPoints[i].y);
				//this dirt is unreachable dont make the bot get it
				root.state.dirts[i] = true;
			}
		}
		Stack<Node> stack = new Stack<Node>();
		stack.push(root);
		//visitedStates = new HashMap<String, Integer>();
		visitedStates = new HashMap<Integer, Integer>();
		Node currNode;
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
				//String str = hashState(currNode.state);
				int str = hashState(currNode.state);
				if(visitedStates.containsKey(str))
				{
					continue;
				}
				visitedStates.put(str, 0);
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
		return null;
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
		// 0000 0000 0000 0000  XXXX XXXY YYYY YYOO
		int hash = 0;
		for(int i = 0; i < state.dirts.length; i++)
		{
			if(state.dirts[i])
			{
				hash += (int) Math.pow(2, i);
			}
		}
		hash = hash << 16;
		hash += state.posx << 9;
		hash += state.posy << 2;
		if(state.orientation == 'N') hash += 0;
		if(state.orientation == 'E') hash += 1;
		if(state.orientation == 'S') hash += 2;
		if(state.orientation == 'W') hash += 3;
		return hash;
	}
}
