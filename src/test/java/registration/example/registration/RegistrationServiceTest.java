package registration.example.registration;


import registration.example.registration.domain.Course;
import registration.example.registration.domain.Registration;
import registration.example.registration.domain.Student;
import registration.example.registration.repository.CourseRepository;
import registration.example.registration.repository.RegistrationRepository;
import registration.example.registration.repository.StudentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import registration.example.registration.service.RegistrationService;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RegistrationServiceTest {

    @Autowired
    RegistrationService registrationService;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    RegistrationRepository registrationRepository;

    @Test
    @DisplayName("수강신청 성공 테스트")
    void registerSuccess() {
        // given
        Student student = createStudent("20240001", "홍길동", "컴퓨터공학과");
        Course course = createCourse("CS101", "자바프로그래밍", "김교수", 50);

        // when
        Long registrationId = registrationService.register(student.getId(), course.getId());

        // then
        Registration registration = registrationRepository.findById(registrationId).orElseThrow();
        assertThat(registration.getStudent()).isEqualTo(student);
        assertThat(registration.getCourse()).isEqualTo(course);
    }

    @Test
    @DisplayName("수강인원 초과시 실패")
    void registerFailWhenFull() {
        // given
        Student student = createStudent("20240002", "이순신", "컴퓨터공학과");
        Course course = createCourse("CS102", "알고리즘", "박교수", 1);

        // 먼저 다른 학생이 수강신청
        Student otherStudent = createStudent("20240003", "강감찬", "컴퓨터공학과");
        registrationService.register(otherStudent.getId(), course.getId());

        // when & then
        assertThrows(IllegalStateException.class, () -> {
            registrationService.register(student.getId(), course.getId());
        });
    }

    @Test
    @DisplayName("중복 수강신청 실패")
    void registerFailWhenDuplicate() {
        // given
        Student student = createStudent("20240004", "을지문덕", "컴퓨터공학과");
        Course course = createCourse("CS103", "데이터베이스", "최교수", 50);

        // 첫 번째 수강신청
        registrationService.register(student.getId(), course.getId());

        // when & then
        assertThrows(IllegalStateException.class, () -> {
            registrationService.register(student.getId(), course.getId());
        });
    }

    private Student createStudent(String studentNumber, String name, String department) {
        Student student = new Student();
        student.setStudentNumber(studentNumber);
        student.setName(name);
        student.setDepartment(department);
        return studentRepository.save(student);
    }

    private Course createCourse(String courseCode, String courseName, String professor, int capacity) {
        Course course = new Course();
        course.setCourseCode(courseCode);
        course.setCourseName(courseName);
        course.setProfessor(professor);
        course.setCapacity(capacity);
        return courseRepository.save(course);
    }
}