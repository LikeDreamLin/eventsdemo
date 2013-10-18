package workflow.easyflow;

import java.util.concurrent.Executor;

import au.com.ds.ef.EasyFlow;
import au.com.ds.ef.Event;
import au.com.ds.ef.FlowBuilder;
import au.com.ds.ef.State;
import au.com.ds.ef.call.EventHandler;
import au.com.ds.ef.call.StateHandler;

public class MachineDefinition
{

    private final State<Machine> OPEN = FlowBuilder.state(events.common.State.OPEN.name());
    private final State<Machine> PROGRESS = FlowBuilder.state(events.common.State.PROGRESS.name());
    private final State<Machine> CLOSED = FlowBuilder.state(events.common.State.CLOSED.name());
    private final Event<Machine> START_WORKING = FlowBuilder.event(events.common.Event.START_WORKING.name());
    private final Event<Machine> FINISH_WORKING = FlowBuilder.event(events.common.Event.FINISH_WORKING.name());

    MachineDefinition()
    {

        START_WORKING.whenTriggered(new EventHandler<Machine>()
        {

            @Override
            public void call(Event<Machine> event, State<Machine> from, State<Machine> to, Machine context)
                throws Exception
            {
                System.out.println("executing START_WORKING");
            }
        });

        FINISH_WORKING.whenTriggered(new EventHandler<Machine>()
        {
            @Override
            public void call(Event<Machine> event, State<Machine> from, State<Machine> to, Machine context)
                throws Exception
            {
                System.out.println("executing FINISH_WORKING");
            }
        });

        OPEN.whenEnter(new StateHandler<Machine>()
        {

            @Override
            public void call(State<Machine> state, Machine context) throws Exception
            {
                System.out.println("on entry OPEN : " + context.getName());
            }
        }).whenLeave(new StateHandler<Machine>()
        {
            @Override
            public void call(State<Machine> state, Machine context) throws Exception
            {
                System.out.println("on exit OPEN : " + context.getName());
            }
        });

        PROGRESS.whenEnter(new StateHandler<Machine>()
        {

            @Override
            public void call(State<Machine> state, Machine context) throws Exception
            {
                System.out.println("on entry PROGRESS : " + context.getName());
            }
        }).whenLeave(new StateHandler<Machine>()
        {
            @Override
            public void call(State<Machine> state, Machine context) throws Exception
            {
                System.out.println("on exit PROGRESS : " + context.getName());
            }
        });

        CLOSED.whenEnter(new StateHandler<Machine>()
        {

            @Override
            public void call(State<Machine> state, Machine context) throws Exception
            {
                System.out.println("on entry CLOSED : " + context.getName());
            }
        }).whenLeave(new StateHandler<Machine>()
        {
            @Override
            public void call(State<Machine> state, Machine context) throws Exception
            {
                System.out.println("on exit CLOSED : " + context.getName());
            }
        });

    }

    public EasyFlow<Machine> createFlow()
    {

        EasyFlow<Machine> flow = FlowBuilder.from(OPEN).transit(
            START_WORKING.to(PROGRESS).transit(FINISH_WORKING.finish(CLOSED)));

        flow.executor(new Executor()
        {
            @Override
            public void execute(Runnable command)
            {
                command.run();
            }
        });

        return flow;
    }

    public Event<Machine> getStartWorkingEvent()
    {
        return START_WORKING;
    }

    public Event<Machine> getFinishWorkingEvent()
    {
        return FINISH_WORKING;
    }

}
