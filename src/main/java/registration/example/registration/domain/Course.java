package registration.example.registration.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courses")
@Getter @Setter
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String courseCode;
    private String courseName;
    private String professor;
    private Integer capacity;

    @OneToMany(mappedBy = "course", fetch = FetchType.EAGER)  // LAZY에서 EAGER로 변경
    private List<Registration> registrations = new ArrayList<>();

    public boolean isFull() {
        return registrations != null && registrations.size() >= capacity;
    }
}