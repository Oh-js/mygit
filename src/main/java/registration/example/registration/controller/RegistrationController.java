package registration.example.registration.controller;


import lombok.extern.slf4j.Slf4j;
import registration.example.registration.dto.CourseDto;
import registration.example.registration.dto.RegistrationRequest;
import registration.example.registration.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j  // 로깅 추가
public class RegistrationController {

    private final RegistrationService registrationService;

    /**
     * 수강신청 페이지를 보여주는 메소드
     */
    @GetMapping("/registration")
    public String registrationPage(Model model) {
        List<CourseDto> availableCourses = registrationService.getAvailableCourses();
        model.addAttribute("courses", availableCourses);
        // 실제 환경에서는 로그인한 학생 ID를 사용해야 함
        model.addAttribute("studentId", 1L);
        return "registration";
    }

    /**
     * 수강신청을 처리하는 API
     */
    @PostMapping("/api/registrations")
    @ResponseBody
    public ResponseEntity<Long> register(@RequestBody RegistrationRequest request) {
        try {
            // 로깅 추가
            log.info("Registration request received - studentId: {}, courseId: {}",
                    request.getStudentId(), request.getCourseId());

            if (request.getStudentId() == null || request.getCourseId() == null) {
                return ResponseEntity.badRequest().body(null);
            }

            Long registrationId = registrationService.register(
                    request.getStudentId(),
                    request.getCourseId()
            );
            return ResponseEntity.ok(registrationId);
        } catch (Exception e) {
            log.error("Registration failed", e);
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * 수강 가능한 과목 목록을 조회하는 API
     */
    @GetMapping("/api/courses/available")
    @ResponseBody
    public ResponseEntity<List<CourseDto>> getAvailableCourses() {
        List<CourseDto> courses = registrationService.getAvailableCourses();
        return ResponseEntity.ok(courses);
    }

    /**
     * 내 수강신청 목록을 조회하는 API
     */
    @GetMapping("/api/registrations/my")
    @ResponseBody
    public ResponseEntity<List<CourseDto>> getMyRegistrations(@RequestParam Long studentId) {
        List<CourseDto> registrations = registrationService.getMyRegistrations(studentId);
        return ResponseEntity.ok(registrations);
    }

    /**
     * 수강신청을 취소하는 API
     */
    @DeleteMapping("/api/registrations")
    @ResponseBody
    public ResponseEntity<Void> cancelRegistration(
            @RequestParam Long studentId,
            @RequestParam Long courseId) {
        try {
            registrationService.cancelRegistration(studentId, courseId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

