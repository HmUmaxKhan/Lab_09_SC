package poet;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import graph.Graph;

/**
 * A poetry generator that uses a word graph to build poems based on a given input text.
 * 
 * <p>The graph is built from a text file where:
 * - Words are the vertices.
 * - Connections between words are edges, weighted by how often one word follows another.
 * 
 * <p>Example: 
 * For the text "Hello hello goodbye", the graph has:
 * - Edge "hello" -> "hello" with weight 1.
 * - Edge "hello" -> "goodbye" with weight 1.
 * 
 * <p>Given an input sentence, the generator tries to insert a "bridge word" between adjacent words.
 * A bridge word connects two words through the strongest weighted path in the graph.
 * If no such path exists, no word is added.
 */
public class GraphPoet {
    private final Graph<String> affinityGraph;
    private final List<String> corpusWords;

    // Abstraction:
    //   This class represents a poetry generator that creates poems using word relationships.
    // Representation Invariant:
    //   - affinityGraph is a non-null graph.
    //   - corpusWords is a non-empty list of lowercase words.
    // Safety:
    //   - All fields are private and final.
    //   - Defensive copies are used where needed to prevent modifications.

    /**
     * Constructs a poetry generator using the given text file as the source.
     * 
     * @param corpus the text file used to build the word graph.
     * @throws IOException if the file cannot be read.
     */
    public GraphPoet(File corpus) throws IOException {
        corpusWords = extractWordsFromFile(corpus);
        affinityGraph = generateAffinityGraph(corpusWords);
        checkRep();
    }

    private void checkRep() {
        assert affinityGraph != null;
    }

    /**
     * Reads words from the given file and converts them to lowercase.
     * 
     * @param corpus the file to read words from.
     * @return a list of lowercase words.
     * @throws IOException if the file cannot be read.
     */
    private List<String> extractWordsFromFile(File corpus) throws IOException {
        List<String> words = new ArrayList<>();
        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(corpus)))) {
            while (scanner.hasNext()) {
                words.add(scanner.next().toLowerCase());
            }
        }
        assert !words.isEmpty();
        return words;
    }

    /**
     * Builds a graph where words are vertices, and edges represent word pairs from the corpus.
     * 
     * @param words the list of words from the corpus.
     * @return a graph representing word connections.
     */
    private Graph<String> generateAffinityGraph(List<String> words) {
        Graph<String> graph = Graph.empty();

        for (int i = 0; i < words.size(); i++) {
            String source = words.get(i);
            graph.add(source);

            if (i + 1 < words.size()) {
                String target = words.get(i + 1);
                int weight = graph.set(source, target, 1);
                graph.set(source, target, weight + 1);
            }
        }
        return graph;
    }

    /**
     * Returns the list of words from the corpus.
     * 
     * @return an unmodifiable list of corpus words in lowercase.
     */
    public List<String> getCorpusWords() {
        return Collections.unmodifiableList(corpusWords);
    }

    /**
     * Generates a poem by adding bridge words between adjacent words in the input text.
     * 
     * @param input the input text.
     * @return the generated poem.
     */
    public String poem(String input) {
        String[] inputWords = input.split("\\s+");
        StringBuilder poem = new StringBuilder(input);
        int fromIndex = 0;

        for (int i = 0; i < inputWords.length - 1; i++) {
            Map<String, Integer> word1Targets = affinityGraph.targets(inputWords[i].toLowerCase());
            Map<String, Integer> word2Sources = affinityGraph.sources(inputWords[i + 1].toLowerCase());

            List<String> bridges = word1Targets.keySet().stream()
                    .filter(word2Sources::containsKey)
                    .collect(Collectors.toList());

            if (!bridges.isEmpty()) {
                String bridge = bridges.get(new Random().nextInt(bridges.size()));
                int insertAt = poem.indexOf(inputWords[i + 1], fromIndex);
                poem.insert(insertAt, bridge + " ");
            }
        }
        checkRep();
        return poem.toString();
    }

    @Override
    public String toString() {
        return affinityGraph.toString();
    }
}
