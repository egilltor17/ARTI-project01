public class State
	{
		boolean[] dirts;
		short posx;
		short posy;
		char orientation; //from 0-3 use modulo 4 when turning
		boolean on;
		String lastAction;
		
		String[] actions = new String[] {"TURN_LEFT", "TURN_RIGHT", "GO"};
		public State(){}
		public State(State state)
		{
			this.dirts = state.dirts;
			this.posx = state.posx;
			this.posy = state.posy;
			this.orientation = state.orientation;
			this.on = state.on;
		}
		public String[] listOfActions()
	    {
	    	return actions;
	    }
		public State act(String s)
		{
			System.out.println("string: " + s);
			State state = new State(this);
			if(s.equals("TURN_LEFT"))
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
	    	actions = new String[] {"TURN_LEFT", "GO"};
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
	    	actions = new String[] {"TURN_RIGHT", "GO"};
	    	return state;
	    	
	    }
	    public boolean goalState (State state)
	    {
	    	return (state.posx == 5 && state.posy == 5);
	    }
	    private State go(State state)
	    {
	    	if(state.orientation == 'N' && state.posy != 1)
	    	{
	    		state.posy--;
	    	}
	    	else if(state.orientation == 'W' && state.posx != 1)
	    	{
	    		state.posx--;
	    	}
	    	else if(state.orientation == 'S' && state.posx != 5)
	    	{
	    		state.posy++;
	    	}
	    	else if (state.orientation == 'E' && state.posx != 5)
	    	{
	    		state.posx++;
	    	}
	    	actions = new String[] {"TURN_LEFT", "TURN_RIGHT", "GO"};
	    	return state;
	    }
	}