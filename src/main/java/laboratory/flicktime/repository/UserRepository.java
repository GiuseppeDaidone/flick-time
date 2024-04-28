package laboratory.flicktime.repository;

import org.springframework.data.repository.CrudRepository;

import laboratory.flicktime.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
}