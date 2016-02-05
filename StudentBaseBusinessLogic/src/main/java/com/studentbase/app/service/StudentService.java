package com.studentbase.app.service;

import java.util.List;

import org.hibernate.Session;

import com.studentbase.app.entity.Student;

public interface StudentService {	
	void create(Student student);
	boolean getBySurname(String surname);
	List<Student> getAllStudents();
}
