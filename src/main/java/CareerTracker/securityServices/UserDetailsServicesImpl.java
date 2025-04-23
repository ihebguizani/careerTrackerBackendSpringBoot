
package CareerTracker.securityServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import CareerTracker.models.User;
import CareerTracker.repositories.IUserRepository;





@Service
public class UserDetailsServicesImpl implements UserDetailsService {

	@Autowired
	private IUserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userRepo.findByUsername(username).get(0);

		if (user == null) {
			throw new UsernameNotFoundException("Could not find user");
		}

		return new MyUserDetails(user);
	}

}
