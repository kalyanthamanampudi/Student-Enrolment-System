//package studentsinfo.controller;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.dao.DataAccessException;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.server.ResponseStatusException;
//
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import jakarta.validation.Valid;
//import studentsinfo.entities.Batch;
//import studentsinfo.entities.Course;
//import studentsinfo.entities.Payment;
//import studentsinfo.entities.Student;
//import studentsinfo.entities.StudentAndPayment;
//import studentsinfo.repositories.BatchRepo;
//import studentsinfo.repositories.CourseRepo;
//import studentsinfo.repositories.PaymentRepo;
//import studentsinfo.repositories.StudentRepo;
//
////@RestController
//public class EnrolmentController {
//
//	@Autowired
//	private CourseRepo courserepo;
//
//	@Autowired
//	private BatchRepo batchrepo;
//
//	@Autowired
//	private StudentRepo studentrepo;
//
//	@Autowired
//	private PaymentRepo paymentrepo;
//
////<----------------------------------------------------------------------------->
//	@GetMapping("/batches")
//	@Operation(summary = "Get information about all batches")
//	public List<Batch> getBatches() {
//		return batchrepo.findAll();
//	}
//
//	@GetMapping("/students")
//	@CrossOrigin
//	@Operation(summary = "Get information about all students")
//	public List<Student> getStudents() {
//		return studentrepo.findAll();
//	}
//
//	@GetMapping("/payments")
//	@Operation(summary = "Get information about all payments")
//	public List<Payment> getPayments() {
//		return paymentrepo.findAll();
//	}
////<------------------------------------------------------------------------------->
//
//	// 1
//	// <---------------add new course---------------------->
//	@PreAuthorize("hasRole('ADMIN')")
//	@PostMapping("/addcourse")
//	@Operation(summary = "Add course", description = "Adding a new courses")
//	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Course added successfully"),
//			@ApiResponse(responseCode = "400", description = "Bad request, Course or Code is Already Present"),
//			@ApiResponse(responseCode = "500", description = "Internal server error") })
//	public Course addNewCourse(@Valid @RequestBody Course newCourse) {
//		try {
//			List<Course> existingcourses = courserepo.findAll();
//			boolean exist = existingcourses.contains(newCourse);
//			if (exist)
//				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course Code or Course Already Present");
//			courserepo.save(newCourse);
//			return newCourse;
//		} catch (ResponseStatusException ex) {
//			throw ex;
//		} catch (DataAccessException ex) {
//			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
//		} catch (Exception ex) {
//			throw ex;
//		}
//	}
//
//	// <---------------delete course---------------------->
//	@PreAuthorize("hasRole('ADMIN')")
//	@DeleteMapping("/deletecourse/{code}")
//	@Operation(summary = "Delete course", description = "Deleting a course by using its code")
//	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Course deleted successfully"),
//			@ApiResponse(responseCode = "404", description = " Course Code Not found"),
//			@ApiResponse(responseCode = "500", description = "Internal server error") })
//	public void deleteOneCourse(@PathVariable("code") String code) {
//		try {
//			Optional<Course> course = courserepo.findById(code);
//			if (course.isPresent())
//				courserepo.deleteById(code);
//			else
//				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course with this code is Not Found!");
//		} catch (ResponseStatusException ex) {
//			throw ex;
//		} catch (DataAccessException ex) {
//			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
//		} catch (Exception ex) {
//			throw ex;
//		}
//	}
//
//	// <---------------update course---------------------->
//	@PreAuthorize("hasRole('ADMIN')")
//	@PutMapping("/updatecourse/{code}")
//	@Operation(summary = "Update course", description = "updating a course by using its code")
//	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Course updated successfully"),
//			@ApiResponse(responseCode = "404", description = " Course Code Not found"),
//			@ApiResponse(responseCode = "500", description = "Internal server error") })
//	public void deleteOneCourse(@PathVariable("code") String code, @RequestBody Course newcourse) {
//		try {
//			Optional<Course> optCourse = courserepo.findById(code);
//			if (optCourse.isPresent()) {
//				Course c = optCourse.get();
//				c.setCode(newcourse.getCode());
//				c.setName(newcourse.getName());
//				c.setDuration(newcourse.getDuration());
//				c.setFee(newcourse.getFee());
//				courserepo.save(c);
//			} else
//				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course code Not Found!");
//		} catch (ResponseStatusException ex) {
//			throw ex;
//		} catch (DataAccessException ex) {
//			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
//		} catch (Exception ex) {
//			throw ex;
//		}
//	}
//
//	// 2
//	// <---------------add new batch ---------------------->
//	@PreAuthorize("hasRole('ADMIN')")
//	@PostMapping("/addbatch")
//	@Operation(summary = "Add batch", description = "Adding a new batch ")
//	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Batch added successfully"),
//			@ApiResponse(responseCode = "400", description = "Bad request, Batch details is incorrect"),
//			@ApiResponse(responseCode = "500", description = "Internal server error") })
//	public Batch addNewBatch(@Valid @RequestBody Batch newbatch) {
//		try {
//			List<Batch> existingbatches = batchrepo.findAll();
//			boolean exist = existingbatches.contains(newbatch);
//			if (exist)
//				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "timimgs in the same day Already Present");
//			//newbatch.getStartDate(),newbatch.getDuration()
//			batchrepo.save(newbatch);
//			return newbatch;
//		} catch (ResponseStatusException ex) {
//			throw ex;
//		} catch (DataAccessException ex) {
//			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
//		} catch (Exception ex) {
//			throw ex;
//		}
//	}
//
//	// <---------------delete batch---------------------->
//	@PreAuthorize("hasRole('ADMIN')")
//	@DeleteMapping("/deletebatch/{id}")
//	@Operation(summary = "delete batch", description = "Deleting a batch by using its id")
//	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Batch deleted successfully"),
//			@ApiResponse(responseCode = "404", description = " Batch id is Not Present"),
//			@ApiResponse(responseCode = "500", description = "Internal server error") })
//	public void deleteOneBatch(@PathVariable("id") Integer id) {
//		try {
//			Optional<Batch> batch = batchrepo.findById(id);
//			if (batch.isPresent())
//				batchrepo.deleteById(id);
//			else
//				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "batch with this id is Not Found!");
//		} catch (ResponseStatusException ex) {
//			throw ex;
//		} catch (DataAccessException ex) {
//			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
//		} catch (Exception ex) {
//			throw ex;
//		}
//	}
//
//	// <---------------update batch---------------------->
//	@PreAuthorize("hasRole('ADMIN')")
//	@PutMapping("/updatebatch/{id}")
//	@Operation(summary = "Update batch", description = "Updating a batch by using its id")
//	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Batch Updated successfully"),
//			@ApiResponse(responseCode = "404", description = " Batch id is Not Present"),
//			@ApiResponse(responseCode = "500", description = "Internal server error") })
//	public void updateOneBatch(@PathVariable("id") Integer id, @RequestBody Batch batch) {
//		try {
//			Optional<Batch> optbatch = batchrepo.findById(id);
//			if (optbatch.isPresent()) {
//				Batch b = optbatch.get();
//				b.setCode(batch.getCode());
//				b.setStartDate(batch.getStartDate());
//				//b.setEndDate();
//				b.setTimings(batch.getTimings());
//				b.setDuration(batch.getDuration());
//				b.setFee(batch.getFee());
//				batchrepo.save(b);
//			} else
//				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Batch id Not Found!");
//		} catch (ResponseStatusException ex) {
//			throw ex;
//		} catch (DataAccessException ex) {
//			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
//		} catch (Exception ex) {
//			throw ex;
//		}
//	}
//
//	// 3
//	// <---------------add student and payment ---------------------->
//	@PreAuthorize("hasRole('ADMIN')")
//	@PostMapping("/addstudentpayment")
//	@Operation(summary = "Add student and payment", description = "Adding new student and payment")
//	@ApiResponses(value = {
//			@ApiResponse(responseCode = "200", description = "Student and Payment are added successfully"),
//			@ApiResponse(responseCode = "400", description = "Bad request, incorrect student details"),
//			@ApiResponse(responseCode = "500", description = "Internal server error") })
//	@Transactional
//	public StudentAndPayment addNewStudentPayment(@Valid @RequestBody StudentAndPayment newstudentandpayment) {
//		try {
//			Student s = new Student();
//			s.setName(newstudentandpayment.getName());
//			s.setEmail(newstudentandpayment.getEmail());
//			s.setMobile(newstudentandpayment.getMobile());
//			s.setBatchid(newstudentandpayment.getBatchid());
//			s.setJoinDate(newstudentandpayment.getJoinDate());
//			List<Student> existingstudent = studentrepo.findAll();
//			boolean exist = existingstudent.contains(s);
//			if (exist)
//				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student Already Present");
//			studentrepo.save(s);
//			Payment p = new Payment();
//			p.setStudentid(s.getStudentid());
//			p.setAmount(newstudentandpayment.getAmount());
//			p.setPayDate(newstudentandpayment.getPayDate());
//			p.setPayMode(newstudentandpayment.getPayMode());
//			paymentrepo.save(p);
//			return newstudentandpayment;
//		} catch (ResponseStatusException ex) {
//			throw ex;
//		} catch (DataAccessException ex) {
//			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
//		} catch (Exception ex) {
//			throw ex;
//		}
//	}
//
//	// <-----delete student along with payment with student id-------------->
//	@PreAuthorize("hasRole('ADMIN')")
//	@DeleteMapping("/deletestudentpayment/{id}")
//	@Operation(summary = "Delete student and associated payment", description = "Deleting a student and associated payment by using student id")
//	@ApiResponses(value = {
//			@ApiResponse(responseCode = "200", description = "Student and Payment are deleted successfully"),
//			@ApiResponse(responseCode = "404", description = "Bad request, student id not found"),
//			@ApiResponse(responseCode = "500", description = "Internal server error") })
//	public void deleteOneStudentPayment(@PathVariable("id") Integer id) {
//		try {
//			Optional<Student> student = studentrepo.findById(id);
//			if (student.isPresent()) {
//				studentrepo.deleteById(id);
//			} else
//				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student with this id is Not Found!");
//		} catch (ResponseStatusException ex) {
//			throw ex;
//		} catch (DataAccessException ex) {
//			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
//		} catch (Exception ex) {
//			throw ex;
//		}
//	}
//
//	// <------update Students and payments with student id--------->
//	@PreAuthorize("hasRole('ADMIN')")
//	@PutMapping("/updatestudentpayment/{id}")
//	@Operation(summary = "Update student and payment ", description = "Updating a student and associated payment by using student id")
//	@ApiResponses(value = {
//			@ApiResponse(responseCode = "200", description = "Student and Payment are updated successfully"),
//			@ApiResponse(responseCode = "404", description = "Bad request, student id not found!"),
//			@ApiResponse(responseCode = "500", description = "Internal server error") })
//	@Transactional
//	public StudentAndPayment updateStudentPayment(@PathVariable("id") Integer id,
//			@RequestBody StudentAndPayment studentandpayment) {
//		try {
//			Optional<Student> exist = studentrepo.findById(id);
//			if (!exist.isPresent())
//				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student with this id is Not Found!");
//			Student s = exist.get();
//			s.setName(studentandpayment.getName());
//			s.setEmail(studentandpayment.getEmail());
//			s.setMobile(studentandpayment.getMobile());
//			s.setBatchid(studentandpayment.getBatchid());
//			s.setJoinDate(studentandpayment.getJoinDate());
//			studentrepo.save(s);
//			Optional<Payment> payment = paymentrepo.findByStudentid(id);
//			if (payment.isPresent()) {
//				Payment p = payment.get();
//				p.setAmount(studentandpayment.getAmount());
//				p.setPayDate(studentandpayment.getPayDate());
//				p.setPayMode(studentandpayment.getPayMode());
//				paymentrepo.save(p);
//			}
//			return studentandpayment;
//		} catch (ResponseStatusException ex) {
//			throw ex;
//		} catch (DataAccessException ex) {
//			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
//		} catch (Exception ex) {
//			throw ex;
//		}
//	}
//
//	// 4
//	// <-------update Payment with given payment id------------>
//	@PreAuthorize("hasRole('ADMIN')")
//	@PutMapping("/updatepayment/{id}")
//	@Operation(summary = "Update payment", description = "Update payment data by using payment id")
//	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Payment are updated successfully"),
//			@ApiResponse(responseCode = "404", description = "Payment id npt not found!"),
//			@ApiResponse(responseCode = "500", description = "Internal server error") })
//	public void updateOnePayment(@PathVariable("id") Integer id, @RequestBody Payment payment) {
//		try {
//			Optional<Payment> optpayment = paymentrepo.findById(id);
//			if (optpayment.isPresent()) {
//				Payment p = optpayment.get();
//				p.setStudentid(payment.getStudentid());
//				p.setAmount(payment.getAmount());
//				p.setPayDate(payment.getPayDate());
//				p.setPayMode(payment.getPayMode());
//				paymentrepo.save(p);
//			} else
//				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment id Not Found!");
//		} catch (ResponseStatusException ex) {
//			throw ex;
//		} catch (DataAccessException ex) {
//			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
//		} catch (Exception ex) {
//			throw ex;
//		}
//	}
//
//	// 5
//	// <---------------All Courses---------------------->
//	@GetMapping("/courses")
//	@Operation(summary = "All courses", description = "list of all courses")
//	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "List of Courses"),
//			@ApiResponse(responseCode = "500", description = "Internal server error") })
//	public List<Course> getCourse() {
//		return courserepo.findAll();
//	}
//
//	// 6
//	// <---------------Running batches---------------------->
//	@GetMapping("/runningbatches")
//	@Operation(summary = "currently running batches", description = "List of batches that are currently running")
//	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "List of Running batches"),
//			@ApiResponse(responseCode = "204", description = "No running batches"),
//			@ApiResponse(responseCode = "500", description = "Internal server error") })
//	public List<Batch> getPresentRunningBatches() {
//		try {
//			List<Batch> b = batchrepo.getRunningBatch(LocalDate.now());
//			if (b.isEmpty())
//				throw new ResponseStatusException(HttpStatus.NO_CONTENT, "no currently running batches!");
//			return b;
//		} catch (ResponseStatusException ex) {
//			throw ex;
//		} catch (DataAccessException ex) {
//			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
//		} catch (Exception ex) {
//			throw ex;
//		}
//	}
//
//	// 7
//	// <---------------Completed batches---------------------->
//	@GetMapping("/completedbatches")
//	@Operation(summary = "completed batches", description = "List of batches that are completed")
//	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "List of completed batches"),
//			@ApiResponse(responseCode = "204", description = "No completed batches"),
//			@ApiResponse(responseCode = "500", description = "Internal server error") })
//	public List<Batch> getcompletedBatches() {
//		try {
//			List<Batch> b = batchrepo.getCompletedBatch(LocalDate.now());
//			if (b.isEmpty())
//				throw new ResponseStatusException(HttpStatus.NO_CONTENT, "no currently completed batches!");
//			return b;
//		} catch (ResponseStatusException ex) {
//			throw ex;
//		} catch (DataAccessException ex) {
//			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
//		} catch (Exception ex) {
//			throw ex;
//		}
//	}
//
//	// 8
//	// <---------------Running batch students---------------------->
//	@GetMapping("/runningbatchesstudents")
//	@Operation(summary = "currently running batches students", description = "List of currently running batches students")
//	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "List of running batch students"),
//			@ApiResponse(responseCode = "204", description = "No running batch students"),
//			@ApiResponse(responseCode = "500", description = "Internal server error") })
//	public List<Student> getPresentRunningBatchesStudent() {
//		try {
//			List<Student> b = batchrepo.getRunningBatchStudent(LocalDate.now());
//			if (b.isEmpty())
//				throw new ResponseStatusException(HttpStatus.NO_CONTENT, "no currently running Students!");
//			return b;
//		} catch (ResponseStatusException ex) {
//			throw ex;
//		} catch (DataAccessException ex) {
//			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
//		} catch (Exception ex) {
//			throw ex;
//		}
//	}
//
//	// 9
//	// <---------------students by the given code---------------------->
//	@GetMapping("/studentsbycode/{code}")
//	@Operation(summary = "student by course code", description = "list of students of a particular course")
//	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "List of student with given code"),
//			@ApiResponse(responseCode = "404", description = "No students are there with given code"),
//			@ApiResponse(responseCode = "500", description = "Internal server error") })
//	public Page<Student> getStudentByCode(@PathVariable("code") String code,
//			@RequestParam(name = "pageSize", defaultValue = "2") int pageSize, Pageable pageable) {
//		Sort sort = Sort.by("studentid").ascending(); // Sort by student ID in ascending order
//		try {
//			Page<Student> students = studentrepo.findByCode(code,
//					PageRequest.of(pageable.getPageNumber(), pageSize, sort));
//			if (students.isEmpty())
//				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No batches for the given Course Code!");
//			return students;
//		} catch (ResponseStatusException ex) {
//			throw ex;
//		} catch (DataAccessException ex) {
//			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
//		} catch (Exception ex) {
//			throw ex;
//		}
//	}
//
//	// 10
//	// <---------------students by the given batch id---------------------->
//	@GetMapping("/studentsbybatch/{batchid}")
//	@Operation(summary = "student by batch id", description = "list of students of a particular batch")
//	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "List of student with given batch id"),
//			@ApiResponse(responseCode = "204", description = "No students are there with given batch id"),
//			@ApiResponse(responseCode = "404", description = "batch id miss match"),
//			@ApiResponse(responseCode = "500", description = "Internal server error") })
//	public Object getStudentByBatchData(@PathVariable("batchid") Integer batchid) {
//		try {
//			return getStudentByBatch(batchid);
//		} catch (ResponseStatusException ex) {
//			return ex.getMessage();
//		} catch (Exception ex) {
//			return ex.getMessage();
//		}
//	}
//
//	public List<Student> getStudentByBatch(Integer batchid) {
//		List<Student> s = studentrepo.findByBatchid(batchid);
//		if (s.isEmpty())
//			throw new ResponseStatusException(HttpStatus.NOT_FOUND, " batch id not found!");
//		return s;
//	}
//
//	// 11
//	// <--------------- batches by the given code---------------------->
//	@GetMapping("/batchesbycode/{code}")
//	@Operation(summary = "batches by course code", description = "List of batches of a particular course")
//	@ApiResponses(value = {
//			@ApiResponse(responseCode = "200", description = "List of batches for the specified course"),
//			@ApiResponse(responseCode = "404", description = "No batches found for the specified course"),
//			@ApiResponse(responseCode = "500", description = "Internal server error") })
//	public List<Batch> getBatchesByCode(@PathVariable("code") String code) {
//		try {
//			List<Batch> b = batchrepo.findByCode(code);
//			if (b.isEmpty())
//				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no batches found with the given code!");
//			return b;
//		} catch (ResponseStatusException ex) {
//			throw ex;
//		} catch (DataAccessException ex) {
//			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
//		} catch (Exception ex) {
//			throw ex;
//		}
//	}
//
//	// 12
//	// <-----List of students whose name contains the given string------------->
//	@GetMapping("/studentsbyname/{name}")
//	@Operation(summary = "students by partial name", description = "List of students whose name contains the given string")
//	@ApiResponses(value = {
//			@ApiResponse(responseCode = "200", description = "List of students whose name containing the given string"),
//			@ApiResponse(responseCode = "404", description = "No student name contains the given string"),
//			@ApiResponse(responseCode = "500", description = "Internal server error") })
//	public List<Student> getStudentByBatch(@PathVariable("name") String name) {
//		try {
//			List<Student> s = studentrepo.findByNameContaining(name);
//			if (s.isEmpty())
//				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no name contains the given string!");
//			return s;
//		} catch (ResponseStatusException ex) {
//			throw ex;
//		} catch (DataAccessException ex) {
//			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
//		} catch (Exception ex) {
//			throw ex;
//		}
//	}
//
//	// 13
//	// <--------running batches between given two dates------------->
//	@GetMapping("/runningbatchesbydates")
//	@Operation(summary = "batches between two dates", description = "List for batches started between given two dates")
//	@ApiResponses(value = {
//			@ApiResponse(responseCode = "200", description = "List of batches with start dates within the specified date range"),
//			@ApiResponse(responseCode = "404", description = "No batches found within the specified date range"),
//			@ApiResponse(responseCode = "500", description = "Internal server error") })
//	public List<Batch> getBatchesbetweentwodates(@RequestParam("firstdate") LocalDate firstdate,
//			@RequestParam("seconddate") LocalDate seconddate) {
//		try {
//			List<Batch> b = batchrepo.getRunningBatchBetween(firstdate, seconddate);
//			if (b.isEmpty())
//				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no batches were found between given dates!");
//			return b;
//		} catch (ResponseStatusException ex) {
//			throw ex;
//		} catch (DataAccessException ex) {
//			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
//		} catch (Exception ex) {
//			throw ex;
//		}
//	}
//
//	// 14
//	// <----------payments of a particular batch-------------->
//	@GetMapping("/paymentsbybatch/{id}")
//	@Operation(summary = "payments by batch ID", description = "List of payments of a particular batch")
//	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "List of payments by the given batch id"),
//			@ApiResponse(responseCode = "404", description = "No payments found with the given batch id"),
//			@ApiResponse(responseCode = "500", description = "Internal server error") })
//	public List<Payment> getPaymentsByBatch(@PathVariable("id") Integer id) {
//		try {
//			List<Payment> p = paymentrepo.getByBatch(id);
//			if (p.isEmpty())
//				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no payment found with this batch id!");
//			return p;
//		} catch (ResponseStatusException ex) {
//			throw ex;
//		} catch (DataAccessException ex) {
//			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
//		} catch (Exception ex) {
//			throw ex;
//		}
//	}
//
//	// 15
//	// <--------payments of a between given two dates------------->
//	@GetMapping("/paymentsbydates")
//	@Operation(summary = "payments between two dates", description = "List of payments between given two dates")
//	@ApiResponses(value = {
//			@ApiResponse(responseCode = "200", description = "List of payments between the given two dates"),
//			@ApiResponse(responseCode = "404", description = "No payments found between the specified dates"),
//			@ApiResponse(responseCode = "500", description = "Internal server error") })
//	public List<Payment> getPaymentsbetweentwodates(@RequestParam("firstdate") LocalDate firstdate,
//			@RequestParam("seconddate") LocalDate seconddate) {
//		try {
//			List<Payment> p = paymentrepo.getRunningBatchBetween(firstdate, seconddate);
//			if (p.isEmpty())
//				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no payment found between given dates!");
//			return p;
//		} catch (ResponseStatusException ex) {
//			throw ex;
//		} catch (DataAccessException ex) {
//			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
//		} catch (Exception ex) {
//			throw ex;
//		}
//	}
//
//	// 16
//	// <-------Payment of particular payment type-------------->
//	@GetMapping("/paymentsbymode/{mode}")
//	@Operation(summary = "payments by payment mode", description = "List of payments with the specified payment mode")
//	@ApiResponses(value = {
//			@ApiResponse(responseCode = "200", description = "List of payments with the specified payment mode"),
//			@ApiResponse(responseCode = "404", description = "No payments found with the specified payment mode"),
//			@ApiResponse(responseCode = "500", description = "Internal server error") })
//	public List<Payment> getPaymentsByBatch(@PathVariable("mode") char mode) {
//		try {
//			List<Payment> p = paymentrepo.findByPayMode(mode);
//			if (p.isEmpty())
//				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no payment found with this payment mode!");
//			return p;
//		} catch (ResponseStatusException ex) {
//			throw ex;
//		} catch (DataAccessException ex) {
//			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
//		} catch (Exception ex) {
//			throw ex;
//		}
//	}
//
//}
