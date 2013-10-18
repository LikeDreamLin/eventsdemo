package workflow.stateless4j;

import com.googlecode.stateless4j.StateMachine;
import com.googlecode.stateless4j.delegates.Action;
import com.googlecode.stateless4j.delegates.Action1;
import com.googlecode.stateless4j.transitions.Transition;

import events.common.Event;
import events.common.State;
import events.common.Utils;

public class Stateless4jDemo
{

    public Stateless4jDemo()
    {
    }

    public StateMachine<State, Event> create(State initial) throws Exception
    {
        final StateMachine<State, Event> machine = new StateMachine<State, Event>(initial);

        machine.Configure(State.OPEN).Permit(Event.START_WORKING, State.PROGRESS).OnEntry(new Action()
        {
            public void doIt()
            {
                onEntryOpen(machine);
            }
        }).OnExit(new Action()
        {
            public void doIt()
            {
                onLeaveOpen(machine);
            }
        });

        machine.Configure(State.PROGRESS).Permit(Event.FINISH_WORKING, State.CLOSED)
            .OnEntryFrom(Event.FINISH_WORKING, new Action()
            {
                public void doIt()
                {
                    onFinishWorking(machine);
                }
            }).OnEntry(new Action()
            {
                public void doIt()
                {
                    onEntryProgress(machine);
                }
            }).OnExit(new Action1<Transition<State, Event>>()
            {
                public void doIt(Transition<State, Event> transition)
                {
                    onExitProgress(machine, transition);
                }
            });
        machine.Configure(State.CLOSED).OnEntry(new Action()
        {
            public void doIt()
            {
                onEntryClosed(machine);
            }
        });
        return machine;
    }

    void onExitProgress(StateMachine<State, Event> machine, Transition<State, Event> transition)
    {
        System.out.println("on exit PROGRESS, transition is :" + transition);
    }

    void onEntryProgress(StateMachine<State, Event> machine)
    {
        System.out.println("on entry PROGRESS");

    }

    void onFinishWorking(StateMachine<State, Event> machine)
    {
        System.out.println("on finish working");
    }

    void onEntryOpen(StateMachine<State, Event> machine)
    {
        System.out.println("on entry OPEN");
    }

    void onLeaveOpen(StateMachine<State, Event> machine)
    {
        System.out.println("on leave OPEN");
    }

    void onEntryClosed(StateMachine<State, Event> machine)
    {
        System.out.println("on entry CLOSED");
    }

    public static void main(String[] args) throws Exception
    {
        Stateless4jDemo s4j = new Stateless4jDemo();

        s4j.runFromOpen();
        s4j.runFromProgress();
    }

    private void runFromOpen() throws Exception
    {
        StateMachine<State, Event> machine = create(State.OPEN);
        Utils.sleep();
        machine.Fire(Event.START_WORKING);
        Utils.sleep();
        machine.Fire(Event.FINISH_WORKING);
        Utils.readline();
    }

    private void runFromProgress() throws Exception
    {
        StateMachine<State, Event> machine = create(State.PROGRESS);
        Utils.sleep();
        try
        {
            machine.Fire(Event.START_WORKING);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Utils.sleep();
        machine.Fire(Event.FINISH_WORKING);
        Utils.readline();
    }

}
