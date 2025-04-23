package CareerTracker.controller;

import CareerTracker.models.*;
import CareerTracker.servicesImpl.EmailService;
import CareerTracker.servicesImpl.MoodleService;
import CareerTracker.servicesImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private MoodleService moodleService;
    private final EmailService emailService;

    @Autowired
    public UserController(EmailService emailService) {
        this.emailService = emailService;
    }



    @GetMapping("/getOne/{userId}")
    public User getOne(@PathVariable Long userId){
        return userService.getUserById(userId);
    }

    @GetMapping("/allUsers")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    //add User
//    @PostMapping("/add")
//    public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto){
//
//        UserDto userDto1=userService.addUser(userDto);
//
//        return new ResponseEntity<>(userDto1,HttpStatus.OK);
//    }

    /*@PostMapping("/add")
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto){
        userDto.setRole("EMPLOYEE");
        String employeeEmail = userDto.getEmail();
        String subject = "Création de votre compte";
        String generatedPassword = RandomStringUtils.randomAlphanumeric(10);
        userDto.setPassword(generatedPassword);
        UserDto userDto1=userService.addUser(userDto);
        // Contenu de l'email avec les informations du compte
        String text = "<html>"
                + "<body>"
                + "<p>Bonjour " + userDto.getFirstname() + " " + userDto.getLastname() + ",</p>"
                + "<p>Votre compte a été créé avec succès.</p>"
                + "<p>Voici vos identifiants :</p>"
                + "<p><strong>Email :</strong> " + userDto.getEmail() + "</p>"
                + "<p><strong>Mot de passe :</strong> " + generatedPassword + "</p>"
                + "<p>Veuillez vous connecter et changer votre mot de passe dès que possible.</p>"
                + "<p><a href=\"http://localhost:4200/login\" style=\"padding:10px 15px; background-color:blue; color:white; text-decoration:none;\">Se connecter</a></p>"
                + "<p>Merci !</p>"
                + "</body>"
                + "</html>";

        // Envoyer l'email
        emailService.sendEmail(employeeEmail, subject, text);
        return new ResponseEntity<>(userDto1,HttpStatus.OK);
    }
*/
    @PostMapping("/add")
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto) {
        userDto.setRole("EMPLOYEE");
        String employeeEmail = userDto.getEmail();
        String subject = "Création de votre compte";

        // Définir les types de caractères
        String upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String specialChars = "!@#$%^&*()-_=+<>?";

        // Générer au moins un de chaque type
        String upper = RandomStringUtils.random(1, upperCaseLetters);
        String lower = RandomStringUtils.random(6, lowerCaseLetters);
        String digit = RandomStringUtils.random(1, digits);
        String special = RandomStringUtils.random(1, specialChars);

        // Générer les autres caractères aléatoires (pour atteindre 10 caractères)
        String remaining = RandomStringUtils.random(1, upperCaseLetters + lowerCaseLetters + digits + specialChars);

        // Construire le mot de passe et le mélanger pour éviter un ordre prévisible
        String generatedPassword = upper + lower + digit + special + remaining;
        List<Character> passwordChars = generatedPassword.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toList());
        Collections.shuffle(passwordChars);
        generatedPassword = passwordChars.stream()
                .map(String::valueOf)
                .collect(Collectors.joining());

        userDto.setPassword(generatedPassword);

        // Ajouter l'utilisateur dans la base de données de Spring Boot
        UserDto savedUser = userService.addUser(userDto);

        // Appeler Moodle pour créer l'utilisateur
        moodleService.createMoodleUser(userDto, generatedPassword);

        // Envoyer l'email avec les identifiants
        String text = "<html>"
                + "<body>"
                + "<p>Bonjour " + userDto.getFirstname() + " " + userDto.getLastname() + ",</p>"
                + "<p>Votre compte a été créé avec succès.</p>"
                + "<p>Voici vos identifiants :</p>"
                + "<p><strong>Email :</strong> " + userDto.getEmail() + "</p>"
                + "<p><strong>Mot de passe :</strong> " + generatedPassword + "</p>"
                + "<p>Veuillez vous connecter et changer votre mot de passe dès que possible.</p>"
                + "<p><a href=\"http://localhost:4200/login\" style=\"padding:10px 15px; background-color:blue; color:white; text-decoration:none;\">Se connecter</a></p>"
                + "<p>Merci !</p>"
                + "</body>"
                + "</html>";

        emailService.sendEmail(employeeEmail, subject, text);

        return new ResponseEntity<>(savedUser, HttpStatus.OK);
    }

    @GetMapping("/courses")
    public ResponseEntity<String> getAllCourses() {
        String courses = moodleService.getAllCourses();
        return ResponseEntity.ok(courses); // Retourne la réponse JSON
    }


    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable Long id){userService.deleteUser(id);}


    @GetMapping("/getByUsername/{username}")
    public User getByUsername(@PathVariable String username){
        return userService.getUsersByUsername(username);
    }


}
