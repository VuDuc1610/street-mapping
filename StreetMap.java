import java.io.*;
import java.lang.Math;
import javax.swing.JPanel;
import java.awt.geom.Line2D;
import java.util.*;
import java.awt.*;
import javax.swing.JFrame;
import java.io.FileNotFoundException;


class Node {
    Queue<Edge> adjacents = new PriorityQueue<Edge>(new edgeComparator());
    Node prev;
    Node closestUnvisited;
    String ID;
    Double lat;
    Double lon;
    Boolean visited = false;
    Double dist = Double.POSITIVE_INFINITY;


    public Node(String ID, Double latitude, Double longitude) {
        this.ID = ID;
        this.lat = latitude;
        this.lon = longitude;
    }

    public Node(String ID) {
        this.ID = ID;
    }

    public Node closestUnvisited() {
        Double d = Double.POSITIVE_INFINITY;
        for (Edge e : adjacents) {
            if (e.destination.visited == false && e.weight < d) {
                this.closestUnvisited = e.destination;
                d = e.weight;
            }
            if (e.destination.visited == false && e.weight < d) {
                this.closestUnvisited = e.destination;
                d = e.weight;
            }
        }
        if (d.equals(Double.POSITIVE_INFINITY)) {
            if (this.prev == null)
                return null;
            return closestUnvisited = this.prev.closestUnvisited();
        }
        return this.closestUnvisited;
    }
    public String toString() {
        return this.ID;
    }
}

class Edge {
    Node destination;
    Double weight;
    String roadID;

    public Edge(Node destination, Double weight, String roadID) {
        this.destination = destination;
        this.weight = weight;
        this.roadID = roadID;
    }

    public String toString() {
        return destination.ID;
    }
}


class edgeComparator implements Comparator<Edge> {
    @Override
    public int compare(Edge edge1, Edge edge2) {
        if (edge1.weight > edge2.weight){
            return 1;
        }
        else if (edge1.weight < edge2.weight){
            return -1;
        }
        return 0;
    }
}

class Path {
    Node node1;
    Node node2;

    public Path(Node n1, Node n2) {
        this.node1 = n1;
        this.node2 = n2;
    }
}

class Graphing {
    ArrayList<Node> allNodes = new ArrayList<>();
    ArrayList<Path> allPaths = new ArrayList<>();
    HashMap<String,Node> graph = new HashMap<String,Node>();

    public void printGraph() {
        for (Node node : graph.values()) {
            System.out.println(node);
        }
    }

    public void addEdge(String source, Edge edge) {
        graph.get(source).adjacents.add(edge);
    }

    ArrayList<Node> djikstra = new ArrayList<>();
    Double maxLat = 0.0;
    Double minLat = 1000.0;
    Double maxLong = -1000.0;
    Double minLong = 0.0;

    public Double distance(Node a, Node b) {
        return distance(a.lat, a.lon, b.lat, b.lon);
    }

// Taken from https://www.geeksforgeeks.org/program-distance-two-points-earth/
    public Double distance(Double lat1, Double lon1, Double lat2, Double lon2) {
        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);

        // Haversine formula
        double r = 3959; //radius in miles
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);

        double c = 2 * Math.asin(Math.sqrt(a));
        return (c * r); 
    }

    public void itemize(String s) {
        Scanner sc = new Scanner(s);
        String i = sc.next();
        if (i.equals("i")) { 
            String a = sc.next(); // IntID
            Double b = Double.valueOf(sc.next()); 
            Double c = Double.valueOf(sc.next()); 
            if (b > maxLat){
                maxLat = b;
            }
            if (b < minLat){
                minLat = b;
            }
            if (c < minLong){
                minLong = c;
            }
            if (c > maxLong){
                maxLong = c;
            }
            graph.put(a, new Node(a, b, c));
            return;
        } else {
            String a = sc.next(); // RoadID
            Node b = graph.get(sc.next()); // Int1
            Node c = graph.get(sc.next()); // Int2

            addEdge(c.ID, new Edge(b, distance(b, c), a));
            addEdge(b.ID, new Edge(c, distance(b, c), a));
            allPaths.add(new Path(b, c));
            return;
        }
    }

    public HashMap<String, Node> FiletoGraph(String readFile) throws FileNotFoundException {
        File file = new File(readFile);
        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) { 
            String s = sc.nextLine();
            itemize(s);
        }
        return graph;
    }

    public Double[] limits() {
        return new Double[] {maxLat, minLat, maxLong, minLong};
    }

    public ArrayList<Node> Dijkstra(String startID, String endID) {
        return Dijkstra(graph.get(startID), graph.get(endID));
    }

    public ArrayList<Node> Dijkstra(Node start, Node end) {
        for (Node n : graph.values()) {
            allNodes.add(n);

        }
        Node current = start;
        start.dist = 0.0;

        // Loop until all nodes have been visited
        while (allNodes.size() > 0) {
            if (current == null) {
                break;
            }

            for (Edge edge : current.adjacents) {
                Node adjacent = edge.destination;
                if (adjacent.dist > current.dist + edge.weight) {
                    adjacent.dist = current.dist + edge.weight;
                    adjacent.prev = current;
                }
            }

            // Mark the current node as visited
            allNodes.remove(current);
            current.visited = true;
            current = current.closestUnvisited();
            if (current == null) {
                for (Node n : allNodes) {
                    if (n.visited == false && n.prev != null) {
                        current = n;
                        break;
                    }
                }
            }
        }

        djikstra.clear();
        Node cur = end;
        while (!cur.equals(start)) {
            djikstra.add(cur);
            cur = cur.prev;
            if (cur == null){
                throw new IllegalArgumentException("two locations don't connect");
            }
        }

        djikstra.add(start);
        return djikstra;
    }
}

public class StreetMap extends JFrame {

    Double x, y;
    ArrayList<Node> dijkstraPath;
    HashMap<String, Node> graph;
    Graphing graphing;

    public StreetMap(String s, Boolean show) throws FileNotFoundException {
        graphing = new Graphing();
        graph = graphing.FiletoGraph(s);
        ArrayList<Path> allPaths = graphing.allPaths;
        Double[] d = graphing.limits(); // maxLat, minLat, maxLong, minLong
        setSize(1000, 1000);

        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics graphics) {
                super.paintComponent(graphics);
                // Custom code to paint all the Circles from the List
                Graphics2D g2d = (Graphics2D) graphics;
                g2d.setColor(Color.BLACK);
                // set the scales
                x = this.getWidth() / (d[2] - d[3]);
                y = this.getHeight() / (d[0] - d[1]);

                Node int1, int2;
                Double x1, y1, x2, y2;

                // graphing all the roads
                x = this.getWidth() / (d[2] - d[3]);
                y = this.getHeight() / (d[0] - d[1]);

                for (int i = 0; i < allPaths.size() - 1; i++) {
                    int1 = allPaths.get(i).node1;
                    int2 = allPaths.get(i).node2;
                    x1 = int1.lon;
                    y1 = int1.lat;
                    x2 = int2.lon;
                    y2 = int2.lat;

                    g2d.draw(new Line2D.Double((x1 - d[3]) * x, getHeight() - ((y1 - d[1]) * y),
                            (x2 - d[3]) * x, getHeight() - ((y2 - d[1]) * y)));
                }
                if (dijkstraPath != null && show == true) {
                    g2d.setColor(Color.BLUE);
                    g2d.setStroke(new BasicStroke(3));

                    for (int i = 0; i < dijkstraPath.size() - 1; i++) {
                        // dijkstraPath
                        x1 = dijkstraPath.get(i).lon;
                        y1 = dijkstraPath.get(i).lat;
                        x2 = dijkstraPath.get(i + 1).lon;
                        y2 = dijkstraPath.get(i + 1).lat;
                        g2d.draw(new Line2D.Double((x1 - d[3]) * x, getHeight() - ((y1 - d[1]) * y),(x2 - d[3]) * x, getHeight() - ((y2 - d[1]) * y)));
                    }
                }
            }
        };
        add(p);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public ArrayList<Node> DijkstraShortestPath(String startID, String endID) {
        if (!graph.containsKey(startID) || !graph.containsKey(endID)) {
            throw new IllegalArgumentException("locations are not in the map");
        }
        return dijkstraPath = graphing.Dijkstra(startID, endID);
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        StreetMap g = new StreetMap(args[0], true);
        for (String s : args) {
            if (s.equals("directions")) {
                ArrayList<Node> list = g.DijkstraShortestPath(args[args.length - 2], args[args.length - 1]); //startNode and endNode are both at the end of the args
                System.out.println(list);
            }
            if (s.equals("show")) {
                g.setVisible(true);
            }
        }
    }
}