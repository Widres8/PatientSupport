package patientsupport.patientsupport.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import patientsupport.patientsupport.models.parameters.StatusReason;

/**
 * StatusReasonRepository
 */
@Repository
public interface StatusReasonRepository extends CrudRepository<StatusReason,Integer> {

    List<StatusReason> findByStatusId(@Param("status_id") Integer status_id);
    
}