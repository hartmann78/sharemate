package com.practice.sharemate.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "requests")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "requestor_id")
    private Long requestorId;

    @NotBlank
    @Column(name = "description")
    private String description;

    @Column(name = "created")
    private LocalDateTime created;

    @OneToMany(mappedBy = "request")
    private List<Answer> answers;
}
