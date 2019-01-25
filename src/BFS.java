import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Queue;

public class BFS {
	
	Node root;
	Queue<Node> que;
	public BFS(State state)
	{
		state.lastAction = "TURN_ON";
		root = new Node(state, new String[] {"TURN_ON"});
		que = new ArrayDeque<Node>();
		que.add(root);
	}
	public Node search(Point[] dirts, boolean[][] obstacles, Point size, Point home)
	{
		HashMap<String, Integer> visitedStates = new HashMap<String, Integer>();
		Node currNode;
		while(!que.isEmpty())
		{
			Node node = que.remove();
			if(node.state.goalState(node.state, home))
			{
				System.out.println("shit's done");
				return node;
			}
			//System.out.println("x:" + node.state.posx + " y:" + node.state.posy + " O:" + node.state.orientation);
			node.state.listOfActions(node, dirts, obstacles, size);
			
			for(String string:node.state.actions)
			{
				currNode = new Node(node.state.act(string), node);
				String str = hashState(node.state);
				if(visitedStates.containsKey(str))
				{
					System.out.print("visited");
					continue;
				}
				visitedStates.put(str, 0);
				que.add(currNode);
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
