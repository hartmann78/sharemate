package repositoryTests.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "answers")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_id")
    private Long itemId;

    @ManyToOne
    @JoinColumn(name = "request_id", referencedColumnName = "id")
    private Request request;

    @Column(name = "description")
    private String description;

    @Column(name = "is_available")
    private Boolean available;

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", itemId=" + itemId +
                ", requestId=" + request.getId() +
                ", description='" + description + '\'' +
                ", available=" + available +
                '}';
    }
}

