package events.camel.repositories;

import org.springframework.transaction.support.TransactionSynchronizationAdapter;

import events.camel.Producer;
import events.common.Event;

/**
 * Sends an event after committed transaction.
 * @author Anders Malmborg
 *
 */
public class PostTransactionEventPublisher extends TransactionSynchronizationAdapter
{
    public final Producer producer;
    
    public PostTransactionEventPublisher(Producer producer)
    {
        this.producer = producer;
    }

    @Override
    public void afterCommit()
    {
        producer.sendEvent(Event.FINISH_WORKING);
    }

}
