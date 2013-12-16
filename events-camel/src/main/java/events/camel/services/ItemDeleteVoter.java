package events.camel.services;

import events.camel.entities.Item;

/**
 * Voter for check if it is OK to delete item. Decission based on the Spring config,
 * @author Anders Malmborg
 *
 */
public class ItemDeleteVoter
{
    private final Boolean deleteOk;
    
    public ItemDeleteVoter(Boolean deleteOk)
    {
        this.deleteOk = deleteOk;
    }

    public Boolean canItemBeDeleted(Item item)
    {
        return deleteOk;
    }
}
