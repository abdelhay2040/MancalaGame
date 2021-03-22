package com.bolcom.assignment.repositories;

import java.util.UUID;
import com.bolcom.assignment.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * PlayerRepository
 */
@Repository
public interface PlayerRepository extends JpaRepository<Player, UUID> {


}
