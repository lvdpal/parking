package nl.finalist.parking.backend.repository;

import nl.finalist.parking.backend.entity.Employee;
import nl.finalist.parking.backend.entity.ParkingSpot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Parking Spot entity.
 */
public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Long> {

    /**
     * Return the parking spot with the given number
     *
     * @param number
     * @return
     */
    Optional<ParkingSpot> findByNumber(int number);

}
