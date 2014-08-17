package com.dwbook.phonebook;

import com.dwbook.phonebook.representations.User;
import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.skife.jdbi.v2.DBI;
import com.dwbook.phonebook.dao.UserDAO;
import com.google.common.base.Optional;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

public class PhonebookAuthenticator implements Authenticator<BasicCredentials, Boolean> {

    private final UserDAO userDao;

    public PhonebookAuthenticator(DBI jdbi) {
        userDao = jdbi.onDemand(UserDAO.class);
    }

    public Optional<Boolean> authenticate(BasicCredentials c) throws AuthenticationException {
        HashFunction hf = Hashing.md5();
        HashCode hc = hf.newHasher()
                .putString(c.getPassword(), Charsets.UTF_8)
                .hash();
        User user = userDao.getUserByUsername(c.getUsername());

        boolean validUser = hc.toString().equals(user.getPassword());
        if (validUser) {
            return Optional.of(true);
        }
        return Optional.absent();
    }
}
