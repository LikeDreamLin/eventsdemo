package events.common;

import java.io.IOException;

public class Utils
{

    /**
     * @throws InterruptedException
     */
    public static void sleep()
    {
        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public static void readline()
    {
        try
        {
            System.out.println("[press enter to continue]");
            System.in.read();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
