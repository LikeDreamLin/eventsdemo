package events.camel.services;

import javax.persistence.OptimisticLockException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.transaction.UnexpectedRollbackException;
import org.testng.Assert;
import org.testng.annotations.Test;

import events.camel.entities.Item;
import events.camel.entities.StockItemType;
import events.camel.repositories.StockRepository;

@ContextConfiguration(locations = { "classpath:META-INF/jpa.spring.xml",
		"classpath:META-INF/service.spring.xml" })
public class StockServiceTest extends AbstractTestNGSpringContextTests {
	@Autowired
	private StockService target;
	@Autowired
	private StockRepository stockRepository;

	private static final String ITEM_NUMBER = "itemNumber";

	@Test(expectedExceptions=UnexpectedRollbackException.class)
    public void updateAndProduceInSameTx() throws InterruptedException
    {
        Item item = new Item(StockItemType.DRIVE, "driveName", ITEM_NUMBER, Double.valueOf(12.34));
        item = stockRepository.save(item);
        target.updateAndProduce(item, true);
    }
	@Test
    public void updateAndProduceAtTxCommit() throws InterruptedException
    {
        Item item = new Item(StockItemType.DRIVE, "driveName", ITEM_NUMBER, Double.valueOf(12.34));
        item = stockRepository.save(item);
        target.updateAndProduce(item, false);
        int i = 3;
        while(!target.isDone() && i >= 0)
        {
            i--;
            Thread.sleep(1000);
        }
        Assert.assertTrue(target.isDone());
    }
}
