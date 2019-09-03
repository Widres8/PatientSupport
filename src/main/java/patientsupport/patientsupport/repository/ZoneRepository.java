package patientsupport.patientsupport.repository;

import java.util.List;

// import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import patientsupport.patientsupport.models.parameters.Zone;

@Repository
public interface ZoneRepository extends CrudRepository<Zone, Integer> {

    //@Query("select z from Zone z where z.countries.id = :country_id")
    List<Zone> findByCountryId(@Param("country_id") Integer country_id);
}