package workflow.bbv;

import static org.testng.Assert.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import events.common.Event;
import events.common.State;

public class SimpleEntityDefinitionTest
{
    private SimpleEntityDefinition target;
    private SimpleEntity fsm;

    @BeforeMethod
    public void setUp()
    {
        target = new SimpleEntityDefinition();
        fsm = target.createPassiveStateMachine("fsm");
        fsm.start();
    }

    @Test
    public void initialeState()
    {
        assertEquals(fsm.getCurrentState(), State.OPEN);
    }

    @Test
    public void openToProgress()
    {
        fsm.fire(Event.START_WORKING);
        assertEquals(fsm.getCurrentState(), State.PROGRESS);
    }
    
    @Test
    public void terminateAtProgress()
    {
        fsm.fire(Event.START_WORKING);
        assertEquals(fsm.getCurrentState(), State.PROGRESS);
        fsm.terminate();
        assertEquals(fsm.getCurrentState(), State.PROGRESS);
    }
    @Test
    public void progressToClosed()
    {
        fsm.fire(Event.START_WORKING);
        fsm.fire(Event.FINISH_WORKING);
        assertEquals(fsm.getCurrentState(), State.CLOSED);
    }
    
    @Test
    public void terminateAtClosed()
    {
        fsm.fire(Event.START_WORKING);
        fsm.fire(Event.FINISH_WORKING);
        assertEquals(fsm.getCurrentState(), State.CLOSED);
        fsm.terminate();
        assertEquals(fsm.getCurrentState(), State.CLOSED);
    }
    @Test
    public void openToClosed()
    {
        fsm.fire(Event.FINISH_WORKING);
        //Transition not specified
        assertEquals(fsm.getCurrentState(), State.OPEN);
    }
    

}
