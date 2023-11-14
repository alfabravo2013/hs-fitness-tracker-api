package fitnesstracker.tracker.repository;

import fitnesstracker.tracker.model.FitnessData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FitnessDataRepository extends JpaRepository<FitnessData, String> {
}
