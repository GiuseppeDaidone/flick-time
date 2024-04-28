package laboratory.flicktime.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import laboratory.flicktime.model.Movie;

public interface MovieRepository extends CrudRepository<Movie, Long> {

	public List<Movie> findByYear(int year);

	public boolean existsByTitleAndYear(String title, int year);	
	
	public Movie findByTitle(String title);
}