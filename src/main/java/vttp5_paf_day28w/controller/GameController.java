package vttp5_paf_day28w.controller;

import java.util.Map;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import vttp5_paf_day28w.service.GameService;

@RestController
public class GameController {

    @Autowired
    private GameService gameService;

    // get game by id
    @GetMapping("/game/{game_id}")
    public ResponseEntity<Object> getGameById(@PathVariable int game_id) {
        return ResponseEntity.ok()
            .body(gameService.getGameById(game_id)); 

    }

    // get game and reviews raw document 
    @GetMapping("/game/{game_id}/reviews/raw")
    public ResponseEntity<Object> getGameAndReviewsRaw(@PathVariable int game_id) {
        return ResponseEntity.ok()
            .body(gameService.getGameAndReviewsRaw(game_id).get());

    }

    // TASK A
    // GET /game/<game_id>/reviews
    // Accept: application/json
    @GetMapping("/game/{game_id}/reviews")
    public ResponseEntity<Object> getGameAndReviews(@PathVariable int game_id) {

        try {
            return ResponseEntity.ok()
                .body(gameService.getGameAndReviews(game_id));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("Error", e.getMessage()));

        }

    }

    // GET /review/<review_id>
    @GetMapping("/review/{review_id}")
    public ResponseEntity<Object> getReviewById(@PathVariable String review_id) {

        if (review_id.length() < 24) {
            return ResponseEntity.badRequest()
                .body(Map.of("Error", "Review ID must be 24 characters long"));
        }

        Optional<Document> optDoc = gameService.getReviewById(review_id);

        if (optDoc.isPresent()) {
            return ResponseEntity.ok()
                .body(optDoc.get());

        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("Error", "Review ID not found"));

        }

    }

    
}
