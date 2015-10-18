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

    /**
     * Get all courses available in the databases
     * @return ArrayList of Course object
     */
    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<Course> getAllCourses(@DefaultValue("courseNumber") @QueryParam("sortBy") final String sortBy) {
        ArrayList<Course> courseList = new ArrayList<Course>();
        try {
            courseList = courseDAO.getAll(sortBy);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return courseList;
    }

    /**
     * Get array list of course which have similar as requested coursename
     * @return ArrayList of Course object
     */
    @GET
    @Path("/courseName/{courseName}")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<Course> getCoursesByName(@NotNull @PathParam("courseName") final String courseName) {
        ArrayList<Course> courseList = new ArrayList<Course>();
        try {
            courseList = courseDAO.getCoursesByName(courseName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return courseList;
    }

    /**
     * Get array list of courses based on scheduleId
     * @param int scheduleId
     * @return Array list of Course object
     */
    @GET
    @Path("/schedule/{scheduleId}")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<Course> getCoursesByScheduleId(@NotNull @PathParam("scheduleId") final int scheduleId) {
        ArrayList<Course> courseList = new ArrayList<Course>();
        try {
            courseList = courseDAO.getBySchedule(scheduleId);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return courseList;
    }

    /**
     * Get specific course based on courseNumber or courseName
     * @param courseNumber
     * @param courseName
     * @return
     * @throws Exception
     */
    @GET
    @Path("/course")
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