package com.benji.payments_services.user;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {


    List<User> getAllUsers(int page, int size);

    Optional<User> getUserByEmailId(String emailAddress);

    Optional<User> findByContact(String contact);
}
