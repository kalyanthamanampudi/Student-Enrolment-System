package studentsinfo.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import studentsinfo.entities.Payment;
import studentsinfo.entities.Student;
import studentsinfo.entities.StudentAndPayment;
import studentsinfo.repositories.PaymentRepo;
import studentsinfo.repositories.StudentRepo;

@RestController
public class StudentController {

	@Autowired
	private StudentRepo studentrepo;

	@Autowired
	private PaymentRepo paymentrepo;

	@GetMapping("/students")
	@CrossOrigin
	@Operation(summary = "Get information about all students")
	public List<Student> getStudents() {
		return studentrepo.findAll();
	}

	// 3
	// <---------------add student and payment ---------------------->
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/addstudentpayment")
	@Operation(summary = "Add student and payment", description = "Adding new student and payment")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Student and Payment are added successfully"),
			@ApiResponse(responseCode = "400", description = "Bad request, incorrect student details"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	@Transactional
	public StudentAndPayment addNewStudentPayment(@Valid @RequestBody StudentAndPayment newstudentandpayment) {
		try {
			Student s = new Student();
			s.setName(newstudentandpayment.getName());
			s.setEmail(newstudentandpayment.getEmail());
			s.setMobile(newstudentandpayment.getMobile());
			s.setBatchid(newstudentandpayment.getBatchid());
			s.setJoinDate(newstudentandpayment.getJoinDate());
			List<Student> existingstudent = studentrepo.findAll();
			boolean exist = existingstudent.contains(s);
			if (exist)
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student Already Present");
			studentrepo.save(s);
			Payment p = new Payment();
			p.setStudentid(s.getStudentid());
			p.setAmount(newstudentandpayment.getAmount());
			p.setPayDate(newstudentandpayment.getPayDate());
			p.setPayMode(newstudentandpayment.getPayMode());
			paymentrepo.save(p);
			return newstudentandpayment;
		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
		} catch (Exception ex) {
			throw ex;
		}
	}

	// <-----delete student along with payment with student id-------------->
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/deletestudentpayment/{id}")
	@Operation(summary = "Delete student and associated payment", description = "Deleting a student and associated payment by using student id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Student and Payment are deleted successfully"),
			@ApiResponse(responseCode = "404", description = "Bad request, student id not found"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public void deleteOneStudentPayment(@PathVariable("id") Integer id) {
		try {
			Optional<Student> student = studentrepo.findById(id);
			if (student.isPresent()) {
				studentrepo.deleteById(id);
			} else
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student with this id is Not Found!");
		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
		} catch (Exception ex) {
			throw ex;
		}
	}

	// <------update Students and payments with student id--------->
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/updatestudentpayment/{id}")
	@Operation(summary = "Update student and payment ", description = "Updating a student and associated payment by using student id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Student and Payment are updated successfully"),
			@ApiResponse(responseCode = "404", description = "Bad request, student id not found!"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	@Transactional
	public StudentAndPayment updateStudentPayment(@PathVariable("id") Integer id,
			@RequestBody StudentAndPayment studentandpayment) {
		try {
			Optional<Student> exist = studentrepo.findById(id);
			if (!exist.isPresent())
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student with this id is Not Found!");
			Student s = exist.get();
			s.setName(studentandpayment.getName());
			s.setEmail(studentandpayment.getEmail());
			s.setMobile(studentandpayment.getMobile());
			s.setBatchid(studentandpayment.getBatchid());
			s.setJoinDate(studentandpayment.getJoinDate());
			studentrepo.save(s);
			Optional<Payment> payment = paymentrepo.findByStudentid(id);
			if (payment.isPresent()) {
				Payment p = payment.get();
				p.setAmount(studentandpayment.getAmount());
				p.setPayDate(studentandpayment.getPayDate());
				p.setPayMode(studentandpayment.getPayMode());
				paymentrepo.save(p);
			}
			return studentandpayment;
		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
		} catch (Exception ex) {
			throw ex;
		}
	}

	// 8
	// <---------------Running batch students---------------------->
	@GetMapping("/runningbatchesstudents")
	@Operation(summary = "Get Studnets in Running Batches", description = "Retrives all the Students that are in currently running batches")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully Retrived"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public List<Student> getCurrentRunningBatchesStudents() {
		try {
			List<Student> b = studentrepo.getRunningBatchStudent(LocalDate.now());
			return b;
		} catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	// 9
	// <---------------students by the given code---------------------->
	@GetMapping("/studentsbycode/{code}")
	@Operation(summary = "student by course code", description = "list of students of a particular course")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "List of student with given code"),
			@ApiResponse(responseCode = "404", description = "No students are there with given code"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public Page<Student> getStudentByCode(@PathVariable("code") String code,
			@RequestParam(name = "pageSize", defaultValue = "2") int pageSize, Pageable pageable) {
		Sort sort = Sort.by("studentid").ascending(); // Sort by student ID in ascending order
		try {
			Page<Student> students = studentrepo.findByCode(code,
					PageRequest.of(pageable.getPageNumber(), pageSize, sort));
			return students;
		} catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
		} catch (Exception ex) {
			throw ex;
		}
	}

	// 10
	// <---------------students by the given batch id---------------------->
	@GetMapping("/studentsbybatch/{batchid}")
	@Operation(summary = "student by batch id", description = "list of students of a particular batch")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "List of student with given batch id"),
			@ApiResponse(responseCode = "204", description = "No students are there with given batch id"),
			@ApiResponse(responseCode = "404", description = "batch id miss match"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public Object getStudentByBatchData(@PathVariable("batchid") Integer batchid) {
		try {
			return getStudentByBatch(batchid);
		} catch (Exception ex) {
			return ex.getMessage();
		}
	}

	public List<Student> getStudentByBatch(Integer batchid) {
		List<Student> s = studentrepo.findByBatchid(batchid);
		return s;
	}

	// 12
	// <-----List of students whose name contains the given string------------->
	@GetMapping("/studentsbyname/{name}")
	@Operation(summary = "students by partial name", description = "List of students whose name contains the given string")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "List of students whose name containing the given string"),
			@ApiResponse(responseCode = "404", description = "No student name contains the given string"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public List<Student> getStudentByBatch(@PathVariable("name") String name) {
		try {
			List<Student> s = studentrepo.findByNameContaining(name);
			return s;
		} catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
		} catch (Exception ex) {
			throw ex;
		}
	}

}
