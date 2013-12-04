package events.camel.entities;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Test entity for JPA, TX and event sync demo.
 * @author Anders Malmborg
 *
 */
@Entity
public class Item {
    @GeneratedValue
    @Id
    private Long id;
	private StockItemType itemType;
	private String name;
	private String number;
	private Double price;
	private Date updated;
	
	Item()
	{
		this(null, null, null, null);
	}
	public Item(StockItemType itemType, String name, String number, Double price) {
		this.itemType = itemType;
		this.name = name;
		this.number = number;
		this.price = price;
		this.updated = Calendar.getInstance().getTime();
	}
	public Long getId()
    {
        return id;
    }
    public String getName() {
		return name;
	}
	public String getNumber() {
		return number;
	}
	public Double getPrice() {
		return price;
	}
	public Date getUpdated() {
		return updated;
	}
	@Override
	public String toString() {
		return "itemType:"+itemType + ", name:" + name+",number:"+number;
	}
	
}
