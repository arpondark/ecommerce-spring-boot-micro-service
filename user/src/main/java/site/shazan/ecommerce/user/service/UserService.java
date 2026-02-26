package site.shazan.ecommerce.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.shazan.ecommerce.user.dtos.AddessDTO;
import site.shazan.ecommerce.user.dtos.UserRequest;
import site.shazan.ecommerce.user.dtos.UserResponse;
import site.shazan.ecommerce.user.models.User;
import site.shazan.ecommerce.user.models.Address;
import site.shazan.ecommerce.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<UserResponse> fetchAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    public void addUser(UserRequest userRequest) {
        User user = new User();
        updateUserFromRequest(user, userRequest);
        userRepository.save(user);
    }



    public Optional<UserResponse> getUserById(String id) {
        return userRepository.findById(id)
                .map(this::mapToUserResponse);
    }

    public Boolean updateUser(String id, UserRequest updatedUser) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setFirstName(updatedUser.getFirstName());
                    existingUser.setLastName(updatedUser.getLastName());
                    existingUser.setEmail(updatedUser.getEmail());
                    existingUser.setPhone(updatedUser.getPhone());
                    existingUser.setRole(updatedUser.getRole());
                    if (updatedUser.getAddress() != null) {
                        AddessDTO addressDTO = updatedUser.getAddress();
                        Address address = new Address();
                        address.setStreet(addressDTO.getStreet());
                        address.setCity(addressDTO.getCity());
                        address.setState(addressDTO.getState());
                        address.setCountry(addressDTO.getCountry());
                        address.setZipCode(addressDTO.getZipCode());
                        existingUser.setAddress(address);
                    }
                    userRepository.save(existingUser);
                    return true;
                }).orElse(false);
    }


    private void updateUserFromRequest(User user, UserRequest userRequest) {
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());
        user.setRole(userRequest.getRole());
        if (userRequest.getAddress() != null) {
            AddessDTO addressDTO = userRequest.getAddress();
            Address address = new Address();
            address.setStreet(addressDTO.getStreet());
            address.setCity(addressDTO.getCity());
            address.setState(addressDTO.getState());
            address.setCountry(addressDTO.getCountry());
            address.setZipCode(addressDTO.getZipCode());
            user.setAddress(address);
        }
    }

    private UserResponse mapToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());
        userResponse.setPhone(user.getPhone());
        userResponse.setRole(user.getRole());
        if (user.getAddress() != null) {
            AddessDTO addressDTO = new AddessDTO();
            addressDTO.setStreet(user.getAddress().getStreet());
            addressDTO.setCity(user.getAddress().getCity());
            addressDTO.setState(user.getAddress().getState());
            addressDTO.setCountry(user.getAddress().getCountry());
            addressDTO.setZipCode(user.getAddress().getZipCode());
            userResponse.setAddress(addressDTO);
        }
        return userResponse;
    }
}
