/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithms;

import Graph_Generator.Vertex;
import Graph_Generator.functionnode;
import Graph_Generator.graphEdge;
import Graph_Reader.graphEdges;
import Graph_Reader.graphVertices;

import java.util.*;


/**
 * @author mjalalitabar1
 */
public class findShortestPath {

    int total_cost = 0;
    private final ArrayList<Vertex> Vertices;
    private final ArrayList<graphEdge> Edges;
    private final ArrayList<functionnode> VNFs;
    String substrateNet;
    ArrayList<OriginalMap> originalRelation = new ArrayList<OriginalMap>();

    ArrayList<ReverseMap> reversedrelation = new ArrayList<ReverseMap>();

    public ArrayList<OriginalMap> getOriginalRelation() {
        return originalRelation;
    }

    public void setOriginalRelation(ArrayList<OriginalMap> originalRelation) {
        this.originalRelation = originalRelation;
    }


    public findShortestPath(ArrayList<Vertex> Vertices, ArrayList<graphEdge> Edges, ArrayList<functionnode> VNFs, String subNet) {
        this.Vertices = Vertices;
        this.Edges = Edges;
        this.VNFs = VNFs;
        this.substrateNet = subNet;
    }

    public ArrayList<Vertex> getVertices() {
        return Vertices;
    }

    public ArrayList<graphEdge> getEdges() {
        return Edges;
    }

    /////////////////////////////////////////////////////////////////////
    //public TreeMap<Integer, ArrayList<Integer>> findShortest(ArrayList<Vertex> listofNodesTocheck,Vertex source,int initialRate){
    //get the source node and list of the nodes that we need to compare the DP, return the one with min
	public ArrayList<Integer> findShortest(ArrayList<Vertex> listofNodesTocheck, Vertex source, int initialRate) {

		System.out.println(listofNodesTocheck);
		System.out.println(source.getNumber());

		
		int dist = 0;
		int minDistance1 = 99999;
		Vertex finalDestination2 = null;
		ArrayList<Vertex> Vertices2 = new ArrayList<Vertex>();
		ArrayList<graphEdge> Edges2 = new ArrayList<graphEdge>();
		graphVertices allVer = new graphVertices();
		Vertices2 = allVer.getAllVerticesNew(VNFs.size(), this.substrateNet);
		Graph_Reader.graphEdges allEdges = new graphEdges();
		Edges2 = allEdges.getAllEdgesNew(this.substrateNet);
		DijkstraAlgorithm1 Da = new DijkstraAlgorithm1(Vertices2, Edges2);
		LinkedList<Integer> pathLast1 = new LinkedList<Integer>(); // path for each candidate we check
		LinkedList<Integer> pathLast2 = new LinkedList<Integer>();// last path that is returned from the selected
																	// candidate
		Da.execute(source.getNumber(), initialRate);
		ArrayList<Integer> forReturnTop = new ArrayList<Integer>();
		TreeMap<Integer, Integer> toReturn = new TreeMap<Integer, Integer>();
		TreeMap<Integer, ArrayList<Integer>> forReturn = new TreeMap<Integer, ArrayList<Integer>>();
		ArrayList<Integer> toret = new ArrayList<Integer>();
		List<Vertex> CanListToCheckDij = new ArrayList<Vertex>();
		CanListToCheckDij = listofNodesTocheck;
		System.out.println("************In the shortest path function**************");
		ArrayList<Vertex> temp = new ArrayList<Vertex>();
		for (Vertex vertex : CanListToCheckDij) {
			dist = Da.getShortestDistance(vertex.getNumber());
			if (dist == Integer.MAX_VALUE) {
				continue;
			}
			pathLast1 = Da.getPath(vertex);// Path from the source to destination
			if (!(pathLast1.isEmpty())) {
				for (Integer vertex2 : pathLast1) {
					System.out.print(vertex2 + "---------------******* "); // printing one SP for one candidate
				}
			}
			if (dist < minDistance1) {
				minDistance1 = dist;
				finalDestination2 = vertex;
				temp.add(vertex);
			}
		}
		if (temp.size() == 0) {
			return (new ArrayList<Integer>());
		}
		toReturn.put(minDistance1, finalDestination2.getNumber());
		if (finalDestination2.getNumber() != source.getNumber()) {
			pathLast2 = Da.getPath(finalDestination2);
			if (pathLast2 != null) {
				if (!(pathLast2.isEmpty())) {
					for (Integer vertex : pathLast2) {
						//System.out.print(vertex + "--------------->>>> ");
					}
				}
				for (int i = 0; i < pathLast2.size(); i++) {
					toret.add(pathLast2.get(i));
				}
				forReturn.put(minDistance1, toret);
				forReturnTop = toret;
			} else {
				forReturn = null;
				return null;
			}
		}

		 //System.out.println("toReturn" + toReturn);//the key is the value of the min
		//// distance and the value is the node for
		// which this distance was found.
		// return forReturn; //returns the treemap

		return forReturnTop; // returns the list of the nodes on the shortest path

	}
    /////////////////////////////////////////////////end of find shortest

    ////////////////////////////////////////////////////
    //Previous written updateBW
    ////////////////////////////////////////////////////////////////
    public int updateEdgeBandwidth(ArrayList<Integer> nodesOnSP, int bw) {
        //Graph graph = new Graph(graph2.getVertexes(), graph2.getEdges());


        for (int edgeNo = 0; edgeNo < Edges.size(); edgeNo++) {

            graphEdge edge = Edges.get(edgeNo);
            int source = edge.getSource();
            int destination = edge.getDestination();
            int currentBW = edge.getBW();

            for (int i = 0; i < nodesOnSP.size() - 1; i++) {
                int sourceV = nodesOnSP.get(i);
                int destinationV = nodesOnSP.get(i + 1);

                if (source == sourceV && destination == destinationV) {
                    //System.out.println("The  sourceV:" + sourceV + " destinationV: " + destinationV + " Previous: BW:" + Edges.get(edgeNo).getBW());
//                    Edges.get(edgeNo).setBW(currentBW - bw);
                    //System.out.println("The upated sourceV:" + sourceV + " destinationV: " + destinationV + " before Updated: Start and End Index\t:" + Edges.get(edgeNo).getStartIndex() + ", " + Edges.get(edgeNo).getEndIndex());
                    Edges.get(edgeNo).setStartIndex(Edges.get(edgeNo).getStartIndex() + bw);
                    //System.out.println("The upated sourceV:" + sourceV + " destinationV: " + destinationV);
                    //System.out.println("The upated sourceV:" + sourceV + " destinationV: " + destinationV + " Updated: BW:" + Edges.get(edgeNo).getBW());
                    //System.out.println("The upated sourceV:" + sourceV + " destinationV: " + destinationV + " Updated: Start and End Index\t:" + Edges.get(edgeNo).getStartIndex() + ", " + Edges.get(edgeNo).getEndIndex());

                    for (int edgeNo2 = 0; edgeNo2 < Edges.size(); edgeNo2++) {
                        graphEdge edge2 = Edges.get(edgeNo2);
                        int source2 = edge2.getSource();
                        int destination2 = edge2.getDestination();
                        int currentBW2 = edge2.getBW();
                        if (source2 == destinationV && destination2 == sourceV) {
//                            Edges.get(edgeNo2).setBW(currentBW2 - bw);
                            Edges.get(edgeNo2).setStartIndex(Edges.get(edgeNo2).getStartIndex() + bw);
                            total_cost += bw;
                            //System.out.println(" source2:" + source2 + " destination2: " + destination2 + " BW:" + Edges.get(edgeNo2).getBW());
                        }
                    }
                }

            }

        }
        return total_cost;
    }

    ////////////////////////////////////////////////////////////////
    public int updateCost(ArrayList<Integer> nodesOnSP, int cost, int intialR) {


        total_cost += intialR * (nodesOnSP.size() - 1);


        return total_cost;
    }

    ////////////////////////////////////////////////////////////////
    public void updateCPU(ArrayList<OriginalMap> originalRelation, functionnode toRemoveVNF, Vertex toRmoveVertex) {
        //the CPU for all the used substrate nodes should be updated based on the relation


        //System.out.println("Updating the CPU for the used substrate node");
        for (OriginalMap om : originalRelation) {
            if (om.getKey().getNumber() == toRemoveVNF.getNumber() && om.getValue().getNumber() == toRmoveVertex.getNumber()) {
                //System.out.println("original : " + " key (VNF): " + om.getKey().getNumber() + " CPU:" + om.key.getReqCPU() + " value (substrate node): " + om.getValue().getNumber() + " CPU: " + om.getValue().getOfferedCPU());
                //System.out.println("******************************************");
                Vertex v = om.getValue();
                //System.out.println("******************************************");
                //System.out.println("previous cpu of the substrate node: " + v.getOfferedCPU() + " Node number:  " + v.getNumber());
                //for each of the vertex. get their key, which is the functionnode, obtain its requested CPU to update the offered CPU of the substrate node
                functionnode key = om.getKey();
                int reqCPU = key.getReqCPU();
                //System.out.println("Requested CPU by the functionode: " + reqCPU);
                //System.out.println("******************************************");
                int h = 0;
                h = Vertices.get(v.getNumber()).getOfferedCPU();
                if ((Vertices.get(v.getNumber()).getOfferedCPU() - reqCPU) >= 0) {
                    //System.out.println("The offered CPU is greater than the requested CPU");
                    int m = Vertices.get(v.getNumber()).getOfferedCPU() - reqCPU;
                    //System.out.println("New CPU for the substrate node: " + v.getNumber() + " is: " + m);
                    Vertices.get(v.getNumber()).setOfferedCPU(m);
                } else {
                    //System.out.println("Substrate node overuse!");
                }
            }
        }
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

    ///////////////////////////////////////////////////////////////////////
	public TreeMap<Integer, ArrayList<Integer>> generateCandidateList(ArrayList<functionnode> priorityListNodesbackUP) {
		// Given the list of the VNFs: generate the candidate list and the treemap
		int CandidateList[][];
		CandidateList = new int[VNFs.size()][Vertices.size()];
		for (int i = 0; i < (priorityListNodesbackUP.size()); i++) {
			for (int j = 0; j < (Vertices.size()); j++) {// Go over all the nodes of the substrate network
				functionnode virtualNode = priorityListNodesbackUP.get(i);
				Vertex substrateNode = Vertices.get(j);
				int c = priorityListNodesbackUP.get(i).getReqCPU();
				int d = Vertices.get(j).getOfferedCPU();
				if ((d >= c)) {

					CandidateList[i][j] = Vertices.get(j).getNumber();
				} else
					CandidateList[i][j] = 99;
			}
		}
		/////////////////////////////////////////////////////////////////////////////
		// We need array of array list to save the candidates for each node separately
		ArrayList<Integer>[] lists = new ArrayList[priorityListNodesbackUP.size()];

		for (int i = 0; i < priorityListNodesbackUP.size(); i++) {
			lists[i] = new ArrayList<Integer>();
		}
		for (int u = 0; u < priorityListNodesbackUP.size(); u++) {
			for (int i = 0; i < Vertices.size(); i++) {
				if (CandidateList[u][i] != 99) {
					lists[u].add(CandidateList[u][i]);
				}
			}
			lists[u] = sortCandidates(lists[u]);
//Here  
		}
		TreeMap<Integer, ArrayList<Integer>> listTrackTreeMap2 = new TreeMap<Integer, ArrayList<Integer>>();
		// To keep candidates
		// Printing the lists for each node separately and sorted
		for (int k = 0; k < priorityListNodesbackUP.size(); k++) {
            //Collections.sort(lists[k]);
//			 System.out.println("*****************************************************");
//			 System.out.println(" VNF: " + priorityListNodesbackUP.get(k).getNumber() + " Requested CPU: " + priorityListNodesbackUP.get(k).getReqCPU()
//			 + " Candidates " + lists[k]);
			ArrayList<Integer> candidate = new ArrayList<Integer>();
			candidate.addAll(lists[k]);
			functionnode func = priorityListNodesbackUP.get(k);
			listTrackTreeMap2.put(func.getNumber(), candidate);
			// Using listTrackTreeMap to keep joint track of funnctionode and its list of
			// vertices
		}

		Set<Integer> keys = listTrackTreeMap2.keySet();// defined to provide an access to the functionodes
		// System.out.println("*****************************************************");
		// System.out.println("checking the treemap" + listTrackTreeMap2);
		// System.out.println("*****************************************************");
		// System.out.println("checking the treemap keys: " +
		// Arrays.toString(keys.toArray()));
		 System.out.println("*****************************************************");
		//for (Integer r : keys) {
			// System.out.println("keys of the treemap: " + r + " plus its list " +
		//	 listTrackTreeMap2.get(r));
		//}
		// System.out.println("*****************************************************");

		return listTrackTreeMap2;
	}

    ////////////////////////////////////////////////////////////////
    public void removeVNF(TreeMap<Integer, ArrayList<Integer>> listTrackTreeMap2, functionnode toRemoveVNF,
                          Vertex toRmoveVertex) {
//        if(listTrackTreeMap2.size() >1) {
//        	listTrackTreeMap2.remove(toRemoveVNF.getNumber());
//        }
    	
        
        for (Integer f : listTrackTreeMap2.keySet()) {
            ArrayList<Integer> candidateList = new ArrayList<Integer>();
            candidateList = listTrackTreeMap2.get(f);
            //System.out.println("*****************************************************");
           // System.out.println("CandidateList before removing" + " for VNF: " + f + " " + candidateList);
            for (int g = 0; g < candidateList.size(); g++) {
                if (candidateList.get(g) == toRmoveVertex.getNumber()) {
                    if (!candidateList.isEmpty()) {

                    	int t = candidateList.get(g);
                    	Vertex vertex = Vertices.get(t);
                    	int orginalCPU = vertex.getOfferedCPU();
                    	int updatedCPU = orginalCPU - toRemoveVNF.getReqCPU();
                        Vertices.get(vertex.getNumber()).setOfferedCPU(updatedCPU);
                        mappingRelation(toRemoveVNF, toRmoveVertex);
                    	listTrackTreeMap2.remove(toRemoveVNF.getNumber());

                        return;
                        //Not remove, update the value
                       // candidateList.remove(g);
                    } else {

                        System.out.println("No Candidate********* ");
                    }

                }
            }
            //System.out.println("*****************************************************");
            //System.out.println("CandidateList at the end " + " the removed substrate node: " + toRmoveVertex.getNumber() + " " + candidateList);
        }
        //System.out.println(".......................................................................................................");

       // System.out.println("Treemap at the end########## " + listTrackTreeMap2);

       // mappingRelation(toRemoveVNF, toRmoveVertex);
      //  updateCPU(originalRelation, toRemoveVNF, toRmoveVertex);
    }

    ////////////////////////////////////////////////////////////////
    public ArrayList<OriginalMap> mappingRelation(functionnode VNF, Vertex substrateNode) {


        originalRelation.add(new OriginalMap(VNF, substrateNode));
        reversedrelation.add(new ReverseMap(substrateNode, VNF));
//        for (OriginalMap om : originalRelation) {
//        	if (VNF.getNumber() == om.getKey().getNumber() && substrateNode.getNumber()== om.getValue().getNumber()) {
//                System.out.println("Mapping relation : " + " VNF: " + om.getKey().getNumber() + " SN: " + om.getValue().getNumber());
//
//			}
//        }
//        for (ReverseMap rm : reversedrelation) {
//            System.out.println("reversed: " + " key: " + rm.getKey().getNumber() + " value: " + rm.getValue().getNumber());
//        }
        return originalRelation;
    }




    public Vertex getVertexRelation(functionnode VNF) { //get a functionnode and return the substrate node that it is mapped to

        Vertex mappedTo = new Vertex();
        OriginalMap om = new OriginalMap();

        mappedTo = om.getVertex(VNF);
        return mappedTo;
    }
}
