package CareerTracker.controller;

import CareerTracker.models.Role;
import CareerTracker.repositories.IRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/role")
public class RoleController {
    @Autowired
    private IRoleRepository roleRepository;

    @GetMapping("/getAll")
    public List<Role> getRoles(){
        return roleRepository.findAll();
    }
}
