package patientsupport.patientsupport.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import patientsupport.patientsupport.models.events.Event;

/**
 * EventRepository
 */
@Repository
public interface EventRepository extends CrudRepository<Event, Integer> {

    
}