package CareerTracker.servicesImpl;

import CareerTracker.models.*;
import CareerTracker.repositories.*;
import CareerTracker.serviceInterfaces.IUserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class UserServiceImpl implements IUserServices {

    @Autowired
    IUserRepository userRepository;
    @Autowired
    IRoleRepository IRoleRepository;
    @Autowired
    private BCryptPasswordEncoder encoder;



    @Override
    public UserDto addUser(UserDto userDto) {
        User user=new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(encoder.encode(userDto.getPassword()));
        user.setLastname(userDto.getLastname());
        user.setFirstname(userDto.getFirstname());
        user.setEmail(userDto.getEmail());
        user.setEnabled(true);
        switch(userDto.getRole()) {
            case "RH":
                user.setRole(IRoleRepository.findByRoleName("RH").get(0));
                break;
            case "MANAGER":
                user.setRole(IRoleRepository.findByRoleName("MANAGER").get(0));
                break;
            case "EMPLOYEE":
                user.setRole(IRoleRepository.findByRoleName("EMPLOYEE").get(0));
                break;
        }
        userRepository.save(user);


        UserDto userDto1=new UserDto(userDto.getFirstname(),userDto.getLastname(),userDto.getEmail(),
                                     userDto.getRole(),userDto.getUsername(), userDto.getPassword());
        return userDto1;
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).get();
    }


    @Override
    public User getUsersByUsername(String username) {
        return userRepository.findByUsername(username).get(0);
    }









}
