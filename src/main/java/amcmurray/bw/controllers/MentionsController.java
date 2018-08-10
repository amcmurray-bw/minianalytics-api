package amcmurray.bw.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import amcmurray.bw.services.MentionService;
import amcmurray.bw.services.QueryService;
import amcmurray.bw.twitterdomainobjects.Mention;
import amcmurray.bw.twitterdomainobjects.Query;

@RestController
public class MentionsController {

    private QueryService queryService;
    private MentionService mentionService;

    @Autowired
    public MentionsController(QueryService queryService, MentionService mentionService) {
        this.queryService = queryService;
        this.mentionService = mentionService;
    }

    /*
    this method will be changed so that the update of queryId takes place in the crawler
     */
    @GetMapping("/mentions/{id}")
    public List<Mention> viewMentionsOfQuery(@PathVariable("id") int id) {

        //find query by ID
        Query query = queryService.findQueryById(id);

        //for each mention, find one with the query text & update the query id
        for (Mention mention : mentionService.findAllQueriedTweets(query.getText())) {
            queryService.updateQueryIdOfMention(mention, query);
        }
        return mentionService.getAllMentions(query.getId());
    }
}
