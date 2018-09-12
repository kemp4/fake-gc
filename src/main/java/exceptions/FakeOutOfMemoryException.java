package exceptions;

public class FakeOutOfMemoryException extends RuntimeException {
    FakeOutOfMemoryException(){};

    public FakeOutOfMemoryException(String message) {
        super(message);
    }

}
