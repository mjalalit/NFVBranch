package Graph_Reader;

import Graph_Generator.Vertex;
import Graph_Generator.functionnode;
import Graph_Generator.writeInFile;
import java.util.ArrayList;
import java.util.Random;
import Graph_Generator.DepenGenerator;
import Graph_Generator.Edge;
import Algorithms.BFSAlgorithm;
import Algorithms.UsefulMethods;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import static networksim.NetworkSim.equal;

/**
 *
 * @author mjalalitabar1
 */
public class requestVNFs {

	private ArrayList<functionnode> VNFs;
	String filename;

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public requestVNFs(ArrayList<functionnode> VNFs) {
		this.VNFs = VNFs;
	}

	public requestVNFs(String filenamehere) {
		this.filename = filenamehere;

	}

	public requestVNFs() {

	}

	public ArrayList<functionnode> getVNFs() {
		return VNFs;
	}

	public void setVNFs(ArrayList<functionnode> VNFs) {
		this.VNFs = VNFs;
	}

	///////////////////////////////////////////////////////
	public void serviceGenerator(ArrayList<functionnode> funcs) {

		int rangeMax = funcs.size() - 1;
		int rangeMin = 0;
		Random r = new Random();
		int numOfDep = rangeMin + (rangeMax - rangeMin) * r.nextInt();
		// double randomValue = 0 + (3 - 0) * r.nextDouble();

		// fix one set of dependency

		// Create different set of CPU/Bw for that

	}
	////////////////////////////////////////////////////////

	public ArrayList<functionnode> generateRequest() {
		int[][] matrixRequest;
		readFromFile rd = new readFromFile();
		matrixRequest = rd.readfiles("serviceRequest.txt");
		ArrayList<functionnode> funcs = new ArrayList<functionnode>();
		for (int i = 0; i < matrixRequest[0].length; i++) {
			functionnode reqVNF = new functionnode(0, i);
			funcs.add(reqVNF);
		}
		return funcs;
	}

	////////////////////////////////////////////
	public ArrayList<functionnode> generateRequestNew(int n) {
		int[][] matrixRequest;
		// readFromFile rd=new readFromFile();
		matrixRequest = new int[n][n];
		ArrayList<functionnode> funcs = new ArrayList<functionnode>();
		for (int i = 0; i < n; i++) {
			functionnode reqVNF = new functionnode(0, i);
			funcs.add(reqVNF);
		}
		return funcs;

	}

	//////////////////////////////////////////
	public int[][] createRequest(int a) { // generate dep matrix a*a

		// System.out.println("In createRequest: " + a);
		int[][] matrixRequest;

		int[][] matrixRequestDep;
		ArrayList<functionnode> nodes;
		nodes = generateRequestNew(a);
		matrixRequest = new int[nodes.size()][nodes.size()];
		DepenGenerator DG = new DepenGenerator(nodes);
		DG.randomDepGenerator(nodes);

		VNFs = nodes;

		for (int i = 0; i < VNFs.size(); i++) {

			// System.out.println("VNF number: " + VNFs.get(i).getNumber() + " Get Func Dep
			// : " + VNFs.get(i).getFuncDep());
		}

		for (int i = 0; i < VNFs.size(); i++) {

			ArrayList<Integer> nodeDepList = new ArrayList<Integer>();
			nodeDepList = VNFs.get(i).getFuncDep();
			if (nodeDepList.isEmpty()) {
				// System.out.println("No dependency for node: " + VNFs.get(i).getNumber());
				// //So make the corresponding row equal to the zero
				for (int j = 0; j < matrixRequest[VNFs.get(i).getNumber()].length; j++) { // If there is no dep the
																							// corresponding col should
																							// be zero
					matrixRequest[VNFs.get(i).getNumber()][j] = 0;
				}
			} else {
				for (int j = 0; j < matrixRequest[VNFs.get(i).getNumber()].length; j++) {
					for (int k = 0; k < nodeDepList.size(); k++) {
						int t = nodeDepList.get(k);
						matrixRequest[VNFs.get(i).getNumber()][t] = 1;

					}
				}
			}

		}

		// System.out.println("Dep matrix generated");
		for (int i = 0; i < matrixRequest.length; i++) {
			for (int j = 0; j < matrixRequest[i].length; j++) {
				// System.out.print(matrixRequest[i][j] + " ");
			}
			// System.out.println("");
		}

		//////////////////////////////////////////////////////////
		// System.out.println("Remove Duplication from matrix");
		/////////////////////////////////////////////////////

		// System.out.println("Before removing the duplication");
		for (int m = 0; m < matrixRequest.length; m++) {
			for (int n = 0; n < matrixRequest[m].length; n++) {
				// System.out.print(matrixRequest[m][n] + " ");
			}
			// System.out.println("");
		}
		////////////////////////////////////////////////////////////
		for (int m = 0; m < matrixRequest.length; m++) { // remove transitive
			for (int n = 0; n < matrixRequest[m].length; n++) {
				if (!(m == n)) {
					if (matrixRequest[m][n] == 1 && matrixRequest[n][m] == 1) {
						matrixRequest[n][m] = 0;
						// //System.out.println("Duplication Removed");
					}
				}
			}
		}
		///////////////////////////////////////////////////////
		// System.out.println("Show the update after removing the duplication for ");
		for (int m1 = 0; m1 < matrixRequest.length; m1++) {
			for (int n1 = 0; n1 < matrixRequest[m1].length; n1++) {
				// System.out.print(matrixRequest[m1][n1] + " ");
			}
			// System.out.println("");
		}

		// System.out.println("Now resolving the transitive problem version path");

		for (int i = 0; i < VNFs.size(); i++) {

			for (int j = 0; j < VNFs.size(); j++) {

				if (j != VNFs.get(i).getNumber()) {

					if (isReachable(VNFs, i, VNFs.get(j).getNumber(), matrixRequest)) {

						if (matrixRequest[VNFs.get(j).getNumber()][i] == 1) {

							// matrixRequest[i][VNFs.get(j).getNumber()]=20;

							matrixRequest[VNFs.get(j).getNumber()][i] = 0;

						}
					}
				}

			}
		}

		// System.out.println("After Resolving path");
		int count = 0;
		for (int k = 0; k < matrixRequest.length; k++) {
			for (int l = 0; l < matrixRequest[k].length; l++) {
				// System.out.print(matrixRequest[k][l] + " ");

			}
			// System.out.println("");

		}

		////////////////////////////////////////

		for (int i = 0; i < matrixRequest.length; i++) {
			for (int j = 0; j < matrixRequest.length; j++) {
				for (int k = 0; k < VNFs.size(); k++) {
					if (matrixRequest[i][j] == 1) {
						if (VNFs.get(k).getNumber() != i && VNFs.get(k).getNumber() != j) {
							if (matrixRequest[k][i] == 1 && matrixRequest[k][j] == 1) {
								matrixRequest[i][j] = 0;
								matrixRequest[j][i] = 0;
							}
						}
					}

					if (matrixRequest[j][i] == 1) {
						if (VNFs.get(k).getNumber() != i && VNFs.get(k).getNumber() != j) {
							if (matrixRequest[k][i] == 1 && matrixRequest[k][j] == 1) {
								matrixRequest[i][j] = 0;
								matrixRequest[j][i] = 0;
							}
						}
					}

				}

			}

		}

		// System.out.println("After Resolving final");
		for (int k = 0; k < matrixRequest.length; k++) {
			for (int l = 0; l < matrixRequest[k].length; l++) {
				// System.out.print(matrixRequest[k][l] + " ");

			}
			// System.out.println("");

		}

		// //System.out.println("count" + count);

		return matrixRequest;
	}
	/////////////////////////////////////////////////////

	public Boolean isReachable(ArrayList<functionnode> virtualNodes, int s, int d, int[][] matrixRequest) {

		//// System.out.println("file name in isReachable : " + file);
		// System.out.println("In reachable :" + s + " to " + d);
		LinkedList<Integer> temp;
		List<Integer> Neighbours;

		// int [][] matrixRequest;
		// readFromFile rd=new readFromFile();
		// matrixRequest=rd.readfiles(file);

		// System.out.println("in reachable print");

		for (int m1 = 0; m1 < matrixRequest.length; m1++) {
			for (int n1 = 0; n1 < matrixRequest[m1].length; n1++) {
				// System.out.print(matrixRequest[m1][n1] + " ");
			}
			// System.out.println("");
		}

		// Mark all the vertices as not visited(By default set
		// as false)

//        //System.out.println("virtualNodes " + virtualNodes.size());
		boolean visited[] = new boolean[virtualNodes.size()];

		// Create a queue for BFS
		LinkedList<Integer> queue = new LinkedList<Integer>();

		// Mark the current node as visited and enqueue it
		visited[s] = true;
		queue.add(s);

		// 'i' will be used to get all adjacent vertices of a vertex
		Iterator<Integer> i;
		while (queue.size() != 0) {
			// Dequeue a vertex from queue and print it
			s = queue.poll();
			int n;

			// String t= getFilename();
			// //System.out.println("filename gggggg " + t);
			// requestVNFs netReq=new requestVNFs(filename);

			requestVNFs netReq = new requestVNFs(filename);

			// requestVNFs netReq=new requestVNFs("SR_1715.txt");

			Neighbours = netReq.getDependentsMatrix(s, matrixRequest);
			// Neighbours=netReq.getDependents(s);
			i = Neighbours.listIterator();

			// Get all adjacent vertices of the dequeued vertex s
			// If a adjacent has not been visited, then mark it
			// visited and enqueue it
			while (i.hasNext()) {
				n = i.next();

				// If this adjacent node is the destination node,
				// then return true
				if (n == d)
					return true;

				// Else, continue to do BFS
				if (!visited[n]) {
					visited[n] = true;
					queue.add(n);
				}
			}
		}
		// If BFS is complete without visited d
		return false;
	}

	////////////////////////////////////////////////////

//     
//        public  boolean reachCheck( ArrayList <functionnode> virtualNodes,int s, int d){
//                    
//                  boolean check;
//                    
//                   //check= isReachable(VNFs, s, d);
//                    return check;
//            }

	/////////////////////////////////// function to check if two 2D arays are equal
	public static boolean equal(final int[][] arr1, final int[][] arr2) {
		if (arr1 == null) {
			return (arr2 == null);
		}
		if (arr2 == null) {
			return false;
		}
		if (arr1.length != arr2.length) {
			return false;
		}
		for (int i = 0; i < arr1.length; i++) {
			if (!Arrays.equals(arr1[i], arr2[i])) {
				return false;
			}
		}
		return true;
	}

	////////////////////////////////
	private static List<Integer> arrayToList(int[][] matrix) {
		List<Integer> out = new ArrayList<Integer>();
		int rows = matrix.length;
		int cols = matrix[0].length;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				out.add(matrix[i][j]);
			}
		}
		return out;
	}

	public void finalRequestGenerator() {

		//Edge newEdge = new Edge();
		int[][] matrixRequest;
		int[][] matrixRequestCPU;
		int[][] matrixRequestBW;
		ArrayList<int[][]> copiesofDep = new ArrayList<int[][]>();
		writeInFile wr = new writeInFile();

		readFromFile rd = new readFromFile();
		matrixRequest = rd.readfiles("outhere22.txt");

		// System.out.println("Number of lines: " + matrixRequest.length );
		int count = 0;
		for (int i = 0; i < 174; i++) {
			int bw = matrixRequest[i][0];
			int CPU = matrixRequest[i][1];
			int numberOfNodes = matrixRequest[i][2];
			int[][] matrixDep;
			// System.out.println("Number Of Nodes " + numberOfNodes);
			HashSet<List<Integer>> hashset = new HashSet<>();
			ArrayList<int[][]> copiesofDep1 = new ArrayList<int[][]>();
			for (int j = 0; j < 10; j++) {
				matrixDep = createRequest(numberOfNodes);// generate dep matrix
				// System.out.println("Final Dep matrix without duplication and Transitive");
				for (int k = 0; k < matrixDep.length; k++) {
					for (int l = 0; l < matrixDep.length; l++) {
						// System.out.print(matrixDep[k][l] + " ");
					}
					// System.out.println("");
				}

				copiesofDep.add(matrixDep);
				List<Integer> matrixAsList = arrayToList(matrixDep);
				if (!hashset.contains(matrixAsList)) {
					copiesofDep1.add(matrixDep);
					hashset.add(matrixAsList);
				}
			}

			for (int k = 0; k < copiesofDep1.size(); k++) {

				wr.writeFileFinal(copiesofDep1.get(k), count);
				// matrixRequestCPU=newEdge.writeCPU(copiesofDep1.get(k),CPU);
				// wr.writeFilesecond(matrixRequestCPU,count);
				// matrixRequestBW=newEdge.writeBR(copiesofDep1.get(k),bw);
				// wr.writeFilesecond(matrixRequestBW,count);
				count++;
			}
		}
		/////////////////////////////////////// Remove duplication from list of dep
		/*
		 * ArrayList<int[][]> copiesofDepBackup = new ArrayList<int[][]>(); for (int i =
		 * 0; i < copiesofDep.size(); i++) { int[][] a=copiesofDep.get(i);
		 * 
		 * for (int m1 = 0; m1 < a.length; m1++) { for (int n1 = 0; n1 < a[m1].length;
		 * n1++) { //System.out.print(a[m1][n1] + " "); } //System.out.println(""); }
		 * boolean found=false; for (int j = i+1; j < copiesofDep.size(); j++) { int[][]
		 * b=copiesofDep.get(j); if(equal(a, b)){ found=true;
		 * //System.out.println("The equals are: " + "i " + i + " j" + j ); for (int m1
		 * = 0; m1 < a.length; m1++) { for (int n1 = 0; n1 < a[m1].length; n1++) {
		 * //System.out.print(a[m1][n1] + " "); } //System.out.println(""); }
		 * 
		 * //System.out.println("****************"); for (int m1 = 0; m1 < b.length;
		 * m1++) { for (int n1 = 0; n1 < b[m1].length; n1++) {
		 * //System.out.print(b[m1][n1] + " "); } //System.out.println(""); } } } if
		 * (found==false){ //System.out.println("No duplication: ");
		 * copiesofDepBackup.add(a); } } //////////////////////////////////////////
		 * //System.out.println("copiesofDep.size() before " + copiesofDep.size());
		 * copiesofDep.clear(); copiesofDep=copiesofDepBackup;
		 * 
		 * //System.out.println("copiesofDep.size() " + copiesofDep.size());
		 */
		/*
		 * for (int j = 0; j < copiesofDep.size(); j++) {
		 * wr.writeFile(copiesofDep.get(j),j); } //
		 * matrixRequest=rd.readfiles("outhere22.txt"); for (int i = 0; i <
		 * copiesofDep.size(); i++) {
		 * 
		 * // int [][] matrixDepBack; //int numberOfNodesBack=matrixRequest[i].length;
		 * // matrixDepBack=new int[numberOfNodesBack][numberOfNodesBack];
		 * 
		 * matrixRequestCPU=newEdge.writeCPU(copiesofDep.get(i),matrixRequest[i][1]);
		 * wr.writeFilesecond(matrixRequestCPU,i);
		 * matrixRequestBW=newEdge.writeBR(copiesofDep.get(i),matrixRequest[i][0]);
		 * wr.writeFilesecond(matrixRequestBW,i);
		 * 
		 * }
		 */
	}

	public ArrayList<functionnode> getAllVNFsNew(String fileName) { // Get the VNFsin the request
		ArrayList<functionnode> allVNFs = new ArrayList<functionnode>();
		int[][] adj;
		readFromFile rd = new readFromFile();
		adj = rd.readfiles(fileName);
		int numOfVertices = adj[0].length;
		int id = 0;
		for (int i = 0; i < numOfVertices; i++) {
			functionnode node = new functionnode();
			node.setNumber(id);
			int CPU = UsefulMethods.generateRandomNumberWithStep(5, 25, 5);
			node.setReqCPU(CPU);
			allVNFs.add(node);
			id++;
		}
		return allVNFs;

	}

	/////////////////////////////////////////////////////////
	public ArrayList<functionnode> getAllVNFs(String fileName) { // Get the VNFsin the request
		int[][] matrix1;
		readFromFile rd = new readFromFile();
		matrix1 = rd.readfiles(fileName);
		ArrayList<functionnode> allVNFs = new ArrayList<functionnode>();
		for (int i = 0; i < matrix1.length; i++) {
			for (int j = 0; j < matrix1[i].length; j++) {
				// System.out.print(matrix1[i][j] + " ");
			}
			// System.out.println("");
		}
		// System.out.println("******************************************************************");
		int numOfVertices = 0;
		//////////////////////////////////////////////////////////////////////////// Node
		//////////////////////////////////////////////////////////////////////////// capacities:
		// number of vertices is equal to the num of cols
		numOfVertices = matrix1[0].length;
		// CPUmatrix=new int[numOfVertices][numOfVertices];
		int startIndexCPU = numOfVertices;
		int endIndexCPU = numOfVertices * 2;
		int t = 0;
		// Get the elements of main diagonal which are the CPUs
		for (int i = startIndexCPU; i < endIndexCPU; i++) {
			//// System.out.println("###########: "+i + " t: "+ t + " Val: "+
			//// matrix1[i][t]);
			Vertex newVer = new Vertex();
			functionnode node = new functionnode();
			node.setNumber(t);
			// newVer.setNumber(t);
			node.setReqCPU(matrix1[i][t]);
			// node.setOfferedCPU(matrix1[i][t]);
			allVNFs.add(node);
			t++;
		}

		int startIndexFunc = 0;
		int endIndexFunc = numOfVertices;
		// System.out.println("VNFs in the request:");
		for (int i = 0; i < allVNFs.size(); i++) {
			// System.out.println("VNF number: " + allVNFs.get(i).getNumber()+ " Requ CPU: "
			// + allVNFs.get(i).getReqCPU());
		}
		// System.out.println("******************************************************************");

		return allVNFs;
	}

	public ArrayList<Integer> getDependents(int node) { // Passing the node number and get its dependents
		////////////////////////////////////////////////////////////////////////// Connections
		////////////////////////////////////////////////////////////////////////// for
		////////////////////////////////////////////////////////////////////////// each
		////////////////////////////////////////////////////////////////////////// vertex
		ArrayList<Integer> neighbours = new ArrayList<Integer>();
		int[] nodeRow;
		int[][] matrix1;
		readFromFile rd = new readFromFile();
		//// System.out.println("Service request read from file: " + filename);
		matrix1 = rd.readfiles(filename);
		///////////////////////////////////
		int numOfVertices = 0;
		numOfVertices = matrix1[0].length;
		int startIndexDep = 0;
		int endIndexDep = numOfVertices;
		nodeRow = new int[matrix1[0].length];

		for (int i = 0; i < nodeRow.length; i++) {
			nodeRow[i] = matrix1[node][i];
			if (nodeRow[i] == 1) {
				neighbours.add(i);
			}
		}
		return neighbours;
	}

	/////////////////////////////////////////////////
	public ArrayList<Integer> getDependentsMatrix(int node, int[][] matrix1) { // Passing the node number and get its
																				// dependents
		////////////////////////////////////////////////////////////////////////// Connections
		////////////////////////////////////////////////////////////////////////// for
		////////////////////////////////////////////////////////////////////////// each
		////////////////////////////////////////////////////////////////////////// vertex

		// System.out.println("Passing ; " + node) ;
		ArrayList<Integer> neighbours = new ArrayList<Integer>();
		int[] nodeRow;
		// int [][] matrix1;
		// readFromFile rd=new readFromFile();
		// System.out.println("filename" + filename);
		// matrix1=rd.readfiles(filename);

		///////////////////////////////////
		int numOfVertices = 0;
		numOfVertices = matrix1[0].length;
		int startIndexDep = 0;
		int endIndexDep = numOfVertices;
		nodeRow = new int[matrix1[0].length];

		for (int i = 0; i < nodeRow.length; i++) {
			nodeRow[i] = matrix1[node][i];
			if (nodeRow[i] == 1) {
				neighbours.add(i);
			}
			// else
			// //System.out.println("No Dependent for the node: " + node );
			// //System.out.println("nodeRow[i] for dep matrix: " + nodeRow[i]);
		}

		// //System.out.println("For node: " + node + " The children/Dependents are: " +
		// neighbours);

		// System.out.println("hamsayeh : " + neighbours);
		return neighbours;
	}

	///////////////////////////////////////////////////

	public ArrayList<Integer> getParents(int node) { // Passing the node number and get its Parents
		ArrayList<Integer> Parents = new ArrayList<Integer>();
		int[] nodeRow;
		int[][] matrix1;
		int[][] matrix1ForHere;
		int[] column;
		readFromFile rd = new readFromFile();
		matrix1ForHere = rd.readfiles(filename);
		int numOfVertices = 0;
		numOfVertices = matrix1ForHere[0].length;
		int startIndexDep = 0;
		int endIndexDep = numOfVertices;
		matrix1 = new int[numOfVertices][numOfVertices];
		for (int i = 0; i < matrix1[0].length; i++) {
			for (int j = 0; j < matrix1[0].length; j++) {
				matrix1[i][j] = matrix1ForHere[i][j];
			}
		}
		// System.out.println("The Dep Matrix");
		for (int i = 0; i < matrix1[0].length; i++) {
			for (int j = 0; j < matrix1[0].length; j++) {
				// System.out.print(matrix1[i][j] + " ");
			}
			// System.out.println("");
		}
		column = getColumn(matrix1, node);
		for (int i = 0; i < column.length; i++) {
			if (column[i] == 1) {
				Parents.add(i);
			}
		}
		// System.out.println("For node: " + node + " The parents are: " + Parents);

		return Parents;
	}

	public int[] getColumn(int[][] matrix1, int index) {
		readFromFile rd = new readFromFile();
		// matrix1=rd.readfiles("servReq.txt");
		int[] column = new int[matrix1[0].length]; // Here I assume a rectangular 2D array!
		for (int i = 0; i < matrix1[0].length; i++) {
			column[i] = matrix1[i][index];
		}
		return column;
	}

}
