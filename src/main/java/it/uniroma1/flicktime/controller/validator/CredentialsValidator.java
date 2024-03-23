package it.uniroma1.flicktime.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma1.flicktime.model.Credentials;
import it.uniroma1.flicktime.repository.CredentialsRepository;

@Component
public class CredentialsValidator implements Validator{

	@Autowired
	private CredentialsRepository credentialsRepository;

	@Override
	public void validate(Object o, Errors errors) {
		Credentials credentials = (Credentials)o;
		if (credentials.getUsername()!=null
				&& credentialsRepository.existsByUsername(credentials.getUsername())) {
			errors.reject("credentials.duplicate");
		}
	}
		
	
	@Override
	public boolean supports(Class<?> aClass) {
		return Credentials.class.equals(aClass);
	}

}
