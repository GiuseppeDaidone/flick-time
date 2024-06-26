package laboratory.flicktime.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import laboratory.flicktime.model.Credentials;

public interface CredentialsRepository extends CrudRepository<Credentials, Long> {

	public Optional<Credentials> findByUsername(String username);
	
	public boolean existsByUsername(String username);

}