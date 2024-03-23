package it.uniroma1.flicktime.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma1.flicktime.model.Movie;
import it.uniroma1.flicktime.model.Review;

public interface ReviewRepository extends CrudRepository<Review, Long> {
	
	public List<Review> findByMovie(Movie movie);

}
