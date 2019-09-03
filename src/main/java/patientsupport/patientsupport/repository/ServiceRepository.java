package patientsupport.patientsupport.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import patientsupport.patientsupport.models.cases.Service;

/**
 * ServiceRepository
 */
@Repository
public interface ServiceRepository extends CrudRepository<Service, Integer>{

    
}