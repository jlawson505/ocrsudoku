package org.hackatron.ocrsudoku.tests;
import junit.framework.TestCase;
import org.hackatron.ocrsudoku.Solver;



public class SolverTest extends TestCase {
	public SolverTest() {
	}
	
	public void test_validator() {
		
		int[] puzzle1 = {  		6,3,9,0,0,0,2,1,8,
								8,0,0,0,0,0,0,0,5,
								0,0,0,0,1,8,0,0,0,
								9,0,0,0,0,4,0,0,2,
								0,7,0,0,0,0,0,6,0,
								2,0,4,3,0,0,8,0,7,
								0,0,0,8,5,0,0,0,0,
								1,0,0,0,0,0,0,0,9,
								7,9,3,0,0,0,5,8,4};
		
		int[] puzzle1_s = { 	6,3,9,5,4,7,2,1,8,
								8,1,7,6,3,2,9,4,5,
								5,4,2,9,1,8,7,3,6,
								9,6,1,7,8,4,3,5,2,
								3,7,8,2,9,5,4,6,1,
								2,5,4,3,6,1,8,9,7,
								4,2,6,8,5,9,1,7,3,
								1,8,5,4,7,3,6,2,9,
								7,9,3,1,2,6,5,8,4};
		
		Solver s = new Solver(9);
		
		
		
		/* Test validator-function for position 2,2 
		 * in  the first box of puzzle1:
		 * 
		 *  6 3 9
		 *  7 0 0 
		 *  0 0 0  0 1 8  0 0 0
		 *  
		 *      0
		 *      0 
		 *      4
		 *       
		 *      0
		 *      0
		 *      3
		 *      
		 *      Possible = 2, 5
		 *      
		 * */
		
		int[] v_result = s.valid(puzzle1, 2, 2);
		
		assert (v_result.length == 2);
		
		for (int i =0 ; i < 2; i++)
			assert (v_result[i] == 2 || 
					v_result[i] == 5);
		

	}



	public void test_adjacent() {
		
		int[] puzzle1 = {  		6,3,9, 0,0,0, 2,1,8,
								8,0,0, 0,0,0, 0,0,5,
								0,0,0, 0,1,8, 0,0,0,
								
								9,0,0, 0,0,4, 0,0,2,
								0,7,0, 0,0,0, 0,6,0,
								2,0,4, 3,0,0, 8,0,7,
								
								0,0,0, 8,5,0, 0,0,0,
								1,0,0, 0,0,0, 0,0,9,
								7,9,3, 0,0,0, 5,8,4};
		
		
		Solver s = new Solver(9);
		
	
		int[] ao = s.get_adjacent(puzzle1, 8, 8);

		assert ( ao[4] == 4); // +1 +1
		assert ( ao[6] == 5); // -1 +1
		assert ( ao[2] == 8); //  0 +1
		assert ( ao[0] == 9); // +1  0
		
		
	}


	public void test_puzzle() { // 4,5 
		
		int[] puzzle1 = {  		6,3,9,0,0,0,2,1,8,
								8,0,0,0,0,0,0,0,5,
								0,0,0,0,1,8,0,0,0,
								9,0,0,0,0,4,0,0,2,
								0,7,0,0,0,0,0,6,0,
								2,0,4,3,0,0,8,0,7,
								0,0,0,8,5,0,0,0,0,
								1,0,0,0,0,0,0,0,9,
								7,9,3,0,0,0,5,8,4};
		
		int[] puzzle1_s = { 	6,3,9,5,4,7,2,1,8,
								8,1,7,6,3,2,9,4,5,
								5,4,2,9,1,8,7,3,6,
								9,6,1,7,8,4,3,5,2,
								3,7,8,2,9,5,4,6,1,
								2,5,4,3,6,1,8,9,7,
								4,2,6,8,5,9,1,7,3,
								1,8,5,4,7,3,6,2,9,
								7,9,3,1,2,6,5,8,4};
		
		Solver s = new Solver(9);
		
	
		int[] puzzle_o = s.solve(puzzle1);
		
		assert (puzzle_o.length == 9*9);
		
		for (int i = 0; i < 9*9; i++)
			assertEquals(puzzle1_s[i], puzzle_o[i]);
		
	}	
	
}
