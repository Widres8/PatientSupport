package patientsupport.patientsupport.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import patientsupport.patientsupport.models.parameters.Status;

/**
 * StatusRepository
 */
@Repository
public interface StatusRepository extends CrudRepository<Status,Integer> {

    
    
}