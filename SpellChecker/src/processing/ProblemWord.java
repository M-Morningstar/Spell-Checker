package processing;

/**
 * Represents a word in the text that has been identified as having a problem,
 * such as a misspelling, a capitalization error, or being a repeated (double) word.
 */
public class ProblemWord {
    private String word; // The problem word itself
    private int position;
    private ProblemType problemType; // The type of problem associated with the word
    
    /**
     * Enum representing the different types of problems that can be detected in the text.
     */
    public enum ProblemType {
        MISSPELLING, // Indicates a misspelling problem
        CAPITALIZATION, // Indicates a capitalization problem
        DOUBLE_WORD // Indicates a repeated word problem
    }

    /**
     * Constructs a ProblemWord instance with specified word, its position, and the problem type.
     *
     * @param word        The word that has a problem.
     * @param problemType The type of problem detected with the word.
     */
    public ProblemWord(String word, int position, ProblemType problemType) {
        this.word = word;
        this.position = position;
        this.problemType = problemType;
    }

    // Getters and setters for the private fields of the class

    /**
     * Gets the problem word.
     *
     * @return The word that has been identified as having a problem.
     */
    public String getWord() {
        return word;
    }

    /**
     * Gets the position associated with the word in the TextProcessor list.
     *
     * @return The problem type.
     */
    public int getPosition() {
        return position;
    }

    /**
     * Gets the type of problem associated with the word.
     *
     * @return The problem type.
     */
    public ProblemType getProblemType() {
        return problemType;
    }

    // Setters for the fields, if mutation is necessary, otherwise they can be omitted.
}
