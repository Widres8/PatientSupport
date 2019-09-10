package patientsupport.patientsupport.repositories;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import patientsupport.patientsupport.models.parameters.Status;
import patientsupport.patientsupport.repository.StatusRepository;

@RunWith(SpringRunner.class)
@TestPropertySource(locations="classpath:application-test.properties")
@SpringBootTest
public class StatusRepositoryTest {

    @Autowired
    public StatusRepository statusRepository;

    @Test
    @Order(1)
    public void testDelete() {
        statusRepository.deleteAll();
        assertEquals(0, statusRepository.count());
    }

    @Test
    @Order(2)
    public void testInsert() {
        Status itemToCreate = new Status();
        itemToCreate.setActive(true);
        itemToCreate.setDescription("Inicial");
        
        // When
        Status itemSaved = statusRepository.save(itemToCreate);

        // Validate
        assertEquals(itemToCreate, itemSaved);
    }

    @Test
    @Order(3)
    public void testUpdate() {
        Status itemToUpdate = statusRepository.findById(1).get();
        itemToUpdate.setActive(false);
        itemToUpdate.setDescription("Final");
        
        // When
        Status itemSaved = statusRepository.save(itemToUpdate);

        // Validate
        assertEquals(itemToUpdate.getDescription(), itemSaved.getDescription());
    }
    
}