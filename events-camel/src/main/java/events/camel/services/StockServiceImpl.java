package events.camel.services;

import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import events.camel.Producer;
import events.camel.entities.Item;
import events.camel.repositories.PostTransactionEventPublisher;
import events.camel.repositories.StockRepository;

/**
 * Transactional service to demonstrate events after TX commit.
 * @author Anders Malmborg
 *
 */
public class StockServiceImpl implements StockService
{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(StockServiceImpl.class);
    private final StockRepository stockRepository;
    private final Producer producer;
    private final ProducerTemplate producerTemplate;
    private boolean isDone = false;
    public static int consumerCalls = 0;
    
    
    public StockServiceImpl(StockRepository stockRepository, Producer producer, ProducerTemplate producerTemplate)
    {
        this.stockRepository = stockRepository;
        this.producer = producer;
        this.producerTemplate = producerTemplate;
    }

    @Transactional
    @Override
    public void updateAndProduce(Item item, boolean withinTx)
    {
    	item.setName("updateAndProduce");
        stockRepository.save(item);
        if (withinTx)
        {
	        producer.sendEvent(item.getId());
	        try {
	        	// Make sure to wait, to enable consumer to commit first
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				LOGGER.error("Interupted...", e);
			}
        }
        else
        {
	        if (TransactionSynchronizationManager.isActualTransactionActive())
	        {
	            TransactionSynchronizationManager.registerSynchronization(new PostTransactionEventPublisher(producer, item.getId()));
	        }
	        else
	        {
	            throw new RuntimeException("Transaction is expected to be active at this point");
	        }
        }
    }
    
    @Transactional
    @Override
    public void consumeAndUpdate(Long id)
    {
        LOGGER.info("Got id {}", id);
        Item item = stockRepository.findOne(id);
        LOGGER.info("Read {}", item.toString());
        item.setName("consumeAndUpdate");
        stockRepository.save(item);
        isDone = true;
        consumerCalls++;
    }

    @Override
    public void consume(Long id)
    {
        LOGGER.info("Got id {}", id);
        Item item = stockRepository.findOne(id);
        LOGGER.info("Read {}", item.toString());
        isDone = true;
        consumerCalls++;
    }

    @Override
    public boolean isDone()
    {
        return isDone;
    }

    @Override
    public void resetDone()
    {
        isDone = false;
    }

    public static void resetConsumerCalls()
    {
        consumerCalls = 0;
    }

    @Transactional(rollbackFor=RuntimeException.class)
    @Override
    public void sendToJms(String endPoint, Item item, boolean commit)
    {
        producerTemplate.sendBody(endPoint, item.getId());
        if (!commit)
        {
            throw new RuntimeException("Will rollback");
        }
    }
    
    
}
