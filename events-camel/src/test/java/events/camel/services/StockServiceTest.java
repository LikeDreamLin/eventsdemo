package events.camel.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

@ContextConfiguration(locations={"classpath:META-INF/jpa.spring.xml",
                                 "classpath:META-INF/service.spring.xml"})
public class StockServiceTest extends AbstractTestNGSpringContextTests
{
    @Autowired
    private StockService target;
    
    
    @Test
    public void produce() throws InterruptedException
    {
        target.saveAndProduce();
        int i = 3;
        while(!target.isDone() && i >= 0)
        {
            i--;
            Thread.sleep(1000);
        }
        Assert.assertTrue(target.isDone());
    }
}
