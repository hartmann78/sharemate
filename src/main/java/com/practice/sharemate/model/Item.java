package com.practice.sharemate.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "name")
    private String name;

    @NotBlank
    @Column(name = "description", length = 512)
    private String description;

    @NotNull
    @Column(name = "is_available")
    private Boolean available;

    @Column(name = "request_id")
    private Long requestId;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner;

    @OneToMany(mappedBy = "item", fetch = FetchType.EAGER)
    private List<Booking> bookings;

    @OneToMany(mappedBy = "item", fetch = FetchType.EAGER)
    private List<Comment> comments;

    @OneToMany(mappedBy = "item", fetch = FetchType.EAGER)
    private List<Answer> answers;

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", available=" + available +
                ", ownerId=" + owner.getId() +
                ", requestId=" + requestId +
                '}';
    }
}
