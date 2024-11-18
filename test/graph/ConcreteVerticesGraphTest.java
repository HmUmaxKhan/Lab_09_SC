package graph;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Set;

public class ConcreteVerticesGraphTest {
    private ConcreteVerticesGraph graph;
    private Vertex vertexA;
    private Vertex vertexB;
    private Vertex vertexC;

    @Before
    public void setUp() {
        graph = new ConcreteVerticesGraph();
        vertexA = new Vertex("A");
        vertexB = new Vertex("B");
        vertexC = new Vertex("C");
    }

    @Test
    public void testInitialVerticesEmpty() {
        assertTrue("Graph should be empty initially", graph.getVertices().isEmpty());
    }

    @Test
    public void testAddVertex() {
        graph.addVertex(vertexA);
        Set<Vertex> vertices = graph.getVertices();
        assertTrue("Graph should contain vertex A", vertices.contains(vertexA));
    }

    @Test
    public void testAddDuplicateVertex() {
        graph.addVertex(vertexA);
        graph.addVertex(vertexA); // Attempt to add duplicate
        Set<Vertex> vertices = graph.getVertices();
        assertEquals("Graph should contain one instance of vertex A", 1, vertices.size());
    }

    @Test
    public void testSetEdge() {
        graph.setEdge(vertexA, vertexB, 5);
        assertTrue("Graph should have an edge from A to B", graph.hasEdge(vertexA, vertexB));
        assertEquals("Edge weight from A to B should be 5", 5, graph.getEdgeWeight(vertexA, vertexB));
    }

    @Test
    public void testRemoveVertex() {
        graph.addVertex(vertexA);
        graph.removeVertex(vertexA);
        Set<Vertex> vertices = graph.getVertices();
        assertFalse("Graph should not contain vertex A after removal", vertices.contains(vertexA));
    }

    @Test
    public void checkSourcesAndTargets() {
        graph.setEdge(vertexA, vertexB, 10);
        graph.setEdge(vertexC, vertexB, 15);

        Set<Vertex> sourcesForB = graph.getSources(vertexB);
        assertTrue("Vertex A should be a source for B", sourcesForB.contains(vertexA));
        assertTrue("Vertex C should also be a source for B", sourcesForB.contains(vertexC));

        Set<Vertex> targetsForA = graph.getTargets(vertexA);
        assertTrue("Vertex B should be a target for A", targetsForA.contains(vertexB));
        assertTrue("Vertex A should not have any other targets", targetsForA.size() == 1);
    }
}