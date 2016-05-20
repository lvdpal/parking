package nl.finalist.parking.backend.resource;


import nl.finalist.parking.backend.dto.ParkingSpotWithStatus;
import nl.finalist.parking.backend.entity.ParkingSpot;
import nl.finalist.parking.backend.entity.Reservation;
import nl.finalist.parking.backend.repository.EmployeeRepository;
import nl.finalist.parking.backend.repository.ParkingSpotRepository;
import nl.finalist.parking.backend.repository.ReservationRepository;
import nl.finalist.parking.backend.security.TokenProvider;
import nl.finalist.parking.backend.service.EmployeeService;
import nl.finalist.parking.backend.service.ParkingSpotService;
import nl.finalist.parking.backend.service.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * REST controller for managing Employees.
 */
@RestController
@RequestMapping("/api")
public class ReservationResource {

    private static final Logger LOG = LoggerFactory.getLogger(ReservationResource.class);

    @Inject
    private EmployeeRepository employeeRepository;

    @Inject
    private ParkingSpotRepository parkingSpotRepository;

    @Inject
    private ReservationService reservationService;


    /**
     * Create a reservation for the given employee and the given parking spot
     *
     * TODO: fix authentication so we can use the logged in user
     * TODO: fix URL
     *
     * @return a list of all parking spots with their status
     */
    @Transactional
    @RequestMapping(value = "/reservation/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> create(@RequestParam Long employeeId, @PathVariable Long id) {
        LOG.debug("REST request to make a reservation for employee {} and spot {}", employeeId, id);

        Reservation reservation = new Reservation();
        reservation.setDate(LocalDate.now());
        reservation.setStartTime(LocalTime.of(8, 0));
        reservation.setEndTime(LocalTime.of(18, 0));
        reservation.setEmployee(employeeRepository.getOne(employeeId));
        reservation.setParkingSpot(parkingSpotRepository.getOne(id));

        if(reservationService.save(reservation)) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Delete the reservation with the given id
     *
     * TODO: fix authentication so we can use the logged in user
     *
     * @return a list of all parking spots with their status
     */
    @Transactional
    @RequestMapping(value = "/reservation/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        LOG.debug("REST request to delete reservation {}", id);

        reservationService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
