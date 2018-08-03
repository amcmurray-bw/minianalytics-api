package search.api.query;

import org.springframework.data.mongodb.repository.MongoRepository;

import amcmurray.bw.twitterdomainobjects.Query;

public interface QueryRepository extends MongoRepository<Query, String> {

    Query findById(String id);
}