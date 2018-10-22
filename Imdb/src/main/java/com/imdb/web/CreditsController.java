package com.imdb.web;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
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
import com.imdb.business.Credits;
import com.imdb.business.CreditsRepository;
import com.imdb.utility.JsonResponse;

@CrossOrigin
@Controller
@RequestMapping(path = "/Credits")
public class CreditsController {
	
	@Autowired
	private CreditsRepository creditsRepository;
	
	@Autowired
	private ActorRepository actorRepository;
	
	@PostMapping(path = "/Add")
	public @ResponseBody JsonResponse addNewCredits(@RequestBody Credits u) {
		return saveCredits(u);
	}

	@GetMapping(path = "/Get/{id}")
	public @ResponseBody JsonResponse getCredits(@PathVariable int id) {
		try {
			Optional<Credits> credits = creditsRepository.findById(id);
			if (credits.isPresent())
				return JsonResponse.getInstance(credits.get());
			else
				return JsonResponse.getErrorInstance("Credits not found for id: " + id, null);
		} catch (Exception e) {
			return JsonResponse.getErrorInstance("Error getting credits:  " + e.getMessage(), null);
		}
	}
	
	@GetMapping(path = "/Get/Actor/{name}")
	public @ResponseBody JsonResponse getCredits(@PathVariable String name) {
		try {
			List<Actor> actors = actorRepository.findByFirstName(name);
			ArrayList<Credits> credits = new ArrayList<Credits>(actors.size());
			for(Actor actor : actors) {
				credits.addAll(creditsRepository.findByActor(actor));
			}
			if (!credits.isEmpty())
				return JsonResponse.getInstance(credits);
			else
				return JsonResponse.getErrorInstance("Credits not found for name: " + name, null);
		} catch (Exception e) {
			return JsonResponse.getErrorInstance("Error getting credits:  " + e.getMessage(), null);
		}
	}
	
	@GetMapping(path = "/Get/Age/{age}")
	public @ResponseBody JsonResponse getByYear(@PathVariable int age) {
		try {
			List<Actor> actors = (List<Actor>) actorRepository.findAll();
			ArrayList<Credits> credits = new ArrayList<Credits>(actors.size());
			for(Actor actor : actors) {
				if(ChronoUnit.YEARS.between(actor.getBirthDate(), LocalDate.now()) > age){
					credits.addAll(creditsRepository.findByActor(actor));					
				}
			}
			if (!credits.isEmpty())
				return JsonResponse.getInstance(credits);
			else
				return JsonResponse.getErrorInstance("Credits not find any for year: " + age, null);
		} catch (Exception e) {
			return JsonResponse.getErrorInstance("Error getting credits:  " + e.getMessage(), null);
		}
	}

	@GetMapping(path = "/List")
	public @ResponseBody JsonResponse getAllCreditss() {
		try {
			return JsonResponse.getInstance(creditsRepository.findAll());
		} catch (Exception e) {
			return JsonResponse.getErrorInstance("Credits list failure:" + e.getMessage(), e);
		}
	}

	@PostMapping(path = "/Remove")
	public @ResponseBody JsonResponse deleteCredits(@RequestBody Credits u) {
		try {
			creditsRepository.delete(u);
			return JsonResponse.getInstance(u);
		} catch (Exception ex) {
			return JsonResponse.getErrorInstance(ex.getMessage(), ex);
		}
	}

	@PostMapping(path = "/Change")
	public @ResponseBody JsonResponse updateCredits(@RequestBody Credits u) {
		return saveCredits(u);
	}

	private @ResponseBody JsonResponse saveCredits(@RequestBody Credits u) {
		try {
			creditsRepository.save(u);
			return JsonResponse.getInstance(u);
		} catch (DataIntegrityViolationException ex) {
			return JsonResponse.getErrorInstance(ex.getRootCause().toString(), ex);
		} catch (Exception ex) {
			return JsonResponse.getErrorInstance(ex.getMessage(), ex);
		}
	}

}
