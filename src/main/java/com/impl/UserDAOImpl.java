package com.impl;

import java.util.*;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.criterion.*;
import org.hibernate.Criteria;
import org.hibernate.sql.JoinType;

import com.dao.UserDAO;
import com.entity.Major;
import com.entity.Course;
import com.entity.User;
import com.entity.Role;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Detail implementations for user related
 */
@Repository("UserDAO")
public class UserDAOImpl extends BaseDAOImpl implements UserDAO {

    private SessionFactory sessionFactory;

    /**
     * Constructor class
     * @param SessionFactory object sessionFactory
     */
    public UserDAOImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    /**
     * Function to get all exisiting user
     * @return Arraylist of User object
     */
    public ArrayList<User> getAll() {
        Criteria cr = this.createCriteria(User.class, "user", false)
                        .createAlias("major", "major", JoinType.INNER_JOIN)
                        .createAlias("role", "role", JoinType.INNER_JOIN);
        List<User> list = cr.list();
        return generateUserList(list);
    }

    /**
     * Function to get user by userId
     * @param UUID userId
     * @return User object
     */
    public User getById(UUID userId) {
        Criteria cr = this.createCriteria(User.class, "user", false)
                        .createAlias("major", "major", JoinType.INNER_JOIN)
                        .createAlias("role", "role", JoinType.INNER_JOIN)
                        .add(Restrictions.eq("user.userId", userId));
        User user = (User) cr.uniqueResult();
        return user;
    }

    /**
     * Function to get user by email and password
     * @param String email
     * @param String pwd
     * @return User object
     */
    public User getByEmail(String email, String pwd) {
        User user = new User();
        return user;
    }

    /**
     * Function to update exisiting user
     * @param User user object
     * @return Boolean value for SUCCESS/FAILURE
     */
    public boolean updateUser(User user) {
        return false;
    }

    /**
     * Function to create user
     * @param User user object
     * @return Boolean value for SUCCESS/FAILURE
     */
    public boolean createUser(String email, String firstName, String lastName, String pwd, int majorId, int roleId) {
        try {
            Major major = (Major) this.get(Major.class, majorId);
            Role role = (Role) this.get(Role.class, roleId);
            UUID userId = UUID.randomUUID();
            User user = new User();
            user.setUserId(userId);
            user.setEmail("kdao@test.com");
            user.setPassword("testing123");
            user.setLastName("Dao");
            user.setFirstName("Khanh");
            user.setMajor(major);
            user.setRole(role);
            this.save(user, userId);
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Function to delete user by userId
     * @param String userId
     * @return Boolen value for SUCCESS/FAILURE
     */
    public boolean deleteUser(String userId) {
        return false;
    }

    /**
     * Helper function to generate array list of users based on result query
     * @param List of user
     * @return ArrayList of user object
     */
    private ArrayList<User> generateUserList(List<User> list) {
        ArrayList<User> userList = new ArrayList<User>();
        if (list == null || list.size() == 0) { //Don't do anything
        } else {
            for (User obj : list) {
                userList.add((User) obj);
            }
        }
        return userList;
    }


}