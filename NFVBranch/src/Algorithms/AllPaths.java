package Algorithms;

import java.util.ArrayList;

public class AllPaths {
	public     ArrayList<Integer>  allListsFinal;

	public ArrayList<Integer> getAllListsFinal() {
		return allListsFinal;
	}

	public void setAllListsFinal(ArrayList<Integer> allListsFinal) {
		this.allListsFinal = allListsFinal;
	}

	public AllPaths(ArrayList<Integer> allListsFinal) {
		
		this.allListsFinal = allListsFinal;
	}

	@Override
	public String toString() {
		
		return "AllPaths [allListsFinal=" + allListsFinal + "]";
	}

	
	
	
	
	


}
