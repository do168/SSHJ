package sshj.sshj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sshj.sshj.model.Meeting;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
}
