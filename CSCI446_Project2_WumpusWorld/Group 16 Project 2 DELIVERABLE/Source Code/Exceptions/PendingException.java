package Exceptions;

/**
 * Synonymous with NotImplimentedException
 */
public class PendingException extends RuntimeException
{
    public PendingException() { }
    public PendingException(String message) { super(message); }
}
