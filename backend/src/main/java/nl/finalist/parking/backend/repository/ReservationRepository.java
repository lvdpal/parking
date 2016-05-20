package nl.finalist.parking.backend.repository;

import nl.finalist.parking.backend.entity.ParkingSpot;
import nl.finalist.parking.backend.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Reservation entity.
 */
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    /**
     * Return all the reservations for a given date
     *
     * @param date
     * @return
     */
    List<Reservation> findByDate(LocalDate date);

    /**
     * Return the reservation for a given date and parking space if there is one.
     *
     * @param date
     * @param parkingSpot
     * @return
     */
    Optional<Reservation> findByDateAndParkingSpot(LocalDate date, ParkingSpot parkingSpot);
}
