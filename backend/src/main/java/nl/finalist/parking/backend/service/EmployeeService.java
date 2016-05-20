package nl.finalist.parking.backend.service;

import nl.finalist.parking.backend.entity.Employee;
import nl.finalist.parking.backend.repository.EmployeeRepository;
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
import java.util.List;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class EmployeeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeService.class);

    @Inject
    private EmployeeRepository employeeRepository;

    @Inject
    private TokenProvider tokenProvider;

    /**
     * Return the logged in user with their roles.
     *
     * @return
     */
    @Transactional(readOnly = true)
    public Employee getEmployeeWithAuthorities() {
        Employee currentEmployee = employeeRepository.findOneByEmail(getLoggedInEmployeeEmail()).get();

        // TODO eagerly load the association. This should be replaced with something more elegant and less 'magic'.
        currentEmployee.getAuthorities().size();
        return currentEmployee;
    }

    /**
     * Return the email address of the logged in user
     *
     * @return
     */
    public String getLoggedInEmployeeEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return tokenProvider.getEmailFromAuthentication(authentication);
    }

    /**
     * Verify whether the logged in employee matches the given ID or is an Admin or Budgetkeeper.
     */
    public void verifySelfOrAllowed(@PathVariable Long id) {
        if (!isSelfOrAllowed(id)) {
            throw new AccessDeniedException("Als medewerker mag je alleen je eigen data bekijken");
        }
    }

    /**
     * Made public for mocking purposes during testing.
     */
    public boolean isSelfOrAllowed(long employeeId) {
        Long loggedInId = getLoggedInEmployeeId();

        if (isAdmin()) {
            return true;
        } else if (isEmployee() && loggedInId.equals(employeeId)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Return the name of the logged in user
     *
     * @return
     */
    public String getLoggedInEmployeeName() {
        return getLoggedInEmployee().getFullname();
    }

    /**
     * Get the employee that is logged in.
     */
    public Employee getLoggedInEmployee() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long id = tokenProvider.getIdFromAuthentication(authentication);
        return employeeRepository.getOne(id);
    }

    private long getLoggedInEmployeeId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return tokenProvider.getIdFromAuthentication(authentication);
    }

    private boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    /**
     * Check if the given list contains an employee with the given id.
     *
     * @param list
     * @param employeeId
     * @return
     */
    public boolean containsEmployee(List<Employee> list, long employeeId) {
        return list.stream().filter(employee -> employee.getId().equals(employeeId)).findFirst().isPresent();
    }

    private boolean isEmployee() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));
    }

}
