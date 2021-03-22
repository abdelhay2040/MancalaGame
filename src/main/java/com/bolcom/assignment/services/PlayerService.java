package com.bolcom.assignment.services;

import com.bolcom.assignment.beans.PlayerBeans;
import com.bolcom.assignment.constants.GameConstants;
import com.bolcom.assignment.models.Game;
import com.bolcom.assignment.models.Player;
import com.bolcom.assignment.repositories.PlayerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * PlayerServiceImpl
 */
@Service
public class PlayerService implements IPlayerService {

  @Autowired
  private PlayerRepository playerRepository;

  @Autowired
  private ModelMapper modelMapper;

  @Override
  public Player getPlayerByGame(Game game, int playerNumber) {
    if (playerNumber == GameConstants.PLAYER_ONE_NUM) {
      return game.getPlayerOne();
    } else {
      return game.getPlayerTwo();
    }
  }

  @Override
  public Player savePlayer(Player player) {
    return playerRepository.save(player);
  }

  @Override
  public Player createNewPlayer(String playerName, int playerNumber) {
    Player player = new Player(playerName, playerNumber);
    return savePlayer(player);
  }

  @Override
  public Player convertPlayerBeansToPlayer(PlayerBeans playerBeans) {
    return modelMapper.map(playerBeans, Player.class);
  }

}
