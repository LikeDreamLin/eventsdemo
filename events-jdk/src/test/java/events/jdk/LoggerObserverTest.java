package events.jdk;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import events.common.Event;

public class LoggerObserverTest
{
    private LoggerObserver target;
    private EventObservable observable;
    
    @BeforeMethod
    public void setUp()
    {
        observable = new EventObservable();
        target = new LoggerObserver(observable);
        observable.addObserver(target);
    }
    @Test
    public void noEvent()
    {
        assertNull(target.lastEvent);
    }
    
    @Test
    public void handleEvent()
    {
        observable.setEvent(Event.START_WORKING);
        assertEquals(target.lastEvent, Event.START_WORKING);
        observable.setEvent(Event.FINISH_WORKING);
        assertEquals(target.lastEvent, Event.FINISH_WORKING);
    }
}
