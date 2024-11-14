package registration.example.registration.repository;


import registration.example.registration.domain.Course;
import registration.example.registration.domain.Registration;
import registration.example.registration.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    boolean existsByStudentAndCourse(Student student, Course course);
    List<Registration> findByStudentId(Long studentId);
    Optional<Registration> findByStudentIdAndCourseId(Long studentId, Long courseId);

    long countByCourseId(Long courseId);
}