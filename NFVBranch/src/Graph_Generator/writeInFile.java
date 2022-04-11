/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graph_Generator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author mjalalitabar1
 */
public class writeInFile {

	private int adjacencyMatrix[][];

	public writeInFile(int[][] adjacencyMatrix) {
		this.adjacencyMatrix = adjacencyMatrix;
	}

	public int[][] getAdjacencyMatrix() {
		return adjacencyMatrix;
	}

	public void setAdjacencyMatrix(int[][] adjacencyMatrix) {
		this.adjacencyMatrix = adjacencyMatrix;
	}

	public writeInFile() {
	}

	public void writeFile(int[][] Matrix, int t, int s) {

		//// System.out.println("We are writing in: " + "SR_" + t +".txt");
		//// System.out.println("We are writing in: " + "SN_" + t + s +".txt");

		for (int i = 0; i < Matrix.length; i++) {
			for (int j = 0; j < Matrix.length; j++) {
				// System.out.println(Matrix[i][j] + " ");

			}
			// System.out.println("");
		}
		// String fileNameh2 = "adjMat.txt";
		File f = new File("s" + t + s + ".txt");

		try {

			FileWriter fw = new FileWriter(f, false); // the true will append the new data
			for (int i = 0; i < Matrix.length; i++) {
				for (int j = 0; j < Matrix.length; j++)
					// fw.write(adjacencyMatrix[i][j] +",");//appends the string to the file
					fw.write(Matrix[i][j] + ((j == Matrix[i].length - 1) ? "" : ","));
				fw.write("\r\n");// appends the string to the file
				// //System.out.print(adjacencyMatrix[i][j] + " ");
			}
			fw.close();

			// System.out.println("Done");

		} catch (IOException e) {
		}

	}
	


	public void writeFileFinal(int[][] Matrix, int t) {

		ArrayList<int[][]> allMatr = new ArrayList<int[][]>();

		for (int k = 0; k < 1; k++) {
			int[][] MatrixCPU = new int[Matrix.length][Matrix.length];
			for (int i = 0; i < Matrix.length; i++) {
				for (int j = 0; j < Matrix.length; j++) {

					if (i == j) { // range of CP [5,35]
						// for VNFs [5,25]
						int lower = 5;
						int upper = 25;
						int step = 5;
						int rand = (int) (Math.random() * (upper - lower + 1));
						int result1 = rand - rand % step + lower;
						Random rn = new Random();
						MatrixCPU[i][j] = result1;
					} else
						MatrixCPU[i][j] = 0;
				}
			}
			int[][] finalMatrix = appendArrays(Matrix, MatrixCPU);
			allMatr.add(finalMatrix);
		}
		for (int i1 = 0; i1 < allMatr.size(); i1++) {
			File f = new File("SRT_" + t + "_" + i1 + ".txt");
			try {

				FileWriter fw = new FileWriter(f, false); // the true will append the new data
				for (int i = 0; i < allMatr.get(i1).length; i++) {
					for (int j = 0; j < allMatr.get(i1)[0].length; j++)
						// fw.write(adjacencyMatrix[i][j] +",");//appends the string to the file
						fw.write(allMatr.get(i1)[i][j] + ((j == allMatr.get(i1)[i].length - 1) ? "" : ","));
					fw.write("\r\n");// appends the string to the file
					// //System.out.print(adjacencyMatrix[i][j] + " ");
				}
				fw.close();

				// System.out.println("Done");

			} catch (IOException e) {
			}
		}
	}

	public int[][] appendArrays(int[][] array1, int[][] array2) {
		int[][] ret = new int[array1.length + array2.length][];
		int i = 0;
		for (; i < array1.length; i++) {
			ret[i] = array1[i];
		}
		for (int j = 0; j < array2.length; j++) {
			ret[i++] = array2[j];		
		}
		return ret;
	}

	public void writeFilesecond(int[][] Matrix, int t) {
		//// System.out.println("MatrixMatrix" + Matrix);
		String fileNameh2 = "SR_" + t + ".txt";
		// File f = new File("SR_" + t +".txt");
		try {

			FileWriter fw = new FileWriter(fileNameh2, true); // the true will append the new data
			for (int i = 0; i < Matrix.length; i++) {
				for (int j = 0; j < Matrix.length; j++)
					// fw.write(adjacencyMatrix[i][j] +",");//appends the string to the file
					fw.write(Matrix[i][j] + ((j == Matrix[i].length - 1) ? "" : ","));
				fw.write("\r\n");// appends the string to the file
				// //System.out.print(adjacencyMatrix[i][j] + " ");
			}
			fw.close();

			// System.out.println("Done");

		} catch (IOException e) {
		}

	}

	////////////////////////////////////////

	public void writeFilesecondTrans(int[][] Matrix, String t) {

		//// System.out.println("MatrixMatrix" + Matrix);

		String fileNameh2 = t;
		// File f = new File("SR_" + t +".txt");
		try {

			FileWriter fw = new FileWriter(fileNameh2, true); // the true will append the new data
			for (int i = 0; i < Matrix[0].length; i++) {
				for (int j = 0; j < Matrix[0].length; j++)
					// fw.write(adjacencyMatrix[i][j] +",");//appends the string to the file
					fw.write(Matrix[i][j] + ((j == Matrix[i].length - 1) ? "" : ","));
				fw.write("\r\n");// appends the string to the file
				// //System.out.print(adjacencyMatrix[i][j] + " ");
			}
			fw.close();

			// System.out.println("Done");

		} catch (IOException e) {
		}

	}

	///////////////////////////////////////

	public void writeFileVirtual(int[][] Matrix, String fileNameh2) {

		// String fileNameh2 = "servReq.txt";
		// String fileNameh2 = "servReqrrr.txt";

		try {

			FileWriter fw = new FileWriter(fileNameh2, true); // the true will append the new data
			for (int i = 0; i < Matrix[0].length; i++) {
				for (int j = 0; j < Matrix[0].length; j++)
					fw.write(Matrix[i][j] + ((j == Matrix[i].length - 1) ? "" : ","));
				fw.write("\r\n");// appends the string to the file
			}
			fw.close();

			// System.out.println("Done");

		} catch (IOException e) {
			System.out.println(e.toString());
		}

	}

	//////////////////////////////////////////////////////////
	public void writeFileEdge(int[][] Matrix) {

		// String fileNameh2 = "NSFNET14.txt";
		String fileNameh2 = "NSFNET141414.txt";

		try {

			FileWriter fw = new FileWriter(fileNameh2, true); // the true will append the new data
			for (int i = 0; i < Matrix.length; i++) {
				for (int j = 0; j < Matrix.length; j++)
					// fw.write(adjacencyMatrix[i][j] +",");//appends the string to the file
					fw.write(Matrix[i][j] + ((j == Matrix[i].length - 1) ? "" : ","));
				fw.write("\r\n");// appends the string to the file
				// //System.out.print(adjacencyMatrix[i][j] + " ");
			}
			fw.close();

			// System.out.println("Done");

		} catch (IOException e) {
		}

	}

	//////////////////////////////////////////////////////////

	public void writeFileFunc(int[][] Matrix) {

		String fileNameh2 = "NSFNET14.txt";
		try {

			FileWriter fw = new FileWriter(fileNameh2, true); // the true will append the new data
			for (int i = 0; i < Matrix.length; i++) {
				for (int j = 0; j < Matrix[i].length; j++)
					// fw.write(adjacencyMatrix[i][j] +",");//appends the string to the file
					fw.write(Matrix[i][j] + ((j == 1) ? "" : ","));
				fw.write("\r\n");// appends the string to the file
				// //System.out.print(adjacencyMatrix[i][j] + " ");
			}
			fw.close();

			// System.out.println("Done");

		} catch (IOException e) {
		}

	}

	public void writeFileOriginal(int numOfNodes) {

		int[][] Matrix;
		Matrix = new int[numOfNodes][numOfNodes];

		for (int i = 0; i < numOfNodes; i++) {
			for (int j = 0; j < numOfNodes; j++) {
				Matrix[i][j] = 4;
			}

		}
		String fileNameh2 = "serviceRequest.txt";

		// String key = "file" + item;
		try {

			FileWriter fw = new FileWriter(fileNameh2, true); // the true will append the new data
			for (int i = 0; i < Matrix.length; i++) {
				for (int j = 0; j < Matrix.length; j++)
					// fw.write(adjacencyMatrix[i][j] +",");//appends the string to the file
					fw.write(Matrix[i][j] + ((j == Matrix[i].length - 1) ? "" : ","));
				fw.write("\r\n");// appends the string to the file
				// //System.out.print(adjacencyMatrix[i][j] + " ");
			}
			fw.close();

			// System.out.println("Done");

		} catch (IOException e) {
		}

	}

	public void writeFileFinal(int[][] Matrix) {

		String fileNameh2 = "final.txt";
		try {

			FileWriter fw = new FileWriter(fileNameh2, true); // the true will append the new data
			for (int i = 0; i < Matrix.length; i++) {
				for (int j = 0; j < Matrix.length; j++)
					// fw.write(adjacencyMatrix[i][j] +",");//appends the string to the file
					fw.write(Matrix[i][j] + ((j == Matrix[i].length - 1) ? "" : ","));
				fw.write("\r\n");// appends the string to the file
				// //System.out.print(adjacencyMatrix[i][j] + " ");
			}
			fw.close();

			// System.out.println("Done");

		} catch (IOException e) {
		}

	}

}
