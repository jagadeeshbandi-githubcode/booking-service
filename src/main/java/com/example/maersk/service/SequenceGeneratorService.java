package com.example.maersk.service;

import com.example.maersk.model.DatabaseSequence;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class SequenceGeneratorService {
    private final ReactiveMongoOperations mongo;

    public SequenceGeneratorService(ReactiveMongoOperations mongo) {
        this.mongo = mongo;
    }

    public Mono<Long> generateSequence(String seqName, long startValue) {
        Query query = new Query(Criteria.where("_id").is(seqName));
        Update update = new Update().inc("seq", 1);
        FindAndModifyOptions options = FindAndModifyOptions.options().returnNew(true).upsert(true);

        return mongo.findAndModify(query, update, options, DatabaseSequence.class)
                .map(DatabaseSequence::getSeq)
                .flatMap(seq -> {
                    if (seq < startValue) {
                        Update setToStart = new Update().set("seq", startValue);
                        return mongo.findAndModify(query, setToStart, FindAndModifyOptions.options().returnNew(true), DatabaseSequence.class)
                                .map(DatabaseSequence::getSeq);
                    }
                    return Mono.just(seq);
                });
    }
}
