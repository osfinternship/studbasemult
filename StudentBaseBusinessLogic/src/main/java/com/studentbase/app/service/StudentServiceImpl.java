package com.studentbase.app.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.studentbase.app.HibernateUtil;
import com.studentbase.app.entity.Student;

public class StudentServiceImpl implements StudentService{

	//logger
	final static Logger LOG = Logger.getLogger(StudentServiceImpl.class);

	private static Session session = HibernateUtil.getSessionFactory().openSession();

	@Override
	public void create(Student student) {
		try {
			session.save(student);
		
			session.beginTransaction().commit();
			LOG.info("Entity saved into db");
			
		} catch (HibernateException e) {
			LOG.error("Hibernate error: " + e);
		}

	}

	@Override
	public boolean getBySurname(String surname) {
		String hql = "from Student student WHERE student.surname = '" + surname + "'";
		Query query = session.createQuery(hql);
		List results = query.list();
		
		LOG.info("Get by surname: " + results);
		if(results.size() != 0) {
			return true;
		}
		
		return false;
	}

	@Override
	public List<Student> getAllStudents() {
		LOG.info("Get all students");
		return session.createCriteria(Student.class).list();
	}

}
