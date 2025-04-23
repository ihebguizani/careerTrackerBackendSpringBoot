package CareerTracker.controller;

import CareerTracker.servicesImpl.MoodleService;
import CareerTracker.servicesImpl.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MoodleController {

    @Autowired
    private  MoodleService moodleService;

    @Autowired
    private  NotificationService notificationService;



    @GetMapping("/api/courses")
    public ResponseEntity<String> getAllCourses() {
        String courses = moodleService.getAllCourses();
        return ResponseEntity.ok(courses); // Retourne la réponse JSON
    }

    @PostMapping("/api/recommend")
    public ResponseEntity<String> recommendCourse(@RequestParam String userEmail, @RequestParam int courseId) {
        String courseName = moodleService.getCourseById(courseId);
        // Envoyer une notification
        String notificationMessage = "Un cours vous a été recommandé : " + courseName;
        notificationService.sendNotification(notificationMessage);

        moodleService.recommendCourseToUser(userEmail,courseId);
        return ResponseEntity.ok("Cours recommandé et notification envoyée !");
    }
}
