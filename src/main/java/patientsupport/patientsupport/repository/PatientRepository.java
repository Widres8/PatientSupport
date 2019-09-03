package patientsupport.patientsupport.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import patientsupport.patientsupport.models.accounts.Patient;

/**
 * PatientRepository
 */
@Repository
public interface PatientRepository extends CrudRepository<Patient,Integer> {

    
    
}