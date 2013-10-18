package workflow.bbv;

import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.impl.AbstractStateMachineDefinition;
import events.common.Event;
import events.common.State;

public class SimpleEntityDefinition extends AbstractStateMachineDefinition<SimpleEntity,
    State, Event>
{
    @Override
    protected SimpleEntity createStateMachine(StateMachine<State, Event> driver)
    {
        return new SimpleEntity(driver);
    }

    public SimpleEntityDefinition()
    {
        super(State.OPEN);
        define();
    }

    void define()
    {
        SimpleEntity prototype = getPrototype();
        in(State.OPEN).executeOnEntry(prototype.onEntryOpen());
        in(State.OPEN).on(Event.START_WORKING).goTo(State.PROGRESS).execute(prototype.onStartWorking());
        in(State.OPEN).executeOnExit(prototype.onLeaveOpen());

        in(State.PROGRESS).executeOnEntry(prototype.onEntryProgress());
        in(State.PROGRESS).executeOnExit(prototype.onExitProgress());

        in(State.PROGRESS).on(Event.FINISH_WORKING).goTo(State.CLOSED).execute(prototype.onFinishWorking());

        in(State.CLOSED).executeOnEntry(prototype.onEntryClosed());
    }
}
