package com.imdb.business;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface CreditsRepository extends CrudRepository<Credits, Integer> {
	List<Credits> findByActor(Actor actor);
}
