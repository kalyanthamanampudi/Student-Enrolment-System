package studentsinfo.entities;

import java.time.LocalDate;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "Payments")
public class Payment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private Integer paymentid;

	@Column(name = "Studentid")
	private Integer studentid;

	@NotNull(message = "Amount is not null")
	@Positive(message = "Amount must be a positive number")
	@Column(name = "Amount")
	private Double amount;

	@NotNull(message = "Payment date is not null")
	@Column(name = "Paydate")
	private LocalDate payDate;

	@NotNull(message = "paymode is not null")
	@Column(name = "Paymode")
	private char payMode;

	@OneToOne
	@JoinColumn(name = "Studentid", insertable = false, updatable = false)
	@JsonIgnore
	private Student student;

	public Payment() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Payment(Integer paymentid, Integer studentid, Double amount, LocalDate payDate, char payMode) {
		super();
		this.paymentid = paymentid;
		this.studentid = studentid;
		this.amount = amount;
		this.payDate = payDate;
		this.payMode = payMode;
	}

	// Getters and Setters

	public Integer getPaymentid() {
		return paymentid;
	}

	public void setPaymentid(Integer paymentid) {
		this.paymentid = paymentid;
	}

	public Integer getStudentid() {
		return studentid;
	}

	public void setStudentid(Integer studentid) {
		this.studentid = studentid;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public LocalDate getPayDate() {
		return payDate;
	}

	public void setPayDate(LocalDate payDate) {
		this.payDate = payDate;
	}

	public char getPayMode() {
		return payMode;
	}

	public void setPayMode(char payMode) {
		this.payMode = payMode;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	@Override
	public int hashCode() {
		return Objects.hash(studentid);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Payment) {
			Payment other = (Payment) obj;
			return Objects.equals(studentid, other.studentid);
		} else
			return false;
	}

	@Override
	public String toString() {
		return "Payment [paymentId=" + paymentid + ", studentId=" + studentid + ", amount=" + amount + ", payDate="
				+ payDate + ", payMode=" + payMode + "]";
	}

}
