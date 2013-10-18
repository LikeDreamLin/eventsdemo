package events.camel;

import events.common.Event;

public class EventsCamelDemo
{
    public static void main(String[] args)
    {
        CamelProducer producer = new CamelProducer();
        producer.sendEvent(Event.FINISH_WORKING);
    }

}
