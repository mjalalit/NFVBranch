package Graph_Reader;

import Graph_Generator.Edge;
import Graph_Generator.Vertex;
import Graph_Generator.graphEdge;
import Graph_Generator.writeInFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Algorithms.UsefulMethods;


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
    //Receives a name of a file, reads the matrix in file, based on that create the edges for
    //a weighted graph. BW represent the weight of each edge
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
    
    //Receives a name of a file, reads the matrix in file, based on that create the vertices for
    //a  graph. Each node has id and a unit that represent the computational capacity (CPU)
  public ArrayList<Vertex> getAllVerticesNew(int vnfs, String filename) {
    	
        readFromFile rd = new readFromFile();
        int[][] matrixNew = rd.readfiles(filename);
        int numOfVertices = matrixNew[0].length;
        ArrayList<Vertex> allV = new ArrayList<Vertex>();
        int id = 0;
        int startIndexCPU = numOfVertices * 2;
        int endIndexCPU = numOfVertices * 3;
        for (int i = startIndexCPU; i < endIndexCPU; i++) {
            Vertex newVer = new Vertex();
            newVer.setNumber(id);
            newVer.setOfferedCPU(matrixNew[i][id]);  
            allV.add(newVer);
            id++;
        }

    	return allV;
    }
    
    


    
}
