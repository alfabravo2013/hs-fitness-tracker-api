package fitnesstracker.developer;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface DeveloperRepository extends CrudRepository<Developer, String> {
    Optional<Developer> findByEmail(String email);
}
