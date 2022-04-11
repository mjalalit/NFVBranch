/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graph_Generator;

import Graph_Reader.readFromFile;
import java.util.ArrayList;
import java.util.Random;


/**
 *
 * @author mjalalitabar1
 */
public class functionnode implements Comparable<functionnode> {
    
   // private int reqFunc;
    private int reqCPU;
    private int number;
    private int weight;
    private int weightDown;
    private ArrayList<Integer> FuncDep;

    public ArrayList<Integer> getFuncDep() {
        return FuncDep;
    }

    public void setFuncDep(ArrayList<Integer> FuncDep) {
        this.FuncDep = FuncDep;
    }

    public int getWeightDown() {
        return weightDown;
    }

    public void setWeightDown(int weightDown) {
        this.weightDown = weightDown;
    }
    
    

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
    

    public functionnode(int reqCPU, int number) {
        this.reqCPU = reqCPU;
        this.number = number;
    }

    
    public functionnode(){
        
        
    }
    public int getReqCPU() {
        return reqCPU;
    }

    public void setReqCPU(int reqCPU) {
        this.reqCPU = reqCPU;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
    
    /////////////////////////////////////////////////////////////
    	public int compareTo(functionnode compareFun) {

		int compareQuantity = ((functionnode) compareFun).getWeight();

		//ascending order
		//return this.weight - compareQuantity;

		//descending order
		return compareQuantity - this.weight;

	}
   /////////////////////////////////////////////////////////////// 
   public int [][] getDepMat(){//read the dependency graph from the file
      int [][] matrix1;
     // ArrayList<Integer> FuncDep=new ArrayList<Integer>();
      readFromFile rd=new readFromFile();
      matrix1=rd.readfiles("servReq.txt");
       //System.out.println("here in functionnode");
       for (int i = 0; i < matrix1[0].length; i++) {
                for (int j = 0; j < matrix1[0].length; j++) {
               
                   //System.out.print(matrix1[i][j] + " ");                     
                }
                //System.out.println("");                
            }
       
       return matrix1; 
       
   }  
   
   /////////////////////////////////////////
     public void writeCPU(){
         int [][] matrixCPU;
         
         matrixCPU=getDepMat();
         
      writeInFile wr=new writeInFile(matrixCPU);
        for (int i = 0; i < matrixCPU[0].length; i++) {
                for (int j = 0; j < matrixCPU[0].length; j++) {
               
                if (i==j){ //range of CP [5,35]
                Random rn = new Random();
                int CPU = rn.nextInt(10 - 5 + 1) + 5; 
                    ////System.out.println(CPU);
                    matrixCPU[i][j]=CPU;                        
                    }
                else 
                     matrixCPU[i][j]=0;
                   //System.out.print(matrixCPU[i][j] + " ");    
                  
                }
                //System.out.println("");
                
            }
                // writeFile(adjacencyMatrix); 
       // wr.writeFileVirtual(matrixCPU);
  }
    
    
    
}
