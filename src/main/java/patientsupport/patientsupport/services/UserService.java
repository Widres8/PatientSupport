package patientsupport.patientsupport.services;

import java.util.Arrays;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import patientsupport.patientsupport.models.Role;
import patientsupport.patientsupport.models.User;
import patientsupport.patientsupport.repository.RoleRepository;
import patientsupport.patientsupport.repository.UserRepository;

@Service
public class UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void saveUser(User user, String role) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        // Role userRole = roleRepository.findByDescription(role);
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

    public User getAuthUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return findUserByEmail(auth.getName());
    }
    
}