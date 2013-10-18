package events.spring;

import org.springframework.context.ApplicationEvent;

import events.common.Event;

public class SpringEvent extends ApplicationEvent
{
    private static final long serialVersionUID = 1L;
    private final Event event;

    public SpringEvent(Object source, Event event)
    {
        super(source);
        this.event = event;
    }

    public Event getEvent()
    {
        return event;
    }


}
