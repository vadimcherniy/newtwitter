package com.newtwitter.repository;

import com.newtwitter.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {

    User findByName(String name);

    User findByActivationCode(String code);
}
