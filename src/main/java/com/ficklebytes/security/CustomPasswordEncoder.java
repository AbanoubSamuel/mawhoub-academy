package com.ficklebytes.security;

import com.ficklebytes.utils.Utils;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CustomPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        return Utils.MD5(rawPassword.toString());

    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return Utils.MD5(rawPassword.toString())
                .equals(encodedPassword);
    }

}
