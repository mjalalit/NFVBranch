/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithms;

import Graph_Generator.Vertex;
import Graph_Generator.functionnode;
import Graph_Generator.graphEdge;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author maria
 */
public class FixChainWithConv {
 ArrayList<Vertex> Vertices;
 ArrayList<graphEdge> Edges;
 ArrayList<functionnode> VNFs;
 int BW;
 String file;
 String substrateNet;

 
 

 
    public FixChainWithConv(ArrayList<Vertex> Vertices, ArrayList<graphEdge> Edges, ArrayList<functionnode> VNFs,String file, int BW, String subNetwork) {
        this.Vertices = Vertices;
        this.Edges = Edges;
        this.VNFs = VNFs;
        this.BW=BW;
        this.file=file;
        this.substrateNet=subNetwork;
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
    
    public int mappingDFS(){
        
        
//        for (int ik = 0; ik< Edges.size(); ik++) {
//              //System.out.println("Edge number: " + Edges.get(ik).getId()+ ">>>> Source: " + Edges.get(ik).getSource() +
//                      " Destination: " + Edges.get(ik).getDestination() + " BW: " + Edges.get(ik).getBW());            
//        }
        boolean flagTop = true;
        int finalCost=0;
        //System.out.println("Starting the DFS mapping********************************************");
        ArrayList<Vertex> usedSub_DFS=new ArrayList<Vertex>();
        DFSAlgorithm dfsCase=new DFSAlgorithm(Vertices,Edges,VNFs,file);
        dfsCase.DFS();       
        ArrayList<Integer> priorityDFS=new ArrayList<Integer>();
      //  priorityDFS=dfsCase.getPriorityListNodes();
        topSortAlgo topSort=new topSortAlgo(VNFs, file);
       priorityDFS= topSort.topologicalSort();
        //System.out.println("Priority list for top sort : " + priorityDFS);
        
        ArrayList<functionnode> priorityDFS_func=new ArrayList<functionnode>();
        for (int i = 0; i < priorityDFS.size(); i++) {
           priorityDFS_func.add(VNFs.get(priorityDFS.get(i)));            
        }
        //change the integer to func
        //System.out.println(" ");
        //System.out.println("PriorityListNodes in DFS" + priorityDFS);
        //System.out.println("Here in mappingDFS: ***********************first VNF mapping*************************");
        functionnode firstVNF_DFS=null;
        firstVNF_DFS=priorityDFS_func.get(0);
        findShortestPathWithConv SP_DFS=new findShortestPathWithConv(Vertices, Edges, VNFs,this.substrateNet);
        
//        //System.out.println("444444444444");
        
//        for (int i = 0; i < Vertices.size(); i++) {
//            
//            //System.out.println("node: " + getVertices().get(i).getOfferedFunc());
//            
//        }
        
        for (int i = 0; i < Vertices.size(); i++) {
              //System.out.println("Vertex number: " + Vertices.get(i).getNumber()+ " CPU: " + Vertices.get(i).getOfferedCPU()  + " Offered Func: " + Vertices.get(i).getOfferedFunc() );            
        }
        
        TreeMap<Integer, ArrayList<Integer>> treemap;
        treemap= SP_DFS.generateCandidateList(priorityDFS_func);
        //System.out.println("*****************************************************");
        //System.out.println("Tree map at the beg: Fix");
        //System.out.println("checking the treemap" + treemap);
        Set<Integer> keys = treemap.keySet();//defined to provide an access to the functionodes
        for (Integer r : keys) {
            if (treemap.get(r).isEmpty()){                
                //System.out.println("***********************No candidate for this functionality: Fix:" + r);
                flagTop=false;
            }
        //System.out.println("keys of the treemap: " + r + " plus its list " + treemap.get(r));
        }
        //System.out.println("*****************************************************");
        //Map 𝑉1 to  the substrate candidate with highest CPU    
        Vertex firstVertexDFS=null;
        int first_DFS;
        
        if (!flagTop==false){
        
        first_DFS=treemap.get(firstVNF_DFS.getNumber()).get(0);
        
        //test
        firstVertexDFS= Vertices.get(first_DFS);    
        //System.out.println("*****************************************************");
        //System.out.println("First Mapped VNF: " + firstVNF_DFS.getNumber() + " first used Sub: " + firstVertexDFS.getNumber());
        //System.out.println("*****************************************************");
        SP_DFS.removeVNF(treemap, firstVNF_DFS, firstVertexDFS);//In this function we set the relation as well
        usedSub_DFS.add(firstVertexDFS);
        updatePriorityList_DFS(priorityDFS_func, firstVNF_DFS);
        ArrayList<functionnode> SFC_DFS=new ArrayList<functionnode>();
        SFC_DFS.add(firstVNF_DFS);
        //System.out.println("*****************************************************First node Mapped DFS!****************************************************");
        
        }
        
         else{
       
       //System.out.println("Request rejected at the very first node for DFS: lack of candidates");
       
       finalCost=0;
            }
        // int h=0;
        
        while(!priorityDFS_func.isEmpty() &&  flagTop  ){           
        //Select the next VNF in priority_list_DFS
        functionnode NesxtVNF_DFS=null;
        NesxtVNF_DFS=priorityDFS_func.get(0);
        //System.out.println("Next node to be mapped in DFS: " + NesxtVNF_DFS.getNumber());
        //Append it to the end of the chain
        Vertex usedCand_DFS=null;
        ArrayList<Integer> candListNextNode_DFS=new ArrayList<Integer>();
        ArrayList<Integer> returnedFromSP_DFS=new ArrayList<Integer>();
        candListNextNode_DFS=treemap.get(NesxtVNF_DFS.getNumber());
        
         if (candListNextNode_DFS.isEmpty()) {
            flagTop=false;
            //System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            ////System.out.println("Request is rejected because there is no substrate candidate for node: case 0 " +nextNode.getNumber() );
            //System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
        
        
        
              if(!candListNextNode_DFS.isEmpty()){
        ArrayList<Vertex> candListNextNodeVertices_DFS=new ArrayList<Vertex>();
        for (int i = 0; i < candListNextNode_DFS.size(); i++) {
        candListNextNodeVertices_DFS.add(Vertices.get(candListNextNode_DFS.get(i)));       
        }
        //System.out.println("Candidate List for nextNode in DFS: " + NesxtVNF_DFS.getNumber() + " are: " + candListNextNode_DFS );
         if (usedSub_DFS != null && !usedSub_DFS.isEmpty()) {
       // //System.out.println("Last element is:");
        usedCand_DFS=usedSub_DFS.get(usedSub_DFS.size()-1);
        //System.out.println(" usedSub here in DFS: "+ usedSub_DFS.get(usedSub_DFS.size()-1).getNumber());
        }
        returnedFromSP_DFS= SP_DFS.findShortest(candListNextNodeVertices_DFS,usedCand_DFS, BW);
        
         if (returnedFromSP_DFS == null) {
                                returnedFromSP_DFS = new ArrayList<Integer> ();
                            }
        if(returnedFromSP_DFS.isEmpty()){
         //System.out.println("################################################Request is rejected: list to check is empty: no path is found#######################################");
          flagTop = false;              
        }
        
     if (!returnedFromSP_DFS.isEmpty() && flagTop) {   
        
        //System.out.println("**************************************************");
//        //System.out.println("**************************************************");
//        //System.out.println("**************************************************");
        int backupVertex_DFS;
        backupVertex_DFS=returnedFromSP_DFS.get(returnedFromSP_DFS.size()-1);
        usedSub_DFS.add(Vertices.get(backupVertex_DFS));
        int help=0;
        help= SP_DFS.updateEdgeBandwidth(returnedFromSP_DFS, BW);
        SP_DFS.removeVNF(treemap, NesxtVNF_DFS, Vertices.get(backupVertex_DFS));  
        updatePriorityList_DFS(priorityDFS_func, NesxtVNF_DFS);
        if(priorityDFS_func.isEmpty()){
            finalCost+=help;
            
        }
        
        
                
        //System.out.println("Done mapping all the nodes*************************************");  
        
        
        }
        }
        }  
        
        //System.out.println("Final cost DFS: " + finalCost);
        
        return finalCost;
    }
    
       public ArrayList<functionnode>  updatePriorityList_DFS (ArrayList<functionnode>  PL, functionnode toRemoveNode){  //Get the priority list and and a node. Remove the node from the PL
       
    ArrayList<functionnode> PLupdate=new ArrayList<functionnode>();  
    PLupdate=PL;
       for (int i = 0; i < PLupdate.size(); i++) {
         if (PLupdate.get(i).getNumber() == toRemoveNode.getNumber()) {
                            PLupdate.remove(i);
        }   
       }
  
     return  PLupdate;
   }

}
