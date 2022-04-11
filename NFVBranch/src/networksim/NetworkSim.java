  
package networksim;

import Algorithms.DynamicChain;
import Algorithms.DynamicChainWithConv;
import Algorithms.FixChain;
import Algorithms.FixChainWithConv;
import Graph_Generator.*;
import Graph_Reader.graphEdges;
import Graph_Reader.graphVertices;
import Graph_Reader.readFromFile;
import Graph_Reader.requestVNFs;
import Algorithms.UsefulMethods;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class NetworkSim {

	public static int getBW(String filename) {

		int[][] matrix1;
		readFromFile rd = new readFromFile();
		matrix1 = rd.readfiles(filename);
		int numOfVertices;
		numOfVertices = matrix1[0].length;
		// int startIndexBW=numOfVertices;
		int endIndexBW = numOfVertices * 2;

		int BW = matrix1[endIndexBW][0];
		return BW;
	}

	public static int numOfDep(String filename) {

		int[][] matrix1;
		int NumOfDep = 0;
		readFromFile rd = new readFromFile();
		matrix1 = rd.readfiles(filename);
		int numOfVertices;
		numOfVertices = matrix1[0].length;
		// int startIndexBW=numOfVertices;
		int endIndexBW = numOfVertices * 2;

		for (int i = 0; i < matrix1[0].length; i++) {
			for (int j = 0; j < matrix1[0].length; j++) {
				if (matrix1[i][j] == 1) {

					NumOfDep++;
				}
			}
		}
		return NumOfDep;
	}

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

	public static int[][] getBWMatrix(String substrate) {

		readFromFile rd = new readFromFile();
		int[][] matrix1;
		int[][] bandwidthMatrix;

		// System.out.println("sub: " + substrate);
		matrix1 = rd.readfiles(substrate);
		bandwidthMatrix = matrix1;
		int numOfVertices;
		numOfVertices = matrix1[0].length;
		// System.out.println("numOfVertices: " + numOfVertices);
		int startIndexBW = numOfVertices;
		int endIndexBW = numOfVertices * 2;

		// int[][] bandwidthMatrix=new int[matrix1[0].length][matrix1[0].length];
		for (int i = startIndexBW; i < endIndexBW; i++) {
			for (int j = 0; j < numOfVertices; j++) {
				if (matrix1[i][j] > 0) {

					int a = matrix1[i][j];
					bandwidthMatrix[i][j] = a;
				}

				// //System.out.println(bandwidthMatrix[i][j] + " ");
			}
			// //System.out.println("");
		}

		return bandwidthMatrix;

	}

	public static void generateAllServiceReq() {

	}

	public static void generateAllNetworks() {
//From here:
		readFromFile rdNew = new readFromFile();
		int[][] matrixNew = rdNew.readfiles(
				"/Users/mjalali/OneDrive - California State University, Northridge/Research/Drafts/PaperRelatedDetails/NSFNET28New.txt");
		// Method to make sure the matrix matches nsf topology
		writeInFile wr = new writeInFile();
		for (int j = 0; j < 5; j++) {
			wr.writeFile(matrixNew, j, j);
		}
		// To here: First round just to generate 5 substrate networks
		Edge newE = new Edge();
		for (int i = 0; i < 5; i++) {
			String substrateNew = "s" + i + i + ".txt";
		//	newE.writeBR(substrateNew);
			//newE.writeCPU(substrateNew);
		}

		// To here: write BR, CPU

		// int[][] funcMatrix;

		// funcMatrix= newE.writeFunc("s90.txt");
//		for (int k = 0; k < 10; k++) {
//			for (int l = 0; l < 10; l++) {
//				String substrate = "s" + k + l + ".txt";
//				newE.writeFuncLast(substrate, funcMatrix);
//				readFromFile rd = new readFromFile();
//				writeInFile wr = new writeInFile();
//				int[][] matrix1;
//				String substrate = "s" + k + l + ".txt";
//				matrix1 = rd.readfiles("NSFNET28.txt");
//				wr.writeFile(matrix1, k, l); // for creating the n different files, it is needed one time
//				int[][] bandwidthMatrix = newE.writeBR(substrate);
//				newE.writeBR(substrate);
//				int[][] bandwidthMatrix;
//				bandwidthMatrix = getBWMatrix(substrate);
//				newE.writeSpectrum(bandwidthMatrix, 55, substrate);
//				newE.writeCPU(substrate);
//
//			}
//
//		}

	}

	public static void printWholeFile() {

		readFromFile rdNew = new readFromFile();
		for (int l = 0; l < 1; l++) {
			String substrate = "s" + l + 0 + ".txt";
			int[][] matrixNew = rdNew.readfiles(substrate);
			for (int i = 0; i < matrixNew.length; i++) {

				for (int j = 0; j < matrixNew[0].length; j++) {
					System.out.print(matrixNew[i][j] + " ");
				}
				System.out.println("");
			}
		}
	}

	public static void printFromFile(String file) {

		readFromFile rdNew = new readFromFile();
		for (int l = 0; l < 1; l++) {
			String substrate = "s" + l + 0 + ".txt";
			int[][] matrixNew = rdNew.readfiles(file);
			for (int i = 0; i < matrixNew.length; i++) {

				for (int j = 0; j < matrixNew[0].length; j++) {
					System.out.print(matrixNew[i][j] + " ");
				}
				System.out.println("");
			}
		}
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

	public static void main(String[] args) throws FileNotFoundException {

		// generateAllNetworks();
		// printWholeFile();
		int numberOfSubstrateNetworks = 1;
		for (int j = 0; j < numberOfSubstrateNetworks; j++) {

			String substrate = "s" + j + ".txt";
//Do this if new requests are needed
//            for (int i = 0; i < 100; i++) {
//    			int[][] tem = DepenGenerator.serviceRequestGenereator(i);
//			}
			for (int k = 0; k < 1; k++) {
				
                String fileName = "SRT_" + k + "_" + 0 + ".txt";


				//int[][] tem = DepenGenerator.serviceRequestGenereator();
				int[][] tem = DepenGenerator.serviceRequest(fileName);
				ArrayList<functionnode> VNFs = new ArrayList<functionnode>();
				requestVNFs all_VNFs = new requestVNFs(VNFs);
				VNFs = all_VNFs.getAllVNFsNew(fileName);

				ArrayList<Vertex> Vertices = new ArrayList<>();
				ArrayList<graphEdge> Edges = new ArrayList<graphEdge>();
				graphVertices allVer = new graphVertices();
				graphEdges allEdges = new graphEdges();
				Vertices = allVer.getAllVerticesNew(VNFs.size(), substrate);
				Edges = allEdges.getAllEdgesNew(substrate);
				for (int i = 0; i < VNFs.size(); i++) {
				    System.out.println("VNF: " + VNFs.get(i).getNumber() + " CPU Request: " + VNFs.get(i).getReqCPU());
				}
				System.out.println(VNFs.size());

//				for (int i = 0; i < Vertices.size(); i++) {
//					System.out.println(
//							"V: " + Vertices.get(i).getNumber() + " CPU Offered: " + Vertices.get(i).getOfferedCPU());
//				}
//				for (int i = 0; i < Edges.size(); i++) {
//					System.out.println("Edge S: " + Edges.get(i).getSource() + " Edge D: "
//							+ Edges.get(i).getDestination() + " Ave BW: " + Edges.get(i).getBW());
//
//				}
				
				

//				File f = new File("small_road_network_01"+ ".txt");
//
//				try {
//
//					FileWriter fw = new FileWriter(f, false); // the true will append the new data
//					for (int i = 0; i < Edges.size(); i++) {
//							fw.write(Edges.get(i).getSource() +  " "+ 
//					        Edges.get(i).getDestination()+ " " + Edges.get(i).getBW());
//						fw.write("\r\n");// appends the string to the file
//					}
//					fw.close();
//				} catch (IOException e) {
//				}
				
				
				
				 int BW = UsefulMethods.generateRandomNumberWithStep(5, 25, 5);
				 BW = 5;
				 DynamicChain DC = new DynamicChain(Vertices, Edges, VNFs, fileName, BW,
				 substrate);
				 DC.mappingNew(DepenGenerator.allListsFinal);



			}
		}

	}
	
    

}
