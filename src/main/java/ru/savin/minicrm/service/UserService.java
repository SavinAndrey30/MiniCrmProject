package ru.savin.minicrm.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.savin.minicrm.dto.FormUser;
import ru.savin.minicrm.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

    Optional<User> findByUserName(String userName);

    User save(FormUser formUser);

    List<User> findAll();

    void delete(Long id);

    boolean existsByEmail(String email);

    boolean existsByUserName(String userName);
}
