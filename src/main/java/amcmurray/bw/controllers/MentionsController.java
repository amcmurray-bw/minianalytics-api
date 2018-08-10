package amcmurray.bw.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import amcmurray.bw.services.MentionService;
import amcmurray.bw.services.QueryService;
import amcmurray.bw.twitterdomainobjects.Mention;

@RestController
public class MentionsController {

    private QueryService queryService;
    private MentionService mentionService;

    @Autowired
    public MentionsController(QueryService queryService, MentionService mentionService) {
        this.queryService = queryService;
        this.mentionService = mentionService;
    }

    //view mentions of single query
    @GetMapping("/mentions/{id}")
    public List<Mention> viewMentionsOfQuery(@PathVariable("id") int id) {
        return mentionService.findAllMentionsOfQuery(id);
    }

    //view all mentions
    @GetMapping("/mentions")
    public List<Mention> viewAllMentions() {
        return mentionService.getAllMentions();
    }

}
