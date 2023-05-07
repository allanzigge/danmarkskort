package handin2;

import java.io.Serializable;
import java.util.*;

/* OLD GRAPH CLASS COMMENTED OUT - MADE BY ALLAN
/* Currently adds every node of all ways to the graph
 

public class Graph implements Serializable  {
    HashSet<Long> IDs; //ID of nodes we have created a vertex of
    HashMap<Long, Vertex> verticies; 

    public Graph(){
        IDs = new HashSet<>();
        verticies = new HashMap<>();
    }

    //Helper class
    private class Vertex implements Serializable{
        double finalCost;
        ArrayList<Vertex> neighbors;
        Node originalNode;
    

        public Vertex(Node node){
            originalNode = node;
            neighbors = new ArrayList<>();
        }

        public void addNeighbor(Vertex neighbor){
            neighbors.add(neighbor);
        }
    } 
    //End of helper class
    
    public void addVerticies(List<Node> nodes){
        
        for(int i = 0; i < nodes.size(); i++){ // all nodes in the way-list
           Node node = nodes.get(i);
            Vertex vertex = new Vertex(node); 
            if(!IDs.contains(node.getID())){ //if not added
                verticies.put(node.getID(), vertex); //add into map
                IDs.add(node.getID()); //and add id to set of added id
                if(i == 0)
                    vertex.addNeighbor(new Vertex(nodes.get(i++)));
                else if(i == (nodes.size()-1))
                    vertex.addNeighbor(new Vertex(nodes.get(i--)));
                else {
                    vertex.addNeighbor(new Vertex(nodes.get(i++)));
                    vertex.addNeighbor(new Vertex(nodes.get(i--)));
                }
            } else { //IF IT DOES EXIST
                vertex = verticies.get(node.getID());
                if(i == 0)
                    vertex.addNeighbor(new Vertex(nodes.get(i++)));
                else if(i == (nodes.size()-1))
                    vertex.addNeighbor(new Vertex(nodes.get(i--)));
                else {
                    vertex.addNeighbor(new Vertex(nodes.get(i++)));
                    vertex.addNeighbor(new Vertex(nodes.get(i--)));
                }

            }
        }

    }


    /* public void addVertex(Node node){
        //create vertex object 
        if(!IDs.contains(node.getID())){
            Vertex vertex = new Vertex(node);
            IDs.add(node.getID());
        }
    }  

    public void addEgde(Node startNode, Node endNode){
            } 

    public void display(){
        //System.out.println(adjList);
    }
}

*/
public class Graph implements Serializable{

    Vertex[] graph;

    public Graph(HashMap<Long, Vertex> vertexMap, int size){
        graph = new Vertex[size];
        fillGraph(vertexMap, size);
    }


    private void fillGraph(HashMap<Long, Vertex> vertexMap, int size) {
        int i = 0;
        for(Map.Entry<Long, Vertex> set : vertexMap.entrySet()){
            graph[i] = set.getValue();
            i++;
        }
    }

}

