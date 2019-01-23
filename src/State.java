public class State
	{
		boolean[] dirts;
		short posx;
		short posy;
		char orientation; //from 0-3 use modulo 4 when turning
		boolean on;
		String lastAction;
		String[] actions;
		
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
		public String[] listOfActions(State state, Point[] dirts, boolean[][] obstacles, Point size)
	    {
			boolean go = true;
			boolean left = true;
			boolean right = true;
			for(int i = 0; i < dirts.length; i++)
			{
				if(this.posx == dirts[i].x && this.posy == dirts[i].y && !this.dirts[i])
				{
					this.dirts[i] = true;
					return new String[] {"SUCK"};
				}
			}
			go =  (((orientation == 'N' && posy == 1) || obstacles[posy - 2][posx - 1])
				|| ((orientation == 'W' && posx == 1) || obstacles[posy - 1][posx - 2])
				|| ((orientation == 'E' && posy == size.x) || obstacles[posy - 1][posx])
				|| ((orientation == 'S' && posy == size.y) || obstacles[posy][posx - 1]));
			/*if((orientation == 'N' && posy == 1) || obstacles[posy - 2][posx - 1])
			{
				go = false;
			}
			else if((orientation == 'W' && posx == 1) || obstacles[posy - 1][posx - 2])
			{
				go = false;
			}
			else if((orientation == 'E' && posy == size.x) || obstacles[posy - 1][posx])
			{
				go = false;
			}
			else if((orientation == 'S' && posy == size.y) || obstacles[posy][posx - 1])
			{
				go = false;
			}*/
			if(this.lastAction == "TURN_LEFT")
			{
				right = false;
			}
			if(this.lastAction == "TURN_RIGHT")
			{
				left = false;
			}
	    	return actions;
	    }
		public State act(String s)
		{
			System.out.println("string: " + s);
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
	    private State suck(State state, int index)
	    {
	    	state.dirts[index] = true;
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