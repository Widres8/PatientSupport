package patientsupport.patientsupport.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import patientsupport.patientsupport.models.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {

    Role findByDescription(String description);
}