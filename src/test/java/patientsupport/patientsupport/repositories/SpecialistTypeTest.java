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

import patientsupport.patientsupport.models.parameters.SpecialistType;
import patientsupport.patientsupport.repository.SpecialistTypeRepository;

@RunWith(SpringRunner.class)
@TestPropertySource(locations="classpath:application-test.properties")
@SpringBootTest
public class SpecialistTypeTest {

    @Autowired
    public SpecialistTypeRepository repository;

    public SpecialistTypeTest() {
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
        SpecialistType itemToCreate = new SpecialistType();
        itemToCreate.setDescription("Cardiología");
        
        // When
        SpecialistType itemSaved = repository.save(itemToCreate);

        // Validate
        assertEquals(itemToCreate, itemSaved);
    }

    @Test
    @Order(3)
    public void testUpdate() {
        SpecialistType itemToUpdate = repository.findById(1).get();
        itemToUpdate.setDescription("Administrativo");
        
        // When
        SpecialistType itemSaved = repository.save(itemToUpdate);

        // Validate
        assertNotEquals(itemToUpdate, itemSaved);
    }
    
}