package amcmurray.bw;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import amcmurray.bw.repository.QueryRepository;
import amcmurray.bw.repository.TweetRepository;
import amcmurray.bw.twitterdomainobjects.Query;
import amcmurray.bw.twitterdomainobjects.SavedTweet;


@Service
public class QueryService {

    private QueryRepository queryRepository;
    private TweetRepository tweetRepository;


    @Autowired
    public QueryService(QueryRepository queryRepository, TweetRepository tweetRepository) {
        this.queryRepository = queryRepository;
        this.tweetRepository = tweetRepository;
    }

    //method to create a query
    public Query createQuery(QueryRequestDTO request) {

        int currentId = 0;

        //if queries exist, get the last one, get the Id and increment
        if (queryRepository.findAll().size() > 0) {
            currentId = queryRepository.findAll().get(queryRepository.findAll().size() - 1).getId() + 1;
        }
        Query query = new Query(currentId, request.getSearch());
        return queryRepository.save(query);
    }

    public void saveQueryToDB(Query query) {
        queryRepository.insert(query);
    }

    public Query findQueryById(int id) {
        return queryRepository.findById(id);
    }

    public List<SavedTweet> getAllMentions(int id) {
        return tweetRepository.findAllByQueryId(id);
    }

    public List<SavedTweet> findAllQueriedTweets(String queryText) {
        return tweetRepository.findAllByTextContaining(queryText);
    }

    public List<Query> getListAllQueries() {
        return queryRepository.findAll();
    }

    public void updateQueryIdOfTweet(SavedTweet existingTweet, Query query) {
        SavedTweet updatedTweet = new SavedTweet(
                existingTweet.getId(),
                existingTweet.getText(),
                query.getId());
        tweetRepository.save(updatedTweet);
    }

}
