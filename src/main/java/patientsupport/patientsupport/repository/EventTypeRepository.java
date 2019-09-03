package patientsupport.patientsupport.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import patientsupport.patientsupport.models.events.EventType;

/**
 * EventTypeRepository
 */
@Repository
public interface EventTypeRepository extends CrudRepository<EventType,Integer> {

    
    
}