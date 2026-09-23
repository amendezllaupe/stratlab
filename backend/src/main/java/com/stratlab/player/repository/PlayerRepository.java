package com.stratlab.player.repository;

import com.stratlab.player.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    Optional<Player> findByRiotTagNormalized(String riotTagNormalized);
}
