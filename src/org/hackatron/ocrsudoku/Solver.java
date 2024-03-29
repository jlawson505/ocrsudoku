package org.hackatron.ocrsudoku;


public class Solver  {
	private int p_size;
	private int p_dim;	
	
	public int get(int[] input, int x, int y) {
		int pos = x+y*p_dim;
		assert ( pos <= p_size );
		return input[pos];
	}

	public int[] get_adjacent(int[] input, int x, int y) {
		int c_x = (x/3)*3+1;
		int c_y = (y/3)*3+1;
		int [] result = new int[p_dim-1];
		
		result[0] = get(input,c_x+1,c_y); 
		result[1] = get(input,c_x-1,c_y);
		result[2] = get(input,c_x,c_y+1);
		result[3] = get(input,c_x,c_y-1);
		result[4] = get(input,c_x+1,c_y+1);
		result[5] = get(input,c_x+1,c_y-1);
		result[6] = get(input,c_x-1,c_y+1);
		result[7] = get(input,c_x-1,c_y-1);
		
		return result;		
	}
	
	public int[] valid (int [] input, int x, int y) {
		/* The list of possible numbers to place in box(x,y) */
						/* 1 2 3 4 5 6 7 8 9 */
		int [] possible = {1,1,1,1,1,1,1,1,1};
		
		
		/* Must be available number-box */
		assert ( get(input,x,y) == 0 );
		
		/*
		 * 1. Check box
		 * 2. Check row
		 * 3. Check col
		 * 
		 * p_size = box_size
		 * 
		 */

		int v = 0;
		
		for (int i=0; i < p_dim; i++) {
			v = get(input, i, y);
			if (v > 0)
				possible[v-1] = 0; 
			v = get(input, x, i);
			if (v > 0)
				possible[v-1] = 0; 
		}

		/* Check Adjacent */
		int[] adjacent = get_adjacent(input, x, y);
		
		for (int i=0; i < p_dim-1; i++) {
			v = adjacent[i];
	
			if (v > 0)
				possible[v-1] = 0; 
		}
		
		int result_count = 0;
		
		for (int i = 0; i < 9; i++) 
			if (possible[i] > 0)
				result_count++;
		
		int [] result = new int[result_count];
		int result_c = 0;
		
		for (int i = 0; i < 9; i++)
			if (possible[i] > 0)
				result[result_c++] = i+1;
		
		return result;
	}
	
	/* Possible solution checker */
	/* Checks for any empty '0'-fields to detect incomplete puzzle */
	private boolean check(int [] input) {
		for (int i = 0; i < p_size; i++)
			if (input[i] == 0)
				return false;
		return true;
	}
	/* The worker, brute force recursion is your friend */
	private int[] solver(int [] input, int ttl) {
		
		if (ttl < 0)
			new Exception("Maximum recursion level reached");
		
		int [] p = new int[p_size];
		/* Make a working copy of the puzzle 
		 * For this recursion level 
		 */ 
  		System.arraycopy(input, 0, p, 0, p_size);
  		
  		/* Search for best possible 
  		 * Starting point...
  		 * */
  		
  		int start_x = 0;
  		int start_y = 0;
  		int c = 10;
  		int [] start_v = new int[10];
  		
		for (int x = 0; x < p_dim; x++) {
			for (int y = 0; y < p_dim; y++) {
				if (get(p,x,y) != 0)
					continue;
					
  				int[] v = valid(p, x, y);
  				
  				if ( v.length < c && v.length > 0) {
  					start_x = x;
  					start_y = y;
  					c = v.length;
  					start_v = v;
  				}
  			}
		}
		
		if ( c == 10 ) 
			return check(p)?p:null;
		
		for (int i = 0; i < start_v.length; i++) {
			p[start_x+start_y*9] = start_v[i];
			int [] p_ret = solver (p, ttl-1);
			if (p_ret != null)
				return p_ret;
			else
				p[start_x+start_y*9] = 0;
		}
		
  		return null;
	}
	
  	public Solver(int size) {
  		p_dim = size;
  		p_size = size*size;
	}

  	public int[] solve(int[] puzzle) {
  		int[] p = new int[p_size];
  		/* Make a working copy of the puzzle */ 
  		System.arraycopy(puzzle, 0, p, 0, p_size);
  		return solver(p, 100);
	}
  	  	
}