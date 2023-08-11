package ru.practicum.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.security.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {



}