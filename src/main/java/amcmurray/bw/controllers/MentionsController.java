package amcmurray.bw.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import amcmurray.bw.MentionPresenter;
import amcmurray.bw.services.MentionService;
import amcmurray.bw.twitterdomainobjects.Mention;
import amcmurray.bw.twitterdomainobjects.MentionDTO;

@RestController
public class MentionsController {

    private MentionService mentionService;
    private MentionPresenter mentionPresenter;

    @Autowired
    public MentionsController(MentionService mentionService, MentionPresenter mentionPresenter) {
        this.mentionService = mentionService;
        this.mentionPresenter = mentionPresenter;
    }

    //view mentions of single query
    @GetMapping("/mentions/{id}")
    public List<MentionDTO> viewMentionsOfQuery(@PathVariable("id") int id) {
        return convertMentionsToMentionDTO(mentionService.findAllMentionsOfQuery(id));
    }

    //view all mentions
    @GetMapping("/mentions")
    public List<MentionDTO> viewAllMentions() {
        return convertMentionsToMentionDTO(mentionService.getAllMentions());
    }

    //takes list of mentions, converts them to MentionDTO and returns
    private List<MentionDTO> convertMentionsToMentionDTO(List<Mention> allMentions) {
        List<MentionDTO> listOfMentionDTO = new ArrayList<>();

        for (Mention mention : allMentions) {
            listOfMentionDTO.add(mentionPresenter.toDTO(mention));
        }
        return listOfMentionDTO;
    }

}
