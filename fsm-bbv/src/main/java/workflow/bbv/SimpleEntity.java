package workflow.bbv;

import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.impl.AbstractStateMachine;
import events.common.Event;
import events.common.State;

public class SimpleEntity extends
    AbstractStateMachine<SimpleEntity, State, Event>
{

    public SimpleEntity()
    {
        this(null);
    }

    public SimpleEntity(final StateMachine<State, Event> driver)
    {
        super(driver);
    }

    Void onExitProgress()
    {
        System.out.println("on exit PROGRESS");
        return null;
    }

    Void onEntryProgress()
    {
        System.out.println("on entry PROGRESS");
        return null;
    }

    Void onFinishWorking()
    {
        System.out.println("on 'FINISH_WORKING'");
        return null;
    }

    Void onEntryOpen()
    {
        System.out.println("on entry OPEN");
        return null;
    }

    Void onLeaveOpen()
    {
        System.out.println("on leave OPEN");
        return null;
    }

    Void onEntryClosed()
    {
        System.out.println("on entry CLOSED");
        return null;
    }

    Void onStartWorking()
    {
        System.out.println("on 'START_WORKING' event");
        return null;
    }

}
