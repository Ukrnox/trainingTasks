package forum.controllers;

import forum.entities.User;
import forum.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
    public @ResponseBody
    List<User> getAllUsers() {
        return userService.findUsers();
    }

    @GetMapping("{userId}")
    public @ResponseBody
    User getUserById(@PathVariable("userId") Long userId) {
        return userService.findUserById(userId);
    }

    @PostMapping
    public @ResponseBody
    User createNewUser(@RequestBody User newUser) {
        String login = newUser.getLogin();
        User newUserCreator = null;
        if (!userService.checkLogin(login)) {
            newUserCreator = new User();
            newUserCreator.setLogin(login);
            newUserCreator.setPassword(newUser.getPassword());
            newUserCreator.setRegistrationDate(LocalDateTime.now());
            userService.save(newUserCreator);
        }
        return newUserCreator;
    }

    @PutMapping("{userId}")
    public @ResponseBody
    User updateUserById(@PathVariable("userId") Long userId, @RequestBody User updatedUser) {
        User updatedUserForResponse = null;
        if (userService.findUserById(userId) != null) {
            userService.changeUserLogin(updatedUser.getLogin(), userId);
            userService.changeUserPassword(updatedUser.getPassword(), userId);
            updatedUserForResponse = userService.findUserById(userId);
        }
        return updatedUserForResponse;
    }

    @DeleteMapping("{userId}")
    public void deleteUser(@PathVariable("userId") Long userId) {
        if (userService.findUserById(userId) != null) {
            userService.deleteUserById(userId);
        }
    }
}
