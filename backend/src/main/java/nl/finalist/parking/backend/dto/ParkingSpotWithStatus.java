package nl.finalist.parking.backend.dto;

import nl.finalist.parking.backend.entity.ParkingSpot;

import java.io.Serializable;

/**
 * Parking spot with it's current status
 */
public class ParkingSpotWithStatus implements Serializable {
    private ParkingSpot parkingSpot;
    private Status status;

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    public void setParkingSpot(ParkingSpot parkingSpot) {
        this.parkingSpot = parkingSpot;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
