import java.util.Stack;

public class State
	{
		boolean[] dirts;
		short posx;
		short posy;
		char orientation; //from 0-3 use modulo 4 when turning
		boolean on;
		String lastAction;
		Stack<String> actions;
		
		public State(){}
		public State(State state)
		{
			this.dirts = state.dirts.clone();
			this.posx = state.posx;
			this.posy = state.posy;
			this.orientation = state.orientation;
			this.on = state.on;
			this.lastAction = state.lastAction;
		}
		public void listOfActions(Node node, Point[] dirts, boolean[][] obstacles, Point size)
	    {
			Stack <String> stack = new Stack<String>() ;
			for(int i = 0; i < dirts.length; i++)
			{
				//System.out.print(dirts[i].x);
				if(this.posx == dirts[i].x && this.posy == dirts[i].y && !this.dirts[i])
				{
					this.dirts[i] = true;
					stack.push("SUCK");
					actions = stack;
					return;
				}
			}
			if(!this.lastAction.equals("TURN_RIGHT") )
			{
				stack.push("TURN_LEFT");
			}
			//System.out.println(orientation + " " + posy + " " + obstacles[posy - 1][posx - 1]);

			if(orientation == 'N' && (posy == 1 || obstacles[posy - 2][posx - 1]))
			{
			}
			else if(orientation == 'W' && (posx == 1 || obstacles[posy - 1][posx - 2]))
			{
			}
			else if(orientation == 'E' && (posx == size.x || obstacles[posy - 1][posx]))
			{
			}
			else if(orientation == 'S' && (posy == size.y || obstacles[posy][posx - 1]))
			{
			}
			else
			{
				stack.push("GO");
			}
			if(!this.lastAction.equals("TURN_LEFT"))
			{
				stack.push("TURN_RIGHT");
			}
			if(node.parent != null && node.parent.parent != null
			&& node.parent.state.lastAction.equals(node.parent.parent.state.lastAction)
			&& !node.parent.state.lastAction.equals("GO"))
			{
				stack.remove(node.parent.state.lastAction);
			}
	    	actions = stack;
	    }
		public State act(String s)
		{
			State state = new State(this);
			if(s.equals("SUCK"))
			{
				return suck(state);
			}
			else if(s.equals("TURN_LEFT"))
			{
				return turnLeft(state);
			}
			else if(s.equals("TURN_RIGHT"))
			{
				return turnRight(state);
			}
			else
			{
				return go(state);
			}
		}
		private State turnLeft(State state)
	    {
	    	if(state.orientation == 'N')
	    	{
	    		state.orientation = 'W';
	    	}
	    	else if(state.orientation == 'W')
	    	{
	    		state.orientation = 'S';
	    	}
	    	else if(state.orientation == 'S')
	    	{
	    		state.orientation = 'E';
	    	}
	    	else 
	    	{
	    		state.orientation = 'N';
	    	}
	    	state.lastAction = "TURN_LEFT";
	    	return state;
	    	
	    }
	    private State turnRight(State state)
	    {
	    	if(state.orientation == 'N')
	    	{
	    		state.orientation = 'E';
	    	}
	    	else if(state.orientation == 'W')
	    	{
	    		state.orientation = 'N';
	    	}
	    	else if(state.orientation == 'S')
	    	{
	    		state.orientation = 'W';
	    	}
	    	else 
	    	{
	    		state.orientation = 'S';
	    	}
	    	state.lastAction = "TURN_RIGHT";
	    	return state;
	    	
	    }
	    private State suck(State state)
	    {
	    	System.out.println("SUCC");
	    	state.lastAction = "SUCK";
	    	return state;
	    }
	    public boolean goalState (State state, Point home)
	    {
	    	for(boolean dirt:dirts)
	    	{
	    		
	    		if(dirt)
	    		{
	    			System.out.print("dirt: " + dirt);
	    			continue;
	    		}
	    		else
	    		{
	    			System.out.print("   ");
	    			return false;
	    		}
	    	}
	    	System.out.print(" " + state.posx + " " + state.posy + "\n");
	    	return (state.posx == home.x && state.posy == home.y);
	    }
	    private State go(State state)
	    {
	    	if(state.orientation == 'N')
	    	{
	    		state.posy--;
	    	}
	    	else if(state.orientation == 'W')
	    	{
	    		state.posx--;
	    	}
	    	else if(state.orientation == 'S')
	    	{
	    		state.posy++;
	    	}
	    	else if (state.orientation == 'E')
	    	{
	    		state.posx++;
	    	}
	    	state.lastAction = "GO";
	    	return state;
	    }
	}