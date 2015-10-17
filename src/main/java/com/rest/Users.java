package com.rest;

import java.util.*;
import java.util.UUID;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.entity.Major;
import com.entity.Course;
import com.entity.User;
import com.dao.MajorDAO;
import com.dao.CourseDAO;
import com.dao.UserDAO;

/**
 * Java class handle all API call related to users
 */

@Path("/users")
public class Users {

    @Autowired
    private UserDAO userDAO;

    /**
     * Function to get all users existing in DB
     * @return ArrayList of user object
     */
    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<User> getAll() {
        ArrayList<User> userList = new ArrayList<User>();
        try {
            userList = userDAO.getAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }

    /**
     * Function to get User by on userId
     * @return User object
     */
    @GET
    @Path("/id/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUserById(@NotNull @PathParam("userId") final String userId) {
        User user = null;
        try {
            user = userDAO.getById(UUID.fromString(userId));
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return user;
    }

}