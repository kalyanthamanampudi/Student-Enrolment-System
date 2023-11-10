package studentsinfo.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import studentsinfo.entities.Payment;
import studentsinfo.repositories.PaymentRepo;

@RestController
public class PaymentController {

	@Autowired
	private PaymentRepo paymentrepo;

	@GetMapping("/payments")
	@Operation(summary = "Get information about all payments")
	public List<Payment> getPayments() {
		return paymentrepo.findAll();
	}

	// 4
	// <-------update Payment with given payment id------------>
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/updatepayment/{id}")
	@Operation(summary = "Update payment", description = "Update payment data by using payment id")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Payment are updated successfully"),
			@ApiResponse(responseCode = "404", description = "Payment id npt not found!"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public void updateOnePayment(@PathVariable("id") Integer id, @RequestBody Payment payment) {
		try {
			Optional<Payment> optpayment = paymentrepo.findById(id);
			if (optpayment.isPresent()) {
				Payment p = optpayment.get();
				p.setStudentid(payment.getStudentid());
				p.setAmount(payment.getAmount());
				p.setPayDate(payment.getPayDate());
				p.setPayMode(payment.getPayMode());
				paymentrepo.save(p);
			} else
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment id Not Found!");
		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
		} catch (Exception ex) {
			throw ex;
		}
	}

	// 14
	// <----------payments of a particular batch-------------->
	@GetMapping("/paymentsbybatch/{id}")
	@Operation(summary = "payments by batch ID", description = "List of payments of a particular batch")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "List of payments by the given batch id"),
			@ApiResponse(responseCode = "404", description = "No payments found with the given batch id"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public List<Payment> getPaymentsByBatch(@PathVariable("id") Integer id) {
		try {
			List<Payment> p = paymentrepo.getByBatch(id);
			if (p.isEmpty())
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no payment found with this batch id!");
			return p;
		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
		} catch (Exception ex) {
			throw ex;
		}
	}

	// 15
	// <--------payments of a between given two dates------------->
	@GetMapping("/paymentsbydates")
	@Operation(summary = "payments between two dates", description = "List of payments between given two dates")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "List of payments between the given two dates"),
			@ApiResponse(responseCode = "404", description = "No payments found between the specified dates"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public List<Payment> getPaymentsbetweentwodates(@RequestParam("firstdate") LocalDate firstdate,
			@RequestParam("seconddate") LocalDate seconddate) {
		try {
			List<Payment> p = paymentrepo.getRunningBatchBetween(firstdate, seconddate);
			if (p.isEmpty())
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no payment found between given dates!");
			return p;
		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
		} catch (Exception ex) {
			throw ex;
		}
	}

	// 16
	// <-------Payment of particular payment type-------------->
	@GetMapping("/paymentsbymode/{mode}")
	@Operation(summary = "payments by payment mode", description = "List of payments with the specified payment mode")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "List of payments with the specified payment mode"),
			@ApiResponse(responseCode = "404", description = "No payments found with the specified payment mode"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public List<Payment> getPaymentsByBatch(@PathVariable("mode") char mode) {
		try {
			List<Payment> p = paymentrepo.findByPayMode(mode);
			if (p.isEmpty())
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no payment found with this payment mode!");
			return p;
		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
		} catch (Exception ex) {
			throw ex;
		}
	}

}
