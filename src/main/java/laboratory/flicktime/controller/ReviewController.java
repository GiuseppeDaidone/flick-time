package laboratory.flicktime.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import laboratory.flicktime.model.Movie;
import laboratory.flicktime.model.Review;
import laboratory.flicktime.service.MovieService;
import laboratory.flicktime.service.ReviewService;
import laboratory.flicktime.service.UserService;

@Controller
public class ReviewController {

	@Autowired
	private ReviewService reviewService;

	@Autowired
	private MovieService movieService;

	@Autowired
	private UserService userService;
	
	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


	@GetMapping("/reviewForm/{movieId}")
	public String reviewForm(@PathVariable("movieId") Long mId,Model model) {
		model.addAttribute("movie",this.movieService.findById(mId));
		model.addAttribute("review",new Review());
		return "formNewReview.html";
	}

	@PostMapping("/movie/{movieId}/{username}")
	public String reviewSubmit(@ModelAttribute("review") Review review , @PathVariable("movieId") Long mId,@PathVariable("username") String username , Model model) {
	
		review.setUserName(username);	
		Movie movie = this.movieService.findById(mId);
		review.setMovie(movie);
		
		List<Review> movieReviews = movie.getReviews();
		movieReviews.add(review);

		this.movieService.save(movie);
		this.reviewService.save(review);
		
		model.addAttribute("review", review);
		model.addAttribute("movie", movie);
		
		return "movie.html"; 
	}
	
	@GetMapping("/admin/cancellaRecensione/{movieId}/{reviewId}")
	public String reviewDelete(@PathVariable("movieId") Long mId  ,@PathVariable("reviewId") Long rId,Model model ) {
		this.reviewService.delete(rId);
		model.addAttribute("movie", this.movieService.findById(mId));
		return "movie.html";
	}

	


}
