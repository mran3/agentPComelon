/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unalcol.agents.examples.labyrinth.teseoeater.agentP;

import java.util.ArrayList;
import java.util.List;
import unalcol.agents.Action;
import unalcol.agents.AgentProgram;
import unalcol.agents.Percept;
import unalcol.agents.simulate.util.SimpleLanguage;
//import unalcol.random.Random;
import java.util.Random;
import unalcol.types.collection.vector.Vector;

/**
 *
 * @author Pedro
 */
public class AgentP implements AgentProgram {

    protected SimpleLanguage language;
    protected Vector<String> cmd = new Vector<String>();
    protected java.util.Vector<String> memoria = new java.util.Vector<String>();
    protected String ultimaComida = null;
    protected Integer energiaAntesDeComer = null;
    Graph graph = new Graph();
    Node currentNode = graph.root;
    int direction = 0;
    int x = 0;
    int y = 0;
    boolean start = true;
    int traveledDistance = 0;
    List<Vertex> pathToShortestUnvisited = new ArrayList<Vertex>();

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
            traveledDistance++;
            if (pathToShortestUnvisited.size() == 1) {
                if (pathToShortestUnvisited.get(0).node == currentNode) {
                    //System.err.println("Llegue!");
                    pathToShortestUnvisited.remove(pathToShortestUnvisited.get(0));
                }
            } else if (pathToShortestUnvisited.size() > 1) {
                //remove current
                pathToShortestUnvisited.remove(0);
                int isParentIndex = graph.isParent(currentNode, pathToShortestUnvisited.get(0).node);
                // if ist not its parent it returns 4
                if (graph.isParent(currentNode, pathToShortestUnvisited.get(0).node) < 4) {
                    //is its parent
                    rotateTo((currentNode.direction + isParentIndex) % 4);
                    currentNode = currentNode.children[isParentIndex];
                    computeCoordinates();
                    System.out.println("Going to... : " + currentNode);
                    return 0;
                } else {
                    //its a child
                    rotateTo(oposite(currentNode.direction));
                    currentNode = currentNode.parent;
                    computeCoordinates();
                    System.out.println("Going to... : " + currentNode);
                    return 0;
                }
                //System.out.println("Path: " + pathToShortestUnvisited.toString());
            }
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
                            currentNode.distancetoParent = traveledDistance;
                            traveledDistance = 0;
                            for (int i = 1; i < 4; i++) {
                                if (havebeen.children[i] != null) {
                                    if (!havebeen.children[i].visited) {
                                        int childDir = (i + havebeen.direction) % 4;
                                        if (oposite(direction) == childDir) {
                                            System.out.println("hijo mio!!!!!!");
                                            havebeen.children[i].visited = true;
                                        //havebeen.children[i]=currentNode.parent;
                                            //distancia recorrida hasta el bucle
                                            int lostDistance = currentNode.distancetoParent;

                                        }
                                    }
                                }
                            }
                            //List<Vertex> v=graph.getVetices(graph.root, new ArrayList<Vertex>(), new Vertex("root",graph.root));
                            //System.out.println(v.size());

                            List<Node> uv = graph.getUnVisited(graph.root, new ArrayList<Node>());
                            // graph.printGraph(graph.root, 0);
                            System.out.println("Unvisited left: " + uv.size());
                            List<Vertex> vertices = graph.getDijkstraGraph();
                            int closestUnvisitedDistance = 1000;
                            Node closestUnvisitedNode = null;
                            for (Vertex v : vertices) {
                                if (v.node == havebeen) {
                                    Dijkstra.computePaths(v);
                                    System.out.println("From " + v);
                                    for (Vertex v1 : vertices) {
                                        if (uv.contains(v1.node)) {
                                            //System.out.println("Distance to "+v1+": " + v1.minDistance);
                                            //List<Vertex> path = Dijkstra.getShortestPathTo(v1);
                                            //System.out.println("Path: " + path);
                                            if (v1.minDistance < closestUnvisitedDistance) {
                                                closestUnvisitedDistance = (int) v1.minDistance;
                                                closestUnvisitedNode = v1.node;

                                            }
                                        }
                                    }

                                    if (closestUnvisitedNode != null) {
                                        System.out.println("Distance to " + closestUnvisitedNode + ": " + closestUnvisitedDistance);
                                        pathToShortestUnvisited = Dijkstra.getShortestPathTo(graph.getVertexOfNode(closestUnvisitedNode, vertices));
                                        System.out.println("Path: " + pathToShortestUnvisited.toString());

                                        //remove current
                                        pathToShortestUnvisited.remove(0);
                                        int isParentIndex = graph.isParent(havebeen, pathToShortestUnvisited.get(0).node);
                                        // if ist not its parent it returns 4

                                        if (graph.isParent(havebeen, pathToShortestUnvisited.get(0).node) < 4) {
                                            //is its parent
                                            rotateTo((havebeen.direction + isParentIndex) % 4);
                                            currentNode = havebeen.children[isParentIndex];
                                            computeCoordinates();
                                            System.out.println("Going to... : " + currentNode);
                                            return 0;
                                        } else {
                                            //its a child
                                            rotateTo(oposite(havebeen.direction));
                                            currentNode = havebeen.parent;
                                            computeCoordinates();
                                            System.out.println("Going to... : " + currentNode);
                                            return 0;
                                        }
                                        //
                                    }
                                }
                            }

//                            System.out.println("family unvisited: "+graph.familyHaveUnvisited(currentNode, x,y,0));
//                            if (graph.familyHaveUnvisited(currentNode, x, y,0)>3) {
//                                System.out.println("Vuelve!");
//                                direction = oposite(direction);
//                                currentNode = currentNode.parent;
//                                computeCoordinates();
//                                return 2;
//                            }
//                            System.out.println("Sigue!");
//
//                            //System.out.println("no hay mas rutas, sigue por donde vino!");
//                            currentNode = havebeen.parent;
//                            rotateTo(oposite(havebeen.direction));
//                            computeCoordinates();
//                            return 0;
                        }
                    }
                    if (graph.haveBeenHere(x, y, currentNode) == null) {
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
                        System.out.println("Distance: " + traveledDistance);
                        currentNode.direction = direction;
                        Random rand = new Random();
                        currentNode.distancetoParent = traveledDistance;
                        traveledDistance = 0;
                        //heuristic
                        if (currentNode.children[0] != null && rand.nextInt(10) < 5) {
                            //la mayoría de las veces sigue derecho
                            currentNode = currentNode.children[0];
                            computeCoordinates();
                            return 0;
                        } else {
                            //if it hasnt available children 
                            for (int i = 1; i < 4; i++) {
                                if (currentNode.children[i] != null) {
                                    currentNode = currentNode.children[i];
                                    direction = (i + direction) % 4;
                                    computeCoordinates();
                                    return i;
                                }
                            }
                        }
                    }

                } else {
                    System.out.println("este nodo ya fue visitado");
                    //ya fue visitado
                    int i = 0;
                    int count = 0;
                    while (count < 16) {
                        Random rand = new Random();
                        i = rand.nextInt(4);
                        if (currentNode.children[i] != null) {
                            if (!currentNode.children[i].visited) {
                                //System.out.println("QUEDA UNA RUTA!!");
                                rotateTo((currentNode.direction + i) % 4);
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
                System.out.println("Restart!");
                Graph graph = new Graph();
                currentNode = graph.root;
                currentNode.visited = false;
                start = true;
                return 0;
            }
        }

        if (possibleWaysCount == 1) {
            //System.out.println("one way!!");
            traveledDistance++;
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
                currentNode.distancetoParent = traveledDistance;
                traveledDistance = 0;
                currentNode = currentNode.parent;
            }

            //go back
            computeCoordinates();
            return 2;
        }
        //aleatorio 
        System.out.println("LOCO2");
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

    //5 es si hay comida

    public boolean comerONo(boolean color, boolean forma, boolean tamaño, boolean peso, int energia) {
        String marcaComida;
        marcaComida = color ? "1" : "0";
        marcaComida += forma ? "1" : "0";
        marcaComida += tamaño ? "1" : "0";
        marcaComida += peso ? "1" : "0";

        String comidaBuena = marcaComida + "1";
        String comidaMala = marcaComida + "0";

        ultimaComida = marcaComida;

        if (memoria.indexOf(comidaBuena) != -1) {
            return true;
        } else if (memoria.indexOf(comidaMala) != -1) {
            return false;
        } else { //si no ha probado esa comida antes...

            //si no ha probado al menos 3 comidas, que trague de one 
            //si tiene mucha energía, que aprenda (trague) de one
            //si está a punto de morir, que se arriesgue (trague)
            if (memoria.size() < 2 || energia > 29 || energia < 3) {
                return true;
            }
            if (evaluarComidaDesconocida(marcaComida) < 0) {
                return false;
            } else {
                return true;
            }

        }

    }

    public double evaluarComidaDesconocida(String marcaComida) {

        double juicioTotal = 0;
        for (String comidaEnMemoria : memoria) {
            double juicioComida = 0;
            String comidaEnMemoriaSinTipo = comidaEnMemoria.substring(0, comidaEnMemoria.length() - 1);
            int comidaEnMemoriaInt = Integer.parseInt(comidaEnMemoriaSinTipo, 2);
            int comidaActualInt = Integer.parseInt(marcaComida, 2);
            int resultado = comidaEnMemoriaInt & comidaActualInt;
            switch (Integer.bitCount(resultado)) {
                case 3: //muy parecido
                    juicioComida = 0.75;
                    break;
                case 2: //mas o menos
                    juicioComida = 0.5;
                    break;
                case 0: //es considerablemente diferente
                    juicioComida = -0.35;
            }
            char ultimoCaracter = comidaEnMemoria.charAt(comidaEnMemoria.length() - 1);
            if (ultimoCaracter == '0') { //si es venenoso (0), se devolverá negativo
                juicioComida *= -1;
            }
            juicioTotal += juicioComida;
        }

        return juicioTotal;

    }

    public Action compute(Percept p) {
        int energia = ((Integer) p.getAttribute(language.getPercept(10)));
        boolean lleno = false;

        if (ultimaComida != null) {
            //Quitamos cualquier info. previa referente a esa comida
            memoria.removeElement(ultimaComida + "1");
            memoria.removeElement(ultimaComida + "0");

            if (energia > energiaAntesDeComer) {
                memoria.add(ultimaComida + "1");
            } else if (energia > 30 && energia == energiaAntesDeComer) { //ya se lleno
                lleno = true;
                System.out.println("Me llené");
            } else {
                memoria.add(ultimaComida + "0");
            }
            ultimaComida = null;
        }
        if (cmd.size() == 0) {
            //Percepciones y acciones sobre comida
            if ((Boolean) p.getAttribute(language.getPercept(5))) { //hay comida
                boolean color = ((Boolean) p.getAttribute(language.getPercept(6)));
                boolean forma = ((Boolean) p.getAttribute(language.getPercept(7)));
                boolean tamaño = ((Boolean) p.getAttribute(language.getPercept(8)));
                boolean peso = ((Boolean) p.getAttribute(language.getPercept(9)));

                if (!lleno) {
                    if (comerONo(color, forma, tamaño, peso, energia)) {
                        energiaAntesDeComer = energia;
                        cmd.add(language.getAction(4)); //trague
                        String x = cmd.get(0);
                        cmd.remove(0);
                        return new Action(x);
                    } else {
                        ultimaComida = null;
                    }
                }

            }

            //acciones y percepciones sobre movimiento
            boolean PF = ((Boolean) p.getAttribute(language.getPercept(0))).
                    booleanValue();
            boolean PD = ((Boolean) p.getAttribute(language.getPercept(1))).
                    booleanValue();
            boolean PA = ((Boolean) p.getAttribute(language.getPercept(2))).
                    booleanValue();
            boolean PI = ((Boolean) p.getAttribute(language.getPercept(3)));
            boolean MT = ((Boolean) p.getAttribute(language.getPercept(4)));

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
