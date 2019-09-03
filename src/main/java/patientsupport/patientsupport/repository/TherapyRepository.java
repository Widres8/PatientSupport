package patientsupport.patientsupport.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import patientsupport.patientsupport.models.parameters.Therapy;

/**
 * TherapyRepository
 */
@Repository
public interface TherapyRepository extends CrudRepository<Therapy,Integer> {

    
    
}