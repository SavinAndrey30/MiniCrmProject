package ru.savin.minicrm.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.savin.minicrm.entity.Role;


public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findRoleByName(String roleName);
}
