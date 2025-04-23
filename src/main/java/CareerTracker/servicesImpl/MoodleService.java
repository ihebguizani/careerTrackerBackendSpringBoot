package CareerTracker.servicesImpl;

import CareerTracker.models.UserDto;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;



@Service
public class MoodleService {
    @Autowired
    private EmailService emailService;
    private final String MOODLE_API_URL = "http://localhost/webservice/rest/server.php";
    private final String TOKEN = "c55d95f69f91fc4cfc80f50313446347";

    public void createMoodleUser(UserDto userDto, String password) {
        RestTemplate restTemplate = new RestTemplate();

        String url = MOODLE_API_URL + "?wstoken=" + TOKEN
                + "&wsfunction=core_user_create_users"
                + "&moodlewsrestformat=json";

        // Encodage des paramètres en x-www-form-urlencoded
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("users[0][username]", userDto.getUsername());
        body.add("users[0][password]", password);
        body.add("users[0][firstname]", userDto.getFirstname());
        body.add("users[0][lastname]", userDto.getLastname());
        body.add("users[0][email]", userDto.getEmail());
        body.add("users[0][auth]", "manual"); // Méthode d'authentification

        // Headers pour form-urlencoded
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        // Envoi de la requête
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        System.out.println(response.getBody());
    }

        public String getAllCourses() {
        RestTemplate restTemplate = new RestTemplate();

        // Construire l'URL de l'API Moodle
        String url = MOODLE_API_URL + "?wstoken=" + TOKEN
                + "&wsfunction=core_course_get_courses"
                + "&moodlewsrestformat=json";

        // Exécuter la requête GET vers Moodle
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);

        return response.getBody(); // Retourner la liste des cours en format JSON
    }
    public String getCourseById(int courseId) {
        RestTemplate restTemplate = new RestTemplate();

        String url = MOODLE_API_URL + "?wstoken=" + TOKEN
                + "&wsfunction=core_course_get_courses"
                + "&moodlewsrestformat=json";

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);

        // Convertir la réponse JSON pour extraire le nom du cours
        JSONArray courses = new JSONArray(response.getBody());
        for (int i = 0; i < courses.length(); i++) {
            JSONObject course = courses.getJSONObject(i);
            if (course.getInt("id") == courseId) {
                return course.getString("fullname");
            }
        }
        return "Cours introuvable";
    }
    public void recommendCourseToUser(String userEmail, int courseId) {
        // Récupérer les informations du cours depuis Moodle
        String courseDetails = getCourseById(courseId);

        // Envoyer une notification par email
        String subject = "Recommandation de cours";
        String message = "<html><body>"
                + "<p>Bonjour,</p>"
                + "<p>Nous vous recommandons le cours suivant :</p>"
                + "<p><strong>" + courseDetails + "</strong></p>"
                + "<p>Vous pouvez vous inscrire en cliquant sur le lien ci-dessous :</p>"
                + "<p><a href='http://localhost/moodle/course/view.php?id=" + courseId + "'"
                + " style='padding:10px 15px; background-color:blue; color:white; text-decoration:none;'>Accéder au cours</a></p>"
                + "<p>Bonne formation !</p>"
                + "</body></html>";

        emailService.sendEmail(userEmail, subject, message);

        // Envoyer une notification à l'utilisateur (WebSocket ou base de données)
        //notificationService.sendNotification(userEmail, "Vous avez une nouvelle recommandation de cours !");
    }


}
