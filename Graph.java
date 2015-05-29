/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unalcol.agents.examples.labyrinth.agentP;

/**
 *
 * @author Pedro
 */
public class Graph {

    Node root;

    public Graph() {
        root = new Node(null);
        root.direction = 0;
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
