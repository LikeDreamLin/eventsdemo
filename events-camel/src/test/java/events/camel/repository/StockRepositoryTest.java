package events.camel.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import events.camel.entities.Item;
import events.camel.entities.StockItemType;
import events.camel.repositories.StockRepository;

//Load the same files here as in StockServiceTest to reuse spring context between the tests
@ContextConfiguration(locations={"classpath:META-INF/jpa.spring.xml",
                                "classpath:META-INF/service.spring.xml"})
public class StockRepositoryTest extends AbstractTransactionalTestNGSpringContextTests
{
    @Autowired
    private StockRepository target;
    
    @Test
    public void insertRead()
    {
        Item item = new Item(StockItemType.DRIVE, "driveName", "driveNumber", Double.valueOf(12.34));
        item = target.save(item);
        item = target.findOne(item.getId());
        Assert.assertEquals(item.getName(), "driveName");
        List<Item> items = target.findByNumber(item.getNumber());
        Assert.assertTrue(items.size() > 0);
        Assert.assertEquals(items.get(0).getName(), "driveName");
    }
    
}
