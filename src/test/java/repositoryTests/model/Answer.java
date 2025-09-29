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

    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "request_id", referencedColumnName = "id")
    private Request request;

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", itemId=" + item.getId() +
                ", requestId=" + request.getId() +
                ", description='" + item.getDescription() + '\'' +
                ", available=" + item.getAvailable() +
                '}';
    }
}

