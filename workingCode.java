import java.util.*;

public class workingCode {


    /*public class Clusterer {
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
            double[] distances = new double[n];
            boolean[] visited = new boolean[n];
            PriorityQueue<WeightedEdge<Integer, Double>> pq = new PriorityQueue<>();

            Arrays.fill(distances, Double.POSITIVE_INFINITY);
            distances[start] = 0;

            pq.add(new WeightedEdge<>(null, start, 0.0));

            while (!pq.isEmpty()) {
                WeightedEdge<Integer, Double> minEdge = pq.remove();
                int currentNode = minEdge.destination;

                if (visited[currentNode]) continue;
                visited[currentNode] = true;

                Integer sourceNode = minEdge.source;
                if (sourceNode != null) {
                    mstAdjList.get(sourceNode).add(new WeightedEdge<>(sourceNode, currentNode, minEdge.weight));
                    mstAdjList.get(currentNode).add(new WeightedEdge<>(currentNode, sourceNode, minEdge.weight));
                }

                for (WeightedEdge<Integer, Double> neighborEdge : adjList.get(currentNode)) {
                    int neighborNode = neighborEdge.destination;
                    double neighborEdgeWeight = neighborEdge.weight;

                    if (!visited[neighborNode] && distances[neighborNode] > neighborEdgeWeight) {
                        distances[neighborNode] = neighborEdgeWeight;
                        pq.add(new WeightedEdge<>(currentNode, neighborNode, neighborEdgeWeight));
                    }
                }
            }
        }

        // Remove the k-1 heaviest edges, then assign nodes to clusters based on remaining MST edges.
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

            System.out.println("Cluster cost: " + cost);
            System.out.println("Cluster size: " + clusters.size());
            System.out.println("mstAdjList size (points): " + mstAdjList.size());
            System.out.println("Cluster layout: " + clusters);
        }

        // BFS traversal to form clusters
        private void bfs(int start, List<Integer> cluster, boolean[] visited) {
            Queue<Integer> queue = new LinkedList<>();
            queue.add(start);
            visited[start] = true;

            while (!queue.isEmpty()) {
                int current = queue.poll();
                cluster.add(current);
                for (WeightedEdge<Integer, Double> edge : mstAdjList.get(current)) {
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
    }*/
}