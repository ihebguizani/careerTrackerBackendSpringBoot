package CareerTracker.controller;

import CareerTracker.models.*;
import CareerTracker.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import CareerTracker.securityServices.JwtUtil;
import CareerTracker.securityServices.MyUserDetails;
import CareerTracker.securityServices.UserDetailsServicesImpl;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/login")
public class LoginController {

	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private AuthenticationManager authManager;
	@Autowired
	private UserDetailsServicesImpl userDetailService;
	@Autowired
	private IUserRepository userRepo;




	@PostMapping("")
	public ResponseEntity<?> generateToken(@RequestBody AuthenticationModel authModel){
		String message="invalid username";
		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		if(!userRepo.findByUsername(authModel.getUsername()).isEmpty()){
			User user = userRepo.findByUsername(authModel.getUsername()).get(0);
			try {
				authManager.authenticate(new UsernamePasswordAuthenticationToken(authModel.getUsername(), authModel.getPassword()));
				MyUserDetails mud = (MyUserDetails) userDetailService.loadUserByUsername(authModel.getUsername());

				response.remove("message");
				response.put("user", user.getFirstname() + " " + user.getLastname());
				response.put("role",user.getRole());
				response.put("token", jwtUtil.generateToken(mud));
				return new ResponseEntity<>(response, HttpStatus.CREATED);

			} catch (Exception ex) {
				message="invalid password";
				response.put("message", message);
				return new ResponseEntity<>(response, HttpStatus.CREATED);
			}
		}
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}



}
