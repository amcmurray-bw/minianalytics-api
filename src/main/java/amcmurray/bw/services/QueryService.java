package amcmurray.bw.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import amcmurray.bw.QueryRequestDTO;
import amcmurray.bw.repositories.MentionRepository;
import amcmurray.bw.repositories.QueryRepository;
import amcmurray.bw.twitterdomainobjects.Mention;
import amcmurray.bw.twitterdomainobjects.Query;


@Service
public class QueryService {

    private QueryRepository queryRepository;
    private MentionRepository mentionRepository;


    @Autowired
    public QueryService(QueryRepository queryRepository, MentionRepository mentionRepository) {
        this.queryRepository = queryRepository;
        this.mentionRepository = mentionRepository;
    }

    public Query createQuery(QueryRequestDTO request) {

        int currentId;
        Query lastQuery = queryRepository.findFirstByOrderByIdDesc();

        //if there are no queries set the key to 0
        if (lastQuery == null) {
            currentId = 0;
        } else {
            currentId = lastQuery.getId() + 1;
        }

        Query query = new Query(currentId, request.getSearch());
        return queryRepository.save(query);
    }

    //save the query
    public void saveQueryToDB(Query query) {
        queryRepository.save(query);
    }

    //find the query
    public Query findQueryById(int id) {
        return queryRepository.findById(id);
    }

    public List<Query> getListAllQueries() {
        return queryRepository.findAll();
    }

    public void updateQueryIdOfMention(Mention existingMention, Query query) {

        Mention updatedMention = new Mention(
                existingMention.getId(),
                query.getId(),
                existingMention.getMentionType(),
                existingMention.getText(),
                existingMention.getLanguageCode(),
                existingMention.getFavouriteCount());

        mentionRepository.save(updatedMention);
    }

}
