package com.example.onlinequiz.Repo;

import com.example.onlinequiz.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);

    Users getByEmail(String email);

    Users getById(Long id);

    Long countAllByCreateDateBetween(Date from, Date to);


    List<Users> findAllByRole(String role);
}
