package events.camel.services;

import events.common.Event;

/**
 * To enable @Transaactional in the implementation without CGLIB.
 * @author Anders Malmborg
 *
 */
public interface StockService
{

    void saveAndProduce();
    void consumeAndRead(Event event);
    boolean isDone();

}