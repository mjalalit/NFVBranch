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
import java.util.ArrayList;

/**
 *
 * @author maria
 */
public class getFunction {
    
    
    ArrayList<Integer> returnSPHop;
    functionnode currentNode;

    public getFunction(ArrayList<Integer> returnSPHop, functionnode currentNode) {
        this.returnSPHop = returnSPHop;
        this.currentNode = currentNode;
    }

    public ArrayList<Integer> getReturnSPHop() {
        return returnSPHop;
    }

    public void setReturnSPHop(ArrayList<Integer> returnSPHop) {
        this.returnSPHop = returnSPHop;
    }

    public functionnode getCurrentNode() {
        return currentNode;
    }

    public void setCurrentNode(functionnode currentNode) {
        this.currentNode = currentNode;
    }
    
    public functionnode getCurrentNode(ArrayList<Integer> returnSPHop) {
        
        return (this.currentNode);
    }
    
     public ArrayList<Integer> getListForCurrentNode(functionnode currentNode) {
        
        return (this.returnSPHop);
    }
}
