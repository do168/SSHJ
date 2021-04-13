package sshj.sshj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sshj.sshj.model.Club;

@Repository
public interface ClubRepository extends CrudRepository<Club, Long> {
}
