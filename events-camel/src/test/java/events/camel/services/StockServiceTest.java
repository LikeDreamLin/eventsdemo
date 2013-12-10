package events.camel.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.transaction.UnexpectedRollbackException;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import events.camel.entities.Item;
import events.camel.entities.StockItemType;
import events.camel.repositories.StockRepository;

@ContextConfiguration(locations = { "classpath:META-INF/jpa.spring.xml",
		"classpath:META-INF/service.spring.xml" })
public class StockServiceTest extends AbstractTestNGSpringContextTests {
	private static final String TOPIC = "jms:topic:moreConsumers";
    private static final String QUEUE = "activemq:queue:itemQ";
    @Autowired
    @Qualifier("service")
	private StockService target;
	@Autowired
	private StockRepository stockRepository;


	private static final String ITEM_NUMBER = "itemNumber";

	@BeforeMethod
	public void beforeMethod()
	{
	    target.resetDone();
	    StockServiceImpl.resetConsumerCalls();
	}
	
	@Test(dataProvider="createItem", expectedExceptions=UnexpectedRollbackException.class)
	public void updateAndProduceInSameTx(Item item) throws InterruptedException
    {
        item = stockRepository.save(item);
        target.updateAndProduce(item, true);
    }
	@Test(dataProvider="createItem")
    public void updateAndProduceAtTxCommit(Item item) throws InterruptedException
    {
        item = stockRepository.save(item);
        target.updateAndProduce(item, false);
        int i = 3;
        while(!target.isDone() && i >= 0)
        {
            i--;
            Thread.sleep(1000);
        }
        Assert.assertTrue(target.isDone());
        Assert.assertEquals(stockRepository.findOne(item.getId()).getName(),  "consumeAndUpdate");
        Assert.assertEquals(StockServiceImpl.consumerCalls, 1);
    }
	
    @Test(dataProvider="createItem")
	public void sendToJmsCommit(Item item) throws InterruptedException
	{
	    target.sendToJms(QUEUE, item, true);
        int i = 3;
        while(!target.isDone() && i >= 0)
        {
            i--;
            Thread.sleep(500);
        }
        Assert.assertTrue(target.isDone());
        Assert.assertEquals(stockRepository.findOne(item.getId()).getName(),  "consumeAndUpdate");
        Assert.assertEquals(StockServiceImpl.consumerCalls, 1);
	}
	
	/**
	 * Sends a message using ActiveMQ which should never arrive as the TX is rolled back. 
	 * @throws InterruptedException
	 */
    @Test(dataProvider="createItem")
    public void sendToJmsRollback(Item item) throws InterruptedException
    {
        try
        {
            target.sendToJms(QUEUE, item, false);
            Assert.assertTrue(false, "should not get here");
        }
        catch (RuntimeException e)
        {
            Assert.assertEquals(e.getMessage(), "Will rollback");
        }
        int i = 3;
        while(!target.isDone() && i >= 0)
        {
            i--;
            Thread.sleep(500);
        }
        Assert.assertFalse(target.isDone());
        Assert.assertEquals(stockRepository.findOne(item.getId()).getName(),  "driveName");
        Assert.assertEquals(StockServiceImpl.consumerCalls, 0);

    }

    @Test(dataProvider="createItem")
    public void jmsPublishToMore(Item item) throws InterruptedException
    {
        target.sendToJms(TOPIC, item, true);
        int i = 3;
        while(!target.isDone() && i >= 0)
        {
            i--;
            Thread.sleep(500);
        }
        Assert.assertTrue(target.isDone());
        Assert.assertEquals(stockRepository.findOne(item.getId()).getName(),  "consumeAndUpdate");
        Assert.assertEquals(StockServiceImpl.consumerCalls, 2);
    }
    
    @DataProvider
	private Object[][] createItem()
	{
	    Item item = new Item(StockItemType.DRIVE, "driveName", ITEM_NUMBER, Double.valueOf(12.34));
        item = stockRepository.save(item);

	    return new Object[][] {
	        {
	            item
	        }
	    };
	}
}
