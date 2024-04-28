package laboratory.flicktime.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import laboratory.flicktime.controller.validator.MovieValidator;
import laboratory.flicktime.model.Artist;
import laboratory.flicktime.model.Credentials;
import laboratory.flicktime.model.Movie;
import laboratory.flicktime.repository.ArtistRepository;
import laboratory.flicktime.repository.MovieRepository;
import laboratory.flicktime.service.ArtistService;
import laboratory.flicktime.service.CredentialsService;
import laboratory.flicktime.service.MovieService;
import laboratory.flicktime.service.UserService;

@Controller
public class MovieController {
    

	@Autowired
	private MovieService movieService;
	
	@Autowired
	private ArtistService artistService;
	
	@Autowired 
	private MovieRepository movieRepository;
	
	@Autowired 
	private ArtistRepository artistRepository;

	@Autowired 
	private MovieValidator movieValidator;
	
	@Autowired
	private CredentialsService credentialsService;

    @Autowired
	private UserService userService;
	
	

	@GetMapping(value="/admin/formNewMovie")
	public String formNewMovie(Model model) {
		model.addAttribute("movie", new Movie());
		return "admin/formNewMovie.html";
	}

	@GetMapping(value="/admin/formUpdateMovie/{id}")
	public String formUpdateMovie(@PathVariable("id") Long id, Model model) {
		model.addAttribute("movie", this.movieService.findById(id));
		return "admin/formUpdateMovie.html";
	}

	@GetMapping(value="/admin/indexMovie")
	public String indexMovie() {
		return "admin/indexMovie.html";
	}
	
	@GetMapping(value="/admin/manageMovies")
	public String manageMovies(Model model) {
		model.addAttribute("movies", this.movieService.findAll());
		return "admin/manageMovies.html";
	}
	
	@GetMapping(value="/admin/setDirectorToMovie/{directorId}/{movieId}")
	public String setDirectorToMovie(@PathVariable("directorId") Long directorId, @PathVariable("movieId") Long movieId, Model model) {
		
		Artist director = this.artistService.findById(directorId);
		Movie movie = this.movieService.findById(movieId);
		director.getActorOf().add(movie);
		movie.setDirector(director);
		this.artistService.save(director);
		this.movieService.save(movie);
		
		model.addAttribute("movie", movie);
		return "admin/formUpdateMovie.html";
	}
	
	
	@GetMapping(value="/admin/addDirector/{id}")
	public String addDirector(@PathVariable("id") Long id, Model model) {
		model.addAttribute("artists", artistService.findAll());
		model.addAttribute("movie", movieService.findById(id));
		return "admin/directorsToAdd.html";
	}

	@PostMapping("/admin/movie")
	public String newMovie(@Valid @ModelAttribute("movie") Movie movie,BindingResult bindingResult ,
			@RequestParam("fileImage") MultipartFile multipartFile , Model model) throws IOException {
		
		this.movieValidator.validate(movie, bindingResult);
		if (!bindingResult.hasErrors()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
	        movie.setUrlImage(fileName);
	         
	        this.movieService.save(movie);
	        model.addAttribute("movie", movie);
	 
	        String uploadDir = "./photos/" + movie.getId();
	        Path uploadPath = Paths.get(uploadDir);
	 
	        if(!Files.exists(uploadPath)) {
	        	Files.createDirectories(uploadPath);
	        }
	        
	        try( InputStream inputStream = multipartFile.getInputStream()){
	        Path filePath = uploadPath.resolve(fileName);
	        System.out.println(filePath.toFile().getAbsolutePath());
	        Files.copy(inputStream, filePath,StandardCopyOption.REPLACE_EXISTING);
	        }
	        catch (IOException e) {
				throw new IOException("could not save Uploaded file" + fileName);
			}
			return "movie.html";
		} else {
			return "admin/formNewMovie.html"; 
		}
	}

	@GetMapping("/movie/{id}")
	public String getMovie(@PathVariable("id") Long id, Model model) {
		model.addAttribute("movie", this.movieService.findById(id));
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
	        return "movie.html";
		}
		else {		
			UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
			
			if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
				return "admin/adminMovie.html";
			}
			
			if (credentials.getRole().equals(Credentials.DEFAULT_ROLE)) {
				return "registeredMovie.html";
			}
		}
        return "movie.html";
	}

	@GetMapping("/movie")
	public String getMovies(Model model) {		
		model.addAttribute("movies", this.movieService.findAll());
		return "movies.html";
	}
	
	@GetMapping("/formSearchMovies")
	public String formSearchMovies() {
		return "formSearchMovies.html";
	}

	@PostMapping("/searchMovies")
	public String searchMovies(Model model, @RequestParam int year) {
		List<Movie> movies = this.movieService.searchMovies(year);
		if (movies.isEmpty()) {
	        return "movieNotFound.html"; // Return a view for movie not found
	    } else {
	        model.addAttribute("movies", movies); // Assuming you want to display the first found movie
	        return "foundMovies.html";
	    }
	}
	
	@GetMapping("/formSearchMovieByTitle")
	public String formSearchMoviesByTitle() {
		return "formSearchMovieByTitle.html";
	}
	
	@PostMapping("/searchMovieByTitle")
	public String searchMovieByTitle(Model model, @RequestParam String title) {
		Movie movie = this.movieService.searchMovieByTitle(title);
		 if (movie == null) {
		        return "movieNotFound.html"; // Return a view for movie not found
		    } else {
		        model.addAttribute("movie", movie);
		        return "movie.html"; // Assuming you have a specific template for displaying a single movie
		    }
	}
	
	@GetMapping("/admin/updateActors/{id}")
	public String updateActors(@PathVariable("id") Long id, Model model) {

		List<Artist> actorsToAdd = this.actorsToAdd(id);
		model.addAttribute("actorsToAdd", actorsToAdd);
		model.addAttribute("movie", this.movieService.findById(id));

		return "admin/actorsToAdd.html";
	}

	@GetMapping(value="/admin/addActorToMovie/{actorId}/{movieId}")
	public String addActorToMovie(@PathVariable("actorId") Long actorId, @PathVariable("movieId") Long movieId, Model model) {
		Movie movie = this.movieService.findById(movieId);
		Artist actor = this.artistService.findById(actorId);
		Set<Artist> actors = movie.getActors();
		actors.add(actor);
		actor.getActorOf().add(movie);
		this.artistService.save(actor);
		this.movieService.save(movie);
		
		List<Artist> actorsToAdd = actorsToAdd(movieId);
		
		model.addAttribute("movie", movie);
		model.addAttribute("actorsToAdd", actorsToAdd);

		return "admin/actorsToAdd.html";
	}
	
	@GetMapping(value="/admin/removeActorFromMovie/{actorId}/{movieId}")
	public String removeActorFromMovie(@PathVariable("actorId") Long actorId, @PathVariable("movieId") Long movieId, Model model) {
		Movie movie = this.movieService.findById(movieId);
		Artist actor = this.artistService.findById(actorId);
		Set<Artist> actors = movie.getActors();
		actors.remove(actor);
		this.movieService.save(movie);

		List<Artist> actorsToAdd = actorsToAdd(movieId);
		
		model.addAttribute("movie", movie);
		model.addAttribute("actorsToAdd", actorsToAdd);

		return "admin/actorsToAdd.html";
	}

	private List<Artist> actorsToAdd(Long movieId) {
		List<Artist> actorsToAdd = new ArrayList<>();

		for (Artist a : this.artistRepository.findActorsNotInMovie(movieId)) {
			actorsToAdd.add(a);
		}
		return actorsToAdd;
	}
}
