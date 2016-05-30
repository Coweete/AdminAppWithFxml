package com.fxmlspringtest.model;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author Sebastian Börebäck, Robin Johansson
 *
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    @Value("${management.security.role}")
    public static String ADMIN;

    public static final String USER = "ROLE_USER";

    public static final String ANONYMOUS = "ROLE_ANONMOUS";

    private AuthoritiesConstants() {
    }
}
