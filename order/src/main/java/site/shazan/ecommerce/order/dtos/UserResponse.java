package site.shazan.ecommerce.order.dtos;

import lombok.Data;
import site.shazan.ecommerce.order.models.UserRole;


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
