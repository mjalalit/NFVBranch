/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Graph_Reader;

import Graph_Generator.Edge;
import Graph_Generator.Vertex;

import java.util.ArrayList;
import java.util.Random;


/**
 * @author mjalalitabar1
 */
public class graphVertices {

    private ArrayList<Vertex> vertices;

    public graphVertices() {
        this.vertices = vertices;
    }

    public ArrayList<Vertex> getVerices() {
        return vertices;
    }

    public void setVerices(ArrayList<Vertex> verices) {
        this.vertices = verices;
    }

    ////////////////////////////////////////////////////////////////
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

    ///////////////////////////////////////////////////////////////////////////////////////////////
    public ArrayList<Vertex> getAllVertices(int vnfs, String filename) { //Get the vertices
        int[][] matrix1;
        Random rnd = new Random();
        readFromFile rd = new readFromFile();
        matrix1 = rd.readfiles(filename);
        ArrayList<Vertex> allV = new ArrayList<Vertex>();
        //System.out.println("Substrate network topology");
        //System.out.println("*********************************************");
//        for (int i = 0; i < matrix1.length; i++) {
//                for (int j = 0; j < matrix1[i].length; j++) {                   
//                   //System.out.print(matrix1[i][j] + " ");    
//                }
//               //System.out.println("");               
//            }
        //System.out.println("*********************************************");

        int numOfVertices = 0;
        ///////////////  Node capacities\\\\\\\\\\\\\\\\\ 

        // number of vertices is equal to the num of cols
        numOfVertices = matrix1[0].length;
        //CPUmatrix=new int[numOfVertices][numOfVertices];
        int startIndexCPU = numOfVertices * 2;
        int endIndexCPU = numOfVertices * 3;
        int t = 0;
        //Get the elements of main diagonal which are the CPUs
        for (int i = startIndexCPU; i < endIndexCPU; i++) {
            ////System.out.println("###########: "+i + " t: "+ t + " Val: "+ matrix1[i][t]);
            Vertex newVer = new Vertex();
            newVer.setNumber(t);
           // newVer.setOfferedCPU(matrix1[i][t]);
            
            
            newVer.setOfferedCPU(500);
            newVer.setStartIndex(0);
            newVer.setEndIndex(55);
            allV.add(newVer);
            t++;
        }

       // int startIndexFunc = numOfVertices * 4;
      //  int endIndexFunc = numOfVertices * 5;

        int startIndexFunc = numOfVertices * 3;
        int endIndexFunc = numOfVertices * 4;
        
        
        ArrayList<ArrayList<Integer>> funcs = new ArrayList<ArrayList<Integer>>();


        for (int i = startIndexFunc; i < endIndexFunc; i++) {

            ArrayList<Integer> tempfunc = new ArrayList<Integer>();

            // for (int j = 0; j < numOfVertices; j++) {

            tempfunc.add(matrix1[i][0]);


            //  tempfunc.add(rnd.nextInt(vnfs));
            // }

            funcs.add(tempfunc);
        }

        //   //System.out.println("test" + funcs);


        for (int i = 0; i < allV.size(); i++) {
            allV.get(i).setOfferedFunc(funcs.get(i));
            //  allV.get(i).setOfferedFunc(generateRand(vnfs));

        }
        //System.out.println("Vertices of the substrate network:");
//        for (int i = 0; i < allV.size(); i++) {
//             System.out.println("Vertex number: " + allV.get(i).getNumber()+ " CPU: " + allV.get(i).getOfferedCPU()  + " Offered Func: " + allV.get(i).getOfferedFunc() );            
//        }

        return allV;
    }


    public ArrayList<Integer> generateRand(int DepRand) {

        ArrayList<Integer> FuncDep = new ArrayList<Integer>();
        int DepRand2 = 0 + (int) (Math.random() * (((DepRand) - 0) + 1));//Max number of depen for each node [0,NodeNum]
        // //System.out.println("The rand: "+DepRand2);
        FuncDep.add(DepRand2);
  /* while (FuncDep.contains(DepRand2)){
   DepRand2 = 0 + (int)(Math.random() * (((DepRand) - 0) + 1));//Max number of depen for each node [0,8]  
   }
   FuncDep.add(DepRand2);*/
        ////System.out.println("The list of rands: "+FuncDep);

        // //System.out.println("FuncDep" + FuncDep);
        return FuncDep;
    }


    public ArrayList<Integer> getNeighbours(int node) { //Passing the node number and get the neighbours(node num)
        //////////////////////////////////////////////////////////////////////////Connections for each vertex
        ArrayList<Integer> neighbours = new ArrayList<Integer>();
        int[] nodeRow;
        int[][] matrix1;
        readFromFile rd = new readFromFile();
        matrix1 = rd.readfiles("adjMat.txt");
        int numOfVertices = 0;
        numOfVertices = matrix1[0].length;
        int startIndexAdj = 0;
        int endIndexAAdj = numOfVertices;
        nodeRow = new int[matrix1[0].length];

        for (int i = 0; i < nodeRow.length; i++) {
            nodeRow[i] = matrix1[node][i];
            if (nodeRow[i] == 1) {
                neighbours.add(i);
            }
            //System.out.println("nodeRow[i]" + nodeRow[i]);
        }

        //System.out.println("neighbours" + neighbours);

        return neighbours;
    }


}
