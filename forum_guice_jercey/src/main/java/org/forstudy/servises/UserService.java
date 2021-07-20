package org.forstudy.servises;

import com.google.inject.ImplementedBy;
import org.forstudy.servises.impl.UserServiceImpl;

@ImplementedBy(UserServiceImpl.class)
public interface UserService {

	String save(String login, String password);
	
	String listOfAllUsers();

    String findUserById(Long userId);

	String updateUserById(String userId, String login, String password);

	void deleteUserById(String userId);
}
