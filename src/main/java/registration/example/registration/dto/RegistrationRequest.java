package registration.example.registration.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RegistrationRequest {
    private Long studentId;
    private Long courseId;
}