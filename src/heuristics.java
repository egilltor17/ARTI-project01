import java.util.Arrays;

public class heuristics {
	
	/**
	 * Returns an array with the weighted heuristics for turning left, going straight and turning right
	 * @param state   -  Initial state
	 * @param target  -  Target's location 
	 * @return int[3] -  {LeftWeight, GoWeight, RightWeight}
	 */
	public int[] singleManhatanHeuristic(State state, Point target)
	{	
		int N = target.manhatanDist(new Point(state.posx, state.posy - 1));
		int E = target.manhatanDist(new Point(state.posx + 1, state.posy));
		int S = target.manhatanDist(new Point(state.posx, state.posy + 1));
		int W = target.manhatanDist(new Point(state.posx - 1, state.posy));
		
		if(state.orientation == 'N' ) 
		{
			return new int[] {W + 1, N, E + 1};
		} 
		else if(state.orientation == 'E' ) 
		{
			return new int[] {N + 1, E, S + 1};
		} 
		else if(state.orientation == 'S' ) 
		{
			return new int[] {E + 1, S, W + 1};
		} 
		else 
		{
			return new int[] {S + 1, W, N + 1};
		} 
	}
	
	
	/**
	 * Returns an array with the weighted heuristics for turning left, going straight and turning right
	 * @param state	  -  Current state
	 * @param dirtPos -  Dirt position array from superAgent
	 * @return int[3] -  {LeftWeight, GoWeight, RightWeight}
	 */
	public int[] manhatanHeuristic(State state, Point[] dirtPos, Point homePos)
	{	
		int distFront[] = new int[state.dirts.length];
		int distLeft[]  = new int[state.dirts.length];
		int distRight[] = new int[state.dirts.length];
		boolean noDirtsLeft = true;
		for(boolean b:state.dirts)
		{
			if(!b)
			{
				noDirtsLeft = !b;
				break;
			}
		}
		if(noDirtsLeft)
		{	// Gives heuristic for going home
			int N = homePos.manhatanDist(new Point(state.posx, state.posy - 1));
			int E = homePos.manhatanDist(new Point(state.posx + 1, state.posy));
			int S = homePos.manhatanDist(new Point(state.posx, state.posy + 1));
			int W = homePos.manhatanDist(new Point(state.posx - 1, state.posy));
			if(state.orientation == 'N' ) 
			{
				return new int[] {W + 1, N, E + 1};
			} 
			else if(state.orientation == 'E' ) 
			{
				return new int[] {N + 1, E, S + 1};
			} 
			else if(state.orientation == 'S' ) 
			{
				return new int[] {E + 1, S, W + 1};
			} 
			else 
			{
				return new int[] {S + 1, W, N + 1};
			}
		}
		else
		{	// Gives heuristic to closes non cleaned dirt
			for(int i = 0; i < state.dirts.length; i++) 
			{
				if(state.dirts[i]) 
				{
					distFront[i] = -1;
					distLeft[i]  = -1;
					distRight[i] = -1;
				} 
				else 
				{
					int N = dirtPos[i].manhatanDist(new Point(state.posx, state.posy - 1));
					int E = dirtPos[i].manhatanDist(new Point(state.posx + 1, state.posy));
					int S = dirtPos[i].manhatanDist(new Point(state.posx, state.posy + 1));
					int W = dirtPos[i].manhatanDist(new Point(state.posx - 1, state.posy));
					if(state.orientation == 'N' ) 
					{
						distFront[i] = N;
						distLeft[i]  = W + 1;
						distRight[i] = E + 1;
					} 
					else if(state.orientation == 'E' ) 
					{
						distFront[i] = E;
						distLeft[i]  = N + 1;
						distRight[i] = S + 1;
					} 
					else if(state.orientation == 'S' ) 
					{
						distFront[i] = S;
						distLeft[i]  = E + 1;
						distRight[i] = W + 1;
					} 
					else 
					{
						distFront[i] = W;
						distLeft[i]  = S + 1;
						distRight[i] = N + 1;
					}
				}
			}
			Arrays.sort(distFront);
			Arrays.sort(distLeft);
			Arrays.sort(distRight);
			return new int[] {distLeft[distLeft.length - 1], distFront[distFront.length - 1], distRight[distRight.length - 1]};  // Is not admissible for multiple objectives.
			//return new int[] {distLeft[0], distFront[0], distRight[0]};  // Is not complete for multiple objectives. 
		}
	}
	
	public int dirtRemainingHeuristics(State state) 
	{
		int dirtCount = 0;
		for(boolean b:state.dirts) 
		{
			if(!b) dirtCount++;
		}
		return dirtCount;
	}
	
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub

	}

}
