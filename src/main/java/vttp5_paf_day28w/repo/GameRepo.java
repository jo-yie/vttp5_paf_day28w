package vttp5_paf_day28w.repo;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.MongoExpression;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationExpression;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import static vttp5_paf_day28w.utils.Constants.*;

import java.util.List;
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

    // TASK B 
    /*
    db.game.aggregate([
        { $limit : 2 }, 
        {
            $lookup : {
                from : 'reviews',
                foreignField : 'ID', 
                localField : 'gid',
                as : 'reviewList'
            } 
        }, 
        {
            $unwind : '$reviewList'
        }, 
        {
            $sort : { 'reviewList.rating' : - 1 }
        }, 
        { 
            $group : {
                _id : '$gid', 
                name : { $first : '$name' },
                rating : { $first : '$reviewList.rating' },
                user : { $first : '$reviewList.user' }, 
                comment : { $first : '$reviewList.comment' },
                review_id : { $first : { $toString : '$reviewList._id' } }
            }
        }
    ])
     */
    // instructions --> ON game collection 
    // 1. limit : 2
    // 2. join with reviews collection on gid + ID 
    // 3. unwind 
    // 4. sort in descending order based on rating 
    // 5. group on GID --> include rating, user, comment, review_id 
    public List<Document> getGamesByRating(boolean isHighest) {

        LimitOperation limitOperation = Aggregation.limit(2);

        LookupOperation lookupOperation = Aggregation.lookup(C_REVIEWS, F_GAME_ID, F_ID, "reviewList");

        UnwindOperation unwindOperation = Aggregation.unwind("reviewList");

        SortOperation sortOperation = Aggregation.sort(Sort.Direction.DESC, "reviewList.rating");

        if (!isHighest) {
            sortOperation = Aggregation.sort(Sort.Direction.ASC, "reviewList.rating");
        } 
        
        // review_id : { $first : { $toString : '$reviewList._id' } }
        MongoExpression idMongo = MongoExpression.create("$toString : '$reviewList._id'");
        AggregationExpression idAgg = AggregationExpression.from(idMongo);

        GroupOperation groupOperation = Aggregation.group("gid")
            .first("name").as("name")
            .first("reviewList.rating").as("rating")
            .first("reviewList.user").as("user")
            .first("reviewList.comment").as("comment")
            .first(idAgg).as("review_id");

        Aggregation pipeline = Aggregation.newAggregation(limitOperation, lookupOperation, unwindOperation, sortOperation, groupOperation);

        return template.aggregate(pipeline, C_GAMES, Document.class).getMappedResults();

    }


}
