package patientsupport.patientsupport.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import patientsupport.patientsupport.models.accounts.Physician;

/**
 * PhysicianRepository
 */
@Repository
public interface PhysicianRepository extends CrudRepository<Physician, Integer>{

    
}
