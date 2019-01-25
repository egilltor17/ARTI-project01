import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Queue;
import java.util.Stack;
//import java.util.Stack;

public class BlindSearch {
	
	Node root;
	//Stack<Node> que;
	public BlindSearch(State state)
	{
		root = new Node(state);
	}
	public Node BFS(Point[] dirtPoints, boolean[][] obstacles, Point size, Point home)
	{
		Queue<Node> que = new ArrayDeque<Node>();
		que.add(root);
		HashMap<String, Integer> visitedStates = new HashMap<String, Integer>();
		Node currNode;
		while(!que.isEmpty())
		{
			Node node = que.remove();
			if(node.state.goalState(node.state, home))
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
		Stack<Node> stack = new Stack<Node>();
		stack.push(root);
		HashMap<String, Integer> visitedStates = new HashMap<String, Integer>();
		Node currNode;
		while(!stack.isEmpty())
		{
			Node node = stack.pop();
			if(node.state.goalState(node.state, home))
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
