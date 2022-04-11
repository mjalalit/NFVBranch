/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithms;

import Graph_Generator.Vertex;
import Graph_Generator.functionnode;

/**
 *
 * @author mjalalitabar1
 */
public class OriginalMap { //shows the mapping relationship between VNF and substrate node
    
    functionnode key;
    Vertex value;

    public OriginalMap() {
    }
    public OriginalMap(functionnode key, Vertex value) {
        
        this.key = key;
        this.value = value;
    }

    public functionnode getKey() {
        return key;
    }

    public void setKey(functionnode key) {
        this.key = key;
    }

    public Vertex getValue() {
        return value;
    }
    
    public Vertex getVertex(functionnode func) {
        return this.getValue();
    }

    public void setValue(Vertex value) {
        this.value = value;
    }
    
}
