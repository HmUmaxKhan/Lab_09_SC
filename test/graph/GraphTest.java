package graph;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GraphTest {
    private Graph<String> graph;

    @Before
    public void setUp() {
        // Initialize a new Graph instance before each test case
        graph = new Graph<>();
    }

    /**
     * Test that a new graph has no vertices.
     * This verifies that the graph is empty upon initialization.
     */
    @Test
    public void testInitialVerticesEmpty() {
        assertTrue("Graph should be empty initially", graph.getVertices().isEmpty());
    }

    /**
     * Test adding a vertex to the graph.
     * This checks that after adding a vertex, it exists in the graph's vertex list.
     */
    @Test
    public void testAddVertex() {
        graph.addVertex("A");
        assertTrue("Graph should contain vertex A", graph.getVertices().contains("A"));
    }

    /**
     * Test adding a duplicate vertex to the graph.
     * This verifies that adding the same vertex again does not increase its count in the graph.
     */
    @Test
    public void testAddDuplicateVertex() {
        graph.addVertex("A");
        graph.addVertex("A"); // Attempt to add duplicate
        assertEquals("Graph should still contain only one A", 1, graph.getVertices().stream().filter(v -> v.equals("A")).count());
    }

    /**
     * Test setting an edge between two vertices with a specified weight.
     * This checks that the edge exists and has the correct weight after being added.
     */
    @Test
    public void testSetEdge() {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.setEdge("A", "B", 5);
        assertTrue("Edge from A to B should exist with weight 5", graph.hasEdge("A", "B"));
        assertEquals("Weight of edge from A to B should be 5", 5, graph.getEdgeWeight("A", "B"));
    }

    /**
     * Test removing a vertex from the graph.
     * This verifies that after removal, the vertex no longer exists in the graph's vertex list.
     */
    @Test
    public void testRemoveVertex() {
        graph.addVertex("A");
        graph.removeVertex("A");
        assertFalse("Graph should not contain vertex A after removal", graph.getVertices().contains("A"));
    }

    /**
     * Check sources and targets for directed edges in the graph.
     * This verifies that the sources of a target vertex and targets of a source vertex are correctly identified.
     */
    @Test
    public void checkSourcesAndTargets() {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.setEdge("A", "B", 3);
        
        assertTrue("Sources of B should include A", graph.getSources("B").contains("A"));
        assertTrue("Targets of A should include B", graph.getTargets("A").contains("B"));
    }
}
