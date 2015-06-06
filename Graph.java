/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unalcol.agents.examples.labyrinth.agentP;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Pedro
 */
public class Graph {

    Node root;

    public Graph() {
        root = new Node(null);
        root.direction = 0;
        root.visited=true;
    }
    public List<Vertex> getVetices(Node current,List<Vertex> ret,Vertex curVertex)
    {
        ret.add(curVertex);
        //System.out.println("distance to parent: "+current.distancetoParent);
        for (int i = 0; i < 4; i++) {
            if (current.children[i] != null){
//               if(curVertex.adjacencies==null){
//                    curVertex.adjacencies = new ArrayList<Edge>();   
//               }
//               curVertex.adjacencies.add( new Edge(v,current.children[i].distancetoParent));
               ret=getVetices(current.children[i], ret,new Vertex(current.children[i].toString(),current.children[i]));
            }
        }
        return ret;
    }
    public List<Vertex> getDijkstraGraph()
    {
        List<Vertex> vertices= getVetices(root,  new ArrayList<Vertex>(), new Vertex("root",root));
        for(Vertex v: vertices){
            if(v.adjacencies==null){
                v.adjacencies = new ArrayList<Edge>();   
            }
            for (int i = 0; i < 4; i++) {
                if (v.node.children[i] != null){
                            
                    //System.out.println("d t p: "+v.node.children[i].distancetoParent);
                    Vertex von=getVertexOfNode(v.node.children[i], vertices);
                    v.adjacencies.add( new Edge(von,v.node.children[i].distancetoParent)); 
                    if(von.adjacencies==null){
                        von.adjacencies = new ArrayList<Edge>();   
                    }
                    von.adjacencies.add(new Edge(v,v.node.children[i].distancetoParent));
                }
            }
        }
        return vertices;
    }
    public int isParent(Node n1, Node n2)
    {
        for(int i=0;i<4;i++){
            if(n1.children[i]!=null)
            {
                if(n1.children[i]==n2){
                return i;
                }
            }
        }
        return 4;
    }
    public void printGraph(Node node,int depth){
        if(node==null){
            System.out.println("--");
        }
        else{
             String result = "";
             for (int j = 0; j < depth ;j += 1) {
                   result += "\t";
             }
             System.out.println(result+node+" d:"+node.distancetoParent);
            for (int i = 0; i < 4; i++) {
                if (node.children[i] != null){
                    printGraph(node.children[i], depth+1);
                }
            }
        }
        
    }
    public Vertex getVertexOfNode(Node node, List<Vertex>vertices)
    {
        for (Vertex v : vertices){
           if(v.node==node){
               return v;
           }
        }
        return null;
    }
    public int familyHaveUnvisited(Node current,int x,int y, int count ) {

        if (current != null && current.parent != null) {
            if(current.parent.x==x && current.parent.y==y)
            {
                return count;
            }
            for (int i = 0; i < 4; i++) {
                if (current.parent.children[i] != null) {
                    if (!current.parent.children[i].visited) {
                        
                        count++;
                    }
                }
            }
            return familyHaveUnvisited(current.parent,x,y,count);

        }
        return count;
    }
    public List<Node> getUnVisited(Node current,List<Node> ret)
    {
        if(!current.visited)
        {
            ret.add(current);
        }
        for (int i = 0; i < 4; i++) {
            if (current.children[i] != null) {
              
                ret= getUnVisited(current.children[i],ret);
                
            }
        }
        return ret;
    }
    public Node haveBeenHere(int x, int y, Node current) {
        if (current != null && current.parent != null) {
            if (current.parent.x == x && current.parent.y == y) {
                return current.parent;
            } else {
                for (int i = 0; i < 4; i++) {
                    if (current.parent.children[i] != null) {
                        if (current.parent.children[i].x == x && current.parent.children[i].y == y) {
                            return current.parent.children[i];
                        }
                    }
                }
                return haveBeenHere(x, y, current.parent);
            }
        }

        return null;

    }
    //boolean Node parentsHaveUnvisited()
    //{

    //}
}
