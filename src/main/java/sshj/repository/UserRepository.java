package sshj.repository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sshj.domain.User;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findById(String id);
}
