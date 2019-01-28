
public class State 
	{
		boolean[] dirts;
		short posx;
		short posy;
		char orientation;
		String lastAction;
		String[] actions;
		int pathCost;
		int heuristicCost;
		
		public State(){}
		public State(State state)
		{
			this.dirts = state.dirts.clone();
			this.posx = state.posx;
			this.posy = state.posy;
			this.orientation = state.orientation;
			this.lastAction = state.lastAction;
			this.pathCost = state.pathCost + 1;
		}
		
		public void listOfActions(Node node, Point[] dirtPoints, boolean[][] obstacles, Point size)
	    {
			actions = new String[] {"", "TURN_LEFT", "TURN_RIGHT"};
			for(int i = 0; i < dirtPoints.length; i++)
			{
				if(this.posx == dirtPoints[i].x && this.posy == dirtPoints[i].y && !this.dirts[i])
				{
					this.dirts[i] = true;
					actions[0] = "SUCK";
					actions[1] = "";
					actions[2] = "";
					return;
				}
			}
			if(!((orientation == 'S' && (posy == 1  || obstacles[posy - 2][posx - 1])) 
			  || (orientation == 'W' && (posx == 1  || obstacles[posy - 1][posx - 2]))
			  || (orientation == 'E' && (posx == size.x || obstacles[posy - 1][posx]))
			  || (orientation == 'N' && (posy == size.y || obstacles[posy][posx - 1])))) 
			{
				actions[0] = "GO";
			}
								
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
	    	pathCost--;
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
	    public int hashState(State state)
		{
			// Hash is now an integer with one bit for each dirt (max 16 dirt),
			// 7 bits for x & y coordinates (max 128 x 128 board) and 2 bits for orientation
			// hash = DDDD DDDD DDDD DDDD  XXXX XXXY YYYY YYOO
			int hash = 0;
			for(int i = 0; i < state.dirts.length; i++)
			{
				if(state.dirts[i]) hash += 1 << (31 - i);
			}
			hash += state.posx << 9;
			hash += state.posy << 2;
			if(state.orientation == 'N') hash += 0;
			if(state.orientation == 'E') hash += 1;
			if(state.orientation == 'S') hash += 2;
			if(state.orientation == 'W') hash += 3;
			return hash;
		}
	    public String toString() 
	    {
	    	return "State is ok.";
	    }
	}