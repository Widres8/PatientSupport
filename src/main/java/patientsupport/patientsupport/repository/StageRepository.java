package patientsupport.patientsupport.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import patientsupport.patientsupport.models.parameters.Stage;

/**
 * StageRepository
 */
@Repository
public interface StageRepository extends CrudRepository<Stage,Integer> {

    
    
}