package nl.finalist.parking.backend.service;

import nl.finalist.parking.backend.dto.ParkingSpotWithStatus;
import nl.finalist.parking.backend.dto.Status;
import nl.finalist.parking.backend.entity.Employee;
import nl.finalist.parking.backend.entity.ParkingSpot;
import nl.finalist.parking.backend.repository.EmployeeRepository;
import nl.finalist.parking.backend.repository.ParkingSpotRepository;
import nl.finalist.parking.backend.repository.ReservationRepository;
import nl.finalist.parking.backend.security.TokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class ParkingSpotService {

    private static final Logger LOG = LoggerFactory.getLogger(ParkingSpotService.class);

    @Inject
    private ParkingSpotRepository parkingSpotRepository;
    @Inject
    private ReservationRepository reservationRepository;

    @Transactional(readOnly = true)
    public List<ParkingSpotWithStatus> getStatusOverview() {
        List<ParkingSpotWithStatus> result = new ArrayList<>();
        List<ParkingSpot> allSpots = parkingSpotRepository.findAll();
        for (ParkingSpot spot : allSpots) {
            ParkingSpotWithStatus parkingSpotWithStatus = new ParkingSpotWithStatus();
            parkingSpotWithStatus.setParkingSpot(spot);
            if(reservationRepository.findByDateAndParkingSpot(LocalDate.now(), spot).isPresent()) {
                parkingSpotWithStatus.setStatus(Status.OCCUPIED);
            } else {
                parkingSpotWithStatus.setStatus(Status.FREE);
            }
            result.add(parkingSpotWithStatus);
        }
        return result;
    }

    @Transactional(readOnly = true)
    public List<ParkingSpot> getFreeSpots() {
        List<ParkingSpot> freeSpots = new ArrayList<>();
        List<ParkingSpot> allSpots = parkingSpotRepository.findAll();
        for (ParkingSpot spot : allSpots) {
            if(!reservationRepository.findByDateAndParkingSpot(LocalDate.now(), spot).isPresent()) {
                freeSpots.add(spot);
            }
        }
        return freeSpots;
    }
}
