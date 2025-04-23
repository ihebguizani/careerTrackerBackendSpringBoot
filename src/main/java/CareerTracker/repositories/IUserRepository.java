package CareerTracker.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import CareerTracker.models.User;



public interface IUserRepository extends JpaRepository<User, Long> {
	
List<User> findByUsername(String username);
List<User> findByEmail(String email);
}
