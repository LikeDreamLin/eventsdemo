package events.jdk;

import java.util.Observable;
import java.util.Observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import events.common.Event;

public class LoggerObserver implements Observer
{
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerObserver.class);
    private final EventObservable observable;
    Event lastEvent;
    
    public LoggerObserver(EventObservable observable)
    {
        this.observable = observable;
    }

    @Override
    public void update(Observable o, Object arg)
    {
        if (o == observable)
        {
            LOGGER.info("{} got {}", Logger.class.getSimpleName(), observable.getEvent());
            lastEvent = observable.getEvent();
        }
    }

}
