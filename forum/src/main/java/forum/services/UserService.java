package forum.services;

import forum.entities.User;
import forum.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
//        User user1 = new User();
//        user1.setLogin("Ron");
//        user1.setPassword("1111");
//        user1.setRegistrationDate(LocalDateTime.now());
//
//        User user2 = new User();
//        user2.setLogin("Lily");
//        user2.setPassword("1111");
//        user2.setRegistrationDate(LocalDateTime.now());
//
//        User user3 = new User();
//        user3.setLogin("Cookie");
//        user3.setPassword("1111");
//        user3.setRegistrationDate(LocalDateTime.now());
//
//        userRepository.save(user1);
//        userRepository.save(user2);
//        userRepository.save(user3);
//
//        Group newGroup1 = new Group();
//        newGroup1.setName("Politics");
//        Group newGroup2 = new Group();
//        newGroup2.setName("Weather");
//        Group newGroup3 = new Group();
//        newGroup3.setName("Sport");
//
//        groupRepository.save(newGroup1);
//        groupRepository.save(newGroup2);
//        groupRepository.save(newGroup3);

        return userRepository.findAll();
    }

    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public User findUserById(Long userId) {
        return userRepository.findUserByUserId(userId);
    }

    @Transactional
    public void changeUserLogin(String newLogin, Long userId) {
        userRepository.changeUserLogin(newLogin, userId);
    }

    @Transactional
    public void changeUserPassword(String newPassword, Long userId) {
        userRepository.changeUserPassword(newPassword, userId);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public boolean checkLogin(String login) {
        boolean result = false;
        for (User user : userRepository.findAll()) {
            if (user.getLogin().equals(login)) {
                result = true;
                break;
            }
        }
        return result;
    }

    public User checkPerson(String login, String password) {
        User registeredUser = null;
        for (User user : userRepository.findAll()) {
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