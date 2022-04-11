/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithms;

/**
 * @author mjalalitabar1
 */

import Graph_Generator.Vertex;
import Graph_Generator.graphEdge;

import java.util.*;


public class DijkstraAlgorithm1WithConv {

    private final List<Vertex> nodes;
    private final List<graphEdge> edges;
    private Set<Integer> settledNodes;
    private Set<Integer> unSettledNodes;
    private Map<Integer, Integer> predecessors;
    private Map<Integer, Integer> distance;

    int BW;

    public DijkstraAlgorithm1WithConv(ArrayList<Vertex> nodes, ArrayList<graphEdge> graphEdges) {
        // create a copy of the array so that we can operate on this array
        this.nodes = nodes;
        this.edges = graphEdges;
    }

    public void execute(int source, int initialRate) {
        Vertex verHere = new Vertex();
        verHere = getVer(source);
        settledNodes = new HashSet<Integer>();
        unSettledNodes = new HashSet<Integer>();
        distance = new HashMap<Integer, Integer>();
        predecessors = new HashMap<Integer, Integer>();
        distance.put(source, 0);
        unSettledNodes.add(source);
        while (unSettledNodes.size() > 0) {
            Integer node = getMinimum(unSettledNodes);
            //   //System.out.println("node from getMin: " +node);
            settledNodes.add(node);
//      for (Integer temp : settledNodes) {
//        //System.out.println("settledNodes: "+ temp);
//     }
            unSettledNodes.remove(node);
            // Here, we need to define a BW
            // Then, send BW attribute to find the shortest path
            BW = initialRate;
            findMinimalDistances(node, BW);
        }
    }

    // Updated findMinimalDistance with adding BW attributes
    private void findMinimalDistances(Integer node, int BW) {

        ////System.out.println("findMinimalDistances node" + node.getNumber());
        // Adding BW for finding neighbors that are satisfying BW constraint
        List<Integer> adjacentNodes = getNeighbors(node, BW);
        
//        if (adjacentNodes.isEmpty()) {
//                    //System.out.println("No neig For node: " + node );
//
//        }

       // //System.out.println("in findMinimalDistances" + adjacentNodes + " For nod: " + node + " the needed BW: " + BW);
        for (Integer target : adjacentNodes) {
            int s = 0;
            s = getShortestDistance(node) + getDistance(node, target);
            if (getShortestDistance(target) > s) {

                int sw = 0;
                sw = getShortestDistance(node) + getDistance(node, target);
                distance.put(target, getShortestDistance(node) + getDistance(node, target));
                predecessors.put(target, node);
                unSettledNodes.add(target);
            }
        }

        for (int name : distance.keySet()) {
            int key = name;
            String value = distance.get(name).toString();
        }
        //System.out.println("");
    }

    private int getDistance(Integer node, Integer target) {
        for (graphEdge edge : edges) {
            if (edge.getSource() == node && edge.getDestination() == target) {
                return edge.getWeight();
            }
        }
        throw new RuntimeException("Should not happen");
    }

    // Adding BW variable to check all connections that are
    // satisfying the requested BW

    private List<Integer> getNeighbors(Integer node, int BW) {
        //  System.out.println("in getNeighbors for node:  " +node);
        List<Integer> neighbors = new ArrayList<Integer>();
        for (graphEdge edge : edges) {

            if (edge.getSource() == node && isSettled(edge.getDestination()) == false && checkSpectrumIndex(edge,BW)) {

                int currentNodeStart = getVer(node).getStartIndex();
                int currentNodeEnd = getVer(node).getEndIndex();

              //  System.out.println("Edge Source: " + edge.getSource() + "Edge Destination: " + edge.getDestination() + " After coming from checkSpectrumIndex: " + " start: " + edge.getStartIndex() + " End: " + edge.getEndIndex() );

               // System.out.println("Edge Source: " + edge.getSource() + "Edge Destination: " + edge.getDestination());

//                int[] alignedIndex = returnAlignedIndex(this.nodes.get(node.intValue()),edge,BW);
//                System.out.println("After alignmnt: " + " start: " + alignedIndex[0] + " End: " + alignedIndex[1] );
//
//                this.nodes.get(edge.getDestination()).setStartIndex(alignedIndex[0]);
//                this.nodes.get(edge.getDestination()).setEndIndex(alignedIndex[1]);

                neighbors.add(edge.getDestination());
            }
        }
        return neighbors;
    }



    public boolean checkSpectrumIndex(Vertex currentNode, graphEdge nextEdge, int bandWidth) {

       // //System.out.println("*************************In:checkSpectrumIndex:******************* ");
       // //System.out.println("Source: currentNode: " + currentNode.getNumber());
       // //System.out.println("The nextEdge to be investigated : " + " S: " + nextEdge.getSource() + " D: " + nextEdge.getDestination());


        int currentNodeStart = currentNode.getStartIndex();
        int currentNodeEnd = currentNode.getEndIndex();

       // //System.out.println("Current Node: Start index: " + currentNodeStart + " End Index: " + currentNodeEnd);

        int nextEdgeStart = nextEdge.getStartIndex();
        int nextEdgeEnd = nextEdge.getEndIndex();

      //  //System.out.println("nextEdgeStart: Start index: " + nextEdgeStart + " nextEdgeEnd: " + nextEdgeEnd);

        if (Math.abs(nextEdgeStart - nextEdgeEnd) < bandWidth) {
            return false;
        }

        if (currentNodeStart < nextEdgeStart) {
            currentNodeStart = nextEdgeStart;
        }

        if (currentNodeEnd > nextEdgeEnd) {
            currentNodeEnd = nextEdgeEnd;
        }

        if (Math.abs(currentNodeStart - currentNodeEnd) < bandWidth) {
            return false;
        }

        if (currentNodeStart > currentNodeEnd) {
            return false;
        }

        return true;
    }

    public int[] returnAlignedIndex(Vertex currentNode, graphEdge nextEdge, int bandWidth) {

        int currentNodeStart = currentNode.getStartIndex();
        int currentNodeEnd = currentNode.getEndIndex();

        int nextEdgeStart = nextEdge.getStartIndex();
        int nextEdgeEnd = nextEdge.getEndIndex();

        if (currentNodeStart < nextEdgeStart) {
            currentNodeStart = nextEdgeStart;
        }

        if (currentNodeEnd > nextEdgeEnd) {
            currentNodeEnd = nextEdgeEnd;
        }

        return new int[]{currentNodeStart, currentNodeEnd};
    }


    public boolean checkSpectrumIndex(graphEdge nextEdge, int bandWidth) {

        int nextEdgeStart = nextEdge.getStartIndex();
        int nextEdgeEnd = nextEdge.getEndIndex();

        if (Math.abs(nextEdgeStart - nextEdgeEnd) < bandWidth) {
            return false;
        }

        return true;
    }

    public Vertex getVer(int num) {
        Vertex org = new Vertex();
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).getNumber() == num)
                org = nodes.get(i);
        }
        return org;
    }

    //The next node to proceed
    private Integer getMinimum(Set<Integer> vertexes) {
        Integer minimum = null;
        for (Integer vertex : vertexes) {
            if (minimum == null) {
                minimum = vertex;
                //   //System.out.println("minimum" + minimum.getNumber());
            } else {
                if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
                    minimum = vertex;
                }
            }
        }
        return minimum;
    }

    private boolean isSettled(Integer vertex) {
        boolean check = false;

        for (Integer temp : settledNodes) {

            if (temp == vertex) {
                check = true;
                // //System.out.println("tempfff: "  +temp);
                // //System.out.println("verte:  "+ vertex);
                // //System.out.println("match in isSet " );
            }

        }

//        for (Integer temp : settledNodes) {
//        //System.out.println("tem: " + temp);
//     }
//      //System.out.println("dest in isSet: " + vertex);
        return check;

    }


    //Obtaining the distance

    public int getShortestDistance(Integer destination) {
        Integer d = distance.get(destination);
        if (d == null) {
            return Integer.MAX_VALUE;
        } else {
            return d;
        }
    }

    /*
     * This method returns the path from the source to the selected target and
     * NULL if no path exists
     */
    public LinkedList<Integer> getPath(Vertex target) {

        //   //System.out.println("Target: " + target);
        LinkedList<Integer> path = new LinkedList<Integer>();
        int step = target.getNumber();
        // check if a path exists
        if (predecessors.get(step) == null) {
            return null;
        }
        path.add(step);
        while (predecessors.get(step) != null) {
            step = predecessors.get(step);
            path.add(step);
        }
        // Put it into the correct order
        Collections.reverse(path);
        ////System.out.println("path" + path);
        return path;


    }

} 

