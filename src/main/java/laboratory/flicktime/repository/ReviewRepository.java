package laboratory.flicktime.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import laboratory.flicktime.model.Movie;
import laboratory.flicktime.model.Review;

public interface ReviewRepository extends CrudRepository<Review, Long> {
	
	public List<Review> findByMovie(Movie movie);

}
