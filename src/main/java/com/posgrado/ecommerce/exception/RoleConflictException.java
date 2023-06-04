package com.posgrado.ecommerce.exception;

public class RoleConflictException extends RuntimeException {

    private static final String ERROR_MESSAGE = "Role with name %s already exists";

    public RoleConflictException(String role) {
        super(String.format(ERROR_MESSAGE, role));
    }
}
