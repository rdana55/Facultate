package com.example.socialnetworkmap.validators;

import com.example.socialnetworkmap.domain.User;
import com.example.socialnetworkmap.exceptions.ValidationException;

public class UserValidator implements Validator<User> {
    @Override
    public void validate(User entity) throws ValidationException {
        if (entity == null) {
            throw new ValidationException("User can not be null");
        }
        if (entity.getFirstName() == null || entity.getFirstName().isEmpty()) {
            throw new ValidationException("First name can not be null");
        }
        if (entity.getLastName() == null || entity.getLastName().isEmpty()) {
            throw new ValidationException("Last name can not be null");
        }
    }
}
