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
import com.imdb.business.Movie;
import com.imdb.business.MovieRepository;
import com.imdb.utility.JsonResponse;

@CrossOrigin
@Controller
@RequestMapping(path = "/Movies")
public class MovieController {
	
	@Autowired
	private MovieRepository movieRepository;
	
	@PostMapping(path = "/Add")
	public @ResponseBody JsonResponse addNewMovie(@RequestBody Movie u) {
		return saveMovie(u);
	}

	@GetMapping(path = "/Get/{id}")
	public @ResponseBody JsonResponse getMovie(@PathVariable int id) {
		try {
			Optional<Movie> movie = movieRepository.findById(id);
			if (movie.isPresent())
				return JsonResponse.getInstance(movie.get());
			else
				return JsonResponse.getErrorInstance("Movie not found for id: " + id, null);
		} catch (Exception e) {
			return JsonResponse.getErrorInstance("Error getting movie:  " + e.getMessage(), null);
		}
	}

	@GetMapping(path = "/List")
	public @ResponseBody JsonResponse getAllMovies() {
		try {
			return JsonResponse.getInstance(movieRepository.findAll());
		} catch (Exception e) {
			return JsonResponse.getErrorInstance("Movie list failure:" + e.getMessage(), e);
		}
	}

	@PostMapping(path = "/Remove")
	public @ResponseBody JsonResponse deleteMovie(@RequestBody Movie u) {
		try {
			movieRepository.delete(u);
			return JsonResponse.getInstance(u);
		} catch (Exception ex) {
			return JsonResponse.getErrorInstance(ex.getMessage(), ex);
		}
	}

	@PostMapping(path = "/Change")
	public @ResponseBody JsonResponse updateMovie(@RequestBody Movie u) {
		return saveMovie(u);
	}

	private @ResponseBody JsonResponse saveMovie(@RequestBody Movie u) {
		try {
			movieRepository.save(u);
			return JsonResponse.getInstance(u);
		} catch (DataIntegrityViolationException ex) {
			return JsonResponse.getErrorInstance(ex.getRootCause().toString(), ex);
		} catch (Exception ex) {
			return JsonResponse.getErrorInstance(ex.getMessage(), ex);
		}
	}

}
