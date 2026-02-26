package site.shazan.ecommerce.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.shazan.ecommerce.user.dtos.UserRequest;
import site.shazan.ecommerce.user.dtos.UserResponse;
import site.shazan.ecommerce.user.service.UserService;


import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return new ResponseEntity<>(userService.fetchAllUsers(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createUsers(@RequestBody UserRequest userRequest) {
        userService.addUser(userRequest);
        return ResponseEntity.ok("Added Successfully");
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable String id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable String id, @RequestBody UserRequest user) {
        Boolean isUpdated = userService.updateUser(id, user);
        if (isUpdated) {
            return ResponseEntity.ok("Updated Successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
