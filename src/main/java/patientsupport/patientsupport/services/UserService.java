package patientsupport.patientsupport.services;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import patientsupport.patientsupport.helpers.mail.EmailServiceImpl;
import patientsupport.patientsupport.helpers.mail.Mail;
import patientsupport.patientsupport.models.Role;
import patientsupport.patientsupport.models.User;
import patientsupport.patientsupport.repository.RoleRepository;
import patientsupport.patientsupport.repository.UserRepository;

@Service
public class UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private EmailServiceImpl emailServiceImpl;

    @Autowired
    public UserService(
        UserRepository userRepository,
        RoleRepository roleRepository,
        EmailServiceImpl emailServiceImpl,
        BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.emailServiceImpl = emailServiceImpl;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public Iterable<User> All() {
		return userRepository.findAll();
	}

    public boolean deleteUser(int id) {
        try {
            User itemToDelete = findById(id);
            userRepository.delete(itemToDelete);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean activeUser(int id) {
        try {
            User itemToActive = findById(id);
            itemToActive.setEnabled(!itemToActive.getEnabled());
            itemToActive.setDeleteBy(getAuthUser().getEmail());
            itemToActive.setDeleteAt(new Date());
            userRepository.save(itemToActive);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public User updatUser(User user, String role) throws DataAccessException, Exception {

        User userlast =  userRepository.findById(user.getId()).get();
        Iterable<Role> rolesuser = userlast.getRoles();
        Role roleuser = new Role();
        for (Role item : rolesuser) {
            roleuser = getRole(item.getDescription());
        }
        userlast.getRoles().remove(roleuser);
        User itemsaved = userRepository.save(userlast);
        itemsaved.setLastModifiedBy(getAuthUser().getEmail());
        itemsaved.setLastModifiedAt(new Date());
        itemsaved.setCreatedAt(user.getCreatedAt());
        itemsaved.setCreatedBy(user.getCreatedBy());
        itemsaved.setEmail(user.getEmail());
        itemsaved.setFirstName(user.getFirstName());
        itemsaved.setLastName(user.getLastName());
        Role userRole = getRole(role);
        itemsaved.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        return userRepository.save(itemsaved);
    }

    public void saveUser(User user, String role) throws DataAccessException, Exception {
        user.setCreatedAt(new Date());
        user.setCreatedBy(getAuthUser().getEmail());
        user.setEnabled(true);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        // Role userRole = roleRepository.findByDescription(role);
        Role userRole = getRole(role);
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        User usersave = userRepository.save(user);
        sendEmail(usersave);
    }

    public Iterable<Role> getRoles() {
        return roleRepository.findAll();
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

    public User getAuthUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return findUserByEmail(auth.getName());
    }

    /**
     * Method find by id model
     */
    public User findById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> 
                    new IllegalArgumentException("Invalid item Id:" + id));
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    private void sendEmail(User item) throws Exception {
        Mail mail = new Mail();

        mail.setTo(item.getEmail());
        mail.setFrom("no-reply@patientsupport.com");
        mail.setSubject("Creación Usuario");

        Map<String, Object> modelEmail = new HashMap<String, Object>();
        modelEmail.put("name", item.getFirstName());
        modelEmail.put("email", item.getEmail());
        int year = Calendar.getInstance().get(Calendar.YEAR);
        modelEmail.put("password","SoportePacientes" + year);
        mail.setModel(modelEmail);
        emailServiceImpl.sendSimpleMessage(mail, "createusertemplate");
    }
    
}