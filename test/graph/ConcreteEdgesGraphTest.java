package graph;

import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

public class ConcreteEdgesGraphTest {
    private ConcreteEdgesGraph<String> graph;

    @Before
    public void setUp() {
        graph = new ConcreteEdgesGraph<>();
    }

    @Test
    public void testInitialVerticesEmpty() {
        assertTrue("Graph should be empty initially", graph.getVertices().isEmpty());
    }

    @Test
    public void testAddVertex() {
        graph.addVertex("A");
        assertTrue("Graph should contain vertex A", graph.getVertices().contains("A"));
        assertEquals("Graph should contain exactly one vertex", 1, graph.getVertices().size());
    }

    @Test
    public void testAddDuplicateVertex() {
        graph.addVertex("A");
        graph.addVertex("A"); // Adding duplicate
        assertEquals("Graph should still contain exactly one vertex", 1, graph.getVertices().size());
    }

    @Test
    public void testSetEdge() {
        graph.setEdge("A", "B", 5);
        assertTrue("Graph should have an edge from A to B", graph.hasEdge("A", "B"));
        assertEquals("Edge weight from A to B should be 5", 5, graph.getEdgeWeight("A", "B"));
        
        // Check if B is a target of A
        Set<String> targets = graph.getTargets("A");
        assertTrue("B should be a target of A", targets.contains("B"));
    }

    @Test
    public void testRemoveVertex() {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.setEdge("A", "B", 5);
        
        // Remove vertex B
        graph.removeVertex("B");
        
        assertFalse("Graph should not contain vertex B", graph.getVertices().contains("B"));
        assertFalse("Graph should not have an edge from A to B", graph.hasEdge("A", "B"));
    }

   
}