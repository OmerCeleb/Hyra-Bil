package com.saferent1.repository;

import com.saferent1.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // optional
public interface UserRepository extends JpaRepository<User, Long> {


    Boolean existsByEmail(String email);


    @EntityGraph(attributePaths = "roles") // Jag ersatte LAZY med EAGER
    Optional<User> findByEmail(String email);
    //user.getEmail();




}
