import java.util.*;

public class Clusterer {
    private List<List<WeightedEdge<Integer, Double>>> adjList; // the adjacency list of the original graph
    private List<List<WeightedEdge<Integer, Double>>> mstAdjList; // the adjacency list of the minimum spanning tree
    private List<List<Integer>> clusters; // a list of k points, each representing one of the clusters.
    private double cost; // the distance between the closest pair of clusters

    //Constructor with a 2d array for representing the distance between nodes
    //builds an adjList from the matrix
    //make k amount of clusters
    public Clusterer(double[][] distances, int k) {
        adjList = new ArrayList<>();
        mstAdjList = new ArrayList<>();
        clusters = new ArrayList<>();

        int n = distances.length;
        for (int i = 0; i < n; i++) {
            adjList.add(new ArrayList<>());
            mstAdjList.add(new ArrayList<>());
        }
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                adjList.get(i).add(new WeightedEdge<>(i, j, distances[i][j]));
                adjList.get(j).add(new WeightedEdge<>(j, i, distances[j][i]));
            }
        }
        prims(0);
        makeKCluster(k);
    }

    //Minimum spanning tree
    //We use a priority queue with edges from a start node
    //add the smallest edge weight to the mst without forming loops
    private void prims(int start) {
        int n = adjList.size();
        boolean[] visited = new boolean[n];
        PriorityQueue<WeightedEdge<Integer, Double>> pq = new PriorityQueue<>();

        pq.addAll(adjList.get(start));
        visited[start] = true;

        while (!pq.isEmpty()) {
            WeightedEdge<Integer, Double> currentEdge = pq.poll();
            int sourceNode = currentEdge.source;
            int destinationNode = currentEdge.destination;

            if (!visited[destinationNode]) {
                visited[destinationNode] = true;
                mstAdjList.get(sourceNode).add(currentEdge);
                mstAdjList.get(destinationNode).add(new WeightedEdge<>(destinationNode, sourceNode, currentEdge.weight));
                pq.addAll(adjList.get(destinationNode));
            }
        }
    }

    //Removes the heaviest edges from the mst
    //priority queue to sort and remove edges
    //hashSet to handle duplicates
    //bfs to create cluster
    private void makeKCluster(int k) {
        PriorityQueue<WeightedEdge<Integer, Double>> pq = new PriorityQueue<>((e1, e2) ->
                Double.compare(e2.weight, e1.weight));
        Set<String> nonDuplicateEdge = new HashSet<>();
        for (List<WeightedEdge<Integer, Double>> nodeEdges : mstAdjList) {
            for (WeightedEdge<Integer, Double> edge : nodeEdges) {
                String edgeKey = edge.source + " " + edge.destination;
                String reverseKey = edge.destination + " " + edge.source;
                if (!nonDuplicateEdge.contains(edgeKey) && !nonDuplicateEdge.contains(reverseKey)) {
                    nonDuplicateEdge.add(edgeKey);
                    nonDuplicateEdge.add(reverseKey);
                    pq.add(edge);
                }
            }
        }
        List<WeightedEdge<Integer, Double>> removedEdges = new ArrayList<>();
        for (int i = 0; i < k - 1; i++) {
            WeightedEdge<Integer, Double> edge = pq.poll();
            if (i == k - 2) cost = edge.weight;
            removedEdges.add(edge);
        }
        for (WeightedEdge<Integer, Double> edge : removedEdges) {
            List<WeightedEdge<Integer, Double>> sourcesEdges = mstAdjList.get(edge.source);
            for (int i = 0; i < sourcesEdges.size(); i++) {
                if (sourcesEdges.get(i).destination.equals(edge.destination) && sourcesEdges.get(i).weight.equals(edge.weight)) {
                    sourcesEdges.remove(i);
                    break;
                }
            }

            List<WeightedEdge<Integer, Double>> destinationEdges = mstAdjList.get(edge.destination);
            for (int i = 0; i < destinationEdges.size(); i++) {
                if (destinationEdges.get(i).destination.equals(edge.source) && destinationEdges.get(i).weight.equals(edge.weight)) {
                    destinationEdges.remove(i);
                    break;
                }
            }
        }
        boolean[] visited = new boolean[mstAdjList.size()];
        for (int i = 0; i < mstAdjList.size(); i++) {
            if (!visited[i]) {
                List<Integer> temp = new ArrayList<>();
                bfs(i, visited, temp);
                clusters.add(temp);
            }
        }
    }

    //breath first search
    // add starting node to que and mark as visited.
    // add node to current cluster, iterate through edges of current node and get destination.
    // if not visited, mark as true and add to queue.
    private void bfs(int start, boolean[] visited, List<Integer> cluster) {
        Queue<Integer> queue = new LinkedList<>();
        queue.add(start);
        visited[start] = true;

        while (!queue.isEmpty()) {
            int node = queue.poll();
            cluster.add(node);

            for (WeightedEdge<Integer, Double> edge : mstAdjList.get(node)) {
                if (!visited[edge.destination]) {
                    visited[edge.destination] = true;
                    queue.add(edge.destination);
                }
            }
        }
    }

    public List<List<Integer>> getClusters() {
        return clusters;
    }

    public double getCost() {
        return cost;
    }
}
