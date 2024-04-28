package laboratory.flicktime.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import laboratory.flicktime.model.Movie;
import laboratory.flicktime.repository.ArtistRepository;
import laboratory.flicktime.repository.MovieRepository;

@Service
public class MovieService {

	@Autowired
	protected MovieRepository movieRepository;

	@Autowired
	protected ArtistRepository artistRepository;

	public List<Movie> searchMovies(Integer year){
		return this.movieRepository.findByYear(year);

	}
	
	public Movie searchMovieByTitle(String title){
		return this.movieRepository.findByTitle(title);

	}

	public Movie findById(Long id) {
		return this.movieRepository.findById(id).get();
	}

	public List<Movie> findAll() {
		List<Movie> result = new ArrayList<>();
		Iterable<Movie> iterable = this.movieRepository.findAll();
		for(Movie movie : iterable)
			result.add(movie);
		return result;
	}

	@Transactional
	public void save(Movie movie) {
		this.movieRepository.save(movie);
		
	}

}
