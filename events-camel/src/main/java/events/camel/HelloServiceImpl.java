package events.camel;

public class HelloServiceImpl implements HelloService
{

    @Override
    public String hi(String msg)
    {
        return "hi "+msg;
    }

}
