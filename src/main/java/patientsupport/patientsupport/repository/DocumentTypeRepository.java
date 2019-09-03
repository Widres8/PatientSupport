package patientsupport.patientsupport.repository;

import java.util.List;

// import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import patientsupport.patientsupport.models.parameters.DocumentType;

@Repository
public interface DocumentTypeRepository extends CrudRepository<DocumentType, Integer> {
    List<DocumentType> findByAlias(@Param("alias") Integer alias);
}