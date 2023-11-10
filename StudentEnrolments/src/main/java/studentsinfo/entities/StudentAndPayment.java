package studentsinfo.entities;

import java.time.LocalDate;

public class StudentAndPayment {
	
	private String name;
	private String email;
	private String mobile;
	private Integer batchid;
	private LocalDate joinDate;
	private Double amount;
	private LocalDate payDate;
	private char payMode;
	public StudentAndPayment(String name, String email, String mobile, Integer batchid, LocalDate joinDate,
			Double amount, LocalDate payDate, char payMode) {
		super();
		this.name = name;
		this.email = email;
		this.mobile = mobile;
		this.batchid = batchid;
		this.joinDate = joinDate;
		this.amount = amount;
		this.payDate = payDate;
		this.payMode = payMode;
	}
	public StudentAndPayment() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getName() {
		return name;
	}
	public String getEmail() {
		return email;
	}
	public String getMobile() {
		return mobile;
	}
	public Integer getBatchid() {
		return batchid;
	}
	public LocalDate getJoinDate() {
		return joinDate;
	}
	public Double getAmount() {
		return amount;
	}
	public LocalDate getPayDate() {
		return payDate;
	}
	public char getPayMode() {
		return payMode;
	}

}
