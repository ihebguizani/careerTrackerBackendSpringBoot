package CareerTracker.serviceInterfaces;

import CareerTracker.models.User;
import CareerTracker.models.UserDto;

import java.util.List;

public interface IUserServices {
    UserDto addUser(UserDto userDto);
    void deleteUser(Long userId);
    List<User> getAllUsers();
    User getUserById(Long id);
    User getUsersByUsername(String username);

}
