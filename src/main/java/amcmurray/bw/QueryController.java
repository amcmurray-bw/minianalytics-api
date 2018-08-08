package amcmurray.bw;


import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;

import amcmurray.bw.twitterdomainobjects.Query;
import amcmurray.bw.twitterdomainobjects.SavedTweet;

@RestController
public class QueryController {

    private MongoClient client = new MongoClient("localhost", 27017);
    private MongoDatabase database = client.getDatabase("test");
    private MongoCollection<Document> collection = database.getCollection("savedTweets");

    private QueryService queryService;


    @Autowired
    public QueryController(QueryService queryService) {
        this.queryService = queryService;
    }

    //POST to create query
    @PostMapping("/query")
    public Query addQuery(@RequestBody QueryRequestDTO request) {
        return queryService.createQuery(request);
    }


    //GET mentions, linked to unique ID of query
    @GetMapping("/queries/{id}")
    public List<SavedTweet> viewMentionsOfQuery(@PathVariable("id") int id) {

        //find query by ID
        Query query = queryService.findQueryById(id);

        //reset indexes for searching
        collection.dropIndexes();
        collection.createIndex(Indexes.text("text"));

        //for each tweet, find one with the query text & update the query id
        for (SavedTweet tweet : queryService.findAllQueriedTweets(query.getText())) {
            queryService.updateQueryIdOfTweet(tweet, query);
        }

        return queryService.getAllMentions(query.getId());
    }

    //GET for mentions,lists all queries
    @GetMapping("/queries")
    public List<Query> viewQueries() {
        return queryService.getListAllQueries();
    }
}
