package repositoryTests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import repositoryTests.model.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
