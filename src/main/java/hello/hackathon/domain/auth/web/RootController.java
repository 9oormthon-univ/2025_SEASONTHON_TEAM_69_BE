package hello.hackathon.domain.auth.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {
    @GetMapping("/")
    public ResponseEntity<String> root() {
        return ResponseEntity.ok("Hackathon Application");
    }
}
