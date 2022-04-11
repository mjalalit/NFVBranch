/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithms;

import Graph_Generator.functionnode;
import java.util.ArrayList;
import java.io.*;
import java.util.*;
import Graph_Reader.requestVNFs;

/**
 *
 * @author mjalalitabar1
 */
public class topSortAlgo {
    ArrayList<functionnode> VNFs;
    String fileName;

    public topSortAlgo(ArrayList<functionnode> VNFs,String fileName) {
        this.VNFs = VNFs;
        this.fileName=fileName;
    }

    public ArrayList<functionnode> getVNFs() {
        return VNFs;
    }

    public void setVNFs(ArrayList<functionnode> VNFs) {
        this.VNFs = VNFs;
    }
    
    
  	// The function to do Topological Sort. It uses
	// recursive topologicalSortUtil()
	public ArrayList<Integer> topologicalSort()
	{
		Stack stack = new Stack();
                ArrayList<Integer> sortedList=new ArrayList<Integer>();
		// Mark all the vertices as not visited
		boolean visited[] = new boolean[VNFs.size()];
		for (int i = 0; i < VNFs.size(); i++)
			visited[i] = false;

		// Call the recursive helper function to store
		// Topological Sort starting from all vertices
		// one by one
		for (int i = 0; i < VNFs.size(); i++)
			if (visited[i] == false)
				topologicalSortUtil(i, visited, stack);

		// Print contents of stack
		while (stack.empty()==false)
                    sortedList.add((Integer)stack.pop());
           ////System.out.print(sortedList + " topSortum");
                        return sortedList;
	}
        
        // A recursive function used by topologicalSort
	void topologicalSortUtil(int v, boolean visited[],Stack stack)
	{
            
                ArrayList<Integer> Neighbours;
		// Mark the current node as visited.
		visited[v] = true;
		Integer i;

		// Recur for all the vertices adjacent to this
		// vertex
                requestVNFs netReq=new requestVNFs(fileName);
                Neighbours= netReq.getDependents(v);
		Iterator<Integer> it = Neighbours.iterator();
		while (it.hasNext())
		{
			i = it.next();
			if (!visited[i])
				topologicalSortUtil(i, visited, stack);
		}

		// Push current vertex to stack which stores result
		stack.push(new Integer(v));
	}
    
    
    
    
    
    
}
