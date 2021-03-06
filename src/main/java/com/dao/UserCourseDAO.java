package com.dao;

import java.util.UUID;
import java.util.ArrayList;

import com.entity.Course;
import com.util.exception.DAOException;

/**
 * Object interface for UserCourse
 */
public interface UserCourseDAO {

    /**
     * Get user's registered courses
     * @param UUID userId
     * @return ArrayList of courses
     */
    public ArrayList<Course> getRegisteredCourses(UUID userId) throws DAOException;

    /**
     * Register courses
     * @param UUID userId
     * @param ArrayList of courseId to be register
     * @return
     */
    public Boolean registerCourses(UUID userId, ArrayList<UUID> courseId) throws DAOException;

    /**
     * Drop courses
     * @param UUID userId
     * @param ArrayList of courseId to be dropped
     * @return
     */
    public Boolean dropCourses(UUID userId, ArrayList<UUID> courseId) throws DAOException;

}
