package repositoryTests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import repositoryTests.model.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByRequestorId(Long userId);

    Request findByIdAndRequestorId(Long requestId, Long userId);
}
