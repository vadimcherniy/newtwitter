package com.newtwitter.repository;

import com.newtwitter.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findAll();

    User findByUsername(String name);

    User findByActivationCode(String code);
}
