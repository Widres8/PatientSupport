package patientsupport.patientsupport.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import patientsupport.patientsupport.models.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    User findByEmail(String email);

}