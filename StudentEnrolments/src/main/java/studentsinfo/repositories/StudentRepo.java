package studentsinfo.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import studentsinfo.entities.Student;

public interface StudentRepo extends JpaRepository<Student, Integer> {

	//9
	@Query("from Student s where s.batch.code=:Code")
	Page<Student> findByCode(String Code, Pageable pageable);

	//10
	List<Student> findByBatchid(Integer batch);

	//12
	List<Student> findByNameContaining(String name);

	@Query(value="select * from Students s where s.id in (select st.id from Students st join Batches b on st.Batchid=b.id where :now between b.Startdate and b.Enddate )", nativeQuery =true)
	List<Student> getRunningBatchStudent(LocalDate now);

}
