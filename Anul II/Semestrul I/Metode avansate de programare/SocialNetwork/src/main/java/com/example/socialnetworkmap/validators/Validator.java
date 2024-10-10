package com.example.socialnetworkmap.validators;

import com.example.socialnetworkmap.exceptions.ValidationException;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}
