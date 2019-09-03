package patientsupport.patientsupport.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import patientsupport.patientsupport.models.parameters.StatusReason;

/**
 * StatusReasonRepository
 */
@Repository
public interface StatusReasonRepository extends CrudRepository<StatusReason,Integer> {

    
    
}