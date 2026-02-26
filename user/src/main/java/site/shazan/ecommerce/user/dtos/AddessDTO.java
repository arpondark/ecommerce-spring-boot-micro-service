package site.shazan.ecommerce.user.dtos;

import lombok.Data;

@Data
public class AddessDTO {
    private String street;
    private String city;
    private String state;
    private String country;
    private String zipCode;
}
