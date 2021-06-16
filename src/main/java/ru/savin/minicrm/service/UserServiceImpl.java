package ru.savin.minicrm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.savin.minicrm.dao.RoleRepository;
import ru.savin.minicrm.dao.UserRepository;
import ru.savin.minicrm.dto.FormUser;
import ru.savin.minicrm.entity.User;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsUserByEmail(email);
    }

    @Override
    public boolean existsByUserName(String userName) {
        return userRepository.existsUserByUserName(userName);
    }

    @Override
    public User save(FormUser formUser) {
        User user = new User();

        setUserParams(formUser, user);

        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return userRepository.findByUserName(userName)
                .map(User::toSpringUser)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username or password"));
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
