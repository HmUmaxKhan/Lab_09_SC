package graph;

import java.util.Map;
import java.util.Set;

/**
 * A mutable weighted directed graph with labeled vertices.
 * 
 * @param <L> type of vertex labels, must be immutable
 */
public interface Graph<L> {

    /**
     * Create an empty graph.
     * 
     * @return a new empty graph
     */
    public static <L> Graph<L> empty() {
        return new ConcreteEdgesGraph<>();
    }
    
    /**
     * Add a vertex.
     * 
     * @param vertex label of the vertex
     * @return true if added; false if it already exists
     */
    public boolean add(L vertex);
    
    /**
     * Add, update, or remove an edge.
     * 
     * @param source source vertex label
     * @param target target vertex label
     * @param weight edge weight (0 removes the edge)
     * @return previous weight, or 0 if none
     */
    public int set(L source, L target, int weight);
    
    /**
     * Remove a vertex and its edges.
     * 
     * @param vertex label of the vertex
     * @return true if removed; false if not found
     */
    public boolean remove(L vertex);
    
    /**
     * Get all vertices.
     * 
     * @return set of vertex labels
     */
    public Set<L> vertices();
    
    /**
     * Get source vertices and weights of edges to a target.
     * 
     * @param target target vertex label
     * @return map of source labels to edge weights
     */
    public Map<L, Integer> sources(L target);
    
    /**
     * Get target vertices and weights of edges from a source.
     * 
     * @param source source vertex label
     * @return map of target labels to edge weights
     */
    public Map<L, Integer> targets(L source);
}
