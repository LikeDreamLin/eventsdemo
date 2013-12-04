package events.camel.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import events.camel.entities.Item;

/**
 * Demo JPA repository.
 * @author Anders Malmborg
 *
 */
public interface StockRepository extends CrudRepository<Item, Long> {
	List<Item> findByNumber(String number);
}
