package nl.finalist.parking.backend.repository;

import nl.finalist.parking.backend.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Employee entity.
 */
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findOneByEmail(String email);


    /**
     * Return the employee with the given id
     *
     * @param employeeId
     * @return
     */
    Employee getById(long employeeId);

}
