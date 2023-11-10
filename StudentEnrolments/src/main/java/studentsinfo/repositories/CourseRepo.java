package studentsinfo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import studentsinfo.entities.Course;

public interface CourseRepo extends JpaRepository<Course, String> {

}
