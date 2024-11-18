package graph;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implements a weighted directed graph.
 * @param <L> the label type for vertices
 */
public class ConcreteVerticesGraph<L> implements Graph<L> {
    private final List<Vertex<L>> vertices = new ArrayList<>();

    // Abstraction function:
    //   Represents a graph with vertices and directed, weighted edges.
    // Representation invariant:
    //   No duplicate vertices. Each vertex has unique edges.
    // Safety from rep exposure:
    //   Defensive copies are used when exposing internals.

    private void checkRep() {
        assert vertices.size() == vertices().size();
    }

    private int indexInVertices(L label) {
        return vertices.stream()
                .filter(v -> v.getLabel().equals(label))
                .findFirst()
                .map(vertices::indexOf)
                .orElse(-1);
    }

    @Override
    public boolean add(L vertex) {
        if (vertices().contains(vertex)) return false;
        vertices.add(new Vertex<>(vertex));
        checkRep();
        return true;
    }

    @Override
    public int set(L source, L target, int weight) {
        assert weight >= 0;
        assert !source.equals(target);

        Vertex<L> sourceVertex = vertices.stream()
                .filter(v -> v.getLabel().equals(source))
                .findFirst()
                .orElseGet(() -> {
                    Vertex<L> v = new Vertex<>(source);
                    vertices.add(v);
                    return v;
                });

        Vertex<L> targetVertex = vertices.stream()
                .filter(v -> v.getLabel().equals(target))
                .findFirst()
                .orElseGet(() -> {
                    Vertex<L> v = new Vertex<>(target);
                    vertices.add(v);
                    return v;
                });

        int prevWeight = sourceVertex.setTarget(target, weight);
        targetVertex.setSource(source, weight);
        checkRep();
        return prevWeight;
    }

    @Override
    public boolean remove(L vertex) {
        int idx = indexInVertices(vertex);
        if (idx == -1) return false;
        vertices.remove(idx);
        vertices.forEach(v -> v.remove(vertex));
        checkRep();
        return true;
    }

    @Override
    public Set<L> vertices() {
        return vertices.stream().map(Vertex::getLabel).collect(Collectors.toSet());
    }

    @Override
    public Map<L, Integer> sources(L target) {
        return vertices.stream()
                .filter(v -> v.getLabel().equals(target))
                .findFirst()
                .map(Vertex::getSources)
                .orElse(Collections.emptyMap());
    }

    @Override
    public Map<L, Integer> targets(L source) {
        return vertices.stream()
                .filter(v -> v.getLabel().equals(source))
                .findFirst()
                .map(Vertex::getTargets)
                .orElse(Collections.emptyMap());
    }

    @Override
    public String toString() {
        return vertices.stream()
                .map(v -> v.getLabel() + " -> " + v.getTargets())
                .collect(Collectors.joining("\n"));
    }
}

/**
 * Represents a vertex in a graph.
 * @param <L> the label type
 */
class Vertex<L> {
    private final L label;
    private final Map<L, Integer> sources = new HashMap<>();
    private final Map<L, Integer> targets = new HashMap<>();

    public Vertex(L label) {
        this.label = label;
    }

    public L getLabel() {
        return label;
    }

    public Map<L, Integer> getSources() {
        return Collections.unmodifiableMap(sources);
    }

    public Map<L, Integer> getTargets() {
        return Collections.unmodifiableMap(targets);
    }

    public int setSource(L source, int weight) {
        return setEdge(sources, source, weight);
    }

    public int setTarget(L target, int weight) {
        return setEdge(targets, target, weight);
    }

    private int setEdge(Map<L, Integer> edges, L vertex, int weight) {
        if (weight == 0) return edges.remove(vertex) == null ? 0 : edges.getOrDefault(vertex, 0);
        return edges.put(vertex, weight) == null ? 0 : edges.get(vertex);
    }

    public int remove(L vertex) {
        int removedSource = sources.remove(vertex) == null ? 0 : sources.getOrDefault(vertex, 0);
        int removedTarget = targets.remove(vertex) == null ? 0 : targets.getOrDefault(vertex, 0);
        return Math.max(removedSource, removedTarget);
    }

    @Override
    public String toString() {
        return label + " -> " + targets + ", <- " + sources;
    }
}
