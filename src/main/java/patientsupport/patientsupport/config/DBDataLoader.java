package patientsupport.patientsupport.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import patientsupport.patientsupport.models.Role;
import patientsupport.patientsupport.models.User;
import patientsupport.patientsupport.models.parameters.Country;
import patientsupport.patientsupport.models.parameters.Zone;
import patientsupport.patientsupport.repository.CountryRepository;
import patientsupport.patientsupport.repository.RoleRepository;
import patientsupport.patientsupport.repository.UserRepository;
import patientsupport.patientsupport.repository.ZoneRepository;

@Component
public class DBDataLoader implements ApplicationRunner {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private CountryRepository countryRepository;
    private ZoneRepository zoneRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public DBDataLoader(
        UserRepository userRepository,
        RoleRepository roleRepository,
        CountryRepository countryRepository,
        ZoneRepository zoneRepository,
        BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.countryRepository = countryRepository;
        this.zoneRepository = zoneRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void run(ApplicationArguments args) {

        if(userRepository.count() == 0) {
            createUsers();
        }
        if(countryRepository.count() == 0) {
            createCountries();
        }
        if(zoneRepository.count() == 0) {
            createZones();
        }
    }

    

    private void createCountries() {
        Country c1 = new Country();
        c1.setDescription("Colombia");
        c1.setCreatedBy("admin@admin.com");
        c1.setCreatedAt(new Date());
        Country c2 = new Country();
        c2.setDescription("Peru");
        c2.setCreatedBy("admin@admin.com");
        c2.setCreatedAt(new Date());
        Country c3 = new Country();
        c3.setDescription("Ecuador");
        c3.setCreatedBy("admin@admin.com");
        c3.setCreatedAt(new Date());
        Country c4 = new Country();
        c4.setDescription("Chile");
        c4.setCreatedBy("admin@admin.com");
        c4.setCreatedAt(new Date());
        Country c5 = new Country();
        c5.setDescription("Argentina");
        c5.setCreatedBy("admin@admin.com");
        c5.setCreatedAt(new Date());
        List<Country> countries = new ArrayList<>();
        countries.add(c1);
        countries.add(c2);
        countries.add(c3);
        countries.add(c4);
        countries.add(c5);

        countryRepository.saveAll(countries);
    }

    private void createZones() {
        Zone z1 = new Zone();
        z1.setDescription("Centro");
        z1.setCountryId(1);
        z1.setActive(true);
        z1.setCreatedBy("admin@admin.com");
        z1.setCreatedAt(new Date());
        Zone z2 = new Zone();
        z2.setDescription("Norte");
        z2.setCountryId(1);
        z2.setActive(true);
        z2.setCreatedBy("admin@admin.com");
        z2.setCreatedAt(new Date());
        Zone z3 = new Zone();
        z3.setDescription("Oriente");
        z3.setCountryId(1);
        z3.setActive(true);
        z3.setCreatedBy("admin@admin.com");
        z3.setCreatedAt(new Date());
        Zone z4 = new Zone();
        z4.setDescription("Occidente");
        z4.setCountryId(1);
        z4.setActive(true);
        z4.setCreatedBy("admin@admin.com");
        z4.setCreatedAt(new Date());
        Zone z5 = new Zone();
        z5.setDescription("Norte");
        z5.setCountryId(2);
        z5.setActive(true);
        z5.setCreatedBy("admin@admin.com");
        z5.setCreatedAt(new Date());
        List<Zone> zones = new ArrayList<>();
        zones.add(z1);
        zones.add(z2);
        zones.add(z3);
        zones.add(z4);
        zones.add(z5);

        zoneRepository.saveAll(zones);
    }
    private void createUsers() {

        User user = new User();
        user.setFirstName("Admin");
        user.setLastName("Admin");
        user.setEmail("admin@admin.com");
        user.setPhone("123456");
        user.setPassword("123456");
        user.setEnabled(true);
        user.setCreatedBy("admin@admin.com");
        user.setCreatedAt(new Date());

        saveUser(user, "ADMIN");

        User userE = new User();
        userE.setFirstName("Enfermero");
        userE.setLastName("Enfermero");
        userE.setEmail("enfermero@enfermero.com");
        userE.setPhone("123456");
        userE.setPassword("123456");
        userE.setEnabled(true);
        userE.setCreatedBy("admin@admin.com");
        userE.setCreatedAt(new Date());

        saveUser(userE, "ENFERMERO");
    }

    public void saveUser(User user, String role) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        Role userRole = getRole(role);
        
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));

        userRepository.save(user);
    }

    private Role getRole(String role) {
        Role roleExits = roleRepository.findByDescription(role);

        if (roleExits != null) {
            return roleExits;
        } else {
            Role roleCreated = new Role();
            roleCreated.setActive(true);
            roleCreated.setCreatedBy("Admin");
            roleCreated.setDescription(role);
            roleRepository.save(roleCreated);

            return roleRepository.findByDescription(role);
        }

    }
}