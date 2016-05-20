package nl.finalist.parking.backend.resource;


import nl.finalist.parking.backend.dto.UserDto;
import nl.finalist.parking.backend.entity.Authority;
import nl.finalist.parking.backend.entity.Employee;

import nl.finalist.parking.backend.repository.EmployeeRepository;

import nl.finalist.parking.backend.security.TokenProvider;
import nl.finalist.parking.backend.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Employees.
 */
@RestController
@RequestMapping("/api")
public class EmployeeResource {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeResource.class);

    @Inject
    private EmployeeRepository employeeRepository;

    @Inject
    private EmployeeService employeeService;

    @Inject
    private TokenProvider tokenProvider;


    /**
     * Get the identity of the employee.
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/identity", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> getLoggedInEmployee() {
        LOG.debug("REST request to get logged in Employee");

        Employee employee = employeeService.getEmployeeWithAuthorities();
        List<String> roles = employee.getAuthorities().stream().map(Authority::getName).collect(Collectors.toList());
        UserDto userDto =
                new UserDto(employee.getId(), employee.getFirstname(), employee.getLastname(), employee.getEmail(),
                        roles);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }


}
