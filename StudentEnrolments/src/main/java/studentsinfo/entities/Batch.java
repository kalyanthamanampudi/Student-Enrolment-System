package studentsinfo.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Batches")
public class Batch {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private Integer batchid;

	@Size(max = 5, message = "Course code length must be less then 5 characters")
	@Column(name = "Coursecode")
	private String code;

	@NotNull(message = "Start date is required")
	@Column(name = "Startdate")
	private LocalDate startDate;

	@Transient
	private LocalDate endDate;

	@NotNull(message = "timing is required null value is rejected")
	@NotBlank(message = "Timing is required")
	@Column(name = "Timings")
	private String timings;

	@NotNull(message = "Duration is required")
	@Positive(message = "Duration must be a positive number")
	@Column(name = "Durationindays")
	private Integer duration;

	@NotNull(message = "Fee is required")
	@Positive(message = "Fee must be a positive number")
	@Column(name = "Fee")
	private Double fee;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Coursecode", insertable = false, updatable = false)
	@JsonIgnore
	private Course course;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "batch")
	@JsonIgnore
	private List<Student> student = new ArrayList<Student>();


	// Getters and Setters

	public Integer getBatchid() {
		return batchid;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return getStartDate().plusDays(getDuration());
	}

	public String getTimings() {
		return timings;
	}

	public void setTimings(String timings) {
		this.timings = timings;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Double getFee() {
		return fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public List<Student> getStudent() {
		return student;
	}

	public void setStudent(List<Student> student) {
		this.student = student;
	}
	

	@Override
	public int hashCode() {
		return Objects.hash(startDate, timings);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Batch) {
			Batch other = (Batch) obj;
			return Objects.equals(startDate, other.startDate) & Objects.equals(timings, other.timings);
		} else
			return false;
	}

	@Override
	public String toString() {
		return "Batch [batchid=" + batchid + ", code=" + code + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", timings=" + timings + ", duration=" + duration + ", fee=" + fee + "]";
	}

}
