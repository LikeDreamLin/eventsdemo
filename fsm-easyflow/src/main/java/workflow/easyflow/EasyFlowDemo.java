package workflow.easyflow;

import au.com.ds.ef.EasyFlow;
import events.common.Utils;

public class EasyFlowDemo
{

    public static void main(String[] args)
    {

        MachineDefinition machineDef = new MachineDefinition();
        EasyFlow<Machine> flow = machineDef.createFlow();

        Machine machine = new Machine("machine-123");
        flow.start(machine);

        Utils.sleep();
        machineDef.getStartWorkingEvent().trigger(machine);

        Utils.sleep();
        machineDef.getFinishWorkingEvent().trigger(machine);

        Utils.readline();

    }

}
