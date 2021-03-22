package com.bolcom.assignment.repositories;

import java.util.UUID;
import com.bolcom.assignment.models.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * GameRepository
 */
@Repository
public interface GameRepository extends JpaRepository<Game, UUID> {


}
