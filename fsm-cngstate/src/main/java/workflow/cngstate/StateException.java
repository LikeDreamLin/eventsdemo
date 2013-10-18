package workflow.cngstate;

public class StateException extends Exception
{
    private static final long serialVersionUID = 1L;
    public StateException(final String message)
    {
        super(message);
    }
    public StateException(final String message, Throwable cause)
    {
        super(message, cause);
    }
}
