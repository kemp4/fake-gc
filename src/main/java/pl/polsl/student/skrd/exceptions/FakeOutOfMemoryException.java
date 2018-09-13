package pl.polsl.student.skrd.exceptions;

public class FakeOutOfMemoryException extends RuntimeException {
    FakeOutOfMemoryException(){}

    public FakeOutOfMemoryException(String message) {
        super(message);
    }

}
