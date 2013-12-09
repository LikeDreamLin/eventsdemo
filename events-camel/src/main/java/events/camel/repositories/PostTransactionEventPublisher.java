package events.camel.repositories;

import org.springframework.transaction.support.TransactionSynchronizationAdapter;

import events.camel.Producer;

/**
 * Sends an event after committed transaction.
 * @author Anders Malmborg
 *
 */
public class PostTransactionEventPublisher extends TransactionSynchronizationAdapter
{
    public final Producer producer;
    public final Object event;
    
    public PostTransactionEventPublisher(Producer producer, Object event)
    {
        this.producer = producer;
        this.event = event;
    }

    @Override
    public void afterCommit()
    {
        producer.sendEvent(event);
    }

}
