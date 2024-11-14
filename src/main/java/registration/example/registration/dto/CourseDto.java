package registration.example.registration.dto;


import registration.example.registration.domain.Course;
import lombok.Getter;
import lombok.Setter;
import registration.example.registration.domain.Registration;

import java.util.List;

@Getter @Setter
public class CourseDto {
    private Long id;
    private String courseCode;
    private String courseName;
    private String professor;
    private Integer capacity;
    private Integer remainingSeats;

    public CourseDto(Course course) {
        this.id = course.getId();
        this.courseCode = course.getCourseCode();
        this.courseName = course.getCourseName();
        this.professor = course.getProfessor();
        this.capacity = course.getCapacity();

        // NPE 방지를 위한 null 체크 추가
        List<Registration> registrations = course.getRegistrations();
        this.remainingSeats = capacity - (registrations != null ? registrations.size() : 0);
    }
}