/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithms;

import java.util.Iterator;
import java.util.List;
import Graph_Generator.*;
import Graph_Reader.requestVNFs;
import java.util.ArrayList;

/**
 *
 * @author maria
 */
public class DFSAlgorithm {

	ArrayList<Integer> priorityListNodes = new ArrayList<Integer>();
	ArrayList<Vertex> Vertices;
	ArrayList<graphEdge> Edges;
	ArrayList<functionnode> VNFs;
	ArrayList<Integer> finalPath = new ArrayList<Integer>();
	String file;

	public DFSAlgorithm(ArrayList<Vertex> Vertices, ArrayList<graphEdge> Edges, ArrayList<functionnode> VNFs,
			String file) {
		this.Vertices = Vertices;
		this.Edges = Edges;
		this.VNFs = VNFs;
		this.file = file;
	}

	public DFSAlgorithm() {
	}

	public ArrayList<Integer> getPriorityListNodes() {
		return priorityListNodes;
	}

	public void setPriorityListNodes(ArrayList<Integer> priorityListNodes) {
		this.priorityListNodes = priorityListNodes;
	}

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

	void DFSUtil(int v, boolean visited[]) {
		ArrayList<Integer> Neighbours;
		// Mark the current node as visited and print it
		visited[v] = true;
		//// System.out.print(v+">>>>>>>>>>>>> ");
		priorityListNodes.add(v);
		finalPath.add(v);

		// Recur for all the vertices adjacent to this vertex
		Iterator<Integer> i;

		// requestVNFs netReq=new requestVNFs();

		requestVNFs netReq = new requestVNFs(file);

		Neighbours = netReq.getDependents(v);

		// System.out.println("nnnnnnnnnnnnnn : " + Neighbours );
		i = Neighbours.listIterator();
		Integer n;

		while (i.hasNext()) {
			n = i.next();
			if (!visited[n])
				DFSUtil(n, visited);
		}
	}

	// The function to do DFS traversal. It uses recursive DFSUtil()
	void DFS() {
		// Mark all the vertices as not visited(set as
		// false by default in java)
		boolean visited[] = new boolean[VNFs.size()];
		ArrayList<functionnode> VNFshere = new ArrayList<functionnode>();
		ArrayList<Integer> VNFsh = new ArrayList<Integer>();
		requestVNFs netReq = new requestVNFs(file);
		ArrayList<Integer> Neighbours;

		for (int i = 0; i < VNFs.size(); i++) {
			Neighbours = netReq.getParents(VNFs.get(i).getNumber());
			// System.out.println("Neighbours " + Neighbours);
			if (Neighbours.isEmpty()) {
				VNFshere.add(0, VNFs.get(i));
			} else
				VNFshere.add(VNFs.get(i));
		}

		for (int i = 0; i < VNFshere.size(); i++) {
			// System.out.println("VNFshere: " + VNFshere.get(i).getNumber());
		}

		// Call the recursive helper function to print DFS traversal
		// starting from all vertices one by one
		for (int i = 0; i < VNFshere.size(); ++i)
			if (visited[i] == false)
				DFSUtil(VNFshere.get(i).getNumber(), visited);
	}

}
