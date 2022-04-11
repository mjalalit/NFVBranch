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
public class ReverseMap {  //shows the mapping relationship between VNF and substrate node
    
    Vertex  key;
    functionnode value;

    public ReverseMap() {
    }
    public ReverseMap(Vertex key,  functionnode value) {
        this.key = key;
        this.value = value;
    }

    public Vertex getKey() {
        return key;
    }

    public void setKey(Vertex key) {
        this.key = key;
    }

    public functionnode getValue() {
        return value;
    }

    public void setValue(functionnode value) {
        this.value = value;
    }
    
}
