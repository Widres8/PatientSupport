package patientsupport.patientsupport.repository;

import java.util.List;

// import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import patientsupport.patientsupport.models.parameters.City;

@Repository
public interface CityRepository extends CrudRepository<City, Integer> {

    //@Query("select z from City z where z.countries.id = :country_id")
    List<City> findByDepartmentId(@Param("department_id") Integer department_id);
}