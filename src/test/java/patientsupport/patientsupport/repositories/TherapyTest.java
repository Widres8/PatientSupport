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

import patientsupport.patientsupport.models.parameters.Therapy;
import patientsupport.patientsupport.repository.TherapyRepository;

@RunWith(SpringRunner.class)
@TestPropertySource(locations="classpath:application-test.properties")
@SpringBootTest
public class TherapyTest {

    @Autowired
    public TherapyRepository repository;

    public TherapyTest() {
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
        Therapy itemToCreate = new Therapy();
        itemToCreate.setActive(true);
        itemToCreate.setCore(true);
        itemToCreate.setDescription("Leucemia Linfoblastica Aguda Ph-");
        itemToCreate.setIndication("EN COMBINACIÓN CON LENALIDOMIDA Y DEXAMETASONA, O CON DEXAMETASONA SOLA, ESTÁ INDICADO PARA EL TRATAMIENTO DE PACIENTES CON MIELOMA MÚLTIPLE EN RECAÍDA O REFRACTARIO QUE HAN RECIBIDO UNA A TRES LÍNEAS DE TRATAMIENTO");
        itemToCreate.setProduct("Kyprolis");
        
        // When
        Therapy itemSaved = repository.save(itemToCreate);

        // Validate
        assertEquals(itemToCreate, itemSaved);
    }

    @Test
    @Order(3)
    public void testUpdate() {
        Therapy itemToUpdate = repository.findById(1).get();
        itemToUpdate.setActive(false);
        itemToUpdate.setCore(false);
        itemToUpdate.setIndication("LEUCEMIA LINFOBLASTICA AGUDA CROMOSOMA PHILADELPHIA NEGATIVO");
        itemToUpdate.setProduct("Blincyto");
        
        // When
        Therapy itemSaved = repository.save(itemToUpdate);

        // Validate
        assertNotEquals(itemToUpdate, itemSaved);
    }
    
}