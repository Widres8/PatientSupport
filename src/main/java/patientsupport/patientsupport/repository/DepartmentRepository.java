package patientsupport.patientsupport.repository;

import java.util.List;

// import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import patientsupport.patientsupport.models.parameters.Department;

@Repository
public interface DepartmentRepository extends CrudRepository<Department, Integer> {

    //@Query("select z from Zone z where z.countries.id = :country_id")
    List<Department> findByZoneId(@Param("zone_id") Integer zone_id);
}