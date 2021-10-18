package com.project.backend.dbAccess;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

public interface UserRepository extends MongoRepository<User, String> {

    User findByUserName(String userName);

}
