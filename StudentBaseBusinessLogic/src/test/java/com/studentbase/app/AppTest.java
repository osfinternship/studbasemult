package com.studentbase.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.studentbase.app.entity.Grade;
import com.studentbase.app.entity.Student;

/**
 * Unit test for simple App.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppTest {

	//logger
	final static Logger LOG = Logger.getLogger(AppTest.class);

	static Session session;
		
	@Before
	public void beforeTest() {
		session = HibernateUtil.getSessionFactory().openSession();
	}
	
	@After
	public void afterTest() {
		session.close();
	}
	
	@Test
	public void test1Save() {
		Student actual = createStudent();
		
		try {
			session.save(actual);
		
			session.beginTransaction().commit();
			LOG.info("Entity saved into db");
			
			assertNotNull(actual.getId());

		} catch (HibernateException e) {
			LOG.error("Hibernate error: " + e);
		}
	}

	@Test
	public void test2Update() {
		Student actual = studentList().get(0);
		Student actualCopy = actual;
		
		LOG.info("Actual student: " + actual);
		actual.setName("Bill");
		actual.setSurname("Hakins");
		
		session.beginTransaction().commit();
		
		LOG.info("After update: " + session.get(Student.class, actual.getId()));
		
		assertEquals(actual, actualCopy);
	}

	@Test
	public void test3Delete() {
		Student newStudent = createStudent();
		
		session.save(newStudent);
		LOG.info("New student: " + newStudent);
		
		session.delete(newStudent);
		LOG.info("Student deleted: " + newStudent);
		
		session.beginTransaction().commit();

		assertFalse(session.contains(newStudent));
	}
	
	@Test
	public void test4GetById() {
		Student student;
		
		student = (Student) session.get(Student.class, studentList().get(0).getId());
		
		assertNotNull(student);
	}
	
	@Test
	public void test5List() {
		List<Student> students = studentList();
		LOG.info("List of students: " + students);
		
		assertNotNull(students);
	}
	
	/**
	 * New student instance
	 * @return
	 */
	private Student createStudent() {
		Student student = new Student();
		student.setName("John");
		student.setSurname("Lock");
		student.setBirthDate(new Date());
		
		Grade grade = new Grade();
		grade.setName("Math");
		grade.setGrade1(5.0d);
		grade.setGrade2(7.3d);
		
		double avg = (grade.getGrade1() + grade.getGrade2()) / 2;
		
		grade.setAverage(avg);
		
		student.getGrades().add(grade);
		
		return student;
	}

	/**
	 * List of students from db
	 * @return
	 */
	private List<Student> studentList() {
		return session.createCriteria(Student.class).list();
	}
}
