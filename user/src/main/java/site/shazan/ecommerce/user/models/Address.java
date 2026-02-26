package site.shazan.ecommerce.user.models;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection = "addresses")
public class Address {
    @Id
    private Long id;
    private String street;
    private String city;
    private String state;
    private String country;
    private String zipCode;
}

