package com.benji.payments_services.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(@RequestParam int pageNo, @RequestParam int size){
       return ResponseEntity.ok(userService.getAllUsers(pageNo,size));
    }

    @GetMapping("/{contact}")
    public ResponseEntity<User> findByContact(@PathVariable String contact) {
        return userService.findByContact(contact)
                 .map(ResponseEntity::ok)
                 .orElseGet(() -> ResponseEntity.noContent().build());
    }
}
