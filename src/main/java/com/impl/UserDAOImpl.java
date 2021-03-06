package com.impl;

import java.util.*;

import com.util.exception.DAOException;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import org.hibernate.criterion.*;
import org.hibernate.Criteria;
import org.hibernate.sql.JoinType;
import java.sql.*;

import com.util.security.Crypto;
import com.dao.UserDAO;
import com.entity.Major;
import com.entity.User;
import com.entity.Role;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Detail implementations for user related
 */
@Repository("UserDAO")
public class UserDAOImpl extends BaseDAOImpl implements UserDAO {

    /**
     * Constructor class
     * @param SessionFactory object sessionFactory
     */
    public UserDAOImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    /**
     * Function to get all existing user
     * @return Arraylist of User object (sorted by lastname)
     */
    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly=true, rollbackFor=Exception.class)
    public ArrayList<User> getAll(int pageNumber, int pageSize) throws DAOException {
        try {
            Criteria cr = this.createCriteria(User.class, "user", false)
                    .createAlias("major", "major", JoinType.INNER_JOIN)
                    .createAlias("role", "role", JoinType.INNER_JOIN)
                    .addOrder(Order.asc("lastName"));
            cr.setFirstResult((pageNumber - 1) * pageSize);
            cr.setMaxResults(pageSize);
            return generateUserList(cr.list());
        } catch(Exception e) {
            String msg = String.format("Error getting all users, Message : %s", e.getMessage());
            this.getLogger().error(msg);
            throw new DAOException(msg);
        }
    }

    /**
     * Function to get user by userId
     * @param UUID userId
     * @return User object
     */
    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly=true, rollbackFor=Exception.class)
    public User getById(UUID userId) throws DAOException {
        try {
            Criteria cr = this.createCriteria(User.class, "user", false)
                    .createAlias("major", "major", JoinType.INNER_JOIN)
                    .createAlias("role", "role", JoinType.INNER_JOIN)
                    .add(Restrictions.eq("user.userId", userId));
            return (User) cr.uniqueResult();
        } catch(Exception e) {
            String msg = String.format("Error getting user, userId=%s, Message : %s", userId.toString(), e.getMessage());
            this.getLogger().error(msg);
            throw new DAOException(msg);
        }
    }

    /**
     * Function to get user by email and password
     * @param String email
     * @param String pwd
     * @return User object
     */
    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly=true, rollbackFor=Exception.class)
    public User getByEmail(String email, String pwd) throws DAOException {
        try {
            Criteria cr = this.createCriteria(User.class, "user", false)
                    .createAlias("major", "major", JoinType.INNER_JOIN)
                    .createAlias("role", "role", JoinType.INNER_JOIN)
                    .add(Restrictions.eq("user.email", email))
                    .add(Restrictions.eq("user.encryptedPassword", Crypto.encrypt(pwd)));
            return (User) cr.uniqueResult();
        } catch(Exception e) {
            String msg = String.format("Error getting user, userEmail=%s, Message : %s", email, e.getMessage());
            this.getLogger().error(msg);
            throw new DAOException(msg);
        }
    }

    /**
     * Function to update exisiting user
     *
     * @param UUID userId
     * @param String firstName
     * @param String lastName
     * @param String pwd
     * @param int majorId
     * @param int roleId
     *
     * NOTE: update will ignore email field so, if user try to manipulate it, they can't
     *       This eventually will be handled in UI, but REST making sure this value will not be updated
     *
     * @return Boolean value for SUCCESS/FAILURE
     */
    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor=Exception.class)
    public boolean updateUser(UUID userId, String pwd, String firstName, String lastName, int roleId, int majorId) throws DAOException {
        try {
            Major major = (Major) this.get(Major.class, majorId);
            Role role = (Role) this.get(Role.class, roleId);
            User user = (User) this.get(User.class, userId);
            //User(UUID userId, String email, String password, String firstName, String lastName)
            if (pwd.equals("") || pwd.equals("default")) {
                //Don't update password
            } else {
                user.setPassword(pwd);
            }
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setMajor(major);
            user.setRole(role);
            this.save(user, userId);
            return true;
        } catch(Exception e) {
            String msg = String.format("Error update user, userId=%s, Message : %s", userId.toString(), e.getMessage());
            this.getLogger().error(msg);
            throw new DAOException(msg);
        }
    }

    /**
     * Function to create user
     *
     * @param String email
     * @param String firstName
     * @param String lastName
     * @param String pwd
     * @param int majorId
     * @param int roleId
     *
     * @return Boolean value for SUCCESS/FAILURE
     */
    @SuppressWarnings("unchecked")
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor=Exception.class)
    public UUID createUser(String email, String pwd, String firstName, String lastName, int roleId, int majorId) throws DAOException {
        try {
            Major major = (Major) this.get(Major.class, majorId);
            Role role = (Role) this.get(Role.class, roleId);
            UUID userId = UUID.randomUUID(); //Generate random uuid
            //User(UUID userId, String email, String password, String firstName, String lastName)
            User user = new User(userId, email, pwd, firstName, lastName);
            user.setMajor(major);
            user.setRole(role);
            this.save(user, userId);
            return userId;
        } catch (Exception e) {
            String msg = String.format("Error create user, userEmail=%s, Message : %s", email, e.getMessage());
            this.getLogger().error(msg);
            throw new DAOException(msg);
        }
    }

    /**
     * Function to delete user by userId
     * @param UUID userId
     * @return Boolean value for SUCCESS/FAILURE
     */
    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor=Exception.class)
    public boolean deleteUser(UUID userId) throws DAOException {
        try {
            boolean result = this.deleteById(User.class, userId);
            return result;
        } catch(Exception e) {
            String msg = String.format("Error delete user, userId=%s, Message : %s", userId.toString(), e.getMessage());
            this.getLogger().error(msg);
            throw new DAOException(msg);
        }
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