import java.util.ArrayDeque;
import java.util.Queue;

public class BFS {
	
	Node root;
	Queue<Node> que;
	public BFS(State state)
	{
		root = new Node(state, new String[] {"TURN_ON"});
		que = new ArrayDeque<Node>();
		que.add(root);
	}
	public boolean search(Point[] dirts, boolean[][] obstacles, Point size)
	{
		while(!que.isEmpty())
		{
			Node node = que.remove();
			if(node.state.goalState(node.state))
			{
				return true;
			}
			System.out.println("x:" + node.state.posx + " y:" + node.state.posy + " O:" + node.state.orientation);

			Node[] nodes = new Node[node.availableActions.length];
			for(int i = 0; i < nodes.length; i++)
			{
				nodes[i] = new Node(node.state.act(node.availableActions[i], dirts, obstacles, size), node);
				que.add(nodes[i]);
			}
			node.children = nodes;
		}
		return false;
	}	
}
