package com.benji.payments_services.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository  extends JpaRepository <User,Integer>{

    Optional<User> findByContact (String contact);
    Optional<User> findByEmail (String email);
}
