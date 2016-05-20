package nl.finalist.parking.backend.service;

import nl.finalist.parking.backend.dto.ParkingSpotWithStatus;
import nl.finalist.parking.backend.dto.Status;
import nl.finalist.parking.backend.entity.ParkingSpot;
import nl.finalist.parking.backend.entity.Reservation;
import nl.finalist.parking.backend.repository.ParkingSpotRepository;
import nl.finalist.parking.backend.repository.ReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class ReservationService {

    private static final Logger LOG = LoggerFactory.getLogger(ReservationService.class);

    @Inject
    private ParkingSpotRepository parkingSpotRepository;
    @Inject
    private ReservationRepository reservationRepository;

    /**
     * Save the reservation if there wasn't already a reservation for the spot for today
     *
     * @param reservation
     * @return true if the reservation was made, false otherwise
     */
    @Transactional(readOnly = false)
    public boolean save(Reservation reservation) {
        if(reservationRepository.findByDateAndParkingSpot(LocalDate.now(), reservation.getParkingSpot()).isPresent()) {
            return false;
        } else {
            reservationRepository.save(reservation);
            return true;
        }
    }

    /**
     * Delete the reservation with the given id
     *
     * @param reservationId
     */
    @Transactional(readOnly = false)
    public void delete(Long reservationId) {
        reservationRepository.delete(reservationId);
    }

}
