package it.uniroma1.flicktime.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma1.flicktime.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
}