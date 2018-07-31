package search.api.query;


import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/")
public class QueryController {

    private MongoClient client = new MongoClient("localhost", 27017);
    private MongoDatabase database = client.getDatabase("test2");
    private MongoCollection<Document> collection = database.getCollection("savedTweets");
    private MongoCollection<Document> queryCollection = database.getCollection("queryCollection");


    //home page
    @RequestMapping("/")
    public String homePage(Model model) {

        return "/home";

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

        saveToDB(query);

        return "redirect:/query/" + query.getId();
    }


    //page for queries, linked to unique ID
    @RequestMapping("/query/{id}")
    public String viewQueryTweets(@PathVariable("id") String id, Model model) {


        Boolean caseSensitive = false, diacriticSensitive = false;
        MongoCursor<Document> cursor = null;



        //find query from database
        String queryText = "";

        BasicDBObject findQueryText = new BasicDBObject();
        findQueryText.put("id", id);
        MongoCursor<Document> queryCursor = queryCollection.find(findQueryText).iterator();

        if (queryCursor.hasNext()) {
            queryText = queryCursor.next().get("text").toString();
        }

        queryCursor.close();

        //reset indexes for searching
        collection.dropIndexes();
        collection.createIndex(Indexes.text("text"));

        //find mentions of that query
        cursor = collection.find(
                new Document("$text",
                        new Document("$search", queryText)
                                .append("$caseSensitive", new Boolean(caseSensitive))
                                .append("$diacriticSensitive", new Boolean(diacriticSensitive)))).iterator();

        //update raw mentions with new query id
        while (cursor.hasNext()) {
            Bson newValue = new Document("queryId", id);
            Bson updateOperationDocument = new Document("$set", newValue);
            collection.updateOne(cursor.next(), updateOperationDocument);
        }
        cursor.close();

        //add updated collection to model
        model.addAttribute("queriedTweets", collection.find(new Document("queryId", id)));

        return "query";
    }


    private void saveToDB(Query query) {

        Gson gson = new Gson();

        Document document = Document.parse(gson.toJson(query));
        queryCollection.insertOne(document);

    }


}


