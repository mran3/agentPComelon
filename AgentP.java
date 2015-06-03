/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unalcol.agents.examples.labyrinth.agentP;

import unalcol.agents.Action;
import unalcol.agents.AgentProgram;
import unalcol.agents.Percept;
import unalcol.agents.simulate.util.SimpleLanguage;
import unalcol.random.Random;
import unalcol.types.collection.vector.Vector;

/**
 *
 * @author Pedro
 */
public class AgentP implements AgentProgram {

    protected SimpleLanguage language;
    protected Vector<String> cmd = new Vector<String>();

    Graph graph = new Graph();
    Node currentNode = graph.root;
    int direction = 0;
    int x = 0;
    int y = 0;
    boolean start = true;

    public AgentP() {
        currentNode.visited = false;
        System.out.println("hello agent Pedro.");
    }

    public void rotateTo(int _direction) {
        System.out.println("current dir:" + direction + " rotate to dir:" + _direction);
        switch (direction) {
            case 0:
                switch (_direction) {
                    case 1:
                        cmd.add(language.getAction(3));
                        direction = _direction;
                        break;
                    case 2:
                        cmd.add(language.getAction(3));
                        cmd.add(language.getAction(3));
                        direction = _direction;
                        break;
                    case 3:
                        cmd.add(language.getAction(3));
                        cmd.add(language.getAction(3));
                        cmd.add(language.getAction(3));
                        direction = _direction;
                        break;
                }
                break;
            case 1:
                switch (_direction) {
                    case 0:
                        cmd.add(language.getAction(3));
                        cmd.add(language.getAction(3));
                        cmd.add(language.getAction(3));
                        direction = _direction;
                        break;
                    case 2:
                        cmd.add(language.getAction(3));
                        direction = _direction;
                        break;
                    case 3:
                        cmd.add(language.getAction(3));
                        cmd.add(language.getAction(3));
                        direction = _direction;
                        break;
                }
                break;
            case 2:
                switch (_direction) {
                    case 0:
                        cmd.add(language.getAction(3));
                        cmd.add(language.getAction(3));
                        direction = _direction;
                        break;
                    case 1:
                        cmd.add(language.getAction(3));
                        cmd.add(language.getAction(3));
                        cmd.add(language.getAction(3));
                        direction = _direction;
                        break;
                    case 3:
                        cmd.add(language.getAction(3));
                        direction = _direction;
                        break;
                }
                break;
            case 3:
                switch (_direction) {
                    case 0:
                        cmd.add(language.getAction(3));
                        direction = _direction;
                        break;
                    case 1:
                        cmd.add(language.getAction(3));
                        cmd.add(language.getAction(3));
                        direction = _direction;
                        break;
                    case 2:
                        cmd.add(language.getAction(3));
                        cmd.add(language.getAction(3));
                        cmd.add(language.getAction(3));
                        direction = _direction;
                        break;
                }
                break;

        }
    }

    public void setLanguage(SimpleLanguage _language) {
        language = _language;
    }

    public void init() {
        cmd.clear();
    }

    public int oposite(int direction) {
        return (direction + 2) % 4;
    }
    
    public int accion(boolean PF, boolean PD, boolean PA, boolean PI, boolean MT) {
        //System.out.println("AF: "+AF+" AD:"+AD+" AA:"+AA+" AI:"+AI);
         //System.out.println(language.getPercept(6));
         //boolean resource=((Boolean) p.getAttribute(language.getPercept(3)));
        //System.out.println(direction);

        if (MT) {
            System.out.println("Fin");
            return -1;
        }

        //System.out.println("("+x+","+y+")");
        int possibleWaysCount = (PF ? 0 : 1) + (PD ? 0 : 1) + (PI ? 0 : 1);
        if (start) {
            if (!PA) {
                currentNode.children[2] = new Node(currentNode);
                possibleWaysCount++;
            }
            start = false;
        }
        if (possibleWaysCount >= 2) {
            //System.out.println(possibleWaysCount);
            //2 is behind
            if (currentNode != null) {
                if (!currentNode.visited) {

                    //verifica si no es un bucle
                    if (currentNode.parent != null) {
                        Node havebeen = graph.haveBeenHere(x, y, currentNode);
                        if (havebeen != null) {
                            System.out.println("Bucle.");
                            currentNode.visited = true;
                               System.out.println("family unvisited: "+graph.familyHaveUnvisited(currentNode, x,y,0));
                            if (graph.familyHaveUnvisited(currentNode, x, y,0)>3) {
                                System.out.println("Vuelve!");
                                direction = oposite(direction);
                                currentNode = currentNode.parent;
                                computeCoordinates();
                                return 2;
                            }
                            System.out.println("Sigue!");
                            
                            currentNode = havebeen.parent;
                            rotateTo(oposite(havebeen.direction));
                            computeCoordinates();
                            return 0;
                        }
                    }
                    //System.out.println("11111");
                    if (!PF) {
                        currentNode.children[0] = new Node(currentNode);
                    }
                    if (!PD) {
                        currentNode.children[1] = new Node(currentNode);
                    }
                    if (!PI) {
                        currentNode.children[3] = new Node(currentNode);

                    }
                    currentNode.visited = true;
                    currentNode.x = x;
                    currentNode.y = y;
                    System.out.println("Node visited, dir: " + direction);
                    currentNode.direction = direction;
                    Random rand = new Random();

                    //heuristic
                    if (currentNode.children[0] != null && rand.nextInt(10) < 9) {
                         //la mayoría de las veces sigue derecho
                        currentNode = currentNode.children[0];
                        computeCoordinates();
                        return 0;
                    }
                    else 
                    {
                        int i = 0;
                        //if it hasnt available children 
                        for(i=0;i<4;i++)
                        {
                            if (currentNode.children[i] != null) {
                                currentNode = currentNode.children[i];
                                direction = (i + direction) % 4;
                                computeCoordinates();
                                return i;
                            }
                        }
                    }

                } else {
                    //System.out.println("11111");
                    //ya fue visitado
                    int i = 0;
                    int count = 0;
                    while ( count < 16) {
                        Random rand = new Random();
                        i = rand.nextInt(4);
                        if (currentNode.children[i] != null) {
                            if (!currentNode.children[i].visited) {
                                //System.out.println("QUEDA UNA RUTA!!");
                                //
 
                                rotateTo((currentNode.direction+i)%4);
                                currentNode = currentNode.children[i];
                                computeCoordinates();
                                return 0;
                            }
                        }
                        count++;
                    }
                    System.out.println("Camino Muerto");
                    rotateTo(oposite(currentNode.direction));
                    currentNode = currentNode.parent;
                    computeCoordinates();
                    return 0;
                }
            } else {
                //aleatorio 
                boolean flag = true;
                int k = 0;
                while (flag) {
                    k = (int) (Math.random() * 4);
                    switch (k) {
                        case 0:
                            flag = PF;
                            break;
                        case 1:
                            flag = PD;
                            break;
                        case 2:
                            flag = PA;
                            break;
                        default:
                            flag = PI;
                            break;
                    }
                }
                computeCoordinates();
                return k;
            }
        }

        if (possibleWaysCount == 1) {
            //System.out.println("one way!!");
            if (!PF) {
                computeCoordinates();
                return 0;
            }
            if (!PD) {
                direction = (direction + 1) % 4;
                computeCoordinates();
                return 1;
            }
            if (!PI) {
                direction = (direction + 3) % 4;
                computeCoordinates();
                return 3;
            }
        }
        if (possibleWaysCount == 0) {
            //System.out.println("dead end!!! dir: "+Integer.toString(direction));
            if (currentNode != null) {
                currentNode.visited = true;
                direction = oposite(direction);
                currentNode = currentNode.parent;
            }

            //go back
            computeCoordinates();
            return 2;
        }
        //aleatorio 
        boolean flag = true;
        int k = 0;
        while (flag) {
            k = (int) (Math.random() * 4);
            switch (k) {
                case 0:
                    flag = PF;
                    break;
                case 1:
                    flag = PD;
                    break;
                case 2:
                    flag = PA;
                    break;
                default:
                    flag = PI;
                    break;
            }
        }
        computeCoordinates();
        return k;
    }

    ;

 public void computeCoordinates() {
        switch (direction) {
            case 0:
                y--;
                break;
            case 1:
                x++;
                break;
            case 2:
                y++;
                break;
            case 3:
                x--;
                break;
        }
    }

    public Action compute(Percept p) {
        if (cmd.size() == 0) {

            boolean PF = ((Boolean) p.getAttribute(language.getPercept(0))).
                    booleanValue();
            boolean PD = ((Boolean) p.getAttribute(language.getPercept(1))).
                    booleanValue();
            boolean PA = ((Boolean) p.getAttribute(language.getPercept(2))).
                    booleanValue();
            boolean PI = ((Boolean) p.getAttribute(language.getPercept(3))).
                    booleanValue();
            boolean MT = ((Boolean) p.getAttribute(language.getPercept(4))).
                    booleanValue();

            int d = accion(PF, PD, PA, PI, MT);
            if (0 <= d && d < 4) {
                for (int i = 1; i <= d; i++) {
                    cmd.add(language.getAction(3)); //rotate
                }
                cmd.add(language.getAction(2)); // advance
            } else {
                cmd.add(language.getAction(0)); // die
            }
        }
        String x = cmd.get(0);
        cmd.remove(0);
        return new Action(x);
    }

    /**
     * goalAchieved
     *
     * @param perception Perception
     * @return boolean
     */
    public boolean goalAchieved(Percept p) {
        return (((Boolean) p.getAttribute(language.getPercept(4))).booleanValue());
    }
}
