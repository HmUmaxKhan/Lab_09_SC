package poet;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.*;
import java.nio.file.*;

public class GraphPoetTest {
	private File createTempCorpusFile(String content) throws IOException {
        File tempFile = File.createTempFile("corpus", ".txt");
        tempFile.deleteOnExit();
        Files.write(tempFile.toPath(), content.getBytes());
        return tempFile;
    }

    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled() {
        // This test verifies that assertions are enabled
        assert false : "Assertions should be enabled";
    }

    @Test
    public void testBasicPoemGeneration() throws IOException {
        // Create a simpler corpus with direct connections
        String corpus = "hello beautiful world\nbeautiful world today";
        File corpusFile = createTempCorpusFile(corpus);
        
        GraphPoet poet = new GraphPoet(corpusFile);
        String input = "hello world";
        String poem = poet.poem(input);
        
        // The poem should contain "beautiful" as a bridge word
        assertTrue("Expected 'hello beautiful world' but got: " + poem, 
                  poem.equals("hello beautiful world"));
    }

    @Test
    public void testNoBridgeWords() throws IOException {
        // Create a corpus with no connecting words
        String corpus = "apple banana\ncat dog";
        File corpusFile = createTempCorpusFile(corpus);
        
        GraphPoet poet = new GraphPoet(corpusFile);
        String input = "hello world";
        String poem = poet.poem(input);
        
        // No bridge words should be added
        assertEquals("hello world", poem);
    }

    @Test
    public void testEmptyInput() throws IOException {
        String corpus = "hello world";
        File corpusFile = createTempCorpusFile(corpus);
        
        GraphPoet poet = new GraphPoet(corpusFile);
        String input = "";
        String poem = poet.poem(input);
        
        // Empty input should return empty output
        assertEquals("", poem);
    }

    @Test
    public void testCaseInsensitivity() throws IOException {
        // Create corpus with specific case
        String corpus = "hello beautiful world";
        File corpusFile = createTempCorpusFile(corpus);
        
        GraphPoet poet = new GraphPoet(corpusFile);
        String input = "HELLO WORLD";
        String poem = poet.poem(input);
        
        // The result should contain "beautiful" regardless of input case
        assertTrue("Expected 'HELLO beautiful WORLD' but got: " + poem,
                  poem.equalsIgnoreCase("HELLO beautiful WORLD"));
    }

    @Test
    public void testSpecialCharacters() throws IOException {
        // Simplified corpus with punctuation
        String corpus = "hello beautiful world!\nbeautiful world today.";
        File corpusFile = createTempCorpusFile(corpus);
        
        GraphPoet poet = new GraphPoet(corpusFile);
        String input = "hello world!";
        String poem = poet.poem(input);
        
        // Should preserve punctuation while adding bridge word
        assertTrue("Expected 'hello beautiful world!' but got: " + poem,
                  poem.equals("hello beautiful world!"));
    }

    @Test
    public void testBridgeWordsInMiddle() throws IOException {
        // Create corpus with clear middle connections
        String corpus = "the quick brown\nbrown fox jumps\nfox over lazy";
        File corpusFile = createTempCorpusFile(corpus);
        
        GraphPoet poet = new GraphPoet(corpusFile);
        String input = "the fox lazy";
        String poem = poet.poem(input);
        
        // Should contain appropriate bridge words from the corpus
        assertTrue("Expected bridge words between 'the fox lazy'",
                  poem.contains("quick") || poem.contains("brown") || 
                  poem.contains("over"));
    }

    @Test(expected = IOException.class)
    public void testInvalidFile() throws IOException {
        // Test with a non-existent file
        File nonExistentFile = new File("nonexistent.txt");
        new GraphPoet(nonExistentFile);
    }

    @Test
    public void testMultiplePossibleBridges() throws IOException {
        // Create corpus with multiple possible bridge words
        String corpus = "hello sweet world\nhello beautiful world\nhello wonderful world";
        File corpusFile = createTempCorpusFile(corpus);
        
        GraphPoet poet = new GraphPoet(corpusFile);
        String input = "hello world";
        String poem = poet.poem(input);
        
        // Should contain one of the possible bridge words
        assertTrue(poem.matches("hello (sweet|beautiful|wonderful) world"));
    }

    @Test
    public void testCorpusWordsImmutability() throws IOException {
        String corpus = "hello world";
        File corpusFile = createTempCorpusFile(corpus);
        
        GraphPoet poet = new GraphPoet(corpusFile);
        var words = poet.getCorpusWords();
        
        // Verify that the returned list is unmodifiable
        assertThrows(UnsupportedOperationException.class, () -> {
            words.add("test");
        });
    }
}