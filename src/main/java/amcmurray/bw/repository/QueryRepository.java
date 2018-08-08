package amcmurray.bw.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import amcmurray.bw.twitterdomainobjects.Query;

public interface QueryRepository extends MongoRepository<Query, String> {

    Query findById(int id);
    Query findByText(String text);
}
