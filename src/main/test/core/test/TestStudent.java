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
 * TestStudent.java is the test class to test the Student API
 * Sarah Quick
 * ECS161 F17 
 * HW3
 */

public class TestStudent {

	private IAdmin admin;
	private IInstructor instructor;
	private IStudent student;

	@Before
    public void setup() {
    	this.admin = new Admin();
        this.instructor = new Instructor();
        this.student = new Student();
    }

	/* Tests for registerForClass() */

	// enroll student in existing class that has not met enrollment capacity; assert student is in class
	@Test
	public void test_registerForClass_1() {
		this.admin.createClass("Test", 2017, "Instructor", 15);
		this.student.registerForClass("Student", "Test", 2017);
		assertTrue(this.student.isRegisteredFor("Student","Test", 2017));
	}

	// enroll student in nonexistent class; make sure fails
	@Test
	public void test_registerForClass_2() {
		this.student.registerForClass("Student", "Test", 2017);
		assertFalse(this.student.isRegisteredFor("Student","Test", 2017));
	}

	// enroll student in class with no space; make sure fails
	@Test
	public void test_registerForClass_3() {
		this.admin.createClass("Test", 2017, "Instructor", 5);
		this.student.registerForClass("Student1", "Test", 2017);
		this.student.registerForClass("Student2", "Test", 2017);
		this.student.registerForClass("Student3", "Test", 2017);
		this.student.registerForClass("Student4", "Test", 2017);
		this.student.registerForClass("Student5", "Test", 2017);
		this.student.registerForClass("Student6", "Test", 2017);
		assertFalse(this.student.isRegisteredFor("Student6","Test", 2017));
	}

	// enroll student in a class that has passed; make sure fails
	@Test
	public void test_registerForClass_4() {
		this.admin.createClass("Test", 2016, "Instructor", 5);
		this.student.registerForClass("Student", "Test", 2016);
		assertFalse(this.student.isRegisteredFor("Student","Test", 2016));
	}

	// make sure invalid year doesn't overwrite class
	@Test
	public void test_registerForClass_5() {
		this.admin.createClass("Test", 2017, "Instructor", 15);
		this.student.registerForClass("Student", "Test", 2016);
		assertTrue(this.admin.classExists("Test", 2017));
	}

	// make sure wrong class doesn't overwrite class
	@Test
	public void test_registerForClass_6() {
		this.admin.createClass("Test", 2017, "Instructor", 15);
		this.student.registerForClass("Student", "Test2", 2017);
		assertTrue(this.admin.classExists("Test", 2017));
		
	}


	
	
	
	/* Tests for dropClass() */

    // drop existing student in existing current class; assert student is not registered
	@Test
	public void test_dropClass_1() {
		this.admin.createClass("Test", 2017, "Instructor", 15);
		this.student.registerForClass("Student", "Test", 2017);
		this.student.dropClass("Student", "Test", 2017);
		assertFalse(this.student.isRegisteredFor("Student", "Test", 2017));
	}

    // drop nonexistent student from class; assert student not registered
	@Test
	public void test_dropClass_2() {
		this.admin.createClass("Test", 2017, "Instructor", 15);
		this.student.dropClass("Student", "Test", 2017);
		assertFalse(this.student.isRegisteredFor("Student", "Test", 2017));
	}

    // drop existing student from class that is over; assert student still registered
	@Test
	public void test_dropClass_3() {
		this.admin.createClass("Test", 2016, "Instructor", 15);
		this.student.registerForClass("Student", "Test", 2016);
		this.student.dropClass("Student", "Test", 2016);
		assertTrue(this.student.isRegisteredFor("Student", "Test", 2016));
	}

    // drop student from nonexistent class; assert class not created
	@Test
	public void test_dropClass_4() {
		this.student.registerForClass("Student", "Test", 2017);
		this.student.dropClass("Student", "Test", 2017);
		assertFalse(this.admin.classExists("Test", 2017));
	}

	// drop student from nonexistent class; assert student not registered
	@Test
	public void test_dropClass_5() {
		this.student.registerForClass("Student", "Test", 2017);
		this.student.dropClass("Student", "Test", 2017);
		assertFalse(this.student.isRegisteredFor("Student", "Test", 2017));
	}

    // make sure wrong year doesn’t overwrite year
	@Test
	public void test_dropClass_6() {
		this.admin.createClass("Test", 2017, "Instructor", 15);
		this.student.registerForClass("Student", "Test", 2017);
		this.student.dropClass("Student", "Test", 2016);
		assertTrue(this.admin.classExists("Test", 2017));
	}


	
	
	
	
	/* Tests for submitHomework() */

	// submit hw for valid student/hw/class combo; assert hw was submitted
	@Test
	public void test_submitHomework_1() {
		this.admin.createClass("Test", 2017, "Instructor", 15);
		this.student.registerForClass("Student", "Test", 2017);
		this.instructor.addHomework("Instructor", "Test", 2017, "HW");
		this.student.submitHomework("Student", "HW", "Answer", "Test", 2017);
		assertTrue(this.student.hasSubmitted("Student", "HW", "Test", 2017));

	}

	// submit invalid hw for valid student, valid class, valid year
	@Test
	public void test_submitHomework_2() {
		this.admin.createClass("Test", 2017, "Instructor", 15);
		this.student.registerForClass("Student", "Test", 2017);
		this.student.submitHomework("Student", "HW1", "Answer", "Test", 2017);
		assertFalse(this.student.hasSubmitted("Student", "HW1", "Test", 2017));
	}

	// submit valid hw for invalid student, valid class, valid year
	@Test
	public void test_submitHomework_3() {
		this.admin.createClass("Test", 2017, "Instructor", 15);
		this.instructor.addHomework("Instructor", "Test", 2017, "HW");
		this.student.submitHomework("Student", "HW", "Answer", "Test", 2017);
		assertFalse(this.student.hasSubmitted("Student", "HW", "Test", 2017));
	}

	// submit valid hw for valid student, invalid class, valid year
	@Test
	public void test_submitHomework_4() {
		this.admin.createClass("Test", 2017, "Instructor", 15);
		this.student.registerForClass("Student", "Test", 2017);
		this.instructor.addHomework("Instructor", "Test", 2017, "HW");
		this.student.submitHomework("Student", "HW", "Answer", "Test2", 2017);
		assertFalse(this.student.hasSubmitted("Student", "HW", "Test2", 2017));
	}

	// submit valid hw for valid student, valid class, invalid year
	@Test
	public void test_submitHomework_5() {
		this.admin.createClass("Test", 2016, "Instructor", 15);
		this.student.registerForClass("Student", "Test", 2016);
		this.instructor.addHomework("Instructor", "Test", 2016, "HW");
		this.student.submitHomework("Student", "HW", "Answer", "Test", 2016);
		assertFalse(this.student.hasSubmitted("Student", "HW", "Test", 2016));
	}

}