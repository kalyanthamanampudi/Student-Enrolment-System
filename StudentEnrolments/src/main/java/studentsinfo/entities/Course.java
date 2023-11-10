package studentsinfo.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Courses")
public class Course {

	@Id
	@Column(name = "Code")
	@NotNull(message = "Course code is not null")
	@NotBlank(message = "Course code is required")
	@Size(max = 5, message = "Course code should be at most 5 characters")
	private String code;

	@Column(name = "Name")
	@NotNull(message = "Course name is required")
	@NotBlank(message = "Course name is required")
	@Size(max = 25, message = "Course name should be at most 25 characters")
	private String name;

	@Column(name = "Durationindays")
	@NotNull(message = "Duration in days is required")
	@Positive(message = "Duration in days should be a positive number")
	private Integer duration;

	@Column(name = "Fee")
	@NotNull(message = "Course fee is required")
	@Positive(message = "Course fee should be a positive number")
	private Double fee;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "course")
	@JsonIgnore
	private List<Batch> batches = new ArrayList<Batch>();

	// Getters and Setters

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String courseName) {
		this.name = courseName;
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

	public List<Batch> getBatches() {
		return batches;
	}

	public void setBatches(List<Batch> batches) {
		this.batches = batches;
	}

	@Override
	public int hashCode() {
		return Objects.hash(code, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Course) {
			Course other = (Course) obj;
			return Objects.equals(code, other.code) & Objects.equals(name, other.name);
		} else
			return false;
	}

	@Override
	public String toString() {
		return "Course [Code=" + code + ", courseName=" + name + ", duration=" + duration + ", fee=" + fee + "]";
	}

}


