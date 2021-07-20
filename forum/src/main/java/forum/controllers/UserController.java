package forum.controllers;

import forum.entities.User;
import forum.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.findUsers();
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable("userId") Long userId) {
        return userService.findUserById(userId);
    }

    @PostMapping
    public User createNewUser(@RequestBody User newUser) {
        return userService.save(newUser);
    }

    @PutMapping("/{userId}")
    public User updateUserById(@PathVariable("userId") Long userId, @RequestBody User updatedUser) {
        return userService.updateUserById(userId, updatedUser);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteUserById(userId);
    }
}
