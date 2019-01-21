import java.util.ArrayDeque;
import java.util.Queue;

public class BFS {
	
	Node root;
	Queue<Node> que;
	public BFS(State state)
	{
		System.out.println("Hello");
		root = new Node(state);
		que = new ArrayDeque<Node>();
		System.out.println("Hello");
		que.add(root);
		System.out.println("Hello");
	}
	public boolean search()
	{
		System.out.println("Hello");
		while(!que.isEmpty())
		{
			Node node = que.remove();
			if(node.state.goalState(node.state))
			{
				return true;
			}
			System.out.println("x: " + node.state.posx + " y:" + node.state.posy);
			
			String[] actions = node.state.listOfActions();
			Node[] nodes = new Node[actions.length];
			for(int i = 0; i < actions.length; i++)
			{
				
				nodes[i] = new Node(node.state.act(actions[i]), node);
				System.out.println("Node: " + nodes[i].state.orientation);
				que.add(nodes[i]);
			}
			node.children = nodes;
		}
		return false;
	}
	
}
