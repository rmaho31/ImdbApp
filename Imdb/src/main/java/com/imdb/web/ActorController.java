package com.imdb.web;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.imdb.business.Actor;
import com.imdb.business.ActorRepository;
import com.imdb.utility.JsonResponse;

@Controller
@RequestMapping(path = "/Actors")
public class ActorController {
	
	@Autowired
	private ActorRepository actorRepository;
	
	@PostMapping(path = "/Add")
	public @ResponseBody JsonResponse addNewActor(@RequestBody Actor u) {
		return saveActor(u);
	}

	@GetMapping(path = "/Get/{id}")
	public @ResponseBody JsonResponse getActor(@PathVariable int id) {
		try {
			Optional<Actor> actor = actorRepository.findById(id);
			if (actor.isPresent())
				return JsonResponse.getInstance(actor.get());
			else
				return JsonResponse.getErrorInstance("Actor not found for id: " + id, null);
		} catch (Exception e) {
			return JsonResponse.getErrorInstance("Error getting actor:  " + e.getMessage(), null);
		}
	}

	@GetMapping(path = "/List")
	public @ResponseBody JsonResponse getAllActors() {
		try {
			return JsonResponse.getInstance(actorRepository.findAll());
		} catch (Exception e) {
			return JsonResponse.getErrorInstance("Actor list failure:" + e.getMessage(), e);
		}
	}

	@PostMapping(path = "/Remove")
	public @ResponseBody JsonResponse deleteActor(@RequestBody Actor u) {
		try {
			actorRepository.delete(u);
			return JsonResponse.getInstance(u);
		} catch (Exception ex) {
			return JsonResponse.getErrorInstance(ex.getMessage(), ex);
		}
	}

	@PostMapping(path = "/Change")
	public @ResponseBody JsonResponse updateActor(@RequestBody Actor u) {
		return saveActor(u);
	}

	private @ResponseBody JsonResponse saveActor(@RequestBody Actor u) {
		try {
			actorRepository.save(u);
			return JsonResponse.getInstance(u);
		} catch (DataIntegrityViolationException ex) {
			return JsonResponse.getErrorInstance(ex.getRootCause().toString(), ex);
		} catch (Exception ex) {
			return JsonResponse.getErrorInstance(ex.getMessage(), ex);
		}
	}

}
