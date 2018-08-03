package search.api.query;


import java.util.UUID;

import javax.inject.Inject;

import org.bson.Document;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;

import amcmurray.bw.twitterdomainobjects.Query;
import amcmurray.bw.twitterdomainobjects.SavedTweet;

@Controller
@RequestMapping("/")
public class QueryController {

    private MongoClient client = new MongoClient("localhost", 27017);
    private MongoDatabase database = client.getDatabase("test");
    private MongoCollection<Document> collection = database.getCollection("savedTweets");

    private QueryService queryService;

    @Inject
    public QueryController(QueryService queryService) {
        this.queryService = queryService;
    }

    //home page
    @RequestMapping("/")
    public String homePage(Model model) {

        return "home";
    }

    //viewing all of the tweets
    @RequestMapping("/all")
    public void viewTweets(Model model) {

        model.addAttribute("savedTweets", collection.find());
    }

    //page for searching
    @GetMapping("/search")
    public String submitSearch(Model model) {

        Query query = new Query();

        model.addAttribute("query", query);

        return "search";
    }

    //on return, set query ID and save to a query DB
    @PostMapping("/search")
    public String getSearch(@ModelAttribute Query query) {

        query.setId(UUID.randomUUID().toString());

        queryService.saveQueryToDB(query);

        return "redirect:/query/" + query.getId();
    }


    //page for queries, linked to unique ID
    @RequestMapping("/query/{id}")
    public String viewQueryTweets(@PathVariable("id") String id, Model model) {

        //find query by ID
        Query query = queryService.findQueryText(id);

        //reset indexes for searching
        collection.dropIndexes();
        collection.createIndex(Indexes.text("text"));

        //for each tweet, find one with the query text & update the query id
        for (SavedTweet tweet : queryService.findAllQueriedTweets(query.getText())) {
            queryService.updateQueryIdOfTweet(tweet, query);
        }

        //add updated collection to model
        model.addAttribute("queriedTweets", collection.find(new Document("queryId", id)));

        return "query";
    }

}
