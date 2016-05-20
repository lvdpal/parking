package nl.finalist.parking.backend.dto;

import java.util.List;

public class UserDto {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private List<String> roles;

    public UserDto() {
    }

    public UserDto(long id, String firstName, String lastName, String email, List<String> roles) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.roles = roles;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getRoles() {
        return roles;
    }

    @Override
    public String toString() {
        return "UserDTO{" + "id='" + id + '\'' + '\'' + ", firstName='" + firstName + '\'' + ", lastName='" + lastName
                + '\'' + ", email='" + email + '\'' + ", roles=" + roles + '}';
    }
}
