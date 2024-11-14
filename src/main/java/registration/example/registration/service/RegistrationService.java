package registration.example.registration.service;


import registration.example.registration.domain.Course;
import registration.example.registration.domain.Registration;
import registration.example.registration.domain.Student;
import registration.example.registration.dto.CourseDto;
import registration.example.registration.repository.CourseRepository;
import registration.example.registration.repository.RegistrationRepository;
import registration.example.registration.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RegistrationService {
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final RegistrationRepository registrationRepository;

    /**
     * 수강신청을 처리하는 메소드
     */
    @Transactional
    public Long register(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 학생입니다."));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 과목입니다."));

        // 수강 인원 검증을 위해 현재 등록된 수강신청 수 확인
        long currentRegistrations = registrationRepository.countByCourseId(courseId);
        if (currentRegistrations >= course.getCapacity()) {
            throw new IllegalStateException("수강 인원이 초과되었습니다.");
        }

        // 중복 수강신청 검증
        boolean alreadyRegistered = registrationRepository.existsByStudentAndCourse(student, course);
        if (alreadyRegistered) {
            throw new IllegalStateException("이미 수강신청한 과목입니다.");
        }

        Registration registration = new Registration();
        registration.setStudent(student);
        registration.setCourse(course);
        registration.setRegisteredAt(LocalDateTime.now());

        return registrationRepository.save(registration).getId();
    }

    /**
     * 수강 가능한 과목 목록을 조회하는 메소드
     */
    public List<CourseDto> getAvailableCourses() {
        return courseRepository.findAll().stream()
                .filter(course -> !course.isFull())
                .map(CourseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * 내 수강신청 목록을 조회하는 메소드
     */
    public List<CourseDto> getMyRegistrations(Long studentId) {
        return registrationRepository.findByStudentId(studentId).stream()
                .map(registration -> new CourseDto(registration.getCourse()))
                .collect(Collectors.toList());
    }

    /**
     * 수강신청을 취소하는 메소드
     */
    @Transactional
    public void cancelRegistration(Long studentId, Long courseId) {
        Registration registration = registrationRepository
                .findByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new IllegalArgumentException("해당 수강신청 내역이 없습니다."));

        registrationRepository.delete(registration);
    }
}