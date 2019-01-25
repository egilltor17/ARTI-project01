import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Queue;
import java.util.Stack;
//import java.util.Stack;

public class BlindSearch {
	
	Node root;
	public BlindSearch(State state)
	{
		root = new Node(state);
	}
	public Node[] reachableDirt(Point[] dirtPoints, boolean[][] obstacles, Point size, Point home)
	{
		Node[] reachableDirt = new Node[dirtPoints.length];
		for(int i = 0; i < dirtPoints.length; i++)
		{
			reachableDirt[i] = null;
			Queue<Node> que = new ArrayDeque<Node>();
			que.add(root);
			HashMap<String, Integer> visitedStates = new HashMap<String, Integer>();
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
					currNode = new Node(node.state.act(string), node);
					String str = hashState(currNode.state);
					if(visitedStates.containsKey(str))
					{
						continue;
					}
					visitedStates.put(str, 0);
					que.add(currNode);
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
		HashMap<String, Integer> visitedStates = new HashMap<String, Integer>();
		Node currNode;
		while(!que.isEmpty())
		{
			Node node = que.remove();
			if(node.state.searchGoalState(node.state, home))
			{
				System.out.println("Shit's done");
				return node;
			}
			node.state.listOfActions(node, dirtPoints, obstacles, size);	// void function
			for(String string:node.state.actions)
			{
				currNode = new Node(node.state.act(string), node);
				String str = hashState(currNode.state);
				if(visitedStates.containsKey(str))
				{
					continue;
				}
				visitedStates.put(str, 0);
				que.add(currNode);
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
		HashMap<String, Integer> visitedStates = new HashMap<String, Integer>();
		Node currNode;
		while(!stack.isEmpty())
		{
			Node node = stack.pop();
			if(node.state.searchGoalState(node.state, home))
			{
				System.out.println("Shit's done");
				return node;
			}
			node.state.listOfActions(node, dirtPoints, obstacles, size);	// void function
			for(String string:node.state.actions)
			{
				currNode = new Node(node.state.act(string), node);
				String str = hashState(currNode.state);
				if(visitedStates.containsKey(str))
				{
					continue;
				}
				visitedStates.put(str, 0);
				stack.push(currNode);
			}
		}
		return null;
	}
	public String hashState(State state)
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
	}
}
