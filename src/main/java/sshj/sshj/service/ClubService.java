package sshj.sshj.service;

import sshj.sshj.model.Club;

import java.util.List;

public interface ClubService {
    Club create(Club clubParam);
    Club update(Club clubParam, long id);
    void delete(long id);
    Club find(long id);
    List<Club> findAll();
}
