package site.shazan.ecommerce.user.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import site.shazan.ecommerce.user.models.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
}

