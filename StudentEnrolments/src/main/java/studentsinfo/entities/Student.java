package studentsinfo.entities;

import java.time.LocalDate;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "Students")
public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private Integer studentid;

	@NotNull(message = "null value is not accepted for name")
	@NotBlank(message = "Name is required")
	@Pattern(regexp = "^[A-Za-z\\s]+$", message = "Name should contain only letters and spaces")
	@Column(name = "Name")
	private String name;

	@Email(message = "Invalid email format")
	@Column(name = "Email")
	private String email;

	@Pattern(regexp = "^\\d{10}$", message = "Mobile number must be 10 digits")
	@Column(name = "Mobile")
	private String mobile;

	@Column(name = "Batchid")
	private Integer batchid;

	@NotNull(message = "Date of joining is required")
	@Column(name = "Dateofjoining")
	private LocalDate joinDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "batchid", insertable = false, updatable = false)
	@JsonIgnore
	private Batch batch;

	@OneToOne(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore
	private Payment payment;
	
	public Student(Integer studentid,
			@NotNull(message = "null value is not accepted for name") @NotBlank(message = "Name is required") @Pattern(regexp = "^[A-Za-z\\s]+$", message = "Name should contain only letters and spaces") String name,
			@Email(message = "Invalid email format") String email,
			@Pattern(regexp = "^\\d{10}$", message = "Mobile number must be 10 digits") String mobile, Integer batchid,
			@NotNull(message = "Date of joining is required") LocalDate joinDate) {
		super();
		this.studentid = studentid;
		this.name = name;
		this.email = email;
		this.mobile = mobile;
		this.batchid = batchid;
		this.joinDate = joinDate;
	}

	// Getters and Setters

	public Student() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getStudentid() {
		return studentid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getBatchid() {
		return batchid;
	}

	public void setBatchid(Integer batchid) {
		this.batchid = batchid;
	}

	public LocalDate getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(LocalDate joinDate) {
		this.joinDate = joinDate;
	}

	public Batch getBatch() {
		return batch;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, mobile, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Student) {
			Student other = (Student) obj;
			return (Objects.equals(email, other.email) & Objects.equals(mobile, other.mobile)
					& Objects.equals(name, other.name));
		} else
			return false;
	}

	@Override
	public String toString() {
		return "Student [id=" + studentid + ", name=" + name + ", email=" + email + ", mobile=" + mobile + ", batchId="
				+ batchid + ", joinDate=" + joinDate + "]";
	}

}
