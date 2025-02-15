package com.markvarga21.studentmanager;

import com.markvarga21.studentmanager.util.Generated;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * A class holding the entrypoint of the application.
 */
@SpringBootApplication
@Generated
@EnableCaching
public class StudentManagerApplication {
	/**
	 * The entrypoint of the application.
	 *
	 * @param args The arguments passed to the application.
	 */
	public static void main(final String[] args) {
		SpringApplication.run(StudentManagerApplication.class, args);
	}
}
