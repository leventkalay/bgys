package com.yesevi.bgysapp.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String BGYS_YONETICISI = "ROLE_BGYS_YONETICISI";

    public static final String RISK_YONETICISI = "ROLE_RISK_YONETICISI";

    public static final String BIRIM_SORUMLUSU = "  ROLE_BIRIM_SORUMLUSU";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    private AuthoritiesConstants() {}
}
