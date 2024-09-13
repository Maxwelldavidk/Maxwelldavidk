import java.util.*;

public class practice2 {


    public class Clusterer {
        private List<List<WeightedEdge<Integer, Double>>> adjList; // the adjacency list of the original graph
        private List<List<WeightedEdge<Integer, Double>>> mstAdjList; // the adjacency list of the minimum spanning tree
        private List<List<Integer>> clusters; // a list of k points, each representing one of the clusters.
        private double cost; // the distance between the closest pair of clusters

        public Clusterer(double[][] distances, int k) {

            int length = distances.length;
            adjList = new ArrayList<>();
            mstAdjList = new ArrayList<>();
            clusters = new ArrayList<>();

            for (int i = 0; i < length; i++) {
                adjList.add(new ArrayList<>());
                mstAdjList.add(new ArrayList<>());
            }

            for (int i = 0; i < length; i++) {
                for (int j = i; j < length; j++) {
                    adjList.get(i).add(new WeightedEdge(i, j, distances[i][j]));
                    adjList.get(j).add(new WeightedEdge(j, i, distances[j][i]));
                }
            }

            prims(0);
            makeKCluster(k);

        }

        // implement Prim's algorithm to find a MST of the graph.
        // in my implementation I used the mstAdjList field to store this.
        private void prims(int start) {

            int totalNodes = adjList.size();

            double[] distances = new double[totalNodes];

            boolean[] visited = new boolean[totalNodes];

            PriorityQueue<WeightedEdge<Integer, Double>> pq = new PriorityQueue<>();

            for (int i = 0; i < totalNodes; i++) {

                distances[i] = Double.POSITIVE_INFINITY;
            }

            distances[start] = 0;

            pq.add(new WeightedEdge(null, start, 0));


            while (!pq.isEmpty()) {
                WeightedEdge<Integer, Double> minEdge = pq.remove();

                int currentNode = minEdge.destination;

                if (visited[currentNode]) continue;
                visited[currentNode] = true;

                Integer sourceNode = minEdge.source;

                if (sourceNode != null) {

                    Double edgeWeight = minEdge.weight;

                    mstAdjList.get(sourceNode).add(new WeightedEdge(sourceNode, currentNode, edgeWeight));
                    mstAdjList.get(currentNode).add(new WeightedEdge(currentNode, sourceNode, edgeWeight));

                }

                for (WeightedEdge<Integer, Double> neighborEdge : adjList.get(currentNode)) {

                    int neighborNode = neighborEdge.destination;
                    double neighbordEdgeWeight = neighborEdge.weight;

                    if (!visited[neighborNode] && distances[neighborNode] > neighbordEdgeWeight) {

                        distances[neighborNode] = neighbordEdgeWeight;
                        pq.add(new WeightedEdge(currentNode, neighborNode, neighbordEdgeWeight));
                    }
                }


            }

        }


        // After making the minimum spanning tree, use this method to
        // remove its k-1 heaviest edges, then assign integers
        // to clusters based on which nodes are still connected by
        // the remaining MST edges.
        private void makeKCluster(int k) {
            List<WeightedEdge<Integer, Double>> edgeCluster = new ArrayList<>();
            for (List<WeightedEdge<Integer, Double>> edges : mstAdjList) {

                edgeCluster.addAll(edges);
            }

            Set<Integer> edgeHash = new HashSet<>();
            List<WeightedEdge<Integer, Double>> unique = new ArrayList<>();

            for (WeightedEdge<Integer, Double> edge : edgeCluster) {

                int hash1 = edge.source.hashCode() + edge.destination.hashCode() + edge.weight.hashCode();
                int hash2 = edge.destination.hashCode() + edge.source.hashCode() + edge.weight.hashCode();


                if (!edgeHash.contains(hash1) && !edgeHash.contains(hash2)) {

                    edgeHash.add(hash1);
                    edgeHash.add(hash2);
                    unique.add(edge);

                }
            }

            unique.sort(Comparator.reverseOrder());

            List<WeightedEdge<Integer, Double>> removedEdges = new ArrayList<>();

            for (int i = 0; i < k - 1; i++) {


                removedEdges.add(unique.get(i));

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
            clusters = new ArrayList<>();

            boolean[] visited = new boolean[mstAdjList.size()];
            for (int i = 0; i < mstAdjList.size(); i++) {
                if (!visited[i]) {
                    List<Integer> cluster = new ArrayList<>();
                    bfs(i, cluster, visited);
                    clusters.add(cluster);


                }


            }


            cost = Double.POSITIVE_INFINITY;
            for (WeightedEdge<Integer, Double> edge : removedEdges) {
                if (edge.weight < cost) {

                    cost = edge.weight;
                }


            }


        }


        private void bfs(int start, List<Integer> cluster, boolean[] visited) {
            Queue<Integer> q = new LinkedList<>();
            q.add(start);
            visited[start] = true;

            while (!q.isEmpty()) {
                int currentNode = q.remove();
                cluster.add(currentNode);

                for (WeightedEdge<Integer, Double> edge : mstAdjList.get(currentNode)) {

                    if (!visited[edge.destination]) {
                        q.add(edge.destination);
                        visited[edge.destination] = true;
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

}





   /* public class Clusterer {
        private List<List<WeightedEdge<Integer, Double>>> adjList; // the adjacency list of the original graph
        private List<List<WeightedEdge<Integer, Double>>> mstAdjList; // the adjacency list of the minimum spanning tree
        private List<List<Integer>> clusters; // a list of k points, each representing one of the clusters.
        private double cost; // the distance between the closest pair of clusters

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

        // Implement Prim's algorithm to find a MST of the graph.
        private void prims(int start) {

            int n = adjList.size();
            boolean[] visited = new boolean[n];
            PriorityQueue<WeightedEdge<Integer, Double>> pq = new PriorityQueue<>(adjList.get(start));
            visited[start] = true;

            while (!pq.isEmpty()) {
                WeightedEdge<Integer, Double> current = pq.poll();
                int source = current.source;
                int destination = current.destination;
                if (!visited[destination]) {
                    visited[destination] = true;
                    mstAdjList.get(source).add(current);
                    pq.addAll(adjList.get(destination));
                }
            }
        }

        // Remove the k-1 heaviest edges, then assign nodes to clusters based on remaining MST edges.
        private void makeKCluster(int k) {
            List<WeightedEdge<Integer, Double>> edges = new ArrayList<>();
            for (List<WeightedEdge<Integer, Double>> edgeList : mstAdjList) {
                edges.addAll(edgeList); // Collect all edges from the MST
            }


            Set<WeightedEdge<Integer, Double>> removedEdges = new HashSet<>();
            for (int i = 0; i < k - 1; i++) {
                double maxWeight = -1;
                WeightedEdge<Integer, Double> maxEdge = null;
                for (WeightedEdge<Integer, Double> edge : edges) {
                    if (!removedEdges.contains(edge) && edge.weight > maxWeight) {
                        maxWeight = edge.weight;
                        maxEdge = edge;
                    }
                }
                if (maxEdge != null) {
                    removedEdges.add(maxEdge);
                    edges.remove(maxEdge);
                }
            }

            clusters.clear();
            boolean[] visited = new boolean[adjList.size()];

            for (int i = 0; i < visited.length; i++) {
                if (!visited[i]) {
                    bfs(i, visited, removedEdges);
                }
            }
            cost = Double.MAX_VALUE;
            for (WeightedEdge<Integer, Double> edge : removedEdges) {
                if (edge.weight < cost) {
                    cost = edge.weight;
                }
            }
            System.out.println("Cluster cost: " + cost);
            System.out.println("Cluster size: " + clusters.size());
            System.out.println("mstAdjList size (points) " + mstAdjList.size());
            System.out.println("cluster layout:" + clusters);
        }

        private void bfs(int start, boolean[] visited, Set<WeightedEdge<Integer, Double>> removedEdges) {
            Queue<Integer> queue = new LinkedList<>();
            queue.add(start);
            visited[start] = true;
            List<Integer> cluster = new ArrayList<>();

            while (!queue.isEmpty()) {
                int current = queue.poll();
                cluster.add(current);
                for (WeightedEdge<Integer, Double> edge : mstAdjList.get(current)) {
                    int neighbor = edge.destination;
                    if (!visited[neighbor] && !removedEdges.contains(edge) &&
                            !removedEdges.contains(new WeightedEdge<>(neighbor, edge.source, edge.weight))) {
                        visited[neighbor] = true;
                        queue.add(neighbor);
                    }
                }
            }
            clusters.add(cluster);
        }

        public List<List<Integer>> getClusters() {
            return clusters;
        }

        public double getCost() {
            return cost;
        }
    }
}*/
