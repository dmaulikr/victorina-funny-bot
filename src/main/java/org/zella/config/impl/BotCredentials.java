package org.zella.config.impl;

import java.util.Objects;

/**
 * @author zella.
 */
public class BotCredentials implements org.zella.config.ICredentials {

    private final String email;
    private final String pass;

    public BotCredentials(String email, String pass) {
        this.email = email;
        this.pass = pass;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPass() {
        return pass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BotCredentials that = (BotCredentials) o;
        return Objects.equals(email, that.email) &&
          Objects.equals(pass, that.pass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, pass);
    }

    @Override
    public String toString() {
        return "BotCredentials{" +
          "email='" + email + '\'' +
          ", pass='" + pass + '\'' +
          '}';
    }
}
