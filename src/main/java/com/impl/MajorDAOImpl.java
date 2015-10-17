package com.impl;

import java.util.*;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.criterion.*;
import org.hibernate.Criteria;

import com.dao.MajorDAO;
import com.entity.Major;

@Repository("Majors")
public class MajorDAOImpl implements MajorDAO {
    private SessionFactory sessionFactory;

    public MajorDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public ArrayList<Major> getMajors() {
        List<Major> list = sessionFactory.getCurrentSession().createCriteria(Major.class).list();
        ArrayList<Major> majorList = new ArrayList<Major>();
        if (list == null || list.size() == 0) { //Don't do anything
        } else {
            for (Major o : list) {
                majorList.add(new Major(o.getMajorId(), o.getMajorName()));
            }
        }
        return majorList;
    }
}