package vttp5_paf_day28w.model;

import java.util.Date;
import java.util.List;

import org.bson.Document;

public class GameByRating {

    private String rating; 
    private List<Document> games; 
    private Date timestamp;
    public GameByRating() {
    }
    public GameByRating(String rating, List<Document> games, Date timestamp) {
        this.rating = rating;
        this.games = games;
        this.timestamp = timestamp;
    }
    public String getRating() {
        return rating;
    }
    public void setRating(String rating) {
        this.rating = rating;
    }
    public List<Document> getGames() {
        return games;
    }
    public void setGames(List<Document> games) {
        this.games = games;
    }
    public Date getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
    
}
