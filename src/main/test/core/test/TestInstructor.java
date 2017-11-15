package core.test;

import core.api.IAdmin;
import core.api.impl.Admin;
import core.api.IInstructor;
import core.api.impl.Instructor;
import core.api.IStudent;
import core.api.impl.Student;
import org.junit.Before;
import org.junit.Test;

//import java.lang.*;
import static org.junit.Assert.*;

/**
 * TestInstructor.java is the test class to test the Instructor API
 * Sarah Quick
 * ECS161 F17 
 * HW3
 */

public class TestInstructor {

	private IAdmin admin;
	private IInstructor instructor;
	private IStudent student;

	@Before
    public void setup() {
    	this.admin = new Admin();
        this.instructor = new Instructor();
        this.student = new Student();
    }

	/* Tests for addHomework() */

	// add hw to valid current class by instructor that is assigned to course; assert hw exists
	@Test
	public void test_addHomework_1() {
		this.admin.createClass("Test", 2017, "Instructor", 15);
		this.instructor.addHomework("Instructor", "Test", 2017, "HW");
		assertTrue(this.instructor.homeworkExists("Test", 2017, "HW"));
	}

	// add hw to valid current class by instructor that is not assigned to course; assert hw dne
	@Test
	public void test_addHomework_2() {
		this.admin.createClass("Test", 2017, "Instructor", 15);
		this.instructor.addHomework("Instructor2", "Test", 2017, "HW");
		assertFalse(this.instructor.homeworkExists("Test", 2017, "HW"));
	}

	// add hw to valid current class by instructor that is not assigned to course; assert instructor not changed
	@Test
	public void test_addHomework_3() {
		this.admin.createClass("Test", 2017, "Instructor", 15);
		this.instructor.addHomework("Instructor2", "Test", 2017, "HW");
		assertTrue(this.admin.getClassInstructor("Test", 2017).equals("Instructor"));
		//assertTrue(equals(this.admin.getClassInstructor("Test", 2017), "Instructor"));
	}

	// add hw to class that does not exist; assert hw was not created
	@Test
	public void test_addHomework_4() {
		this.instructor.addHomework("Instructor", "Test", 2017, "HW");
		assertFalse(this.instructor.homeworkExists("Test", 2017, "HW"));
	}

	// add hw to class that does not exist; assert class was not created
	@Test
	public void test_addHomework_5() {
		this.instructor.addHomework("Instructor", "Test", 2017, "HW");
		assertFalse(this.admin.classExists("Test", 2017));
	}


	/* Tests for assignGrade() */

	// assign grade to valid class/instructor/student combo; assert grade assigned
	@Test
	public void test_assignGrade_1() {
		this.admin.createClass("Test", 2017, "Instructor", 15);
		this.student.registerForClass("Student", "Test", 2017);
		this.instructor.addHomework("Instructor", "Test", 2017, "HW");
		this.student.submitHomework("Student", "HW", "Answer", "Test", 2017);
		this.instructor.assignGrade("Instructor", "Test", 2017, "HW", "Student", 95);
		assertTrue(this.instructor.getGrade("Test", 2017, "HW", "Student") == 95);
	}

	// assign grade where class doesn’t exist; assert grade dne
	@Test
	public void test_assignGrade_2() {
		this.instructor.assignGrade("Instructor", "Test2", 2017, "HW", "Student", 95);
		assertTrue(this.instructor.getGrade("Test2", 2017, "HW", "Student") == null);
	}

	// assign grade where instructor is not assigned to course; make sure it fails
	@Test
	public void test_assignGrade_3() {
		this.admin.createClass("Test", 2017, "Instructor", 15);
		this.student.registerForClass("Student", "Test", 2017);
		this.instructor.addHomework("Instructor", "Test", 2017, "HW");
		this.student.submitHomework("Student", "HW", "Answer", "Test", 2017);
		this.instructor.assignGrade("Instructor2", "Test", 2017, "HW", "Student", 95);
		assertTrue(this.instructor.getGrade("Test", 2017, "HW", "Student") == null);
	}

	// assign grade where student has not submitted homework; assert grade not assigned
	@Test
	public void test_assignGrade_4() {
		this.admin.createClass("Test", 2017, "Instructor", 15);
		this.student.registerForClass("Student", "Test", 2017);
		this.instructor.addHomework("Instructor", "Test", 2017, "HW");
		this.instructor.assignGrade("Instructor", "Test", 2017, "HW", "Student", 95);
		assertTrue(this.instructor.getGrade("Test", 2017, "HW", "Student") == null);
	}

	// assign grade that is a negative percentage; make sure it fails
	@Test
	public void test_assignGrade_5() {
		this.admin.createClass("Test", 2017, "Instructor", 15);
		this.student.registerForClass("Student", "Test", 2017);
		this.instructor.addHomework("Instructor", "Test", 2017, "HW");
		this.student.submitHomework("Student", "HW", "Answer", "Test", 2017);
		this.instructor.assignGrade("Instructor", "Test", 2017, "HW", "Student", -50);
		assertTrue(this.instructor.getGrade("Test", 2017, "HW", "Student") == null);
	}

	// assign grade that is a 100+ percentage; make sure it fails
	@Test
	public void test_assignGrade_6() {
		this.admin.createClass("Test", 2017, "Instructor", 15);
		this.student.registerForClass("Student", "Test", 2017);
		this.instructor.addHomework("Instructor", "Test", 2017, "HW");
		this.student.submitHomework("Student", "HW", "Answer", "Test", 2017);
		this.instructor.assignGrade("Instructor", "Test", 2017, "HW", "Student", 200);
		assertTrue(this.instructor.getGrade("Test", 2017, "HW", "Student") == null);
	}

	// make sure wrong instructor does not overwrite instructor
	@Test
	public void test_assignGrade_7() {
		this.admin.createClass("Test", 2017, "Instructor", 15);
		this.student.registerForClass("Student", "Test", 2017);
		this.instructor.addHomework("Instructor", "Test", 2017, "HW");
		this.student.submitHomework("Student", "HW", "Answer", "Test", 2017);
		this.instructor.assignGrade("Instructor2", "Test", 2017, "HW", "Student", 95);
		assertTrue(this.admin.getClassInstructor("Test", 2017).equals("Instructor"));
		//assertTrue(equals(this.admin.getClassInstructor("Test", 2017), "Instructor"));
	}

	// make sure wrong instructor does not create new class
	@Test
	public void test_assignGrade_8() {
		this.admin.createClass("Test", 2017, "Instructor", 15);
		this.student.registerForClass("Student", "Test", 2017);
		this.instructor.addHomework("Instructor", "Test", 2017, "HW");
		this.student.submitHomework("Student", "HW", "Answer", "Test", 2017);
		this.instructor.assignGrade("Instructor2", "Test", 2017, "HW", "Student", 95);
		assertFalse(this.admin.getClassInstructor("Test", 2017).equals("Instructor2"));
		//assertFalse(equals(this.admin.getClassInstructor("Test", 2017), "Instructor2"));
	}

	// make sure wrong class does not overwrite class
	@Test
	public void test_assignGrade_9() {
		this.admin.createClass("Test", 2017, "Instructor", 15);
		this.student.registerForClass("Student", "Test", 2017);
		this.instructor.addHomework("Instructor", "Test", 2017, "HW");
		this.student.submitHomework("Student", "HW", "Answer", "Test", 2017);
		this.instructor.assignGrade("Instructor", "Test2", 2017, "HW", "Student", 95);
		assertTrue(this.admin.classExists("Test", 2017));
	}

	// make sure wrong class does not create new class
	@Test
	public void test_assignGrade_10() {
		this.admin.createClass("Test", 2017, "Instructor", 15);
		this.student.registerForClass("Student", "Test", 2017);
		this.instructor.addHomework("Instructor", "Test", 2017, "HW");
		this.student.submitHomework("Student", "HW", "Answer", "Test", 2017);
		this.instructor.assignGrade("Instructor", "Test2", 2017, "HW", "Student", 95);
		assertFalse(this.admin.classExists("Test2", 2017));
	}

	// grade hw that dne; assert grade dne
	@Test
	public void test_assignGrade_11() {
		this.admin.createClass("Test", 2017, "Instructor", 15);
		this.student.registerForClass("Student", "Test", 2017);
		this.instructor.addHomework("Instructor", "Test", 2017, "HW");
		this.student.submitHomework("Student", "HW", "Answer", "Test", 2017);
		this.instructor.assignGrade("Instructor", "Test", 2017, "HW2", "Student", 95);
		assertFalse(this.instructor.getGrade("Test", 2017, "HW2", "Student") == null);
	}

	// make sure wrong assignemnt does not create new assignment
	@Test
	public void test_assignGrade_12() {
		this.admin.createClass("Test", 2017, "Instructor", 15);
		this.student.registerForClass("Student", "Test", 2017);
		this.instructor.addHomework("Instructor", "Test", 2017, "HW");
		this.student.submitHomework("Student", "HW", "Answer", "Test", 2017);
		this.instructor.assignGrade("Instructor", "Test", 2017, "HW2", "Student", 95);
		assertFalse(this.instructor.homeworkExists("Test", 2017, "HW2"));
	}

	// make sure wrong student does not overwrite student
	@Test
	public void test_assignGrade_13() {
		this.admin.createClass("Test", 2017, "Instructor", 15);
		this.student.registerForClass("Student", "Test", 2017);
		this.instructor.addHomework("Instructor", "Test", 2017, "HW");
		this.student.submitHomework("Student", "HW", "Answer", "Test", 2017);
		this.instructor.assignGrade("Instructor", "Test", 2017, "HW", "Student2", 95);
		assertTrue(this.student.isRegisteredFor("Student", "Test", 2017));
	}

	// make sure wrong student does not create new student
	@Test
	public void test_assignGrade_14() {
		this.admin.createClass("Test", 2017, "Instructor", 15);
		this.student.registerForClass("Student", "Test", 2017);
		this.instructor.addHomework("Instructor", "Test", 2017, "HW");
		this.student.submitHomework("Student", "HW", "Answer", "Test", 2017);
		this.instructor.assignGrade("Instructor", "Test", 2017, "HW", "Student", 95);
		assertFalse(this.student.isRegisteredFor("Student2", "Test", 2017));
		
	}

	// make sure you can reassign grade; assert new grade is assgnied
	@Test
	public void test_assignGrade_15() {
		this.admin.createClass("Test", 2017, "Instructor", 15);
		this.student.registerForClass("Student", "Test", 2017);
		this.instructor.addHomework("Instructor", "Test", 2017, "HW");
		this.student.submitHomework("Student", "HW", "Answer", "Test", 2017);
		this.instructor.assignGrade("Instructor", "Test", 2017, "HW", "Student", 95);
		this.instructor.assignGrade("Instructor", "Test", 2017, "HW", "Student", 100);
		assertTrue(this.instructor.getGrade("Test", 2017, "HW", "Student") == 100);
	}




}