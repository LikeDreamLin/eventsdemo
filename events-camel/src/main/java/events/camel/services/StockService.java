package events.camel.services;

import events.camel.entities.Item;

/**
 * To enable @Transaactional in the implementation without CGLIB.
 * @author Anders Malmborg
 *
 */
public interface StockService
{

	/**
	 * Update the record on the db.
	 * @param item item to update
	 * @param withinTx true to fail due to optimistic locking
	 */
    void updateAndProduce(Item item, boolean withinTx);
    
    /**
     * Receives an event and updates (save to db) the name of the item.
     * @param id primary key of Item to update
     */
    void consumeAndUpdate(Long id);
    
    /**
     * @return true if consumeAndUpdate is performed
     */
    boolean isDone();
    
    /**
     * (Re)set the the done attribute. 
     * @param isDone true/false
     */
    void setDone(boolean isDone);

    
    /**
     * Sends an async message over Jms.
     * @param item Item to send
     * @param commit true to commit, false rollback
     */
    void sendToJms(Item item, boolean commit);


}