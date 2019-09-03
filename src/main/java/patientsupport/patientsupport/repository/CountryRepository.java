package patientsupport.patientsupport.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import patientsupport.patientsupport.models.parameters.Country;

@Repository
public interface CountryRepository extends CrudRepository<Country, Integer> {

    
}