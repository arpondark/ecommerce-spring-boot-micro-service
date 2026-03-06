package site.shazan.ecommerce.user.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import site.shazan.ecommerce.user.models.UserRole;

@Data
public class UserRequest {
    @JsonProperty("username")
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private UserRole role ;
    private AddessDTO address;
}
