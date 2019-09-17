package patientsupport.patientsupport.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import patientsupport.patientsupport.models.parameters.StatusReason;
import patientsupport.patientsupport.repository.StatusReasonRepository;

@RunWith(SpringRunner.class)
@TestPropertySource(locations="classpath:application-test.properties")
@SpringBootTest
@Order(2)
public class StatusReasonTest {

    @Autowired
    public StatusReasonRepository repository;

    public StatusReasonTest() {
        super();
    }

    @Test
    @Order(1)
    public void testDelete() {
        repository.deleteAll();
        assertEquals(0, repository.count());
    }

    @Test
    @Order(2)
    public void testInsert() {
        StatusReason itemToCreate = new StatusReason();
        itemToCreate.setActive(true);
        itemToCreate.setStatusId(1);
        itemToCreate.setDescription("En Aprobación");
        
        // When
        StatusReason itemSaved = repository.save(itemToCreate);

        // Validate
        assertEquals(itemToCreate, itemSaved);
    }

    @Test
    @Order(3)
    public void testUpdate() {
        StatusReason itemToUpdate = repository.findById(1).get();
        itemToUpdate.setActive(false);
        itemToUpdate.setDescription("En Tratamiento");
        
        // When
        StatusReason itemSaved = repository.save(itemToUpdate);

        // Validate
        assertNotEquals(itemToUpdate, itemSaved);
    }
    
}