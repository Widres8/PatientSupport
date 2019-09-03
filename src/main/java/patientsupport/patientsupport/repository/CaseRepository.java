package patientsupport.patientsupport.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import patientsupport.patientsupport.models.cases.Case;

/**
 * CaseRepository
 */
@Repository
public interface CaseRepository extends CrudRepository<Case, Integer> {

    
}