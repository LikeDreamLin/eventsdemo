package events.camel.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

import events.camel.Producer;
import events.camel.entities.Item;
import events.camel.entities.StockItemType;
import events.camel.repositories.PostTransactionEventPublisher;
import events.camel.repositories.StockRepository;
import events.common.Event;

/**
 * Transactional service to demonstrate events after TX commit.
 * @author Anders Malmborg
 *
 */
@Transactional
public class StockServiceImpl implements StockService
{
    
    private static final String ITEM_NUMBER = "itemNumber";
    private static final Logger LOGGER = LoggerFactory.getLogger(StockServiceImpl.class);
    private final StockRepository stockRepository;
    private final Producer producer;
    private boolean isDone = false;
    
    public StockServiceImpl(StockRepository stockRepository, Producer producer)
    {
        this.stockRepository = stockRepository;
        this.producer = producer;
    }

    @Override
    public void saveAndProduce()
    {
        Item item = new Item(StockItemType.DRIVE, "driveName", ITEM_NUMBER, Double.valueOf(12.34));
        stockRepository.save(item);
        if (TransactionSynchronizationManager.isActualTransactionActive())
        {
            TransactionSynchronizationManager.registerSynchronization(new PostTransactionEventPublisher(producer));
        }
        else
        {
            throw new RuntimeException("Transaction is expected to be active at this point");
        }
    }
    
    @Override
    public void consumeAndRead(Event event)
    {
        LOGGER.info("Got event {}", event);
        readItem();
    }
    
    private void readItem()
    {
        List<Item> items = stockRepository.findByNumber(ITEM_NUMBER);
        Assert.isTrue(items.size() > 0);
        LOGGER.info("Read {}", items.get(0).toString());
        isDone = true;
    }

    @Override
    public boolean isDone()
    {
        return isDone;
    }
}
