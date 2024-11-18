package graph;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A graph implementation with vertices and weighted edges.
 */
public class ConcreteEdgesGraph<L> implements Graph<L> {

    private final Set<L> vertices = new HashSet<>();
    private final List<Edge<L>> edges = new ArrayList<>();

    // Abstraction function:
    //   Represents a graph with vertices connected by weighted edges.
    // Representation invariant:
    //   - No duplicate edges (source, target) pairs.
    //   - Weight of an edge is positive.
    //   - vertices.size() >= √(2 * edges.size) to ensure minimum connectivity.
    // Safety from rep exposure:
    //   - Defensive copies ensure internal state is protected.

    private void checkRep() {
        int requiredVertices = edges.isEmpty() ? 0 : (int) Math.ceil(Math.sqrt(2 * edges.size()) + 0.5);
        assert vertices.size() >= requiredVertices;
    }

    @Override
    public boolean add(L vertex) {
        return vertices.add(vertex);
    }

    @Override
    public int set(L source, L target, int weight) {
        assert weight >= 0;
        int index = findEdgeIndex(source, target);
        int previousWeight = 0;

        if (weight > 0) {
            Edge<L> newEdge = new Edge<>(source, target, weight);
            if (index < 0) {
                add(source);
                add(target);
                edges.add(newEdge);
            } else {
                previousWeight = edges.set(index, newEdge).getWeight();
            }
        } else if (index >= 0) {
            previousWeight = edges.remove(index).getWeight();
        }

        checkRep();
        return previousWeight;
    }

    private int findEdgeIndex(L source, L target) {
        for (int i = 0; i < edges.size(); i++) {
            Edge<L> edge = edges.get(i);
            if (edge.getSource().equals(source) && edge.getTarget().equals(target)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean remove(L vertex) {
        boolean vertexRemoved = vertices.remove(vertex);
        edges.removeIf(edge -> edge.getSource().equals(vertex) || edge.getTarget().equals(vertex));
        checkRep();
        return vertexRemoved;
    }

    @Override
    public Set<L> vertices() {
        return Collections.unmodifiableSet(vertices);
    }

    @Override
    public Map<L, Integer> sources(L target) {
        return edges.stream()
                .filter(edge -> edge.getTarget().equals(target))
                .collect(Collectors.toMap(Edge::getSource, Edge::getWeight));
    }

    @Override
    public Map<L, Integer> targets(L source) {
        return edges.stream()
                .filter(edge -> edge.getSource().equals(source))
                .collect(Collectors.toMap(Edge::getTarget, Edge::getWeight));
    }

    @Override
    public String toString() {
        return edges.isEmpty()
                ? "Empty Graph"
                : edges.stream().map(Edge::toString).collect(Collectors.joining("\n"));
    }
}

/**
 * Represents a weighted edge in a graph.
 */
class Edge<L> {
    private final L source;
    private final L target;
    private final int weight;

    // Abstraction function:
    //   Represents an edge from source to target with a positive weight.
    // Representation invariant:
    //   - source and target are non-null.
    //   - weight > 0.
    // Safety from rep exposure:
    //   - Fields are private and final, ensuring immutability.

    public Edge(L source, L target, int weight) {
        assert weight > 0;
        this.source = Objects.requireNonNull(source, "Source cannot be null");
        this.target = Objects.requireNonNull(target, "Target cannot be null");
        this.weight = weight;
        checkRep();
    }

    private void checkRep() {
        assert source != null && target != null && weight > 0;
    }

    public L getSource() {
        return source;
    }

    public L getTarget() {
        return target;
    }

    public int getWeight() {
        return weight;
    }

    public Edge<L> setWeight(int newWeight) {
        return new Edge<>(source, target, newWeight);
    }

    @Override
    public String toString() {
        return source + " -> " + target + ": " + weight;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Edge)) return false;
        Edge<?> other = (Edge<?>) obj;
        return source.equals(other.source) && target.equals(other.target) && weight == other.weight;
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, target, weight);
    }
}
