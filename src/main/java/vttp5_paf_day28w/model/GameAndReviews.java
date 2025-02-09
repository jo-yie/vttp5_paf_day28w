package vttp5_paf_day28w.model;

import java.util.Date;
import java.util.List;

public class GameAndReviews {

    private int game_id; 
    private String name; 
    private int year; 
    private int rank; 
    private int users_rated; 
    private String url; 
    private String thumbnail; 
    private List<String> reviews; 
    private Date timestamp;

    public GameAndReviews() {
    }

    public GameAndReviews(int game_id, String name, int year, int rank, int users_rated, String url, String thumbnail,
            List<String> reviews, Date timestamp) {
        this.game_id = game_id;
        this.name = name;
        this.year = year;
        this.rank = rank;
        this.users_rated = users_rated;
        this.url = url;
        this.thumbnail = thumbnail;
        this.reviews = reviews;
        this.timestamp = timestamp;
    }

    public int getGame_id() {
        return game_id;
    }

    public void setGame_id(int game_id) {
        this.game_id = game_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getUsers_rated() {
        return users_rated;
    }

    public void setUsers_rated(int users_rated) {
        this.users_rated = users_rated;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public List<String> getReviews() {
        return reviews;
    }

    public void setReviews(List<String> reviews) {
        this.reviews = reviews;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
    
}
