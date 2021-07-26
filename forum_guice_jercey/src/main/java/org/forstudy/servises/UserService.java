package org.forstudy.servises;

import com.google.inject.ImplementedBy;
import org.forstudy.entities.User;
import org.forstudy.exceptionhandling.AppException;
import org.forstudy.exceptionhandling.ValidationException;
import org.forstudy.servises.impl.UserServiceImpl;

import java.util.List;

@ImplementedBy(UserServiceImpl.class)
public interface UserService {

    User save(User user, String link) throws AppException;

    List<User> listOfAllUsers();

    User findUserById(String userId, String link) throws AppException;

    User updateUserById(String userId, User user, String lick) throws AppException;

    void deleteUserById(String userId, String link) throws AppException;
}
