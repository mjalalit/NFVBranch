/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithms;

import Graph_Generator.Vertex;
import Graph_Generator.functionnode;
import Graph_Generator.graphEdge;
import Graph_Reader.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

public class globeDynamicChain {

	ArrayList<Vertex> Vertices;
	ArrayList<graphEdge> Edges;
	ArrayList<functionnode> VNFs;
	int bw;
	String filename;
	String substrate;

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

	public globeDynamicChain(ArrayList<Vertex> Vertices, ArrayList<graphEdge> Edges, ArrayList<functionnode> VNFs,
			String file, int BW, String subs) {
		this.Vertices = Vertices;
		this.Edges = Edges;
		this.VNFs = VNFs;
		this.bw = BW;
		this.filename = file;
		this.substrate = subs;

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

		// System.out.println("In rank calculator for node: "+ node+ " the dependents
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
		// System.out.println("************************************Calculating the
		// Upward rank***************************************");

		// System.out.println("For node: " + node); //Calculating the rand for each node
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
				// System.out.println("Calcuing the rank for dependent: " + ChildDC.get(k));
				t = rankCalculatornodeUpWard(ChildDC.get(k));
				helpMaxDCUpward.add(t);// we need this arraylist because at the end we need the dependent with highest
										// weight
			}
			// System.out.println("The ranks for all dependents before sort: " +
			// helpMaxDCUpward);
			Collections.sort(helpMaxDCUpward); // low to high: ascending order
			// System.out.println("The ranks for all dependents after sort: " +
			// helpMaxDCUpward);
			// System.out.println("The highest rank between dependents: " +
			// helpMaxDCUpward.get(helpMaxDCUpward.size()-1));
			node_rank = helpMaxDCUpward.get(helpMaxDCUpward.size() - 1) + VNFs.get(node).getReqCPU();
		}
		// System.out.println("Final Upward rank for node:" + node + " is: " +
		// node_rank);
		// System.out.println("***************************************************************************");

		return node_rank;
	}

	//////////////////////////////////////////////////
	//
	public int rankCalculatornodeDownWard(int node) { // the new ranking approach
		System.out.println("******************************Calculating the DownWard rank**********************");
		System.out.println("for node: " + node);
		// Calculating the rand for each node in the depen graph
		functionnode entry = null;
		functionnode exit = null;
		int node_rank = 0;
		ArrayList<Integer> ParentsDC;
		requestVNFs req = new requestVNFs(filename);
		ParentsDC = req.getParents(node);

		// System.out.println("In rank calculator for node: "+ node+ " the dependents
		// are: "+ Dependents);

		if (ParentsDC.isEmpty()) {// If no dependent, the rank is equal to the CPU
			node_rank = 0;
			System.out.println("No parents for node: " + node);
		} else {
			System.out.println("The list of parents: " + ParentsDC);
			int sum_of_rank_successors = 0;
			// Integer [] helpMax=new Integer[ParentsDC.size()];
			ArrayList<Integer> helpMaxDC = new ArrayList<Integer>();

			for (int k = 0; k < ParentsDC.size(); k++) {
				int t = 0;

				t = rankCalculatornodeDownWard(ParentsDC.get(k)) + VNFs.get(ParentsDC.get(k)).getReqCPU();
				helpMaxDC.add(t);

			}
			System.out.println("helpMaxDC" + helpMaxDC);
			Collections.sort(helpMaxDC);
			System.out.println("helpMaxDC" + helpMaxDC);
			node_rank = helpMaxDC.get(helpMaxDC.size() - 1);
			System.out.println("node_rank:" + node_rank);
		}
		System.out.println("node_rank:" + node_rank);
		return node_rank;
	}

	//////////////////////////////////////////////////////
	public ArrayList<functionnode> priorityList() { // First set the weight for each VNF then sort the nodes based on
													// weight and add it PL
		// PL is sorted from the highest weight to the lowest weight
		// System.out.println("^^^^^^^^^^^^^^^^^^^In
		// priorityList^^^^^^^^^^^^^^^^^^^^^^^^");
		ArrayList<functionnode> PL = new ArrayList<functionnode>();
		for (int i = 0; i < VNFs.size(); i++) {
			// int weightDown=rankCalculatornodeDownWard(VNFs.get(i).getNumber());
			// System.out.println("DownWard Rank: " + weightDown + " for node: " +
			// VNFs.get(i).getNumber());
			int weightUp = rankCalculatornodeUpWard(VNFs.get(i).getNumber());
			// System.out.println("Calculated Upward rank: " + weightUp + " for node: " +
			// VNFs.get(i).getNumber());
			System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
			// System.out.println("Final weight: DownWard+ Upward: " + (weightDown+
			// weightUp));
			// System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
			VNFs.get(i).setWeight(weightUp);
			// VNFs.get(i).setWeight(weightDown+ weightUp);
			// System.out.println("Weight for VNF: " + VNFs.get(i).getNumber() + " is: " +
			// VNFs.get(i).getWeight());
			PL.add(VNFs.get(i));
		}
		Collections.sort(PL);

		for (int i = 0; i < PL.size(); i++) {
			System.out.println("Weight for VNF: after sort testing " + PL.get(i).getNumber() + " the weight: "
					+ PL.get(i).getWeight());
		}
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
				System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				System.out.println("Request is rejected because there is no substrate candidate for node: nextNode +  "
						+ currentVNF);
				System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			}

			if (!VerticesCurrentVNF.isEmpty() && VerticesCurrentVNF != null) {

				ArrayList<Vertex> VerticesCurrentVNF_Vertex = new ArrayList<Vertex>();
				for (int j = 0; j < VerticesCurrentVNF.size(); j++) {
					VerticesCurrentVNF_Vertex.add(Vertices.get(VerticesCurrentVNF.get(j)));
				}

				if (tail == null) {
					System.out.println("tail is null");

					returnedFromSPNewH = SP.findShortest(VerticesCurrentVNF_Vertex, head, BW);
					if (returnedFromSPNewH == null) {
						returnedFromSPNewH = new ArrayList<Integer>();
					}
					if (returnedFromSPNewH.isEmpty()) {
						System.out.println(
								"################################################Request is rejected: list to check is empty: no path is found: flex New Child #######################################");
						flagTop = false;
					}
					if (returnedFromSPNewH != null && !returnedFromSPNewH.isEmpty() && flagTop) { // Distance from Head
																									// is available
						getFunc.add(new getFunction(returnedFromSPNewH, currentVNFNode));

					}
				}

				else {

					// System.out.println("tailllllll");
					returnedFromSPNewH = SP.findShortest(VerticesCurrentVNF_Vertex, head, BW);
					if (returnedFromSPNewH == null) {
						returnedFromSPNewH = new ArrayList<Integer>();
					}
					if (returnedFromSPNewH.isEmpty()) {
						System.out.println(
								"################################################Request is rejected: list to check is empty: no path is found: flex New Child #######################################");
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
						System.out.println(
								"################################################Request is rejected: list to check is empty: no path is found: flex New Child #######################################");
						flagTop = false;
					}

					if (returnedFromSPNewT != null && !returnedFromSPNewT.isEmpty() && flagTop) {// Distance from Tail
																									// is available
						getFunc.add(new getFunction(returnedFromSPNewT, currentVNFNode));
					}

				}
			}

			// ArrayList<Integer> returnedFromSPNewFinal=new ArrayList<Integer>();
			// functionnode nextNode=new functionnode();

			// Now we have the getFunc which has the path for all the current nodes from
			// head and tail. We want to choose the minimum
			int minDistance = 100;
			for (int iu = 0; iu < getFunc.size(); iu++) {
				getFunction currentPair = getFunc.get(iu);
				System.out.println("Pair that is returnedeeeeee: " + " for the VNF: "
						+ currentPair.getCurrentNode().getNumber() + " the list: " + currentPair.getReturnSPHop());
				if (currentPair.getReturnSPHop().size() < minDistance) {
					minDistance = currentPair.getReturnSPHop().size();
					nextNode = currentPair.getCurrentNode();
					returnedFromSPNewFinal = currentPair.getReturnSPHop();
				}

			}

			// tail=Vertices.get(returnedFromSPNewFinal.get(returnedFromSPNewFinal.size()-1));

		}

		System.out.println("Final pair that is returned: " + " for the VNF: " + nextNode.getNumber() + " the list: "
				+ returnedFromSPNewFinal);

		return (new getFunction(returnedFromSPNewFinal, nextNode));

	}

	/////////////////////////////////////////

	public getFunction getNextNodeOtherGroups(findShortestPath SP, TreeMap<Integer, ArrayList<Integer>> treemap,
			ArrayList<functionnode> elementsOfGroup, int BW, Vertex tail, boolean flagTop) {

		// tail is the node that we want to find shortest path to

		ArrayList<getFunction> getFunc = new ArrayList<getFunction>();
		ArrayList<Integer> returnedFromSPNewT = new ArrayList<Integer>();
		ArrayList<Integer> returnedFromSPNewHead = new ArrayList<Integer>();
		ArrayList<Integer> returnedFromSPNewTail = new ArrayList<Integer>();
		ArrayList<Integer> returnedFromSPNewFinal = new ArrayList<Integer>();
		functionnode nextNode = new functionnode();
		for (int i = 0; i < elementsOfGroup.size(); i++) {

			System.out.println("elementsOfGroup.get(i).getNumber();" + elementsOfGroup.get(i).getNumber());
		}
		for (int i = 0; i < elementsOfGroup.size(); i++) { // Current elements in one group
			int currentVNF;
			functionnode currentVNFNode = new functionnode();
			currentVNF = elementsOfGroup.get(i).getNumber();
			currentVNFNode = elementsOfGroup.get(i);
			ArrayList<Integer> VerticesCurrentVNF = new ArrayList<Integer>();
			System.out.println("currentVNFNode: " + currentVNFNode.getNumber());
			System.out.println("Treemap  " + treemap);
			VerticesCurrentVNF = treemap.get(currentVNF);
			System.out.println("VerticesCurrentVNFmmmmmmm: " + VerticesCurrentVNF);
			if (VerticesCurrentVNF == null) {
				VerticesCurrentVNF = new ArrayList<Integer>();
			}
			if (VerticesCurrentVNF.isEmpty()) {
				flagTop = false;
				System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				System.out.println(
						"Request is rejected because there is no substrate candidate for the currentVNFNode: ");
				System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
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
					System.out.println(
							"################################################Request is rejected: list to check is empty: no path is found: flex New Child #######################################");
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
			System.out.println("Pair that is returned: " + " for the VNF: " + currentPair.getCurrentNode().getNumber()
					+ " the list: " + currentPair.getReturnSPHop());

			if (currentPair.getReturnSPHop().size() < minDistance) {
				minDistance = currentPair.getReturnSPHop().size();
				nextNode = currentPair.getCurrentNode();
				returnedFromSPNewFinal = currentPair.getReturnSPHop();
			}

		}
		System.out.println("Final pair that has the shortest distance to the tail: " + " for the VNF: "
				+ nextNode.getNumber() + " the list: " + returnedFromSPNewFinal);
		// System.out.println("The tail is: " + tail.getNumber());
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

			System.out.println("node name: " + test.getNumber() + "result " + bwTotal);
		}

	}

	public ArrayList<ArrayList<functionnode>> groupCreator(ArrayList<functionnode> priorityListNodes) {

		requestVNFs req = new requestVNFs(filename);
		for (functionnode n : priorityListNodes) {
			ArrayList<Integer> dependentsOfCurrentNode;
			dependentsOfCurrentNode = req.getDependents(n.getNumber());
			System.out.print("---node " + n.getNumber() + ":");
			System.out.println(dependentsOfCurrentNode);
		}
		System.out.println("**********************************");
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

	public int[] mapping() {

		ArrayList<Integer> finalPath = new ArrayList<Integer>();
		Vertex head_0 = new Vertex();
		Vertex tail_0 = null;
		boolean flagTop = true;
		int[] finalCostDC = { 0, 0 };
		ArrayList<functionnode> ParentList = new ArrayList<functionnode>();
		ArrayList<functionnode> ChildList = new ArrayList<functionnode>();
		findShortestPath SP = new findShortestPath(Vertices, Edges, VNFs, this.substrate);
		ArrayList<functionnode> ParentListback = new ArrayList<functionnode>();
		ArrayList<functionnode> ChildListback = new ArrayList<functionnode>();
		ArrayList<functionnode> PL = new ArrayList<functionnode>();
		ArrayList<functionnode> PLFinal = new ArrayList<functionnode>();
		ArrayList<Integer> PLNew = new ArrayList<Integer>();
		ArrayList<functionnode> PLNewBack = new ArrayList<functionnode>();

		ArrayList<functionnode> SFC = new ArrayList<functionnode>();
		ArrayList<Integer> usedSub = new ArrayList<Integer>();
		ArrayList<Integer> usedSubackup = new ArrayList<Integer>();
		ArrayList<Integer> usedSubackup1 = new ArrayList<Integer>();
		ArrayList<OriginalMap> originalRelation = new ArrayList<OriginalMap>();
		PL = priorityList();

		System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
		System.out.println("Sorted VNFs based ascending order of the weghits");
		for (int i = 0; i < PL.size(); i++) {
			System.out.println("Priority list element:" + PL.get(i).getNumber());

		}
		ArrayList<ArrayList<functionnode>> groups;
		// ArrayList<ArrayList<Vertex>> mappedChain;
		ArrayList<ArrayList<Vertex>> mappedChain = new ArrayList<ArrayList<Vertex>>();

		groups = groupCreator(PL);
		// chains=groups;

		PL = new ArrayList<functionnode>();

		PL = priorityList();
		System.out.println("Priority list after creating the groups");

		for (int i = 0; i < PL.size(); i++) {
			System.out.println("PL element:" + PL.get(i).getNumber());

		}

		int numOfGrpups = groups.size();
		System.out.println("numOfGrpups : " + numOfGrpups);
		int count = 0;
		for (int i = 0; i < groups.size(); i++) {
			if (groups.get(i).size() > 0) {
				count++;
			}
			for (int j = 0; j < groups.get(i).size(); j++) {

				System.out.println("Group: " + i + " Current element: " + groups.get(i).get(j).getNumber());
			}
			System.out.println("**********************************");
		}
		System.out.println("count%%%%%: " + count);
		TreeMap<Integer, ArrayList<Integer>> treemap;
		TreeMap<Integer, ArrayList<Integer>> treemapFinal;

		PLFinal = PL;

		treemap = SP.generateCandidateList(PL);
		treemapFinal = SP.generateCandidateList(PLFinal);

		System.out.println("*****************************************************");
		System.out.println("*****************************************************");
		Set<Integer> keys = treemap.keySet();// defined to provide an access to the functionodes
		for (Integer r : keys) {
			if (treemap.get(r).isEmpty()) {
				System.out.println("***********************No candidate for this functionality: flex " + r);
				flagTop = false;
			}
		}
		System.out.println("*****************************************************");

		functionnode firstVNF = null;
		// firstVNF=PL.get(0);
		firstVNF = groups.get(0).get(0);
		// ChildList.add(firstVNF);//add it into child list: starts here
		System.out.println(
				"********************************************Start mapping: ************************************************");
		System.out.println("First VNF to be mapped is VNF: " + firstVNF.getNumber());
		requestVNFs netReq = new requestVNFs(filename);
		// Map ð‘‰1 to the substrate candidate with enough CPU
		Vertex firstVertex = null;
		int first;
		if (!flagTop == false) {
			ArrayList<Integer> subCands = new ArrayList<Integer>();
			subCands = treemap.get(firstVNF.getNumber());
			System.out.println("subCands " + subCands);
			int lastCand = subCands.get(subCands.size() - 1);
			first = lastCand;
			System.out.println("first substrate candidate that is used for mapping the first VNF: " + first);
			firstVertex = Vertices.get(first);
			System.out.println("*****************************************************");
			SP.removeVNF(treemap, firstVNF, firstVertex);// In this function we set the relation as well
			usedSub.add(firstVertex.getNumber());
			usedSubackup.add(firstVertex.getNumber());
			System.out.println(
					"*****************************************************First node Mapped!****************************************************");
			// Add children of ð‘‰1 to the child list if its parent is not in the child
			// list
			for (int i = 0; i < PL.size(); i++) {
				System.out.println("PL List before removing the first VNF : " + PL.get(i).getNumber());
			}
			updatePriorityList(PL, firstVNF); // remove first vnf from priority list

			System.out.println("*******************************************");
			for (int i = 0; i < PL.size(); i++) {
				System.out.println("PL List after removing the first VNF  : " + PL.get(i).getNumber());
			}

			SFC.add(firstVNF);

			head_0 = firstVertex;
			groups.get(0).remove(0);

			ArrayList<Vertex> ch = new ArrayList<Vertex>();
			ch.add(firstVertex); // add the mapped VNF to the ch for g0
			// chains.add(ch);

			mappedChain.add(ch);
			System.out.println("************Groups after removing the first element**********************");
			for (int i = 0; i < groups.size(); i++) {
				for (int j = 0; j < groups.get(i).size(); j++) {

					System.out.println("Group: " + i + " Current element: " + groups.get(i).get(j).getNumber());
				}
				System.out.println("**********************************");
			}

			for (int i = 0; i < mappedChain.size(); i++) {
				for (int j = 0; j < mappedChain.get(i).size(); j++) {

					System.out.println(
							"mappedChain: " + i + " Current element: " + mappedChain.get(i).get(j).getNumber());
				}
				System.out.println("**********************************");
			}

			System.out.println(
					"*****************************************************Start Mapping the rest of the nodes****************************************************");

		} else {

			System.out.println("Request rejected at the very first node");

			finalCostDC[0] = 0;
			finalCostDC[1] = 0;
		}

		/// Starting the groups:
		getFunction selectedVNF;
		getFunction selectedVNF_g1;
		getFunction selectedVNF_rest;
		functionnode currentNode = null;
		Vertex lastTail = new Vertex();
		int groupSizeOrg = 0;
		int groupSizeOrg0 = 0;
		int sizeg1 = 0;

		if (groups.size() > 1) {
			groupSizeOrg = groups.get(1).size();
		}
		ArrayList<Vertex> ch1 = new ArrayList<Vertex>();
		for (int k = 0; k < groups.size(); k++) {
			System.out.print("......................................starting group: " + k
					+ "....................................................");

			if (k == 0) {
				groupSizeOrg0 = groups.get(0).size();
			}

			System.out.println();

			if (groups.size() > 1) {
				sizeg1 = groups.get(1).size();
			}

			if (k == 0 && (usedSub.size() == 1) && (groups.get(0).size() == 0) && (groups.get(1).size() > 0)
					&& (sizeg1 == groups.get(1).size())) {

				System.out.println("catched");

				System.out.println("We are in special ");
				System.out.println("**********usedSub : " + usedSub);
				// Rest of the elements, starting group1, element 1, No we just get the distance
				// to the updated tail
				selectedVNF_rest = getNextNodeOtherGroups(SP, treemap, groups.get(1), bw,
						getNodeVer(usedSub.get(usedSub.size() - 1)), flagTop);
				if (selectedVNF_rest.getCurrentNode() == null || selectedVNF_rest.getReturnSPHop().isEmpty()) {
					flagTop = false;
					int[] toret = { 0, 0 };
					return toret;
				}
				functionnode removeVNFRest;
				Vertex removeVertexNodeRest;
				removeVNFRest = selectedVNF_rest.getCurrentNode();
				int temphere = selectedVNF_rest.getReturnSPHop().get(selectedVNF_rest.getReturnSPHop().size() - 1);

				int t = selectedVNF_rest.getReturnSPHop().get((selectedVNF_rest.getReturnSPHop()).size() - 1);
				removeVertexNodeRest = getNodeVer(t);
				lastTail = removeVertexNodeRest;
				int tempCostRest = 0;
				tempCostRest = SP.updateEdgeBandwidth(selectedVNF_rest.getReturnSPHop(), bw); // Update the BW of
																								// the links on the
																								// returned path
				for (int i = 0; i < selectedVNF_rest.getReturnSPHop().size(); i++) {
					finalPath.add(selectedVNF_rest.getReturnSPHop().get(i));
				}

				finalCostDC[0] = tempCostRest;
				SP.removeVNF(treemap, removeVNFRest, removeVertexNodeRest);// In this function we set the relation
																			// as well,update treemap,update CPU
				if (k == 1) {

					mappedChain.get(1).add(removeVertexNodeRest);
				} else {

					ArrayList<Vertex> chk = new ArrayList<Vertex>();
					mappedChain.add(chk);
					mappedChain.get(k).add(removeVertexNodeRest);

				}

				usedSub.add(temphere);
				usedSubackup.add(temphere);
				SFC.add(removeVNFRest);
				// Now remove the element from group
				updatePriorityList(groups.get(1), removeVNFRest);

				System.out.println("Current UsedSub for the rest: " + usedSub);

			}
			while (groups.get(k).size() > 0 && flagTop) {

				if (k == 0) { // For the elements of the group 0, the mapping is different:
					// Sub chain for group 0
					System.out.println("herrr");

					if (usedSub.size() == 1) { // Just one VNF is mapped up to now
						// Selected VNF is a pair of the VNF and the shortest path
						selectedVNF = getNextNodeOtherGroups(SP, treemap, groups.get(0), bw,
								getNodeVer(usedSub.get(usedSub.size() - 1)), flagTop); // After this function we know
																						// which node is mapped next and
																						// we have its SP

						if (selectedVNF.getCurrentNode() == null || selectedVNF.getReturnSPHop().isEmpty()) {
							flagTop = false;
							int[] toret = { 0, 0 };
							return toret;
						}
						int temp;

						temp = selectedVNF.getReturnSPHop().get((selectedVNF.getReturnSPHop()).size() - 1);
						// Add the node at the end of Temp list

						System.out.println("Added node to the end of the used sub:" + temp);
						usedSub.add(temp);
						usedSubackup.add(temp);
						System.out.println("Updated usedSub:" + usedSub);
						// System.out.println("usedSubackup044444" + usedSubackup);
					}
					// For each element in group 0 get the distance from head and tail

					else { // Still in group 0 but we have more than one element
						selectedVNF = getNextNodeOtherGroups(SP, treemap, groups.get(0), bw,
								getNodeVer(usedSub.get(usedSub.size() - 1)), flagTop);

						if (selectedVNF.getCurrentNode() == null || selectedVNF.getReturnSPHop().isEmpty()) {
							flagTop = false;
							int[] toret = { 0, 0 };
							return toret;
						}
						int temp1;
						int temp2;
						boolean toSwithc = false;
						temp1 = selectedVNF.getReturnSPHop().get(0); // First element of the returned list
						temp2 = selectedVNF.getReturnSPHop().get(selectedVNF.getReturnSPHop().size() - 1);
						if (temp1 == usedSub.get(0)) { // SP was found to the head of the current usedSub, so the
														// usedSub should be updated from the start by adding to element
														// 0
							usedSub.add(0, temp2);
							toSwithc = true;
							usedSubackup.add(temp2);
						}

						if (temp1 == usedSub.get(usedSub.size() - 1)) { // SP was found to the head of the current
																		// usedSub, so the usedSub should be updated
																		// from the start by adding to element 0
							usedSub.add(temp2);
							usedSubackup.add(temp2);
						}

						// Check if the shortest distance was found to the end of used sub or start of
						// the used sub

					}
					functionnode removeVNF;
					Vertex removeVertexNode;

					removeVNF = selectedVNF.getCurrentNode();
					removeVertexNode = Vertices
							.get(selectedVNF.getReturnSPHop().get((selectedVNF.getReturnSPHop()).size() - 1));
					int tempCost = 0;
					tempCost = SP.updateEdgeBandwidth(selectedVNF.getReturnSPHop(), bw); // Update the BW of the links
																							// on the returned path
					for (int i = 0; i < selectedVNF.getReturnSPHop().size(); i++) {
						finalPath.add(selectedVNF.getReturnSPHop().get(i));
					}
					finalCostDC[0] = tempCost;
					SP.removeVNF(treemap, removeVNF, removeVertexNode);// In this function we set the relation as
																		// well,update treemap,update CPU
					// System.out.println("removeVertexNode.getNumber() "+
					// removeVertexNode.getNumber());
					// usedSub.add(removeVertexNode.getNumber());
					System.out.println("Current UsedSub in g0: " + usedSub);
					SFC.add(removeVNF);
					// Now remove the element from group

					for (int i = 0; i < groups.get(0).size(); i++) {
						System.out.println("groups 0: before " + groups.get(0).get(i).getNumber());
					}
					updatePriorityList(groups.get(0), removeVNF);
					// ch0.add(removeVertexNode);
					mappedChain.get(0).add(removeVertexNode);
					for (int i = 0; i < groups.get(0).size(); i++) {
						System.out.println("groups 0: after " + groups.get(0).get(i).getNumber());
					}

					for (int i = 0; i < mappedChain.get(0).size(); i++) {
						System.out.println("mappedChain : after " + mappedChain.get(0).get(i).getNumber());
					}

				}

				// groupSizeOrg >> original size for group 1, we check this one to differentiate
				// first element of group 1 from rest of them
				else if (k == 1 && (groups.get(1).size() == groupSizeOrg)) { // For the very first element of this group
																				// we find the SP to the both head and
					System.out.println("come here"); // tail of the group 0

					selectedVNF_g1 = getNextNodeOtherGroups(SP, treemap, groups.get(1), bw,
							getNodeVer(usedSub.get(usedSub.size() - 1)), flagTop); // After this function we know which
																					// node is mapped next and we have
																					// its SP
					if (selectedVNF_g1.getCurrentNode() == null || selectedVNF_g1.getReturnSPHop().isEmpty()) {
						flagTop = false;
						int[] toret = { 0, 0 };
						return toret;
					}

					int temp1g1;
					int temp2g2;

					temp1g1 = selectedVNF_g1.getReturnSPHop().get(0); // First element of the returned list
					temp2g2 = selectedVNF_g1.getReturnSPHop().get(selectedVNF_g1.getReturnSPHop().size() - 1);

					System.out.println("temp1g1: " + temp1g1);
					System.out.println("temp1g2: " + temp2g2);
					System.out.println("usedSub usedSub" + usedSub);
					System.out.println("usedSubackup02222222222" + usedSubackup);
					/*
					 * if (temp1g1 == usedSub.get(0) ) { // SP was found to the head of the current
					 * usedSub, so the usedSub // should be updated from the start by adding to
					 * element 0 // usedSub.add(0, temp2g2); // Direction of chain is decided here
					 * 
					 * System.out.println("usedSubackup before adding used sub" + usedSubackup); //
					 * usedSubackup=usedSub; usedSubackup.clear(); usedSubackup.addAll(usedSub);
					 * 
					 * System.out.println("usedSubackup after adding used sub" + usedSubackup);
					 * Collections.reverse(usedSub); System.out.println("usedSubackup after" +
					 * usedSubackup); usedSub.add(temp2g2); System.out.println("usedSub usedSub" +
					 * usedSub); usedSubackup.add(temp2g2); System.out.println("usedSub usedSub1" +
					 * usedSub);
					 * 
					 * }
					 */
					System.out.println("usedSssffffubackup0" + usedSubackup);
					if (temp1g1 == usedSub.get(usedSub.size() - 1)) { // SP was found to the tail of the current
																		// usedSub, so the usedSub should be updated
						System.out.println("jjjjj"); // from the start by adding to element 0
						usedSub.add(temp2g2);
						usedSubackup.add(temp2g2);
						System.out.println("usedSubackup0" + usedSubackup);
					}

					functionnode removeVNFGroup1first;
					Vertex removeVertexNodeGroup1First;
					removeVNFGroup1first = selectedVNF_g1.getCurrentNode();
					removeVertexNodeGroup1First = Vertices
							.get(selectedVNF_g1.getReturnSPHop().get((selectedVNF_g1.getReturnSPHop()).size() - 1));
					lastTail = removeVertexNodeGroup1First;
					int tempCostG1 = 0;
					tempCostG1 = SP.updateEdgeBandwidth(selectedVNF_g1.getReturnSPHop(), bw); // Update the BW of the
																								// links on the returned
																								// path

					for (int i = 0; i < selectedVNF_g1.getReturnSPHop().size(); i++) {
						finalPath.add(selectedVNF_g1.getReturnSPHop().get(i));
					}
					System.out.println("tempCostG1" + tempCostG1);
					finalCostDC[0] = tempCostG1;
					SP.removeVNF(treemap, removeVNFGroup1first, removeVertexNodeGroup1First);// In this function we set
					// treemap,update CPU
					SFC.add(removeVNFGroup1first);
					// Now remove the element from group
					updatePriorityList(groups.get(1), removeVNFGroup1first);
					ch1.add(removeVertexNodeGroup1First);
					mappedChain.add(ch1);
					for (int i = 0; i < mappedChain.get(1).size(); i++) {
						System.out.println("mappedChain : after: g1 " + mappedChain.get(1).get(i).getNumber());
					}

					System.out.println("Current UsedSub in g1 after mapping the first element: " + usedSub);

				} else {
					System.out.println("We are in else");
					System.out.println("**********usedSub : " + usedSub);
					// Rest of the elements, starting group1, element 1, No we just get the distance
					// to the updated tail
					selectedVNF_rest = getNextNodeOtherGroups(SP, treemap, groups.get(k), bw,
							getNodeVer(usedSub.get(usedSub.size() - 1)), flagTop);
					if (selectedVNF_rest.getCurrentNode() == null || selectedVNF_rest.getReturnSPHop().isEmpty()) {
						flagTop = false;
						int[] toret = { 0, 0 };
						return toret;
					}
					functionnode removeVNFRest;
					Vertex removeVertexNodeRest;
					removeVNFRest = selectedVNF_rest.getCurrentNode();
					int temphere = selectedVNF_rest.getReturnSPHop().get(selectedVNF_rest.getReturnSPHop().size() - 1);

					int t = selectedVNF_rest.getReturnSPHop().get((selectedVNF_rest.getReturnSPHop()).size() - 1);
					removeVertexNodeRest = getNodeVer(t);
					lastTail = removeVertexNodeRest;
					int tempCostRest = 0;
					tempCostRest = SP.updateEdgeBandwidth(selectedVNF_rest.getReturnSPHop(), bw); // Update the BW of
																									// the links on the
																									// returned path
					for (int i = 0; i < selectedVNF_rest.getReturnSPHop().size(); i++) {
						finalPath.add(selectedVNF_rest.getReturnSPHop().get(i));
					}

					finalCostDC[0] = tempCostRest;
					SP.removeVNF(treemap, removeVNFRest, removeVertexNodeRest);// In this function we set the relation
																				// as well,update treemap,update CPU
					if (k == 1) {

						mappedChain.get(1).add(removeVertexNodeRest);
					} else {

						ArrayList<Vertex> chk = new ArrayList<Vertex>();
						mappedChain.add(chk);
						mappedChain.get(k).add(removeVertexNodeRest);

					}

					usedSub.add(temphere);
					usedSubackup.add(temphere);
					SFC.add(removeVNFRest);
					// Now remove the element from group
					updatePriorityList(groups.get(k), removeVNFRest);

					System.out.println("Current UsedSub for the rest: " + usedSub);

				}
				if (usedSub.size() == VNFs.size()) {
					System.out.println("finalCostDC: " + Integer.toString(finalCostDC[0]));
					System.out.println("Current UsedSub for the rest: final: " + usedSub);
					Set<Integer> keys2 = treemapFinal.keySet();// defined to provide an access to the functionodes
					System.out.println("*****************************************************");
					System.out.println("checking the treemapsssssssssssssssss" + treemapFinal);
					System.out.println("*****************************************************");
					System.out.println("checking the treemap keys: " + Arrays.toString(keys2.toArray()));
					System.out.println("*****************************************************");
					for (Integer r : keys2) {
						System.out.println("keys of the treemap: " + r + " plus its list " + treemapFinal.get(r));
					}
					System.out.println("*****************************************************");

					ArrayList<ArrayList<Vertex>> mappedChainFinal = new ArrayList<ArrayList<Vertex>>();

					for (int i = 0; i < count; i++) {
						ArrayList<Vertex> subChain = new ArrayList<Vertex>();
						for (int j = 0; j < mappedChain.get(i).size(); j++) {
							subChain.add(mappedChain.get(i).get(j));

							System.out.println(
									"mappedChain: " + i + " Current element: " + mappedChain.get(i).get(j).getNumber());
						}
						mappedChainFinal.add(subChain);
						System.out.println("**********************************");
					}

					System.out.println("Final test:@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

					for (int i = 0; i < mappedChainFinal.size(); i++) {
						for (int j = 0; j < mappedChainFinal.get(i).size(); j++) {

							/*
							 * if (i == 0) { if (!(usedSubackup.get(0) ==
							 * mappedChainFinal.get(i).get(j).getNumber())) {
							 * 
							 * System.out.println("we have to swap"); ArrayList<Vertex> test = new
							 * ArrayList<Vertex>(); int chain0Size = mappedChainFinal.get(i).size();
							 * mappedChainFinal.get(i).clear(); for (int l = 0; l < chain0Size; l++) {
							 * 
							 * mappedChainFinal.get(i).add(getNodeVer(usedSub.get(l)));
							 * 
							 * }
							 * 
							 * } }
							 */

							System.out.println("mappedChain: " + i + " Current element: "
									+ mappedChainFinal.get(i).get(j).getNumber());
						}
						System.out.println("**********************************");
					}

					for (int i = 0; i < mappedChainFinal.size(); i++) {
						for (int j = 0; j < mappedChainFinal.get(i).size(); j++) {
							System.out.println("Final: mappedChain: " + i + " Current element: "
									+ mappedChainFinal.get(i).get(j).getNumber());
						}
						System.out.println("**********************************");

					}

					System.out.println("**********************************Starting TTU");
					System.out.println("Number of groups before TuneUp:\t" + mappedChain.size());

					System.out.println("Used Sub before: " + usedSub);

					System.out.println("usedSubackup" + usedSubackup);

					ArrayList<Vertex> Vertices2 = new ArrayList<Vertex>();
					ArrayList<graphEdge> Edges2 = new ArrayList<graphEdge>();
					graphVertices allVer = new graphVertices();
					graphEdges allEdges = new graphEdges();
					Vertices2 = allVer.getAllVertices(VNFs.size(), this.substrate);
					Edges2 = allEdges.getAllEdges(this.substrate);

					// TuneUp TTU = new TuneUp(mappedChainFinal, Vertices2, Edges2, VNFs, filename,
					// bw, treemapFinal,
					/// usedSub, count);
//						ArrayList<Integer> finalChain = new ArrayList<Integer>();
//
//						ArrayList<Integer> finalChainNodes = new ArrayList<Integer>();
//						//finalChain = TTU.tuneUpAlgorithm();
//                                                if (finalChain==null){
//                                                   finalCostDC[1]=0; 
//                                                }
//                                                else{
//						for (int i = 0; i < finalChain.size(); i++) {
//							finalChainNodes.add(finalChain.get(i));
//						}
//
//						System.out.println("Mapped chain before TuneUp:\t"+Arrays.toString(mappedChainFinal.toArray()));
//						System.out.println("Final used before TTU: " + usedSubackup);
//						System.out.println("finalChainNodes: " + finalChainNodes);
//						System.out.println("finalCostDC: " + Integer.toString(finalCostDC[0]));
//
//						System.out.println("finalChainNodes: " + (finalChainNodes.size() - 1));
//                                                System.out.println("this.bw: " + this.bw);
//                                                int afterTune=0;
//                                                afterTune=((finalChainNodes.size() - 1)*this.bw);
//                                                finalCostDC[1]=afterTune;
//                                                System.out.println("afterTune: " + afterTune);
//                                                }
//                                                
//                                                if (finalCostDC[1]>finalCostDC[0]){
//                                                    
//                                                    System.out.println("TTU is greater!!!!" + "Before TTU:" + finalCostDC[0] + " after TTU: " + finalCostDC[1] + " File Name: " + this.filename);
//                                                }
					return finalCostDC;
				}
				// }

			}

		}

		return finalCostDC;

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
			System.out.println("Parent of the grandchild is in the childList");
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
