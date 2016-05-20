package nl.finalist.parking.backend.resource;


import nl.finalist.parking.backend.dto.ParkingSpotWithStatus;
import nl.finalist.parking.backend.entity.ParkingSpot;
import nl.finalist.parking.backend.repository.ParkingSpotRepository;
import nl.finalist.parking.backend.service.ParkingSpotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;

/**
 * REST controller for managing Employees.
 */
@RestController
@RequestMapping("/api")
public class ParkingSpotResource {

    private static final Logger LOG = LoggerFactory.getLogger(ParkingSpotResource.class);

    @Inject
    private ParkingSpotRepository parkingSpotRepository;

    @Inject
    private ParkingSpotService parkingSpotService;

    /**
     * Get the current status for all parking spots
     *
     * @return a list of all parking spots with their status
     */
    @RequestMapping(value = "/spots/currentStatus", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ParkingSpotWithStatus>> getParkingSpotStatuses() {
        LOG.debug("REST request to get status for all parking spots");

        List<ParkingSpotWithStatus> statusOverview = parkingSpotService.getStatusOverview();
        return new ResponseEntity<>(statusOverview, HttpStatus.OK);
    }

    /**
     * Get a list of all free parking spots
     *
     * @return a list of parking spots
     */
    @RequestMapping(value = "/spots/free", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ParkingSpot>> getFreeParkingSpots() {
        LOG.debug("REST request to get a list of all free parking spots");

        List<ParkingSpot> parkingSpots = parkingSpotService.getFreeSpots();
        return new ResponseEntity<>(parkingSpots, HttpStatus.OK);
    }
}
