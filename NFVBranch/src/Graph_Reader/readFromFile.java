/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graph_Reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

/**
 *
 * @author mjalalitabar1
 */
public class readFromFile {

	private int adjacencyMatrix[][];

	public readFromFile() {
		this.adjacencyMatrix = adjacencyMatrix;
	}

	public int[][] getAdjacencyMatrix() {
		return adjacencyMatrix;
	}

	public void setAdjacencyMatrix(int[][] adjacencyMatrix) {
		this.adjacencyMatrix = adjacencyMatrix;
	}

	/*
	 * Read the matrix from the file and return it as an integer matrix
	 */
	public int[][] readfiles(String file) {

		String filetoprocess;
		if (file == "adjMat.txt") {
			filetoprocess = "adjMat.txt";
		} else if (file == "servReq.txt") {
			filetoprocess = "servReq.txt";
		} else if (file == "serviceRequest.txt") {
			filetoprocess = "serviceRequest.txt";
		} else if (file == "outhere22.txt") {
			filetoprocess = "outhere22.txt";
		}
		else {

			filetoprocess = file;
		}

		//System.out.println("filetoprocess: here" + filetoprocess);
		String[][] matrix;
		try {
			// read the number of lines to get number of rows for adj matrix
			BufferedReader reader = new BufferedReader(new FileReader(filetoprocess));
			int lines = 0;
			// Get the number of rows for the matrix
			while (reader.readLine() != null) {
				lines++;
			}
			reader.close();
			// Read the elements in the first line to get the number of vertices
			Scanner file12 = new Scanner(new File(filetoprocess));
			String str1 = file12.nextLine();
			String[] tokens1 = str1.split(",");
			matrix = new String[lines][tokens1.length];
			Scanner file1 = new Scanner(new File(filetoprocess));
			for (int i = 0; i < lines; i++) {
				String str = file1.nextLine();
				String[] tokens = str.split(",");
				if (tokens.length == tokens1.length) {
					for (int j = 0; j < tokens1.length; j++) {
						matrix[i][j] = tokens[j];
					}
				} else {
					for (int j = 0; j < 2; j++) {
						matrix[i][j] = tokens[j];
					}
				}
			}
		} catch (Exception e) {
			return null;
		}
		int[][] matrixInt;
		matrixInt = new int[matrix.length][matrix[0].length];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (matrix[i][j] == null) {
					matrixInt[i][j] = 0;
				} else
					matrixInt[i][j] = Integer.parseInt(matrix[i][j]);
			}
		}

		return matrixInt;

	}

}
