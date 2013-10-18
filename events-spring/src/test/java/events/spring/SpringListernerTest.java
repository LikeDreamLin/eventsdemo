package events.spring;

import static org.testng.Assert.assertEquals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import events.common.Event;

@ContextConfiguration(locations="classpath:/META-INF/spring.xml")
public class SpringListernerTest extends AbstractTestNGSpringContextTests
{
    @Autowired
    private SpringListener target;
    
    @Test
    public void handleEvent()
    {
        applicationContext.publishEvent(new SpringEvent(this, Event.START_WORKING));
        assertEquals(target.lastEvent, Event.START_WORKING);
        applicationContext.publishEvent(new SpringEvent(this, Event.FINISH_WORKING));
        assertEquals(target.lastEvent, Event.FINISH_WORKING);
    }
}
