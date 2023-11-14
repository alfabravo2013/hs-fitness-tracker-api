package fitnesstracker.tracker;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FitnessDataService {
    private final FitnessDataRepository repository;

    public FitnessDataService(FitnessDataRepository repository) {
        this.repository = repository;
    }

    public void insertData(DataUploadRequest request) {
        var entity = new FitnessData();
        entity.setUsername(request.username());
        entity.setActivity(request.activity());
        entity.setDuration(request.duration());
        entity.setCalories(request.calories());
        repository.save(entity);
    }

    public List<DataDto> getAllData() {
        Sort sort = Sort.by(Sort.Order.desc("timestamp"));
        return repository.findAll(sort).stream()
                .map(this::entityToDto)
                .toList();
    }

    private DataDto entityToDto(FitnessData entity) {
        return new DataDto(
                entity.getId(),
                entity.getUsername(),
                entity.getActivity(),
                entity.getDuration(),
                entity.getCalories()
        );
    }
}
