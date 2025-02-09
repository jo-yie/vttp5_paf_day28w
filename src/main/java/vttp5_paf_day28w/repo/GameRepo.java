package vttp5_paf_day28w.repo;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import static vttp5_paf_day28w.utils.Constants.*;

import java.util.Optional;

@Repository
public class GameRepo {

    @Autowired
    private MongoTemplate template;

    /*
    db.game.findOne(
        { gid : <GAME ID> }
    )
     */
    public Document getGameById(int game_id) {

        Criteria criteria = Criteria
            .where(F_GAME_ID).is(game_id);

        Query query = new Query().addCriteria(criteria);

        return template.findOne(query, Document.class, C_GAMES);

    }
    
    // TASK A 
    /*
    db.game.aggregate([
        { $match : { gid : <GAME ID> }},
        {
            $lookup : {
                from : 'reviews',
                localField : 'gid',
                foreignField : 'ID', 
                as : 'reviews'
            }
        }
    ])
     */
    public Optional<Document> getGameAndReviews(int game_id) { 

        Criteria criteria = Criteria.where(F_GAME_ID)
            .is(game_id);

        MatchOperation matchOperation = Aggregation.match(criteria);
        LookupOperation lookupOperation = LookupOperation.newLookup()
            .from(C_REVIEWS)
            .localField(F_GAME_ID)
            .foreignField(F_ID)
            .as("reviews");

        Aggregation pipeline = Aggregation.newAggregation(matchOperation, lookupOperation);

        Document doc = template.aggregate(pipeline, C_GAMES, Document.class).getUniqueMappedResult();

        return Optional.ofNullable(doc);

    }

    /*
    db.reviews.findOne(
        { _id : ObjectId("<OBJECT ID STRING>")" }
    )
     */
    public Optional<Document> getReviewById(String review_id) {

        ObjectId objectId = new ObjectId(review_id);

        Criteria criteria = new Criteria()
            .where(F_OBJECT_ID).is(objectId);

        Query query = new Query().addCriteria(criteria);
        Document doc = template.findOne(query, Document.class, C_REVIEWS);

        return Optional.ofNullable(doc);

    }

}
