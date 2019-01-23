import java.util.ArrayDeque;
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
		while(!que.isEmpty())
		{
			Node node = que.remove();
			if(node.state.goalState(node.state, home))
			{
				return node;
			}
			//System.out.println("x:" + node.state.posx + " y:" + node.state.posy + " O:" + node.state.orientation);
			node.state.listOfActions(node, dirts, obstacles, size);
			
			for(String string:node.state.actions)
			{
				//System.out.println("action:" + string);
				que.add(new Node(node.state.act(string), node));
			}
		}
		return null;
	}	
}
