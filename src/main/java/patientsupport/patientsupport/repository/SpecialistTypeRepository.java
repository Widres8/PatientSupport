package patientsupport.patientsupport.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import patientsupport.patientsupport.models.parameters.SpecialistType;

/**
 * SpecialistTypeRepository
 */
@Repository
public interface SpecialistTypeRepository extends CrudRepository<SpecialistType,Integer> {
    
}