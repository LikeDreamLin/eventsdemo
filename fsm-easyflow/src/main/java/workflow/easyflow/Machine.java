package workflow.easyflow;

import au.com.ds.ef.StatefulContext;

public class Machine extends StatefulContext
{

    final String name;

    public Machine(String name)
    {
        this.name = name;
    }

    public Machine(String aId, String name)
    {
        super(aId);
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

}
