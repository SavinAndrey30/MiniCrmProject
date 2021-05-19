package ru.savin.minicrm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.savin.minicrm.dao.RoleRepository;
import ru.savin.minicrm.dao.UserRepository;
import ru.savin.minicrm.dto.FormUser;
import ru.savin.minicrm.entity.Role;
import ru.savin.minicrm.entity.User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public Optional<User> findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User save(FormUser formUser) {
        User user = new User();

        setUserParams(formUser, user);

        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username or password"));

        return user.toSpringUser();
    }



    public void setUserParams(FormUser formUser, User user) {
        user.setUserName(formUser.getUserName());
        user.setPassword(passwordEncoder.encode(formUser.getPassword()));
        user.setFirstName(formUser.getFirstName());
        user.setLastName(formUser.getLastName());
        user.setEmail(formUser.getEmail());

        // give user default role of "employee"
        user.setRoles(Collections.singletonList(roleRepository.findRoleByName("ROLE_EMPLOYEE")));
    }
}
