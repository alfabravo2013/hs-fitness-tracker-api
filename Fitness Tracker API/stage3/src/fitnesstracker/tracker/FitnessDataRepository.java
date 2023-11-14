package fitnesstracker.tracker;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FitnessDataRepository extends JpaRepository<FitnessData, String> {
}
