package Algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import Graph_Reader.readFromFile;


public class UsefulMethods {
	
	public static int generateRandomNumberWithStep(int lower, int upper, int step) {

		int frq = 1; // Number of required random numbers
		int rand = 0;
		int result = 0;
		for (int i = 0; i < frq; i++) {
			rand = (int) (Math.random() * (upper - lower + 1));
			result = rand - rand % step + lower;
		}
		return result;

	}
	
	public static int[][] getMat(String fileName) {
		
		readFromFile rd = new readFromFile();
		int[][] adj = rd.readfiles(fileName);
		return adj;
	}
	
    public static boolean hasDuplicate(List<Integer> arr) {
    	
    	 Collections.sort(arr);
		
		for (int i = 0; i < arr.size()-1; i++) {
			if (arr.get(i) == arr.get(i+1)) {
				return true;
			}
			
		}
		return false;
	}

  

	public static void printGraph(int[][] mat) {
		for (int i = 0; i < mat.length; i++) {
			for (int j = 0; j < mat[0].length; j++) {
				System.out.print(mat[i][j] + " ");
			}
			System.out.println();
		}
		for (int i = 0; i < mat.length; i++) {
			System.out.print("Vertex " + i + " is connected to:");
			for (int j = 0; j < mat[0].length; j++) {
				if (mat[i][j] == 1) {
					System.out.print(j + " ");
				}
			}
			System.out.println();
		}
	}
	
	public static List<Integer> findNeighbours (int[][] mat, int node) {
		
		List<Integer> result = new ArrayList<>();
		for (int i = 0; i < mat.length; i++) {
			if (i == node) {
			//System.out.print("Vertex " + i + " is connected to:");
			for (int j = 0; j < mat[0].length; j++) {
				if (mat[i][j] == 1) {
					//System.out.print(j + " ");
					result.add(j);
				}
			}
			}
			//System.out.println();
		}
		return result;
	}

	

}
