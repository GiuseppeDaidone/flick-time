package laboratory.flicktime.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import laboratory.flicktime.controller.validator.ArtistValidator;
import laboratory.flicktime.model.Artist;
import laboratory.flicktime.repository.ArtistRepository;
import laboratory.flicktime.service.ArtistService;

@Controller
public class ArtistController {
	
	@Autowired
	private ArtistService artistService;
	
	@Autowired 
	private ArtistRepository artistRepository;
	
	@Autowired
	private ArtistValidator artistValidator;

	@GetMapping(value="/admin/formNewArtist")
	public String formNewArtist(Model model) {
		model.addAttribute("artist", new Artist());
		return "admin/formNewArtist.html";
	}
	
	@GetMapping(value="/admin/indexArtist")
	public String indexArtist() {
		return "admin/indexArtist.html";
	}
	
	@PostMapping("/admin/artist")
	public String newArtist(@Valid @ModelAttribute("artist") Artist artist,BindingResult bindingResult ,
            @RequestParam("fileImage") MultipartFile multipartFile, Model model) throws IOException {
		
		this.artistValidator.validate(artist, bindingResult);
		if(!bindingResult.hasErrors()) {
		
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        artist.setUrlOfPicture(fileName);
         
        this.artistService.save(artist);
        model.addAttribute("artist", artist);
 
        String uploadDir = "./photos/" + artist.getId();
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
        return "artist.html";
		}
        else {	        
            return "admin/formNewArtist.html";
	        }
		}

	@GetMapping("/artist/{id}")
	public String getArtist(@PathVariable("id") Long id, Model model) {
		model.addAttribute("artist", this.artistService.findById(id));
		return "artist.html";
	}

	@GetMapping("/artist")
	public String getArtists(Model model) {
		model.addAttribute("artists", this.artistService.findAll());
		return "artists.html";
	}
}
