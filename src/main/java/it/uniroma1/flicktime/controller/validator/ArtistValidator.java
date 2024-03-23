package it.uniroma1.flicktime.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma1.flicktime.model.Artist;
import it.uniroma1.flicktime.model.Movie;
import it.uniroma1.flicktime.repository.ArtistRepository;

@Component
public class ArtistValidator implements Validator{

	@Autowired
	private ArtistRepository artistRepository;
	
	@Override
	public void validate(Object o, Errors errors) {
		Artist artist = (Artist)o;
		if(artist.getName() !=null && artist.getSurname() !=null
				&& artistRepository.existsByNameAndSurname(artist.getName(),artist.getSurname())) {
			errors.reject("artist.duplicate");
		}
	}
	
	@Override
	public boolean supports(Class<?> aClass) {
		return Artist.class.equals(aClass);
	}


}
