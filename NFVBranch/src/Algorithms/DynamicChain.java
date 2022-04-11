/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithms;

import Graph_Generator.DepenGenerator;




import Graph_Generator.Vertex;

import Graph_Generator.functionnode;
import Graph_Generator.graphEdge;
import Graph_Reader.*;
import networksim.Edge;
import networksim.Eppstein;
import networksim.Graph;
import networksim.Path;
import networksim.Yen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;


public class DynamicChain {

	static ArrayList<Vertex> Vertices;
	static ArrayList<graphEdge> Edges;
	static ArrayList<functionnode> VNFs;
	int bw;
	String filename;
	static String substrateNet;

	public int getBw() {
		return bw;
	}

	public void setBw(int bw) {
		this.bw = bw;
	}

	ArrayList<functionnode> ParentList = new ArrayList<functionnode>();
	ArrayList<functionnode> ChildList = new ArrayList<functionnode>();

	public ArrayList<functionnode> getParentList() {
		return ParentList;
	}

	public void setParentList(ArrayList<functionnode> ParentList) {
		this.ParentList = ParentList;
	}

	public ArrayList<functionnode> getChildList() {
		return ChildList;
	}

	public void setChildList(ArrayList<functionnode> ChildList) {
		this.ChildList = ChildList;
	}

	public DynamicChain(ArrayList<Vertex> Vertices, ArrayList<graphEdge> Edges, ArrayList<functionnode> VNFs,
			String file, int BW, String substrate) {
		this.Vertices = Vertices;
		this.Edges = Edges;
		this.VNFs = VNFs;
		this.bw = BW;
		this.filename = file;
		this.substrateNet = substrate;

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

	public static ArrayList<functionnode> getVNFs() {
		return VNFs;
	}

	public void setVNFs(ArrayList<functionnode> VNFs) {
		this.VNFs = VNFs;
	}

	//////////////////////////////////////////////////////
	public int rankCalculatornode(int node) {

		// Calculating the rand for each node in the depen graph
		functionnode entry = null;
		functionnode exit = null;
		int node_rank = 0;
		ArrayList<Integer> Dependents;
		requestVNFs req = new requestVNFs(filename);
		// requestVNFs req=new requestVNFs();

		Dependents = req.getDependents(node);

		// ////System.out.println("In rank calculator for node: "+ node+ " the
		// dependents
		// are: "+ Dependents);

		if (Dependents.isEmpty()) {// If no dependent, the rank is equal to the CPU
			node_rank = VNFs.get(node).getReqCPU();
		}
		int sum_of_rank_successors = 0;
		Integer[] helpMax = new Integer[Dependents.size()];
		for (int k = 0; k < Dependents.size(); k++) {
			sum_of_rank_successors = sum_of_rank_successors + rankCalculatornode(Dependents.get(k));
			helpMax[k] = sum_of_rank_successors;
		}
		node_rank = VNFs.get(node).getReqCPU() + sum_of_rank_successors;
		return node_rank;
	}

	/////////////////////////////////////////////////
	public int rankCalculatornodeUpWard(int node) {
		// ////System.out.println("************************************Calculating the
		// Upward rank***************************************");

		// ////System.out.println("For node: " + node); //Calculating the rand for each
		// node
		// in the depen graph
		int node_rank = 0;
		ArrayList<Integer> ChildDC;
		requestVNFs req = new requestVNFs(filename);
		ChildDC = req.getDependents(node);
		if (ChildDC.isEmpty()) {// If no dependent, the rank is equal to the CPU
			node_rank = VNFs.get(node).getReqCPU();
		} else {
			int sum_of_rank_successors = 0;
			ArrayList<Integer> helpMaxDCUpward = new ArrayList<Integer>();
			for (int k = 0; k < ChildDC.size(); k++) {
				int t = 0;
				// ////System.out.println("Calcuing the rank for dependent: " + ChildDC.get(k));
				t = rankCalculatornodeUpWard(ChildDC.get(k));
				helpMaxDCUpward.add(t);// we need this arraylist because at the end we need the dependent with highest
				// weight
			}
			// ////System.out.println("The ranks for all dependents before sort: " +
			// helpMaxDCUpward);
			Collections.sort(helpMaxDCUpward); // low to high: ascending order
			// ////System.out.println("The ranks for all dependents after sort: " +
			// helpMaxDCUpward);
			// ////System.out.println("The highest rank between dependents: " +
			// helpMaxDCUpward.get(helpMaxDCUpward.size()-1));
			node_rank = helpMaxDCUpward.get(helpMaxDCUpward.size() - 1) + VNFs.get(node).getReqCPU();
		}
		// ////System.out.println("Final Upward rank for node:" + node + " is: " +
		// node_rank);
		// //////System.out.println("***************************************************************************");

		return node_rank;
	}

	//////////////////////////////////////////////////
	//
	public int rankCalculatornodeDownWard(int node) { // the new ranking approach
		////// System.out.println("******************************Calculating the
		////// DownWard rank**********************");
		////// System.out.println("for node: " + node);
		// Calculating the rand for each node in the depen graph
		functionnode entry = null;
		functionnode exit = null;
		int node_rank = 0;
		ArrayList<Integer> ParentsDC;
		requestVNFs req = new requestVNFs(filename);
		ParentsDC = req.getParents(node);

		// //////System.out.println("In rank calculator for node: "+ node+ " the
		// dependents
		// are: "+ Dependents);

		if (ParentsDC.isEmpty()) {// If no dependent, the rank is equal to the CPU
			node_rank = 0;
			////// System.out.println("No parents for node: " + node);
		} else {
			////// System.out.println("The list of parents: " + ParentsDC);
			int sum_of_rank_successors = 0;
			// Integer [] helpMax=new Integer[ParentsDC.size()];
			ArrayList<Integer> helpMaxDC = new ArrayList<Integer>();

			for (int k = 0; k < ParentsDC.size(); k++) {
				int t = 0;

				t = rankCalculatornodeDownWard(ParentsDC.get(k)) + VNFs.get(ParentsDC.get(k)).getReqCPU();
				helpMaxDC.add(t);

			}
			////// System.out.println("helpMaxDC" + helpMaxDC);
			Collections.sort(helpMaxDC);
			////// System.out.println("helpMaxDC" + helpMaxDC);
			node_rank = helpMaxDC.get(helpMaxDC.size() - 1);
			////// System.out.println("node_rank:" + node_rank);
		}
		////// System.out.println("node_rank:" + node_rank);
		return node_rank;
	}

	//////////////////////////////////////////////////////
	public ArrayList<functionnode> priorityList() { // First set the weight for each VNF then sort the nodes based on
		// weight and add it PL
		// PL is sorted from the highest weight to the lowest weight
		// //////System.out.println("^^^^^^^^^^^^^^^^^^^In
		// priorityList^^^^^^^^^^^^^^^^^^^^^^^^");
		ArrayList<functionnode> PL = new ArrayList<functionnode>();
		for (int i = 0; i < VNFs.size(); i++) {
			// int weightDown=rankCalculatornodeDownWard(VNFs.get(i).getNumber());
			// //////System.out.println("DownWard Rank: " + weightDown + " for node: " +
			// VNFs.get(i).getNumber());
			int weightUp = rankCalculatornodeUpWard(VNFs.get(i).getNumber());
			// //////System.out.println("Calculated Upward rank: " + weightUp + " for node:
			// " +
			// VNFs.get(i).getNumber());
			////// System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
			// //////System.out.println("Final weight: DownWard+ Upward: " + (weightDown+
			// weightUp));
			// //////System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
			VNFs.get(i).setWeight(weightUp);
			// VNFs.get(i).setWeight(weightDown+ weightUp);
			// //////System.out.println("Weight for VNF: " + VNFs.get(i).getNumber() + " is:
			// " +
			// VNFs.get(i).getWeight());
			PL.add(VNFs.get(i));
		}
		Collections.sort(PL);

		return PL;
	}

	//////////////////////////////////////////////////////
	public ArrayList<functionnode> updatePriorityList(ArrayList<functionnode> PL, functionnode toRemoveNode) {
		// Get a list and and a node. Remove the node from the PL

		ArrayList<functionnode> PLupdate = new ArrayList<functionnode>();
		PLupdate = PL;
		for (int i = 0; i < PLupdate.size(); i++) {
			if (PLupdate.get(i).getNumber() == toRemoveNode.getNumber()) {
				PLupdate.remove(i);
			}
		}

		return PLupdate;
	}

	////////////////////////////////////////////////////
	public ArrayList<Integer> updateList(ArrayList<Integer> PL, int toRemoveNode) { // Get the priority list and and a
		// node. Remove the node from the PL

		ArrayList<Integer> PLupdate = new ArrayList<Integer>();
		PLupdate = PL;
		for (int i = 0; i < PLupdate.size(); i++) {
			if (PLupdate.get(i) == toRemoveNode) {
				PLupdate.remove(i);
			}
		}

		return PLupdate;
	}
	//////////////////////////////////////////////////////

	public getFunction getNextNodeFirstGroup(findShortestPath SP, TreeMap<Integer, ArrayList<Integer>> treemap,
			ArrayList<functionnode> elementsOfGroup, int BW, Vertex head, Vertex tail, boolean flagTop) {
		ArrayList<getFunction> getFunc = new ArrayList<getFunction>();
		ArrayList<Integer> returnedFromSPNewH = new ArrayList<Integer>();
		ArrayList<Integer> returnedFromSPNewT = new ArrayList<Integer>();
		ArrayList<Integer> returnedFromSPNewHead = new ArrayList<Integer>();
		ArrayList<Integer> returnedFromSPNewTail = new ArrayList<Integer>();
		ArrayList<Integer> returnedFromSPNewFinal = new ArrayList<Integer>();
		functionnode nextNode = new functionnode();

		for (int i = 0; i < elementsOfGroup.size(); i++) {
			int currentVNF;
			functionnode currentVNFNode = new functionnode();
			currentVNF = elementsOfGroup.get(i).getNumber();
			currentVNFNode = elementsOfGroup.get(i);
			ArrayList<Integer> VerticesCurrentVNF = new ArrayList<Integer>();
			VerticesCurrentVNF = treemap.get(currentVNF);

			if (VerticesCurrentVNF == null) {
				VerticesCurrentVNF = new ArrayList<Integer>();
			}
			if (VerticesCurrentVNF.isEmpty()) {
				flagTop = false;
				////// System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				////// System.out.println("Request is rejected because there is no substrate
				////// candidate for node: nextNode + " + currentVNF);
				////// System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			}

			if (!VerticesCurrentVNF.isEmpty() && VerticesCurrentVNF != null) {

				ArrayList<Vertex> VerticesCurrentVNF_Vertex = new ArrayList<Vertex>();
				for (int j = 0; j < VerticesCurrentVNF.size(); j++) {
					VerticesCurrentVNF_Vertex.add(Vertices.get(VerticesCurrentVNF.get(j)));
				}

				if (tail == null) {
					////// System.out.println("tail is null");

					returnedFromSPNewH = SP.findShortest(VerticesCurrentVNF_Vertex, head, BW);
					if (returnedFromSPNewH == null) {
						returnedFromSPNewH = new ArrayList<Integer>();
					}
					if (returnedFromSPNewH.isEmpty()) {
						////// System.out.println(
//                                "################################################Request is rejected: list to check is empty: no path is found: flex New Child #######################################");
						flagTop = false;
					}
					if (returnedFromSPNewH != null && !returnedFromSPNewH.isEmpty() && flagTop) { // Distance from Head
						// is available
						getFunc.add(new getFunction(returnedFromSPNewH, currentVNFNode));

					}
				} else {

					returnedFromSPNewH = SP.findShortest(VerticesCurrentVNF_Vertex, head, BW);
					if (returnedFromSPNewH == null) {
						returnedFromSPNewH = new ArrayList<Integer>();
					}
					if (returnedFromSPNewH.isEmpty()) {
						////// System.out.println(
//                                "################################################Request is rejected: list to check is empty: no path is found: flex New Child #######################################");
						flagTop = false;
					}
					if (returnedFromSPNewH != null && !returnedFromSPNewH.isEmpty() && flagTop) { // Distance from Head
						// is available
						getFunc.add(new getFunction(returnedFromSPNewH, currentVNFNode));

					}

					//////////////////////////

					returnedFromSPNewT = SP.findShortest(VerticesCurrentVNF_Vertex, tail, BW);
					if (returnedFromSPNewT == null) {
						returnedFromSPNewT = new ArrayList<Integer>();
					}
					if (returnedFromSPNewT.isEmpty()) {
						////// System.out.println(
//                                "################################################Request is rejected: list to check is empty: no path is found: flex New Child #######################################");
						flagTop = false;
					}

					if (returnedFromSPNewT != null && !returnedFromSPNewT.isEmpty() && flagTop) {// Distance from Tail
						// is available
						getFunc.add(new getFunction(returnedFromSPNewT, currentVNFNode));
					}

				}
			}
			// Now we have the getFunc which has the path for all the current nodes from
			// head and tail. We want to choose the minimum
			int minDistance = 100;
			for (int iu = 0; iu < getFunc.size(); iu++) {
				getFunction currentPair = getFunc.get(iu);
				////// System.out.println("Pair that is returnedeeeeee: " + " for the VNF: "
//                        + currentPair.getCurrentNode().getNumber() + " the list: " + currentPair.getReturnSPHop());
				if (currentPair.getReturnSPHop().size() < minDistance) {
					minDistance = currentPair.getReturnSPHop().size();
					nextNode = currentPair.getCurrentNode();
					returnedFromSPNewFinal = currentPair.getReturnSPHop();
				}

			}

			// tail=Vertices.get(returnedFromSPNewFinal.get(returnedFromSPNewFinal.size()-1));

		}

		////// System.out.println("Final pair that is returned: " + " for the VNF: " +
		////// nextNode.getNumber() + " the list: "
//                + returnedFromSPNewFinal);

		return (new getFunction(returnedFromSPNewFinal, nextNode));

	}

	/////////////////////////////////////////

	public getFunction getNextNodeOtherGroups(findShortestPath SP, TreeMap<Integer, ArrayList<Integer>> treemap,
			ArrayList<functionnode> elementsOfGroup, int BW, Vertex tail, boolean flagTop) {

		// tail is the node that we want to find shortest path to

		ArrayList<getFunction> getFunc = new ArrayList<getFunction>();
		ArrayList<Integer> returnedFromSPNewT = new ArrayList<Integer>();
		ArrayList<Integer> returnedFromSPNewFinal = new ArrayList<Integer>();
		functionnode nextNode = new functionnode();

		for (int i = 0; i < elementsOfGroup.size(); i++) { // Current elements in one group
			int currentVNF;
			functionnode currentVNFNode = new functionnode();
			currentVNF = elementsOfGroup.get(i).getNumber();
			currentVNFNode = elementsOfGroup.get(i);
			ArrayList<Integer> VerticesCurrentVNF = new ArrayList<Integer>();
			VerticesCurrentVNF = treemap.get(currentVNF);

			if (VerticesCurrentVNF.isEmpty()) {
				flagTop = false;
				////// System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				////// System.out.println(
//                        "Request is rejected because there is no substrate candidate for the currentVNFNode: ");
				////// System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			}

			if (!VerticesCurrentVNF.isEmpty() && VerticesCurrentVNF != null) { // there are a number of candidates for
				// the currentVNFNode mapping

				ArrayList<Vertex> VerticesCurrentVNF_Vertex = new ArrayList<Vertex>();
				for (int j = 0; j < VerticesCurrentVNF.size(); j++) {
					VerticesCurrentVNF_Vertex.add(Vertices.get(VerticesCurrentVNF.get(j)));

					// VerticesCurrentVNF_Vertex: the substrate node candidate list
				}

				returnedFromSPNewT = SP.findShortest(VerticesCurrentVNF_Vertex, tail, BW);
				if (returnedFromSPNewT == null) {
					returnedFromSPNewT = new ArrayList<Integer>();
				}
				if (returnedFromSPNewT.isEmpty()) {
					////// System.out.println(
//                            "################################################Request is rejected: list to check is empty: no path is found: flex New Child #######################################");
					flagTop = false;
				}

				if (returnedFromSPNewT != null && !returnedFromSPNewT.isEmpty() && flagTop) {// Distance from Tail is
					// available
					getFunc.add(new getFunction(returnedFromSPNewT, currentVNFNode));
				}
			}
		}

		// Now we have the getFunc which has the path for all the current nodes from
		// tail. We want to choose the minimum
		int minDistance = 100;
		for (int i = 0; i < getFunc.size(); i++) {
			getFunction currentPair = getFunc.get(i);
			////// System.out.println("Pair that is returned: " + " for the VNF: " +
			////// currentPair.getCurrentNode().getNumber()
//                    + " the list: " + currentPair.getReturnSPHop());

			for (int j = 0; j < currentPair.getReturnSPHop().size(); j++) {

				////// System.out.println("Node on the path: " +
				////// currentPair.getReturnSPHop().get(j) +
//                        " start index: " + getNodeVer(currentPair.getReturnSPHop().get(j)).getStartIndex() +
//                        " end index: " + getNodeVer(currentPair.getReturnSPHop().get(j)).getEndIndex());

			}

			if (currentPair.getReturnSPHop().size() < minDistance) {
				minDistance = currentPair.getReturnSPHop().size();
				nextNode = currentPair.getCurrentNode();
				returnedFromSPNewFinal = currentPair.getReturnSPHop();
			}

		}
		////// System.out.println("Final pair that has the shortest distance to the
		////// tail: " + " for the VNF: "
//                + nextNode.getNumber() + " the list: " + returnedFromSPNewFinal);
		// //////System.out.println("The tail is: " + tail.getNumber());
		return (new getFunction(returnedFromSPNewFinal, nextNode));

	}

	/////////////////////////////////////////

	public void getMappingCand(ArrayList<Vertex> toCheck) {

		// int bwTotalFinal=99999;
		Vertex testBack = new Vertex();

		for (int i = 0; i < toCheck.size(); i++) {

			Vertex test = toCheck.get(i);
			int bwTotal = 0;
			int val = 0;
			for (int j = 0; j < Edges.size(); j++) {

				if (Edges.get(j).getSource() == test.getNumber() || Edges.get(j).getDestination() == test.getNumber()) {

					bwTotal += Edges.get(j).getBW();
				}

				val = bwTotal * test.getOfferedCPU();

			}

			////// System.out.println("node name: " + test.getNumber() + "result " +
			////// bwTotal);
		}

	}

	public ArrayList<ArrayList<functionnode>> groupCreator(ArrayList<functionnode> priorityListNodes) {

		requestVNFs req = new requestVNFs(filename);
		for (functionnode n : priorityListNodes) {
			ArrayList<Integer> dependentsOfCurrentNode;
			dependentsOfCurrentNode = req.getDependents(n.getNumber());
			//// System.out.print("---node " + n.getNumber() + ":");
			////// System.out.println(dependentsOfCurrentNode);
		}
		////// System.out.println("**********************************");
		ArrayList<ArrayList<functionnode>> allgroups = new ArrayList<ArrayList<functionnode>>();
		ArrayList<functionnode> group_0 = new ArrayList<functionnode>();
		functionnode firstNode = priorityListNodes.get(0);
		group_0.add(firstNode);
		allgroups.add(group_0);
		functionnode removed = priorityListNodes.remove(0);
		for (functionnode e : priorityListNodes) {
			boolean has_dependency = false;
			ArrayList<functionnode> last_group = allgroups.get(allgroups.size() - 1);
			for (functionnode n : last_group) {
				ArrayList<Integer> dependentsOfCurrentGroupElement;
				dependentsOfCurrentGroupElement = req.getDependents(n.getNumber());
				if (!(dependentsOfCurrentGroupElement.isEmpty())) {
					if (dependentsOfCurrentGroupElement.contains(e.getNumber())) {
						has_dependency = true;
					}
				}
			}
			if (has_dependency == true) {
				ArrayList<functionnode> new_group = new ArrayList<functionnode>();
				new_group.add(e);
				allgroups.add(new_group);

			} else {
				allgroups.get(allgroups.size() - 1).add(e);
			}
		}
		return allgroups;
	}

	public static void printPL(ArrayList<functionnode> PL) {
		for (int i = 0; i < PL.size(); i++) {
			System.out.print(PL.get(i).getNumber() + " ");
		}

	}

	public static List<Path> usageExample1(String graphFilename, String source, String target, int k) {
		/* Read graph from file */
		System.out.print("Reading data from file... ");
		Graph graph = new Graph(graphFilename);
		System.out.println("complete.");

		/* Compute the K shortest paths and record the completion time */
		System.out.print("Computing the " + k + " shortest paths from [" + source + "] to [" + target + "] ");
		System.out.print("using Yen's algorithm... ");
		List<Path> ksp;
		long timeStart = System.currentTimeMillis();
		Yen yenAlgorithm = new Yen();
		ksp = yenAlgorithm.ksp(graph, source, target, k);
		long timeFinish = System.currentTimeMillis();
		System.out.println("complete.");

		System.out.println("Operation took " + (timeFinish - timeStart) / 1000.0 + " seconds.");

		/* Output the K shortest paths */
		System.out.println("k) cost: [path]");
		int n = 0;
//        for (Path p : ksp) {
//            System.out.println(++n + ") " + p);
//        }
		return ksp;

	}
	 public ArrayList<Integer> sortCandidatesA(ArrayList<Integer> list){

			ArrayList<Vertex> sortedVertices = new ArrayList<>();
			ArrayList<Integer> sortedVerticesNumber = new ArrayList<>();
			for (int i = 0; i < list.size(); i++) {
				sortedVertices.add(Vertices.get(list.get(i)));
			}
	        Collections.sort(sortedVertices);
	        for (int i = 0; i < sortedVertices.size(); i++) {
	        	sortedVerticesNumber.add(sortedVertices.get(i).getNumber());
			}

	    	return sortedVerticesNumber;
	    }
	public static int selectSubstrateCandidate(ArrayList<Integer> subCands) {
		//We need to return the max of min, so accept the list
		//Sort based on the residual cap, we look for max of the min
		
		return 0;
	}

	public boolean sameBranch(ArrayList<ArrayList<Integer>> allListsFinal, int node1, int node2) {
		
		for (int i = 0; i < allListsFinal.size(); i++) {
			ArrayList<Integer> Branch = new ArrayList<>();
			Branch = allListsFinal.get(i);
			//Set<Integer> branchSet = new HashSet<>();
	        Set<Integer> branchSet = new HashSet<>(Branch);
	        if (branchSet.contains(node1) && branchSet.contains(node2)) {
	        	return true;
	        }			
		}
		
		return false;
	}
	public int[] mappingNew(ArrayList<ArrayList<Integer>> allListsFinal) {

		String graphFilename, sourceNode, targetNode;
		int K;
		boolean flagTop = true;
		int[] finalCostDC = { 0, 0 };
		ArrayList<functionnode> PL = new ArrayList<functionnode>();
		ArrayList<functionnode> PLFinal = new ArrayList<functionnode>();
		ArrayList<functionnode> SFC = new ArrayList<functionnode>();
		ArrayList<Integer> usedSub = new ArrayList<Integer>();
		ArrayList<Integer> usedSubackup = new ArrayList<Integer>();
		findShortestPath SP = new findShortestPath(Vertices, Edges, VNFs, this.substrateNet);
		int orginalSize = VNFs.size();
		PL = new ArrayList<functionnode>();
		PL = VNFs;
		TreeMap<Integer, ArrayList<Integer>> treemap;
		TreeMap<Integer, ArrayList<Integer>> treemapFinal;

		PLFinal = PL;

		treemap = SP.generateCandidateList(PL);
		treemapFinal = SP.generateCandidateList(PLFinal);
		Set<Integer> keys = treemap.keySet();// defined to provide an access to the functionodes
		for (Integer r : keys) {
			if (treemap.get(r).isEmpty()) {
				flagTop = false;
			}
		}
		functionnode firstVNF = VNFs.get(0);
		functionnode lastVNF = VNFs.get(VNFs.size() - 1);
		Vertex firstVertex = null;
		Vertex lastVertex = null;

		int first;
		int last;
		if (!flagTop == false) {
			// Mapping the source

			ArrayList<Integer> subCands = new ArrayList<Integer>();
			subCands = treemap.get(firstVNF.getNumber());
			System.out.println("Substrate candidates for Source VNF: " + firstVNF.getNumber() + " are: " + subCands);
			int lastCand = subCands.get(subCands.size() - 1);
			first = lastCand;
			System.out.println("First substrate candidate that is used for mapping the first VNF: " + first);
			firstVertex = Vertices.get(first);
			// System.out.println("*****************************************************");
			SP.removeVNF(treemap, firstVNF, firstVertex);// In this function we set the relation as well
			usedSub.add(firstVertex.getNumber());
			usedSubackup.add(firstVertex.getNumber());
			// System.out.println(
			// "*****************************************************First node
			// Mapped!****************************************************");
			System.out.println("PL before removing the first VNF : ");
			printPL(PL);
			System.out.println();
			updatePriorityList(PL, firstVNF); // remove first VNF from priority list
			System.out.println("PL after removing the first VNF : ");
			printPL(PL);
			System.out.println();
			SFC.add(firstVNF);
			// Mapping the destination
			
			ArrayList<Integer> subCandd = new ArrayList<Integer>();
			subCandd = treemap.get(lastVNF.getNumber());
			System.out
					.println("Substrate candidates for Destination VNF: " + lastVNF.getNumber() + " are: " + subCandd);
			
			//Here we have to make sure the second cand is not on same branch
			//int lastCandd = subCandd.get(subCandd.size() - 1);
			int t = subCandd.size() - 1;
			int lastCandd = subCandd.get(t);
			if(usedSub.contains(lastCandd)) {
				lastCandd = subCandd.get(t-1);
			}
			last = lastCandd;
			System.out.println("Second substrate candidate that is used for mapping the last VNF: " + last);
			lastVertex = Vertices.get(last);
			SP.removeVNF(treemap, lastVNF, lastVertex);// In this function we set the relation as well
			usedSub.add(lastVertex.getNumber());
			usedSubackup.add(lastVertex.getNumber());
			System.out.println("PL before removing the last VNF : ");
			printPL(PL);
			System.out.println();
			updatePriorityList(PL, lastVNF); // remove first VNF from priority list
			System.out.println("PL after removing the last VNF : ");
			printPL(PL);
			System.out.println();
			SFC.add(lastVNF);
			System.out.println("****Source & Destination mapped!********");

			// Find all the shortest paths between the hosting nodes for s and d
			// We need to map branch 0

			graphFilename = "/Users/mjalali/eclipse-workspace/KSP/src/tiny_graph_01.txt";
			sourceNode = String.valueOf(first);

			targetNode = String.valueOf(last);
			K = 50;
			List<Path> BWCompatible = new ArrayList<>();
			List<Path> ksp = usageExample1(graphFilename, sourceNode, targetNode, K);
			for (Path p : ksp) {

				List<String> nodesOnPathList = p.getNodes();
				List<Edge> edgesOnPathList = p.getEdges();
				// System.out.println(edgesOnPathList);

				List<Integer> nodesOnPathListIntegers = new ArrayList<>();
				for (int i = 0; i < nodesOnPathList.size(); i++) {
					nodesOnPathListIntegers.add(Integer.parseInt(nodesOnPathList.get(i)));
				}
				boolean result = satisfyBW(edgesOnPathList, this.bw);
				if (result) {
					BWCompatible.add(p);
				}
				//System.out.println(++n + ") " + p);
				//System.out.println("******************************");

			}
			// List of the paths that satisfy BW
			System.out.println("^^^^^^^^^^^^^^^^^^^^^");

			System.out.println("List of BW compatible");

			for (Path path : BWCompatible) {
				System.out.println(path);

			}
			//System.out.println(this.bw);

			// s and d mapped, now rest of branch
			for (int i = 0; i < DepenGenerator.allListsFinal.size(); i++) {
				System.out.println(DepenGenerator.allListsFinal.get(i));
			}
			//Mapping the branch 0 is different:
			//Find the length of branch zero:
			int firstBranchUpdatedSize = DepenGenerator.allListsFinal.get(0).size()-2;
			//Find the very first shortest path that has enough nodes
			int pathIndex = pathMatch (BWCompatible, firstBranchUpdatedSize);
			//System.out.println("Selected path: " + BWCompatible.get(pathIndex));
			
			//System.out.println(SFC.size());
			Path result = CPUCheck( BWCompatible,pathIndex,0);

			while (result == null) {
				if (BWCompatible.size() == 0) {
					System.out.println("No path found at all!!! " );
					break;
				}
				BWCompatible.remove(pathIndex);
				pathIndex = pathMatch (BWCompatible, firstBranchUpdatedSize);
				result = CPUCheck( BWCompatible,pathIndex,0);
			}
			
		        System.out.println("***********");

			    System.out.println("Final selcted path: \n"+ result );
			    System.out.println("Branch: "+ DepenGenerator.allListsFinal.get(0) );

			    //Branch zero mapped
//			    for (int i = 0; i < result.getNodes().size(); i++) {
//			    	int t2= Integer.parseInt(result.getNodes().get(i));
//				    System.out.println(Vertices.get(t2).getOfferedCPU());
//				}

			    int selectedNode = 0;
			    System.out.println("***********");
			    int node = 0;
			

				for (int i = 1; i < DepenGenerator.allListsFinal.get(0).size() - 1; i++) {
					node = DepenGenerator.allListsFinal.get(0).get(i);

					String substrateNode = result.getNodes().get(i);
					selectedNode = Integer.parseInt(substrateNode);
					int index = -1;
					for (int j = 0; j < VNFs.size(); j++) {

						if (VNFs.get(j).getNumber() == node) {
							index = j;
						}
					}
					

				//Here we have to update CPU for sub node
					Vertex r = Vertices.get(selectedNode);
					SP.removeVNF(treemap, VNFs.get(index), r);// In this function we set the relation as well
					usedSub.add(r.getNumber());
					SFC.add(VNFs.get(index));



				}	
				//These are going to be ExcludedNodes;
				System.out.println("Used substrate nodes so far: " + usedSub);
				ArrayList<OriginalMap> originalRelation = SP.getOriginalRelation();
		        for (OriginalMap om : originalRelation) {
		        	
	                System.out.println("Mapping relation : " + " VNF: " + om.getKey().getNumber() + " SN: " + om.getValue().getNumber());
		        }
		        List<Integer> branchMerge = new ArrayList<>();
		        for (int i = 0; i < originalRelation.size(); i++) {
					if (i == 2) {
						branchMerge.add(originalRelation.get(i).getValue().getNumber());
					}
					if (i == originalRelation.size()-1) {
						branchMerge.add(originalRelation.get(i).getValue().getNumber());
					}
				}
				System.out.println("Branch and merge points: " + branchMerge);
				System.out.println("Mapping other branches****************** ");

				
				graphFilename = "/Users/mjalali/eclipse-workspace/KSP/src/tiny_graph_01.txt";
				sourceNode = String.valueOf(branchMerge.get(0));

				targetNode = String.valueOf(branchMerge.get(1));
				K = 50;
				
				List<Path> BWCompatible1 = new ArrayList<>();
				List<Path> ksp1 = usageExample1(graphFilename, sourceNode, targetNode, K);
				int n1 = 0;
				for (Path p : ksp1) {

					List<String> nodesOnPathList = p.getNodes();
					List<Edge> edgesOnPathList = p.getEdges();
					// System.out.println(edgesOnPathList);

					List<Integer> nodesOnPathListIntegers = new ArrayList<>();
					for (int i = 0; i < nodesOnPathList.size(); i++) {
						nodesOnPathListIntegers.add(Integer.parseInt(nodesOnPathList.get(i)));
					}
					boolean result1 = satisfyBW(edgesOnPathList, this.bw);
					if (result1) {
						BWCompatible1.add(p);
					}
					//System.out.println(++n + ") " + p);
					//System.out.println("******************************");
					
					//System.out.println("List of BW compatible new");

//					for (Path path : BWCompatible1) {
//						System.out.println(path);
//
//					}
					
					
					

				}
				for (int i = 1; i < DepenGenerator.allListsFinal.size(); i++) {
				//	System.out.println(DepenGenerator.allListsFinal.get(i));
					int BranchUpdatedSize = DepenGenerator.allListsFinal.get(i).size()-4;
					//System.out.println(BranchUpdatedSize);

					int pathIndexBranch = pathMatch (BWCompatible1, BranchUpdatedSize);
					Path resultBranch = CPUCheck( BWCompatible1,pathIndexBranch,i);

					while (resultBranch == null) {
						if (BWCompatible1.size() == 0) {
							System.out.println("No path found at all!!! " );
							break;
						}
						BWCompatible1.remove(pathIndexBranch);
						pathIndexBranch = pathMatch (BWCompatible1, BranchUpdatedSize);
						resultBranch = CPUCheck( BWCompatible1,pathIndexBranch,i);
					}
					    System.out.println("Final selcted path: \n"+ resultBranch);
					    System.out.println("Branch: "+ DepenGenerator.allListsFinal.get(i) );
					    System.out.println("***********");
					    
					

						for (int m = 2; m < DepenGenerator.allListsFinal.get(i).size() - 2; m++) {
							List<Integer> subNodesForThisBranch = new ArrayList<>();
							node = DepenGenerator.allListsFinal.get(i).get(m);
						    //System.out.println(node);

							int nodetoPick = m;
							String substrateNode = resultBranch.getNodes().get(nodetoPick);
							selectedNode = Integer.parseInt(substrateNode);
							if(subNodesForThisBranch.contains(selectedNode)) {
								nodetoPick = nodetoPick+1;
							    substrateNode = resultBranch.getNodes().get(nodetoPick);
								selectedNode = Integer.parseInt(substrateNode);
							}
							int index = -1;
							for (int j = 0; j < VNFs.size(); j++) {

								if (VNFs.get(j).getNumber() == node) {
									index = j;
								}
							}


						//Here we have to update CPU for sub node
							Vertex r = Vertices.get(selectedNode);

							SP.removeVNF(treemap, VNFs.get(index), r);// In this function we set the relation as well
							usedSub.add(r.getNumber());
							subNodesForThisBranch.add(r.getNumber());
							SFC.add(VNFs.get(index));

						}
					    
					    
				}

				 originalRelation = SP.getOriginalRelation();
		        for (OriginalMap om : originalRelation) {
		        	
	                System.out.println("Mapping relation : " + " VNF: " + om.getKey().getNumber() + " SN: " + om.getValue().getNumber());
		        }
		        

		}

		return finalCostDC;

	}

	public static Path CPUCheck(List<Path> BWCompatible, int pathIndex,int k) {
		int selectedNode = 0;
		int node = 0;
		for (int i = 1; i < DepenGenerator.allListsFinal.get(k).size() - 1; i++) {
			node = DepenGenerator.allListsFinal.get(k).get(i);
			String substrateNode = BWCompatible.get(pathIndex).getNodes().get(i);
			selectedNode = Integer.parseInt(substrateNode);

			int index = 0;
			for (int j = 0; j < VNFs.size(); j++) {
				if (VNFs.get(j).getNumber() == node) {
					index = j;
				}
			}
			if (Vertices.get(selectedNode).getOfferedCPU() < VNFs.get(index).getReqCPU()) {
				return null;
			} 

			
		}
		return BWCompatible.get(pathIndex);

	}
	
	public static int pathMatch (List<Path> BWCompatible, int branchUpdatedSize) {
		int pathIndex = -1;
		for (int i = 0; i < BWCompatible.size(); i++) {
			int size = BWCompatible.get(i).size()-2;
			if (size >= branchUpdatedSize) {
				pathIndex = i;
				return pathIndex;
			}
		}
		return pathIndex;
		
	}
	
	public Path candidatePath(int size, ArrayList<Integer> branch, List<Path> candidatePaths) {
		//First factor is length
		
		List<functionnode> VNFsOnPathListVertex = new ArrayList<>();
		for (int j = 0; j < branch.size(); j++) {
			int node = branch.get(j);
			VNFsOnPathListVertex.add(VNFs.get(node));
		}
		Collections.sort(VNFsOnPathListVertex);
		int minCPU = VNFsOnPathListVertex.get(VNFsOnPathListVertex.size()-1).getReqCPU();

		
		List<Path> potentialPaths = new ArrayList<>();
		for (int i = 0; i < candidatePaths.size(); i++) {
			if (candidatePaths.get(i).size()-2 >= size) {
				potentialPaths.add(candidatePaths.get(i));
			}			
		}
		for (int i = 0; i < potentialPaths.size(); i++) {
			List<Integer> cpuList = new ArrayList<>();
			Path p = potentialPaths.get(i);
			List<String> nodesOnPathList = p.getNodes();
			List<Vertex> nodesOnPathListVertex = new ArrayList<>();
			for (int j = 0; j < nodesOnPathList.size(); j++) {
				int node = Integer.parseInt(nodesOnPathList.get(i));
				nodesOnPathListVertex.add(Vertices.get(node));
			}
			Collections.sort(nodesOnPathListVertex);
			Vertex firstVertex = nodesOnPathListVertex.get(0);
		}
		
		return null;		
	}

	public ArrayList<Integer> sortCandidates(ArrayList<Integer> list){

		ArrayList<Vertex> sortedVertices = new ArrayList<>();
		ArrayList<Integer> sortedVerticesNumber = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			sortedVertices.add(Vertices.get(list.get(i)));
		}
        Collections.sort(sortedVertices);
        for (int i = 0; i < sortedVertices.size(); i++) {
        	sortedVerticesNumber.add(sortedVertices.get(i).getNumber());
		}

    	return sortedVerticesNumber;
    }
		

	public static boolean satisfyBW(List<Edge> edgesOnPathList, int bw) {

		for (int i = 0; i < edgesOnPathList.size(); i++) {
			if (!(edgesOnPathList.get(i).getWeight() >= bw)) {
				return false;
			}

		}
		return true;
	}



	private ArrayList<Integer> findFullPathForDC(ArrayList<ArrayList<Vertex>> mappedChainFinal,
			ArrayList<Vertex> vertices2, ArrayList<graphEdge> edges2, ArrayList<functionnode> vnFs, String filename,
			int bw, TreeMap<Integer, ArrayList<Integer>> treemapFinal, ArrayList<Integer> usedSub, int count,
			String substrateNet) {

		// System.out.println("findFullPathForDC");
		ArrayList<Integer> path = new ArrayList<Integer>();

		ArrayList<Vertex> connectedChain = getConnectedChain(mappedChainFinal);

		// System.out.println("Connected Chain for
		// DC:\t"+Arrays.toString(connectedChain.toArray()));

		for (int i = connectedChain.size() - 1; i > 0; i--) {
			ArrayList<Vertex> Vertices2 = new ArrayList<Vertex>();
			ArrayList<graphEdge> Edges2 = new ArrayList<graphEdge>();
			graphVertices allVer = new graphVertices();
			Graph_Reader.graphEdges allEdges = new graphEdges();
			Vertices2 = allVer.getAllVertices(this.VNFs.size(), this.substrateNet);
			Edges2 = allEdges.getAllEdges(this.substrateNet);

			findShortestPath shortestPath = new findShortestPath(Vertices2, Edges2, this.VNFs, this.substrateNet);

			ArrayList<Vertex> currentNode = new ArrayList<Vertex>();
			currentNode.add(connectedChain.get(i));

			ArrayList<Integer> currentPath = shortestPath.findShortest(currentNode, connectedChain.get(i - 1), bw);

			Collections.reverse(currentPath);

			////// System.out.println("DC-Path from " + connectedChain.get(i)+" to "+
			////// connectedChain.get(i+1)+":\t"+Arrays.toString(currentPath.toArray()));

			path.addAll(currentPath);
		}

		// remove duplication
		// System.out.println("DC********: Size of path w/ duplication:\t" +
		// Arrays.toString(path.toArray()));
		for (int i = 0; i < path.size() - 1; i++) {

			if (path.get(i) == path.get(i + 1)) {
				path.remove(i + 1);
			}
		}

		Collections.reverse(path);
		// System.out.println("DC*********************: Size of path w/o duplication:\t"
		// + Arrays.toString(path.toArray()));

		return path;

	}

	private ArrayList<Vertex> getConnectedChain(ArrayList<ArrayList<Vertex>> mappedChainFinal) {

		ArrayList<Vertex> connectedChain = new ArrayList<Vertex>();

		for (int i = 0; i < mappedChainFinal.size(); i++) {
			for (int j = 0; j < mappedChainFinal.get(i).size(); j++) {
				connectedChain.add(mappedChainFinal.get(i).get(j));
			}
		}

		return connectedChain;
	}

	public boolean parentPresentInList(ArrayList<functionnode> listToCheck, int toCheck) {// a node is in a list or not
		boolean check = false;
		for (int i = 0; i < listToCheck.size(); i++) {
			if (listToCheck.get(i).getNumber() == toCheck) {
				check = true;
			}
		}

		return check;
	}

	public ArrayList<functionnode> listCheckList(ArrayList<Integer> listToCheck, ArrayList<functionnode> ChildList,
			ArrayList<functionnode> PL, functionnode grandChild) {
		// To check if elements of the list are presented in another list
		// First one for loop to get the elements of temp

		boolean checkOrg = false;
		for (int i = 0; i < listToCheck.size(); i++) {
			if (parentPresentInList(ChildList, listToCheck.get(i))) {
				checkOrg = true;
			}
		}

		if (checkOrg == false) { // none of the parents are in the child list
			// now the grandChild can be added to the child list
			ChildList.add(grandChild);

		} else { // at list one parent is in the child list
			////// System.out.println("Parent of the grandchild is in the childList");
		}

		return ChildList;
	}

	public Vertex getNodeVer(int number) {
		Vertex helpVer = new Vertex();
		for (int i = 0; i < Vertices.size(); i++) {

			if (Vertices.get(i).getNumber() == number) {

				helpVer = Vertices.get(i);
			}

		}

		return helpVer;
	}

}
