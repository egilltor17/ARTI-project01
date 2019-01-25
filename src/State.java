import java.util.Stack;

public class State 
	{
		boolean[] dirts;
		short posx;
		short posy;
		char orientation;
		String lastAction;
		Stack<String> actions;
		
		public State(){}
		public State(State state)
		{
			this.dirts = state.dirts.clone();
			this.posx = state.posx;
			this.posy = state.posy;
			this.orientation = state.orientation;
			this.lastAction = state.lastAction;
		}
		
		public void listOfActions(Node node, Point[] dirtPoints, boolean[][] obstacles, Point size)
	    {
			actions = new Stack<String>();
			for(int i = 0; i < dirtPoints.length; i++)
			{
				if(this.posx == dirtPoints[i].x && this.posy == dirtPoints[i].y && !this.dirts[i])
				{
					this.dirts[i] = true;
					actions.push("SUCK");
					return;
				}
			}
			actions.push("TURN_LEFT");
			if(!((orientation == 'S' && (posy == 1  || obstacles[posy - 2][posx - 1])) 
			  || (orientation == 'W' && (posx == 1  || obstacles[posy - 1][posx - 2]))
			  || (orientation == 'E' && (posx == size.x || obstacles[posy - 1][posx]))
			  || (orientation == 'N' && (posy == size.y || obstacles[posy][posx - 1])))) 
			{
				actions.push("GO");
			}
			actions.push("TURN_RIGHT");
			
			if(this.lastAction.equals("TURN_LEFT"))
			{
				actions.remove("TURN_RIGHT");
			}
			if(this.lastAction.equals("TURN_RIGHT") )
			{
				actions.remove("TURN_LEFT");
			}
			
			/*
			if(orientation == 'N' && (posy == 1 || obstacles[posy - 2][posx - 1]))
			{
				actions.remove("GO");
			}
			else if(orientation == 'W' && (posx == 1 || obstacles[posy - 1][posx - 2]))
			{
				actions.remove("GO");
			}
			else if(orientation == 'E' && (posx == size.x || obstacles[posy - 1][posx]))
			{
				actions.remove("GO");
			}
			else if(orientation == 'S' && (posy == size.y || obstacles[posy][posx - 1]))
			{
				actions.remove("GO");
			}
			*/						
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
	    	state.lastAction = "SUCK";
	    	return state;
	    }
	    public boolean dirtGoalState(State state, Point target)
	    {
	    	if(state.posx == target.x && state.posy == target.y)
	    	{
	    		return true;
	    	}
	    	return false;
	    }
	    public boolean searchGoalState (State state, Point home)
	    {	
	    	//boolean noDirtLeft = true;
	    	for(boolean dirt:dirts)
	    	{
	    		if(dirt)
	    		{
	    			//System.out.print("X");
	    		}
	    		else
	    		{
	    			//System.out.print("_");
	    			//noDirtLeft = false;
	    			return false;
	    		}
	    	}
	    	/*
	    	System.out.println();
	    	if(!noDirtLeft)
    		{
	    		return noDirtLeft;
    		}
	    	System.out.print("Pos:  " + state.posx + " " + state.posy + "\n");
	    	*/
	    	return (state.posx == home.x && state.posy == home.y);
	    }
	    private State go(State state)
	    {
	    	if(state.orientation == 'S')
	    	{
	    		state.posy--;
	    	}
	    	else if(state.orientation == 'W')
	    	{
	    		state.posx--;
	    	}
	    	else if(state.orientation == 'N')
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
	    public String toString() 
	    {
	    	return "State is ok.";
	    }
	}