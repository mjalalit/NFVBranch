
package Graph_Generator;

import java.util.ArrayList;


public class Vertex implements  Comparable<Vertex>  {
    
   private int number;
  final private String id;
  final private String name;
  private int offeredCPU;
  private ArrayList<Integer> OfferedFunc;
  private int adjacencyMatrix[][];
  private int startIndex;
  private int endIndex;
  

    public int[][] getAdjacencyMatrix() {
        return adjacencyMatrix;
    }
  
    
    public void setAdjacencyMatrix(int[][] adjacencyMatrix) {
        this.adjacencyMatrix = adjacencyMatrix;
    }

    public ArrayList<Integer> getOfferedFunc() {
        return OfferedFunc;
    }

    public void setOfferedFunc(ArrayList<Integer> OfferedFunc) {
        this.OfferedFunc = OfferedFunc;
    }
  
  public Vertex() {
    this.id = "0";
    this.name = "0";
    this.number=1;
    this.startIndex = 0;
    this.endIndex = 0;
  }
  public Vertex(String id, String name, int number) {
    this.id = id;
    this.name = name;
    this.number=number;
    this.startIndex = 0;
    this.endIndex = 0;
  }
  
  public Vertex(int number, int CPU){
      
      this.number=number;
      this.offeredCPU=CPU;
      this.id = "0";
      this.name="5";
    this.startIndex = 0;
    this.endIndex = 0;
      
  }

  public void setStartIndex(int startIndex){this.startIndex = startIndex;}
  public void setEndIndex(int endIndex){this.endIndex = endIndex;}
  public int getStartIndex(){return this.startIndex;}
  public int getEndIndex(){return this.endIndex;}

    public int getNumber() {
        return number;
    }
  
  public void setNumber(int number) {
        this.number=number;
    }
  
  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }
  
  
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

    public int getOfferedCPU() {
        return offeredCPU;
    }

    public void setOfferedCPU(int offeredCPU) {
        this.offeredCPU = offeredCPU;
    }

   
    
  //Function to compare two vertexes based CPU
    public int compareTo (Vertex compareCPU){

    int compareCPUAmount= ((Vertex) compareCPU).offeredCPU;

    //ascending order
    return this.offeredCPU - compareCPUAmount;

    //descending order
    //return compareCPUAmount - this.offeredCPU;
     }
    
  public  boolean isEmpty(Vertex obj) {
    return obj == null ;
}

 
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Vertex other = (Vertex) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

  @Override
  public String toString() {
	  return String.valueOf(this.number);
  }
    
}
