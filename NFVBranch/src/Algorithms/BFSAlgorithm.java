/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithms;

import Graph_Generator.Vertex;
import Graph_Generator.functionnode;
import Graph_Generator.graphEdge;
import Graph_Reader.readFromFile;
import Graph_Reader.requestVNFs;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author mjalalitabar1
 */
public class BFSAlgorithm {

	ArrayList<Vertex> Vertices;
	ArrayList<graphEdge> Edges;
	ArrayList<functionnode> VNFs;
	String file;

	public ArrayList<Vertex> getVertices() {
		return Vertices;
	}

	public void setVertices(ArrayList<Vertex> Vertices) {
		this.Vertices = Vertices;
	}

	public ArrayList<graphEdge> getEdges() {
		return Edges;
	}

	public void setEdges(ArrayList<graphEdge> Edges) {
		this.Edges = Edges;
	}

	public ArrayList<functionnode> getVNFs() {
		return VNFs;
	}

	public void setVNFs(ArrayList<functionnode> VNFs) {
		this.VNFs = VNFs;
	}

	public BFSAlgorithm(ArrayList<Vertex> Vertices, ArrayList<graphEdge> Edges, ArrayList<functionnode> VNFs,
			String file) {
		this.Vertices = Vertices;
		this.Edges = Edges;
		this.VNFs = VNFs;
		this.file = file;
	}

	public BFSAlgorithm(ArrayList<Vertex> Vertices, ArrayList<graphEdge> Edges, ArrayList<functionnode> VNFs) {
		this.Vertices = Vertices;
		this.Edges = Edges;
		this.VNFs = VNFs;
		// this.file=file;
	}

	public Boolean isReachable(ArrayList<Integer> virtualNodes, int s, int d) {
		LinkedList<Integer> temp;
		List<Integer> Neighbours;

		// Mark all the vertices as not visited(By default set
		// as false)
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

			requestVNFs netReq = new requestVNFs(file);

			// requestVNFs netReq=new requestVNFs();

			int[][] matrix1;
			int[][] matrix2;
			int NumOfDep = 0;
			readFromFile rd = new readFromFile();
			matrix1 = rd.readfiles(file);
			int numOfVertices;
			numOfVertices = matrix1[0].length;
			// int startIndexBW=numOfVertices;
			int endIndexBW = numOfVertices;
			matrix2 = new int[numOfVertices][numOfVertices];
			for (int ir = 0; ir < endIndexBW; ir++) {
				for (int j = 0; j < endIndexBW; j++) {

					matrix2[ir][j] = matrix1[ir][j];
				}
			}

			Neighbours = netReq.getDependentsMatrix(s, matrix2);

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

}
