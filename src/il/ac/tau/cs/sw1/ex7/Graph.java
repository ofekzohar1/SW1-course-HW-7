package il.ac.tau.cs.sw1.ex7;

import java.util.*;


public class Graph implements Greedy<Graph.Edge> {
    List<Edge> lst; //Graph is represented in Edge-List. It is undirected. Assumed to be connected.
    int n; //nodes are in [0,...,n-1]

    Graph(int n1, List<Edge> lst1) {
        lst = lst1;
        n = n1;
    }

    public static class Edge implements Comparable<Edge> {
        int node1, node2;
        double weight;

        Edge(int n1, int n2, double w) {
            node1 = n1;
            node2 = n2;
            weight = w;
        }

        @Override
        public String toString() {
            return "{" + "(" + node1 + "," + node2 + "), weight=" + weight + '}';
        }

        /**
         * Compare two Edge objects - this object with the argument object.
         * @param o Other Item object to compare to
         * @return Positive num if this > o, negative if this < o and 0 if this == o
         */
        @Override
        public int compareTo(Edge o) {
            if (this.weight != o.weight)
                return Double.compare(weight, o.weight);
            if (this.node1 != o.node1)
                return Integer.compare(this.node1, o.node1);
            return Integer.compare(this.node2, o.node2);
        }
    }

    /**
     * Sort best selection list according to Edge's compareTo method.
     * Time complexity: O(nlogn) (sort), auxiliary space: O(1)
     * @return Iterator for the best selection list
     */
    @Override
    public Iterator<Edge> selection() {
        Collections.sort(lst);
        return lst.iterator();
    }

    /**
     * This method checks if adding 'element' edge to candidates list don't create a cycle.
     * Time complexity: O(n^2), auxiliary space: O(n) (candidates_lst.size() == n)
     * @param candidates_lst Optional solution list
     * @param element Edge object to check for the feasibility
     * @return True - if the added edge is valid, else false
     */
    @Override
    public boolean feasibility(List<Edge> candidates_lst, Edge element) {
        if (candidates_lst.size() > n) // A spanning tree can't exceed more than |V|-1 edges (== n)
            return false;
        boolean[] visited = new boolean[n + 1];
        visited[element.node1] = true;
        return !isCyclicRecur(candidates_lst, element.node2, element.node1, visited); // Check for cycles using private method
    }

    /*
    Recursion method searching for cycles in candidates_lst.
    Time complexity: O(n), auxiliary space: O(n) (candidates_lst.size() == n)
     */
    private boolean isCyclicRecur(List<Edge> candidates_lst, int vertex, int prevVertex, boolean[] visited) {
        visited[vertex] = true;
        int nextVertex;
        if (vertex == prevVertex) // Self loop
            return true;
        for (Edge nextEdge : candidates_lst) {
            if (nextEdge.node1 == vertex) {
                nextVertex = nextEdge.node2;
            } else if (nextEdge.node2 == vertex) {
                nextVertex = nextEdge.node1;
            } else continue;

            if (!visited[nextVertex]) { // Not visited vertex
                if (isCyclicRecur(candidates_lst, nextVertex, vertex, visited))
                    return true;
            } else if (nextVertex != prevVertex) // next vertex isn't the current vertex's parent
                return true;
        }
        return false;
    }

    /**
     * This method assign new element to the candidates list.
     * Time complexity: O(n), auxiliary space: O(1) (candidates_lst.size() == n)
     * @param candidates_lst Optional solution list
     * @param element Edge object to assign
     */
    @Override
    public void assign(List<Edge> candidates_lst, Edge element) {
        for (Edge edge: candidates_lst) {
            if (edge.node1 == element.node1 && edge.node2 == element.node2) // Edge already Added
                return;
        }
        candidates_lst.add(element);
    }

    /**
     * This method checks if the given candidates list is a valid and optimal solution.
     * Valid adn optimal == Minimum spanning tree
     * Time complexity: O(n^2), auxiliary space: O(n) (candidates_lst.size() == n)
     * @param candidates_lst Optional solution list
     * @return True - if a valid and optimum solution, else false
     */
    @Override
    public boolean solution(List<Edge> candidates_lst) {
        if (candidates_lst.size() != n)
            return false;
        boolean[] visited = new boolean[n + 1];
        boolean isCyclic = isCyclicRecur(candidates_lst, 0, -1, visited);
        for (boolean visitedVertex: visited) {
            if (!visitedVertex)
                return false; // Graph isn't connected, at least one vertex not visited
        }
        return !isCyclic; // Not cyclic connected graph with n (|V|-1) edges
    }
}
