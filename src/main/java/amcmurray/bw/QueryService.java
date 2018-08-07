package amcmurray.bw;

import java.util.List;
import java.util.UUID;

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

    public void saveQueryToDB(Query query) { queryRepository.insert(query); }

    public Query findQueryById(String id) {
        return queryRepository.findById(id);
    }

    public List<SavedTweet> findAllQueriedTweets(String queryText) { return tweetRepository.findAllByTextContaining(queryText); }

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

    public Query searchForQueryInDB(Query query) {
        Query foundQuery = queryRepository.findByText(query.getText());

        //if query does not exist, give an Id and save
        //else set the query Id to the existing Id
        if (foundQuery == null) {
            query.setId(UUID.randomUUID().toString());
            saveQueryToDB(query);
        } else {

            query.setId(foundQuery.getId());
        }
        return query;
    }


}
