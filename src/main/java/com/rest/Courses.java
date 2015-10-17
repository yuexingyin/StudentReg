package com.rest;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import com.entity.Course;
import com.dao.CourseDAO;

/**
 * Java class handle all API call related to courses
 */

@Path("/courses")
public class Courses {

    @Autowired
    private CourseDAO courseDAO;

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<Course> getAllCourses() {
        ArrayList<Course> courseList = new ArrayList<Course>();
        try {
            courseList = courseDAO.getAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return courseList;
    }

    /**
     * Get course based on courseNumber or courseName
     * @param courseNumber
     * @param courseName
     * @return
     * @throws Exception
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Course getSpecificCourse(@QueryParam("id") Integer courseNumber,
                                    @QueryParam("name") String courseName,
                                    @DefaultValue("courseName") @QueryParam ("type") String type) throws Exception {
        Course course = null;
        try {
            if (type.equals("courseNumber")) {
                course = courseDAO.getByNumber(courseNumber);
            } else { //Search by course name
                course = courseDAO.getByName(courseName);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return course;
    }
}