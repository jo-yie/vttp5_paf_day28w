package vttp5_paf_day28w.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import vttp5_paf_day28w.model.GameAndReviews;
import vttp5_paf_day28w.model.GameByRating;
import vttp5_paf_day28w.repo.GameRepo;

@Service
public class GameService {

    @Autowired
    private GameRepo gameRepo;

    public Document getGameById(int game_id) {
        return gameRepo.getGameById(game_id);

    }

    public Optional<Document> getGameAndReviewsRaw(int game_id) {
        return gameRepo.getGameAndReviews(game_id);

    }

    // TASK A 
    public GameAndReviews getGameAndReviews(int game_id) {

        Optional<Document> doc = gameRepo.getGameAndReviews(game_id);

        if (doc.isEmpty()) {
            throw new NoSuchElementException("Game ID doesn't exist");

        }

        GameAndReviews g = docToGameAndReviewsPojo(doc.get());
        return g; 

    }

    // helper method 
    // Document --> GameAndReviews POJO
    private GameAndReviews docToGameAndReviewsPojo(Document doc) {

        GameAndReviews g = new GameAndReviews(); 

        g.setGame_id(doc.getInteger("gid"));
        g.setName(doc.getString("name"));
        g.setYear(doc.getInteger("year"));
        g.setRank(doc.getInteger("ranking"));
        g.setUsers_rated(doc.getInteger("users_rated"));
        g.setUrl(doc.getString("url"));
        g.setThumbnail(doc.getString("image"));
        
        List<Document> reviews = doc.getList("reviews", Document.class);
        List<String> stringReviews = new ArrayList<>(); 

        if (reviews != null) {
            for (Document d : reviews) {
                String reviewId = d.get("_id").toString();
                stringReviews.add("/review/" + reviewId);

            }

        }
        
        g.setReviews(stringReviews);

        g.setTimestamp(new Date());

        return g;

    }
    
    // get review by id 
    public Optional<Document> getReviewById(String review_id) {

        return gameRepo.getReviewById(review_id);

    }

    // TASK B 
    public GameByRating getGamesByRating(boolean isHighest) {

        GameByRating g = new GameByRating(); 

        if (isHighest) {
            g.setRating("highest");
        } else {
            g.setRating("lowest");
        }

        List<Document> games = gameRepo.getGamesByRating(isHighest);
        g.setGames(games);

        g.setTimestamp(new Date());

        return g;

    }

}
