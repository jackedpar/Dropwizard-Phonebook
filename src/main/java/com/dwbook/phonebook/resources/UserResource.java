package com.dwbook.phonebook.resources;

import com.dwbook.phonebook.dao.UserDAO;
import com.dwbook.phonebook.representations.User;
import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import io.dropwizard.auth.Auth;
import org.skife.jdbi.v2.DBI;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Set;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    private final UserDAO userDAO;
    private final Validator validator;

    public UserResource(DBI jdbi, Validator validator) {
        userDAO = jdbi.onDemand(UserDAO.class);
        this.validator = validator;
    }

    @GET
    @Path("/{username}")
    public Response getUser(@PathParam("username") String username, @Auth Boolean isAuthenticated) {
        // retrieve information about the user with the provided username
        User user = userDAO.getUserByUsername(username);
        return Response
                .ok(user)
                .build();
    }

    @POST
    public Response createUser(User user, @Auth Boolean isAuthenticated) throws URISyntaxException {
        // Validate the contact's data
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        // Are there any constraint violations?
        if (violations.size() > 0) {
            // Validation errors occurred
            ArrayList<String> validationMessages = new ArrayList<String>();
            for (ConstraintViolation<User> violation : violations) {
                validationMessages.add(violation.getPropertyPath().toString() + ": " + violation.getMessage());
            }
            return Response
                    .status(Status.BAD_REQUEST)
                    .entity(validationMessages)
                    .build();
        } else {
            // OK, no validation errors
            // Store the new user

            HashFunction hf = Hashing.md5();
            HashCode hc = hf.newHasher()
                    .putString(user.getPassword(), Charsets.UTF_8)
                    .hash();
            userDAO.createUser(user.getUsername(), hc.toString());
            return Response.created(new URI(String.valueOf(user.getUsername()))).build();
        }
    }

}
