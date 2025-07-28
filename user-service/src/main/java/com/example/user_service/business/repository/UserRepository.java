package com.example.user_service.business.repository;


import com.example.user_service.business.repository.model.UserDAO;
import com.example.user_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<UserDAO, Long> {

    Optional<UserDAO> findByEmail(String email);

    boolean existsByEmail(String email);


}
