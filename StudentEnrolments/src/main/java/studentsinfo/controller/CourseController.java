package studentsinfo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import studentsinfo.entities.Course;
import studentsinfo.repositories.CourseRepo;

@RestController
public class CourseController {

	@Autowired
	private CourseRepo courserepo;

	// 1
	// <---------------add new course---------------------->
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/addcourse")
	@Operation(summary = "Add course", description = "Adding a new courses")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Course added successfully"),
			@ApiResponse(responseCode = "400", description = "Bad request, Course or Code is Already Present"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public Course addNewCourse(@Valid @RequestBody Course newCourse) {
		try {
			List<Course> existingcourses = courserepo.findAll();
			boolean exist = existingcourses.contains(newCourse);
			if (exist)
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course Code or Course Already Present");
			courserepo.save(newCourse);
			return newCourse;
		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
		} catch (Exception ex) {
			throw ex;
		}
	}

	// <---------------delete course---------------------->
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/deletecourse/{code}")
	@Operation(summary = "Delete course", description = "Deleting a course by using its code")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Course deleted successfully"),
			@ApiResponse(responseCode = "404", description = " Course Code Not found"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public void deleteOneCourse(@PathVariable("code") String code) {
		try {
			Optional<Course> course = courserepo.findById(code);
			if (course.isPresent())
				courserepo.deleteById(code);
			else
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course with this code is Not Found!");
		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
		} catch (Exception ex) {
			throw ex;
		}
	}

	// <---------------update course---------------------->
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/updatecourse/{code}")
	@Operation(summary = "Update course", description = "updating a course by using its code")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Course updated successfully"),
			@ApiResponse(responseCode = "404", description = " Course Code Not found"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public void deleteOneCourse(@PathVariable("code") String code, @RequestBody Course newcourse) {
		try {
			Optional<Course> optCourse = courserepo.findById(code);
			if (optCourse.isPresent()) {
				Course c = optCourse.get();
				c.setCode(newcourse.getCode());
				c.setName(newcourse.getName());
				c.setDuration(newcourse.getDuration());
				c.setFee(newcourse.getFee());
				courserepo.save(c);
			} else
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course code Not Found!");
		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
		} catch (Exception ex) {
			throw ex;
		}
	}

	// 5
	// <---------------All Courses---------------------->
	@GetMapping("/courses")
	@Operation(summary = "All courses", description = "list of all courses")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "List of Courses"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public List<Course> getCourse() {
		return courserepo.findAll();
	}

}
