public class MissingNullTerminationException extends Exception {
    public MissingNullTerminationException() { super("Label wasn't null-terminated"); }
}
