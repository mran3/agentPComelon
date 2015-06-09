/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unalcol.agents.examples.labyrinth.teseoeater.agentP;

/**
 *
 * @author Pedro
 */
public class Node {
    Node[] children;
    Node parent;
    boolean visited;
    int direction;
    int x=0;
    int y=0;
    int distancetoParent=0;
    public Node(Node _parent)
    {
        children = new Node[4];
        visited=false;
        parent=_parent;
        direction=0;
    }
}
