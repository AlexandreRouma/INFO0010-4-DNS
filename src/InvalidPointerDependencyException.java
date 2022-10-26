public class InvalidPointerDependencyException extends Exception {
    public InvalidPointerDependencyException() { super("Circular or forward dependency detected in pointer"); }
}