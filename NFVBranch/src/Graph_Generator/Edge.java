/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graph_Generator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Arrays;
import java.util.Collections;

import Graph_Generator.writeInFile;
import Graph_Reader.readFromFile;

/**
 * @author mjalalitabar1
 */
public class Edge {

	private int adjacencyMatrix[][];
	private int vertexCount;
	private int BW;

	writeInFile wr = new writeInFile(adjacencyMatrix);
	readFromFile rd = new readFromFile();

	public int getBW() {
		return BW;
	}

	public void setBW(int BW) {
		this.BW = BW;
	}

	public int[][] getAdjacencyMatrix() {
		return adjacencyMatrix;
	}

	public void setAdjacencyMatrix(int[][] adjacencyMatrix) {
		this.adjacencyMatrix = adjacencyMatrix;
	}

	public int getVertexCount() {
		return vertexCount;
	}

	public void setVertexCount(int vertexCount) {
		this.vertexCount = vertexCount;
	}

	public Edge() {
		this.vertexCount = vertexCount;
		adjacencyMatrix = new int[vertexCount][vertexCount];
	}

	public graphEdge addEdge(int laneId, int sourceLocNo, int destLocNo, int weight, int bandwidth) {
		graphEdge edge = new graphEdge(laneId, sourceLocNo, destLocNo, weight, bandwidth);
		return edge;
	}

//    public void addEdge(int i, int j) {   
//        if (i >= 0 && i < vertexCount && j > 0 && j < vertexCount) {      
//            adjacencyMatrix[i][j] = 1;
//            adjacencyMatrix[j][i] = 1;
//         
//        }
//    }

	public void removeEdge(int i, int j) {
		if (i >= 0 && i < vertexCount && j > 0 && j < vertexCount) {
			adjacencyMatrix[i][j] = 0;
			adjacencyMatrix[j][i] = 0;
		}
	}

	public int isEdge(int i, int j) {
		if (i >= 0 && i < vertexCount && j > 0 && j < vertexCount) {
			return adjacencyMatrix[i][j];
		} else {
			return 0;
		}
	}

	public void displayGraph() throws FileNotFoundException {

		int[][] matrix1;
		ArrayList<Integer> FuncDep = new ArrayList<Integer>();
		matrix1 = rd.readfiles("adjMat.txt");

		// FuncDep=generateRand(2,matrix1);

		// //System.out.println("FuncDep"+FuncDep);
		// //System.out.println( matrix1.length);
		// writeFunc(matrix1.length, matrix1[0].length);
		// setAdjacencyMatrix(matrix1);
		// wr.writeFileFunc(adjacencyMatrix);
		// writeFunc(adjacencyMatrix);
		//// System.out.print(" Original adj Matrix " +matrix1.length);
		// //System.out.println("here");
		// each time when writing in the file comment this part to here
//             for (int i = 0; i < adjacencyMatrix.length; i++) {
//                for (int j = 0; j < adjacencyMatrix.length; j++) {
//               
//                   //System.out.print(adjacencyMatrix[i][j] + " ");    
//                  
//                }
//                //System.out.println("");
//                
//            }
//            //System.out.println("");
//            //System.out.print(" BW Matrix ");  
//            //System.out.println("");
//            //Now having the adj matrix, in the writebr function for any cell of the matrix that is 1 
//            //generate a random number and set it as the new value for the cell
//            writeBR(adjacencyMatrix);             
//            //System.out.println("");
//            //System.out.print(" CPU Matrix "); 
//            //System.out.println("");
//            writeCPU(adjacencyMatrix);
//            //System.out.print(" Function Matrix "); 
//            //System.out.println("");

	}
	// write the BW for the edges

	public int[][] writeBR(String network) {
		
		
		try {
		int[][] MatrixBR= rd.readfiles(network); // Read the topology

		for (int i = 0; i < MatrixBR.length; i++) {
			for (int j = 0; j < MatrixBR[0].length; j++) {
				if (MatrixBR[i][j] == 1) { // range of bw [5,45]
					int lower = 5;
					int upper = 45;
					int step = 5;
					int rand = (int) (Math.random() * (upper - lower + 1));
					int result1 = rand - rand % step + lower;

					MatrixBR[i][j] = result1;
					MatrixBR[j][i] = result1;
				}
				// //System.out.print(MatrixBR[i][j] + " ");
			}
			// //System.out.println("");

		}
		// System.out.println("writing in: " + network);
		wr.writeFileVirtual(MatrixBR,network);
		return MatrixBR;
		} catch (Exception e) {
			System.out.println("here");
			return null;
		}
		
	}
	/*
	 * Generate the cpu for each sub node
	 */

	public int[][] writeCPU(String network) {
		int[][] Matrix = rd.readfiles(network);
		for (int i = 0; i < Matrix.length; i++) {
			for (int j = 0; j < Matrix[0].length; j++) {
				if (i == j) { // For substrate [10,50]
					int lower = 10;
					int upper = 50;
					int step = 5;
					int rand = (int) (Math.random() * (upper - lower + 1));
					int result1 = rand - rand % step + lower;
					Matrix[i][j] = result1;

				} else {
					Matrix[i][j] = 0;
				}
			}
		}
		wr.writeFileVirtual(Matrix, network);
		return Matrix;

	}

	public int[][] writeCPUFinal() {
		int[][] Matrix;
		readFromFile rd = new readFromFile();

		Matrix = rd.readfiles("NSFNET141.txt");

		for (int i = 0; i < Matrix.length; i++) {
			for (int j = 0; j < Matrix.length; j++) {

				if (i == j) { // range of CP [5,35]
					Random rn = new Random();
					int CPU = rn.nextInt(35 - 5 + 1) + 5;
					Matrix[i][j] = CPU;

				} else
					Matrix[i][j] = 0;
				// System.out.print(Matrix[i][j] + " ");

			}
			// System.out.println("");

		}
		// writeFile(adjacencyMatrix);
		// wr.writeFileEdge(Matrix);
		return Matrix;

	}

	// generate the function for each sub node
	public int[][] writeFunc(String substrate) {
		int[][] adjacencyMatrixhere;
		int[][] matrix1;
		readFromFile rd = new readFromFile();
		// matrix1=rd.readfiles("NSFNET28.txt");
		matrix1 = rd.readfiles(substrate);
		adjacencyMatrixhere = matrix1;
		ArrayList<Integer> generatedRand = new ArrayList<Integer>();

		// System.out.println("generatedRand" + generatedRand);
		for (int i = 0; i < matrix1[0].length; i++) {
			// generatedRand= generateRand(2);
			generatedRand = generateRand(2, matrix1);
			for (int j = 0; j < matrix1[0].length; j++) {
				if (j < 1) {
					adjacencyMatrixhere[i][0] = generatedRand.get(0);
					// adjacencyMatrixhere[i][1]=generatedRand.get(1);
				} else {
					adjacencyMatrixhere[i][j] = 0;
				}
				// System.out.print(adjacencyMatrixhere[i][j] + " ");
			}
			// System.out.println("");

		}

		// wr.writeFileVirtual(adjacencyMatrixhere,substrate);

		// wr.writeFileFunc(adjacencyMatrixhere);
		return adjacencyMatrixhere;

	}

	public void writeFuncLast(String substrate, int[][] matrix2) {

		wr.writeFileVirtual(matrix2, substrate);

	}

	// generate random number without rep
	public ArrayList<Integer> generateRand(int DepRand, int[][] Matrix) {

		ArrayList<Integer> FuncDep = new ArrayList<Integer>();
		int DepRand2 = 0 + (int) (Math.random() * (((9) - 0) + 1));// Max number of depen for each node [0,NodeNum]
		// //System.out.println("The rand: "+DepRand2);
		FuncDep.add(DepRand2);
		while (FuncDep.contains(DepRand2)) {
			DepRand2 = 0 + (int) (Math.random() * (((9) - 0) + 1));// Max number of depen for each node [0,8]
		}
		FuncDep.add(DepRand2);
		// System.out.println("The list of rands: " + FuncDep);

		// //System.out.println("FuncDep" + FuncDep);
		return FuncDep;
	}

	public int[][] writeSpectrum(int[][] MatrixBR, int maxSpectrum, String substrate) {

		// System.out.println("Updating spectrum for: " + substrate);

		int bandwidth = 0;
		int[][] matrixSpectrum = new int[MatrixBR[0].length][MatrixBR[0].length];

		int startIndex = MatrixBR[0].length;
		int endIndex = MatrixBR[0].length * 2;

		int counterRow = 0;
		int counterColumn = 0;

		for (int i = startIndex; i < endIndex; i++) {
			for (int j = 0; j < MatrixBR[0].length; j++) {
				// System.out.println("[i,j]=["+counterRow+","+counterColumn+"]");
				if (MatrixBR[i][j] != 0) { // spectrum availability

					int rand = (int) (Math.random() * (maxSpectrum - MatrixBR[i][j]));
//                    int result1 = rand - rand % step + lower;
					// System.out.println("Random i-j = "+i+"-"+j+" = "+rand);

					matrixSpectrum[counterRow][counterColumn] = rand;
					matrixSpectrum[counterColumn][counterRow] = rand;
				}

				counterColumn++;
				// //System.out.print(MatrixBR[i][j] + " ");
			}

			counterRow++;
			counterColumn = 0;
			// //System.out.println("");

		}

		for (int[] row : matrixSpectrum) {
			// System.out.println("Row:\t"+Arrays.toString(row));
		}

		// writeFile(adjacencyMatrix);
		wr.writeFileVirtual(matrixSpectrum, substrate);
		// wr.writeFile(adjacencyMatrix,1);
		return MatrixBR;
	}

}
