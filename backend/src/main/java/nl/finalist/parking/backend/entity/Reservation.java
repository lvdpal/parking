package nl.finalist.parking.backend.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

/**
 * A reservation for a parking spot by an employee for a given date
 */
@Entity
@Table(name="RESERVATION")
public class Reservation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parkingspot_id")
    private ParkingSpot parkingSpot;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @NotNull
    @Column(name="date")
    private LocalDate date;

    /**
     * For now start and end time aren't used yet
     */
    private LocalTime startTime;
    private LocalTime endTime;

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    public void setParkingSpot(ParkingSpot parkingSpot) {
        this.parkingSpot = parkingSpot;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Reservation reservation = (Reservation) o;

        if(!Objects.equals(date, reservation.date) ||
                !Objects.equals(employee, reservation.employee) ||
                !Objects.equals(parkingSpot, reservation.parkingSpot)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, employee, parkingSpot);
    }

    @Override
    public String toString() {
        return "Reservation["+parkingSpot.toString()+", Date{"+date.toString()+
                "], Employee["+employee.getFullname()+"]]";
    }
}
