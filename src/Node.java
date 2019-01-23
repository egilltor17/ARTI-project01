
public class Node{
		private Node parent;
		public Node[] children;
		public State state;
		public String[] availableActions;
		public Node(State state, String[] s)
		{
			this.state = state;
			this.parent = null;
		}
		public Node(State state, Node parent, String[] s)
		{
			this.parent = parent;
			this.state = state;
		}
		public void setChildren(Node[] n)
		{
			children = n;
		}
	}