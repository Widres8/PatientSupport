package patientsupport.patientsupport.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import patientsupport.patientsupport.models.parameters.LabType;

/**
 * LabTypeRepository
 */
@Repository
public interface LabTypeRepository extends CrudRepository<LabType,Integer> {

    
    
}