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
    void consumeAndUpdate(Long id);
    boolean isDone();

}