package db.service;

import db.repository.CoordinatesRepository;
import jakarta.validation.Valid;
import models.Coordinates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CoordinatesService {
    private final CoordinatesRepository coordinatesRepository;

    @Autowired
    public CoordinatesService(CoordinatesRepository coordinatesRepository) {
        this.coordinatesRepository = coordinatesRepository;
    }

    public Coordinates createCoordinates(@Valid Coordinates coordinates) {
        return this.coordinatesRepository.save(coordinates);
    }
    public List<Coordinates> getAllCoordinates() {
        return coordinatesRepository.findAll();
    }

}
