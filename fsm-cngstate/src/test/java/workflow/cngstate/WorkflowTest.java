package workflow.cngstate;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

import events.common.State;

public class WorkflowTest
{
    private WorkFlow target;
    
    @BeforeMethod
    public void setUp()
    {
        target = new WorkFlow();
    }
    
    @Test
    public void setNewState()
    {
        target.setState(State.PROGRESS);
        assertEquals(target.getState(), State.PROGRESS);
    }
    
    @Test
    public void changeValidState()
    {
        target.setState(State.OPEN);
        target.setState(State.PROGRESS);
        assertEquals(target.getState(), State.PROGRESS);
    }

    @Test
    public void changeInValidState()
    {
        target.setState(State.OPEN);
        target.setState(State.CLOSED);
        assertEquals(target.getState(), State.OPEN);
    }

    private static class WorkFlow extends AbstractWorkflow<State>
    {
        private static final Multimap<State, State> VALID_TRANSITIONS = ImmutableMultimap
            .<State, State> builder()
            .putAll(State.OPEN, State.PROGRESS)
            .putAll(State.PROGRESS, State.CLOSED)
            .build();

        @Override
        protected Multimap<State, State> getStateMap()
        {
            return VALID_TRANSITIONS;
        }
        
    }
}
