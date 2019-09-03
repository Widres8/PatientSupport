package patientsupport.patientsupport.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import patientsupport.patientsupport.models.accounts.HealthOperatorAccount;

/**
 * HealtOperatorAccountRepository
 */
@Repository
public interface HealtOperatorAccountRepository extends CrudRepository<HealthOperatorAccount,Integer> {

    
}