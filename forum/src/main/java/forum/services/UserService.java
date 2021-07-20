package forum.services;

import forum.entities.User;
import forum.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }

    @Transactional()
    public List<User> findUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User save(User user) {
        String login = user.getLogin();
        User newUserCreator = null;
        if (!checkLogin(login)) {
            newUserCreator = new User();
            newUserCreator.setLogin(login);
            newUserCreator.setPassword(user.getPassword());
            newUserCreator.setRegistrationDate(LocalDateTime.now());
            userRepository.save(newUserCreator);
        }
        return newUserCreator;
    }

    @Transactional
    public User findUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Transactional
    public User updateUserById(Long userId, User updatedUser) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setPassword(updatedUser.getPassword());
            user.setLogin(updatedUser.getLogin());
        }
        return user;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public boolean checkLogin(String login) {
        boolean result = false;
        List<User> allUsers = userRepository.findAll();
        for (User user : allUsers) {
            if (user.getLogin().equals(login)) {
                result = true;
                break;
            }
        }
        return result;
    }

    public User findUserByLoginAndPassword(String login, String password) {
        User registeredUser = null;
        List<User> allUsers = userRepository.findAll();
        for (User user : allUsers) {
            if (user.getLogin().equals(login)) {
                if (user.getPassword().equals(password)) {
                    registeredUser = user;
                }
                break;
            }
        }
        return registeredUser;
    }
}