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

import patientsupport.patientsupport.models.parameters.LabType;
import patientsupport.patientsupport.repository.LabTypeRepository;

@RunWith(SpringRunner.class)
@TestPropertySource(locations="classpath:application-test.properties")
@SpringBootTest
public class LabTypeTest {

    @Autowired
    public LabTypeRepository repository;

    public LabTypeTest() {
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
        LabType itemToCreate = new LabType();
        itemToCreate.setActive(true);
        itemToCreate.setDescription("Perfil Lipídico");
        
        // When
        LabType itemSaved = repository.save(itemToCreate);

        // Validate
        assertEquals(itemToCreate, itemSaved);
    }

    @Test
    @Order(3)
    public void testUpdate() {
        LabType itemToUpdate = repository.findById(1).get();
        itemToUpdate.setActive(false);
        itemToUpdate.setDescription("Calcio");
        
        // When
        LabType itemSaved = repository.save(itemToUpdate);

        // Validate
        assertNotEquals(itemToUpdate, itemSaved);
    }
    
}