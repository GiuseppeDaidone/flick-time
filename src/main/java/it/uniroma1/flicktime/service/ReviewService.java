package it.uniroma1.flicktime.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma1.flicktime.model.Movie;
import it.uniroma1.flicktime.model.Review;
import it.uniroma1.flicktime.repository.ReviewRepository;

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
