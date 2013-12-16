package events.camel.services;

import org.apache.camel.Message;

public class DeleteVoterDecission
{
    private final StockService stockService;
    
    public DeleteVoterDecission(StockService stockService)
    {
        this.stockService = stockService;
    }

    public void deleteOrNot(Message message)
    {
        stockService.setOkToDeleteItem(message.getHeader("itemId", Long.class), message.getBody(Boolean.class));
    }
}
