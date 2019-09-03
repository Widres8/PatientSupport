package patientsupport.patientsupport.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import patientsupport.patientsupport.models.parameters.InsuranceType;

/**
 * InsuranceTypeRepository
 */
@Repository
public interface InsuranceTypeRepository extends CrudRepository<InsuranceType,Integer> {

    
    
}