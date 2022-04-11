/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graph_Generator;

import java.util.ArrayList;

import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import Algorithms.AllPaths;
import Algorithms.UsefulMethods;
import Algorithms.getFunction;

/**
 *
 * @author mjalalitabar1
 */
public class DepenGenerator {

	private ArrayList<functionnode> VNFs;
	public static ArrayList<ArrayList<Integer>> allListsFinal  = new ArrayList<>();



	public DepenGenerator(ArrayList<functionnode> VNFs) {
		this.VNFs = VNFs;
	}

	public ArrayList<functionnode> getVNFs() {
		return VNFs;
	}

	public void setVNFs(ArrayList<functionnode> VNFs) {
		this.VNFs = VNFs;
	}

	public static int[][] serviceRequestGenereator() {
		//writeInFile wr = new writeInFile();
		
		System.out.println("***Service Request****");

		int numberOfBranches = UsefulMethods.generateRandomNumberWithStep(2, 4, 1);
		//numberOfBranches =2;
		System.out.println("Number of branches: " + numberOfBranches);
		List<Integer> numberOfNodes = new ArrayList<>();
		int totalNumberOfNodes = 4;// Counting s,b,m,d
		for (int i = 0; i < numberOfBranches; i++) {
			int numberOfBranchNodes = UsefulMethods.generateRandomNumberWithStep(1, 3, 1);
			//numberOfBranchNodes = 2;
			numberOfNodes.add(numberOfBranchNodes);
			totalNumberOfNodes += numberOfBranchNodes;
		}
		System.out.println("Number of nodes on each branch " + numberOfNodes.toString());
		System.out.println("Total number of VNFs in the SR: " +totalNumberOfNodes);

		int[][] temp = new int[totalNumberOfNodes][totalNumberOfNodes];
		temp[0][1] = 1;
		temp[1][0] = 1;
		temp[totalNumberOfNodes - 2][totalNumberOfNodes - 1] = 1;
		temp[totalNumberOfNodes - 1][totalNumberOfNodes - 2] = 1;
		int counter = 2;
		for (int i = 0; i < numberOfBranches; i++) {
			int number = numberOfNodes.get(i);
			if (number == 1) {
				temp[counter][1] = 1;
				temp[1][counter] = 1;
				temp[counter][totalNumberOfNodes - 2] = 1;
				temp[totalNumberOfNodes - 2][counter] = 1;
				counter++;
			} else {
				for (int j = 0; j < number - 1; j++) {
					if (j == 0) {
						temp[counter][1] = 1;
						temp[1][counter] = 1;
					}

					temp[counter][counter + 1] = 1;
					temp[counter + 1][counter] = 1;
					counter++;

				}
				temp[counter][totalNumberOfNodes - 2] = 1;
				temp[totalNumberOfNodes - 2][counter] = 1;
				counter++;
			}
		}
		System.out.println("Adjacency graph for SR: ");
		UsefulMethods.printGraph(temp);
		System.out.println("#################");
		 List<ArrayList<Integer>> allLists = new ArrayList<>();

		printAllPaths(0, totalNumberOfNodes-1, totalNumberOfNodes, temp);
		
		//wr.writeFileFinal(temp, t);

		return temp;

	}
	public static int[][] serviceRequest(String fileName) {
		int[][] adj = UsefulMethods.getMat(fileName);
		printAllPaths(0, adj[0].length-1, adj[0].length, adj);

		return UsefulMethods.getMat(fileName);
		
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
	
	// v: number of vertices

		public static void printAllPaths(int s, int d, int v,int[][] mat) {


			boolean[] isVisited = new boolean[v];
			ArrayList<Integer> pathList = new ArrayList<>();

			// add source to path[]
			pathList.add(s); 

			// Call recursive utility
			printAllPathsUtil(mat,s, d, isVisited, pathList,allListsFinal);

			
		} 
		

		public static void printAllPathsUtil(int[][] mat,Integer u, Integer d, boolean[] isVisited, 
				ArrayList<Integer> localPathList,
				ArrayList<ArrayList<Integer>> allListsFinal) {

			if (u.equals(d)) {
				System.out.println("Branch: " + localPathList);
				allListsFinal.add(new ArrayList<>(localPathList));
				//System.out.println("ssBranch: " + allListsFinal);
				// if match found then no need to traverse more till depth
				return;
			}

			// Mark the current node
			isVisited[u] = true;

			// Recur for all the vertices
			// adjacent to current vertex
			List<Integer> result = findNeighbours (mat, u);
			for (Integer i : result) {
				if (!isVisited[i]) {
					// store current node
					// in path[]
					localPathList.add(i);
					printAllPathsUtil(mat,i, d, isVisited, localPathList,allListsFinal);

					// remove current node
					// in path[]
					localPathList.remove(i);
				}
			}

			// Mark the current node
			isVisited[u] = false;
		}
	
	public static List<Integer> dfsGraph() {

		Deque<Integer> stack = new LinkedList<>();
		List<Integer> result = new ArrayList<>();

		return null;

	}

	public void randomDepGenerator(ArrayList<functionnode> funcs) { // generate random dependency for the VNFs

		for (int jq = 0; jq < funcs.size(); jq++) {
			ArrayList<Integer> Deplist = new ArrayList<Integer>();
			ArrayList<Integer> pool = new ArrayList<Integer>();
			for (int k1 = 0; k1 < funcs.size(); k1++) {
				if (k1 != jq) {
					pool.add(k1);
				}
			}
			// System.out.println("pool " + pool + " for node: " + jq);

			/////////////////////////////////////////////////////////////////////
			int DepRand = 0;
			Random r = new Random();
			double rangeMin = 0;
			double rangeMax = 1.5;
			double randomValue1 = rangeMin + (rangeMax - rangeMin) * r.nextDouble();

			if (randomValue1 < .5) {
				DepRand = 0;
			} else if (randomValue1 < 1) {
				DepRand = 1;
			} else
				DepRand = 2;

			////////////////////////////////////////////////////////////////////

			// System.out.println("Num op possible dep: " + DepRand);
			int t = pool.size() - 1;
			for (int k2 = 0; k2 < DepRand; k2++) {
				int t1 = pool.size() - 1;
				Random rn = new Random();
				int randomValue = 0 + (int) (Math.random() * (((pool.size() - 1) - 0) + 1));
				// System.out.println("random index" + randomValue);
				if (!(pool.indexOf(pool.get(randomValue)) == -1)) {
					// System.out.println("element of the index: " + pool.get(randomValue));
					Deplist.add(pool.get(randomValue));
					int removedDataIndex = pool.indexOf(pool.get(randomValue));
					pool.remove(removedDataIndex);
					// System.out.println("updated pool: " + pool);
					// System.out.println("Deplist: " + Deplist);
				} else {
					// System.out.println("pppp");
				}
			}
			// System.out.println("Deplist " + Deplist + " For Node: jq " + jq);

			VNFs.get(jq).setFuncDep(Deplist);
			// nodesFile[jq].setFuncDep(Deplist);
			// vnfs.add(nodesFile[jq]);

		}

		for (int ie = 0; ie < VNFs.size(); ie++) {
			// System.out.println(" Node: " + VNFs.get(ie).getNumber() + " CPU: " +
			// VNFs.get(ie).getReqCPU()
			// + " dependencies: " + VNFs.get(ie).getFuncDep());
		}
		
		
	}

}
