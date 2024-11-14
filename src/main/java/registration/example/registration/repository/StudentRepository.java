package registration.example.registration.repository;



import registration.example.registration.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    // 기본 CRUD 메소드는 JpaRepository에서 제공됨

    // 학번으로 학생 찾기
    Optional<Student> findByStudentNumber(String studentNumber);

    // 학과로 학생 목록 찾기
    List<Student> findByDepartment(String department);

    // 이름으로 학생 찾기
    List<Student> findByName(String name);

    // 학과별 학생 수 조회
    long countByDepartment(String department);

    // 학번 존재 여부 확인
    boolean existsByStudentNumber(String studentNumber);
}