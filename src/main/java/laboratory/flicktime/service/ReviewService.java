package laboratory.flicktime.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import laboratory.flicktime.model.Movie;
import laboratory.flicktime.model.Review;
import laboratory.flicktime.repository.ReviewRepository;

@Service
public class ReviewService {

	@Autowired ReviewRepository reviewRepository;

	public void save(Review review) {
		this.reviewRepository.save(review);
	}

	public void delete(Long id) {
		this.reviewRepository.deleteById(id);
	}

	public Review findById(Long id) {
		return this.reviewRepository.findById(id).get();
	}

	public List<Review> findByMovie(Movie movie){
		List<Review> result = new ArrayList<>();
		Iterable<Review> iterable = this.reviewRepository.findByMovie(movie);
		for(Review review : iterable)
			result.add(review);
		return result;
	}

}
