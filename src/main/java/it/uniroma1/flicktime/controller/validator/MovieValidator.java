package it.uniroma1.flicktime.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma1.flicktime.model.Movie;
import it.uniroma1.flicktime.repository.MovieRepository;

@Component
public class MovieValidator implements Validator {
	@Autowired
	private MovieRepository movieRepository;

	@Override
	public void validate(Object o, Errors errors) {
		Movie movie = (Movie)o;
		if (movie.getTitle()!=null && movie.getYear()!=null 
				&& movieRepository.existsByTitleAndYear(movie.getTitle(), movie.getYear())) {
			errors.reject("movie.duplicate");
		}
	}
	@Override
	public boolean supports(Class<?> aClass) {
		return Movie.class.equals(aClass);
	}
}