package amcmurray.bw.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import amcmurray.bw.repositories.MentionRepository;
import amcmurray.bw.twitterdomainobjects.Mention;

@Service
public class MentionService {

    private MentionRepository mentionRepository;

    @Autowired
    public MentionService(MentionRepository mentionRepository) {
        this.mentionRepository = mentionRepository;
    }

    public List<Mention> findAllQueriedTweets(String queryText) {
        return mentionRepository.findAllByTextContaining(queryText);
    }

    public List<Mention> getAllMentions(int id) {
        return mentionRepository.findAllByQueryId(id);
    }
}
