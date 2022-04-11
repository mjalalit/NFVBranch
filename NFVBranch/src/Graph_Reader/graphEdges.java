/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graph_Reader;

import Graph_Generator.Edge;
import Graph_Generator.Vertex;
import Graph_Generator.graphEdge;
import Graph_Generator.writeInFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Algorithms.UsefulMethods;


/**
 * @author mjalalitabar1
 */
public class graphEdges {

    private ArrayList<Edge> edges;

    public graphEdges() {
        //  this.edges = null;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public void setEdges(ArrayList<Edge> edges) {
        this.edges = edges;
    }

    public ArrayList<graphEdge> getAllEdgesNew(String filename) {
        ArrayList<graphEdge> alledges = new ArrayList<graphEdge>();
        readFromFile rd = new readFromFile();
        int[][] matrix1 = rd.readfiles(filename);
        int numOfVertices = matrix1[0].length;
        Edge newEdge = new Edge();
        newEdge.setVertexCount(numOfVertices);
        newEdge.setAdjacencyMatrix(matrix1);
        int t = 0;
        int startIndexBW = numOfVertices;
        int endIndexBW = numOfVertices * 2;
        for (int i = startIndexBW; i < endIndexBW; i++) {
            for (int j = 0; j < numOfVertices; j++) {
                if (matrix1[i][j] > 0) {
                	int BW = matrix1[i][j];
                    alledges.add(newEdge.addEdge(t, i - 28, j, 1, BW));
                    t++;
                }
            }
        }

    	return alledges;
    }

    public ArrayList<graphEdge> getAllEdges(String filename) { //Get the edges

        ArrayList<graphEdge> alledges = new ArrayList<graphEdge>();
        int[][] matrix1;
        int[][] matrix2;
        readFromFile rd = new readFromFile();
        // matrix1=rd.readfiles("NSFNET14.txt");
        matrix1 = rd.readfiles(filename);


        matrix2 = matrix1;
        int numOfVertices;
        numOfVertices = matrix1[0].length;
        //CPUmatrix=new int[numOfVertices][numOfVertices];
        int startIndexBW = numOfVertices;
        int endIndexBW = numOfVertices * 2;
        Edge newEdge = new Edge();
        newEdge.setVertexCount(numOfVertices);
        newEdge.setAdjacencyMatrix(matrix1);
        int t = 0;
        for (int i = startIndexBW; i < endIndexBW; i++) {
            for (int j = 0; j < numOfVertices; j++) {


                if (matrix1[i][j] > 0) {

                   // int BW = matrix1[i][j];
                    int BW = 0;
                    BW=500;
                 //   alledges.add(newEdge.addEdge(t, i - 28, j, 1, matrix1[i][j]));
                   alledges.add(newEdge.addEdge(t, i - 28, j, 1, BW));

                    t++;
                    matrix2[i][j] = BW;

                }

            }
            ////System.out.println("");
        }


        int startIndexSpectrum = numOfVertices * 4;
        int endIndexSpectrum = numOfVertices * 5;

        t = 0;
        for (int i = startIndexSpectrum; i < endIndexSpectrum; i++) {
            for (int j = 0; j < numOfVertices; j++) {


                if (matrix2[startIndexBW][j] > 0) {

                    int startIndex = matrix1[i][j];
//                    //System.out.println("Start Index of Edge "+t+": "+startIndex + " and BW: "+matrix2[startIndexBW][j]);
                    alledges.get(t).setStartIndex(startIndex);
                    alledges.get(t).setEndIndex(startIndex + matrix2[startIndexBW][j]);
                    t++;
                }

            }
            startIndexBW++;
            ////System.out.println("");
        }


//      for (int i = 0; i < alledges.size(); i++) {
//          
//          
//              System.out.println("Edge number: " + alledges.get(i).getId()+ ">>>> Source: " + alledges.get(i).getSource() +
//                      " Destination: " + alledges.get(i).getDestination() + " BW: " + alledges.get(i).getBW() + " start Index: "+ alledges.get(i).getStartIndex() + " End index: " 
//                      + alledges.get(i).getEndIndex());            
//        }

        writeInFile newAdj = new writeInFile();

        //newAdj.writeFileEdge(matrix2);

//        for (graphEdge edge :
//                alledges) {
//            //System.out.println("Edge " + edge.getId() + ":\t BW:" + edge.getBW() + ", Source-Destination: " + edge.getSource() + " - " + edge.getDestination() + ", Start-End Index: " + edge.getStartIndex() + " - " + edge.getEndIndex());
//        }

        return alledges;
    }

    
}
