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
     * Receives an event and reads the item from db.
     * @param id primary key of Item to update
     */
    void consume(Long id);

    /**
     * @return true if consumeAndUpdate is performed
     */
    boolean isDone();
    
    /**
     * Reset the the done attribute. 
     */
    void resetDone();

    
    /**
     * Sends an async message over Jms.
     * @param endPoint to send to, e.g. "jms:queue:someQueue"
     * @param item Item to send
     * @param commit true to commit, false rollback
     */
    void sendToJms(String endPoint, Item item, boolean commit);
    
    /**
     * Sends messages to all registered consumers awaiting a voting on the event, e.g. can the item be deleted.
     * @param endPoint to send to, e.g. "seda:deleteVotersAccept?multipleConsumers=true"
     * @param item Item
     */
    void canItemBeDeleted(String endPoint, Item item);
    
    /**
     * Sets the ok to delete flag, called from DeleteVoterDecission.
     */
    void setOkToDeleteItem(Long id, boolean ok);
    
    /**
     * Gets the result of {@link #canItemBeDeleted(String, Item)}
     * @param id
     * @return
     */
    boolean isOkToDeleteItem(Long id);

}