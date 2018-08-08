package amcmurray.bw.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import amcmurray.bw.twitterdomainobjects.SavedTweet;

public interface TweetRepository extends MongoRepository<SavedTweet, String> {

    List<SavedTweet> findAllByTextContaining(String queryText);
    List<SavedTweet> findAllByQueryId(int queryId);

}
