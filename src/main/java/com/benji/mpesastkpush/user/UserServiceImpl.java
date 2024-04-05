package com.benji.mpesastkpush.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements  UserService{
    private final UserRepository userRepository;
    @Override
    public List<User> getAllUsers(int pageNo, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC,"createdOn");
        PageRequest pageRequest = PageRequest.of(pageNo, size,sort);
        return userRepository.findAll(pageRequest).get().toList();
    }

    @Override
    public Optional<User> getUserByEmailId(String emailAddress) {
        return userRepository.findByEmail(emailAddress);
    }

    @Override
    public Optional<User> findByContact(String contact) {
        return userRepository.findByContact(contact);    }
}
