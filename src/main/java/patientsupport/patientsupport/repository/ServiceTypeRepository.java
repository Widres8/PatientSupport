package patientsupport.patientsupport.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import patientsupport.patientsupport.models.parameters.ServiceType;

/**
 * ServiceTypeRepository
 */
@Repository
public interface ServiceTypeRepository extends CrudRepository<ServiceType,Integer> {

    
    
}