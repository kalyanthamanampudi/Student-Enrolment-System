package studentsinfo.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import studentsinfo.entities.Payment;

public interface PaymentRepo extends JpaRepository<Payment, Integer> {

	//14
	@Query("from Payment p where p.student.batchid=:id")
	List<Payment> getByBatch(Integer id);

	//15
	@Query("from Payment p where  p.payDate between :firstdate and :seconddate ")
	List<Payment> getRunningBatchBetween(LocalDate firstdate, LocalDate seconddate);

	//16
	List<Payment> findByPayMode(char mode);

	Optional<Payment> findByStudentid(Integer id);
	
	

}
