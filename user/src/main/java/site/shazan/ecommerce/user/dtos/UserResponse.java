package site.shazan.ecommerce.user.dtos;

import lombok.Data;
import site.shazan.ecommerce.user.models.UserRole;


@Data
public class UserResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private UserRole role ;
    private AddessDTO address;
}
