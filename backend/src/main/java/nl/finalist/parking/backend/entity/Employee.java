package nl.finalist.parking.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Employee.
 */
@Entity
@Table(name = "EMPLOYEE")
public class Employee implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "email")
    private String email;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "department")
    private Department department;

    @NotNull
    @Column(name = "receive_notifications")
    private Boolean receiveNotifications;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "EMPLOYEE_AUTHORITY", joinColumns = { @JoinColumn(name = "employee_id",
            referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "authority_name",
            referencedColumnName = "name") })
    private Set<Authority> authorities = new HashSet<>();

    public Employee() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * Returns the full name of the employee, meaning the first name followed by the last name
     *
     * @return
     */
    public String getFullname() {
        return firstname + " " + lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Boolean getReceiveNotifications() {
        return receiveNotifications;
    }

    public void setReceiveNotifications(Boolean receiveNotifications) {
        this.receiveNotifications = receiveNotifications;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Employee employee = (Employee) o;

        if (!Objects.equals(id, employee.id)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Employee{" + "id=" + id + ", firstname='" + firstname + "'" + ", lastname='" + lastname + "'"
                + ", department='" + department + "'" + ", email='" + email + "'" + '}';
    }

    public boolean isNew() {
        return id == null;
    }
}
