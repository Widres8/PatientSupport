package patientsupport.patientsupport.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import patientsupport.patientsupport.models.approvals.Approval;


/**
 * ApprovalRepository
 */
@Repository
public interface ApprovalRepository extends CrudRepository<Approval,Integer> {

    
}