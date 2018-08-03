package search.api.query;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void saveQueryToDB(Query query) {

        queryRepository.insert(query);
    }

    public Query findQueryText(String id) {
        return queryRepository.findById(id);
    }

    public List<SavedTweet> findAllQueriedTweets(String queryText) {
        return tweetRepository.findAllByTextContaining(queryText);
    }

    public void updateQueryIdOfTweet(SavedTweet existingTweet, Query query) {
        SavedTweet updatedTweet = new SavedTweet(
                existingTweet.getId(),
                existingTweet.getText(),
                query.getId());
        tweetRepository.save(updatedTweet);
    }

}
