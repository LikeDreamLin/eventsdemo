package workflow.bbv;

import events.common.Event;
import events.common.State;
import events.common.Utils;

public class BbvDemo
{

    SimpleEntityDefinition definition = new SimpleEntityDefinition();

    public static void main(String[] args)
    {
        BbvDemo bbv = new BbvDemo();
        bbv.run();
        bbv.runFromProgress();
    }

    private void run()
    {

        SimpleEntity entity = definition.createPassiveStateMachine("passive-1");
        entity.start();
        Utils.sleep();

        entity.fire(Event.START_WORKING);
        Utils.sleep();
        entity.fire(Event.FINISH_WORKING);

        Utils.readline();
        entity.terminate();

    }

    private void runFromProgress()
    {

        SimpleEntity entity = definition.createPassiveStateMachine("passive-2", State.PROGRESS);
        entity.start();
        Utils.sleep();

        entity.fire(Event.START_WORKING);
        Utils.sleep();
        entity.fire(Event.FINISH_WORKING);

        Utils.readline();
        entity.terminate();

    }

}
