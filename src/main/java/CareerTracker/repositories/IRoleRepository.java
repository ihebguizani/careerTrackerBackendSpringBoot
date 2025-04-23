package CareerTracker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import CareerTracker.models.Role;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Integer> {
    List<Role> findByRoleName(String roleName);
}
