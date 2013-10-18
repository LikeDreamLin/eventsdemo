package workflow.cngstate;

import java.util.Collection;

import com.google.common.collect.Multimap;

public abstract class AbstractWorkflow<T>
{
    private T state;
    protected abstract Multimap<T, T> getStateMap();
    
    public T getState()
    {
        return state;
    }

    public void setState(T toState)
    {
        if (canTransition(state, toState))
        {
            state = toState;
        }
    }
    
    /**
     * Checks if the specified state can transition to the other specified one.
     *
     * @param from the source state
     * @param to the intended target state
     * @return {@code true} if the target state is directly reachable from the source state, {@code false} otherwise.
     */
    public boolean canTransition(final T from, final T to)
    {
        if(from==null||from.equals(to))
        {
            return true;
        }
        return getPossibleStateTransitions(from).contains(to);
    }
    
    /**
     * @param originalState the source state, must not be {@code null}.
     * @return a collection of possible states from the given original state
     */
    public Collection<T> getPossibleStateTransitions(final T originalState)
    {
        return getStateMap().get(originalState);
    }
    
    
}
