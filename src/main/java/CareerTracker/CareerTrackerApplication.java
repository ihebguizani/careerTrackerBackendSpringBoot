package CareerTracker;

import CareerTracker.models.Role;
import CareerTracker.repositories.IRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class CareerTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CareerTrackerApplication.class, args);
		System.out.println("✅ Application Career Tracker démarrée !");
	}

	@Autowired
	private IRoleRepository roleRepository;

	@PostConstruct
	public void addRoles(){
		if(roleRepository.findAll().size()<5){
			List<Role> roles=new ArrayList<>();
			Role r1=roleRepository.save(new Role(1,"MANAGER"));
			Role r2=roleRepository.save(new Role(2,"ADMIN"));
			Role r3=roleRepository.save(new Role(3,"EMPLOYEE"));
			roles.add(r1);
			roles.add(r2);
			roles.add(r3);
			roleRepository.saveAll(roles);
		}
	}
}
