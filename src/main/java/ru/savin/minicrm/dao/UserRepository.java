package ru.savin.minicrm.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.savin.minicrm.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserName(String userName);

    List<User> findAll();

    boolean existsUserByEmail(String email);

    boolean existsUserByUserName(String userName);

}
