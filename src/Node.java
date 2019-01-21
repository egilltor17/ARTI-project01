
public class Node{
		private Node parent;
		public Node[] children;
		public State state;
		public Node(State state)
		{
			this.state = state;
		}
		public Node(State state, Node parent)
		{
			this.parent = parent;
			this.state = state;
		}
		public void setChildren(Node[] n)
		{
			children = n;
		}
	}