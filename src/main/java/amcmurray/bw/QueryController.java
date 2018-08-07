package amcmurray.bw;


import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public QueryController(QueryService queryService) {
        this.queryService = queryService;
    }

    //home page
    @GetMapping("/")
    public String homePage(Model model) {

        return "home";
    }

    //page for viewing all of the tweets
    @GetMapping("/all")
    public void viewTweets(Model model) {

        model.addAttribute("savedTweets", collection.find());
    }

    //page for adding a new query
    @GetMapping("/newQuery")
    public String newQuery(Model model) {

        Query query = new Query();
        model.addAttribute("query", query);

        return "newQuery";
    }

    //page choosing an existing query
    @GetMapping("/existingQuery")
    public String existingQuery(Model model) {

        model.addAttribute("allQueries", database.getCollection("savedQueries").find());

        return "existingQuery";
    }


    //page to process a query
    @PostMapping("/processQuery")
    public String getSearch(@ModelAttribute("savedQueries") Query query) {

        queryService.searchForQueryInDB(query);

        return "redirect:/mentions/" + query.getId();
    }


    //page for mentions, linked to unique ID
    @GetMapping("/mentions/{id}")
    public String viewQueryTweets(@PathVariable("id") String id, Model model) {

        //find query by ID
        Query query = queryService.findQueryById(id);

        //reset indexes for searching
        collection.dropIndexes();
        collection.createIndex(Indexes.text("text"));

        //for each tweet, find one with the query text & update the query id
        for (SavedTweet tweet : queryService.findAllQueriedTweets(query.getText())) {
            queryService.updateQueryIdOfTweet(tweet, query);
        }

        //add updated collection to model
        model.addAttribute("queriedTweets", collection.find(new Document("queryId", id)));

        return "mentions";
    }
}
