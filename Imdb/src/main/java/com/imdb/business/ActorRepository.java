package com.imdb.business;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface ActorRepository extends CrudRepository<Actor, Integer> {
	List<Actor> findByFirstName(String firstName);
}
