package core.test;

import core.api.IAdmin;
import core.api.impl.Admin;
import org.junit.Before;
import org.junit.Test;

//import java.lang.*;
import static org.junit.Assert.*;

/**
 * TestAdmin.java is the test class to test the Admin API
 * Sarah Quick
 * ECS161 F17 
 * HW3
 */

public class TestAdmin {
	
	private IAdmin admin;
	
	@Before
    public void setup() {
        this.admin = new Admin();
    }

	/* Tests for createClass() */

    // create class with valid className/year/Instructor pairing; assert class now exists
	@Test
	public void test_createClass_1() {
		this.admin.createClass("Test", 2017, "Instructor", 15);
		assertTrue(this.admin.classExists("Test", 2017));
	} // from ExampleTest.java

	// create class with invalid year; assert new class not created
	@Test
	public void test_createClass_2() {
		this.admin.createClass("Test", 2016, "Instructor", 15);
		assertFalse(this.admin.classExists("Test", 2016));
	} // from ExampleTest.java

	// create class with non-unique className; assert new class not created/Instructor not overwritten
	@Test
	public void test_createClass_3() {
		this.admin.createClass("Test", 2017, "Instructor1", 15);
		this.admin.createClass("Test", 2017, "Instructor2", 15);
		assertFalse(this.admin.getClassInstructor("Test", 2017).equals("Instructor2"));
		//assertFalse(equals(this.admin.getClassInstructor("Test", 2017), "Instructor2"));
		//assertFalse(this.admin.getClassInstructor("Test", 2017) == "Instructor2");
	}

	// create class with instructor that has two courses; assert new class not created
	@Test
	public void test_createClass_4() {
		this.admin.createClass("Test1", 2017, "Instructor", 15);
		this.admin.createClass("Test2", 2017, "Instructor", 15);
		this.admin.createClass("Test3", 2017, "Instructor", 15);
		assertFalse(this.admin.classExists("Test3", 2017));
	}

	// create duplicate class with different capacity; assert capacity not overwritten
	@Test
	public void test_createClass_5() {
		this.admin.createClass("Test", 2017, "Instructor", 15);
		this.admin.createClass("Test", 2017, "Instructor", 20);
		assertFalse(this.admin.getClassCapacity("Test", 2017) == 20);
	}

	// create duplicate class with different year; assert year not overwritten
	@Test
	public void test_createClass_6() {
		this.admin.createClass("Test", 2017, "Instructor", 15);
		this.admin.createClass("Test", 2016, "Instructor", 15);
		assertFalse(this.admin.classExists("Test", 2016));
	}

	// create class with negative capacity; assert class not created
	@Test
	public void test_createClass_7() {
		this.admin.createClass("Test", 2017, "Instructor", -1);
		assertFalse(this.admin.classExists("Test", 2017));
	}







	/* Tests for changeCapacity() */

	// change capacity for existing course to a number equal to number enrolled; assert capacity stayed the same
	@Test
	public void test_changeCapacity_1() {
		this.admin.createClass("Test", 2017, "Instructor", 15);
		this.admin.changeCapacity("Test", 2017, 15);
		assertTrue(this.admin.getClassCapacity("Test", 2017) == 15);
	}

	// change capacity for existing course to a number greater than number enrolled; assert capacity changed
	@Test
	public void test_changeCapacity_2() {
		this.admin.createClass("Test", 2017, "Instructor", 15);
		this.admin.changeCapacity("Test", 2017, 20);
		assertTrue(this.admin.getClassCapacity("Test", 2017) == 20);
	}

	// change capacity for existing course to a number less than number enrolled; assert capacity didn't change
	@Test
	public void test_changeCapacity_3() {
		this.admin.createClass("Test", 2017, "Instructor", 15);
		this.admin.changeCapacity("Test", 2017, 10);
		assertFalse(this.admin.getClassCapacity("Test", 2017) == 10);
	}

	// change capacity for a non-existing course; assert nothing was done (class wasn't created)
	@Test
	public void test_changeCapacity_4() {
		this.admin.changeCapacity("Test", 2017, 15);
		assertFalse(this.admin.classExists("Test", 2017));
	}

	// change with valid capacity and wrong date; make sure it fails and doesn’t overwrite date
	@Test
	public void test_changeCapacity_5() {
		this.admin.createClass("Test", 2017, "Instructor", 15);
		this.admin.changeCapacity("Test", 2016, 20);
		assertTrue(this.admin.getClassCapacity("Test", 2017) == 15);
	}

	// change with valid capacity and wrong date; make sure it fails and doesn't create class
	@Test
	public void test_changeCapacity_6() {
		this.admin.createClass("Test", 2017, "Instructor", 15);
		this.admin.changeCapacity("Test", 2016, 20);
		assertFalse(this.admin.classExists("Test", 2016));
	}






	
}