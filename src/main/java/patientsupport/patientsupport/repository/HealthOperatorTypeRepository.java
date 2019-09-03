package patientsupport.patientsupport.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import patientsupport.patientsupport.models.parameters.HealthOperatorType;

/**
 * HealthOperatorTypeRepository
 */
@Repository
public interface HealthOperatorTypeRepository extends CrudRepository<HealthOperatorType,Integer> {

    
    
}