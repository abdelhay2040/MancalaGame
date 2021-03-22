package com.bolcom.assignment.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import com.bolcom.assignment.beans.PlayerBeans;
import com.bolcom.assignment.constants.GameConstants;
import com.bolcom.assignment.models.Player;
import com.bolcom.assignment.repositories.PlayerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * PlayerServiceTest
 */
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class PlayerServiceTest {

  @InjectMocks
  private PlayerServiceImpl playerServiceImpl;

  @Mock
  private PlayerRepository playerRepository;

  @Spy
  private ModelMapper modelMapper;

  @Test
  public void createNewPlayer_providedNameAndNumber_shouldSaveCorrectly() {
    // Arrange
    String playerName = "A";
    int playerNumber = GameConstants.PLAYER_ONE_NUM;
    ArgumentCaptor<Player> captor = ArgumentCaptor.forClass(Player.class);

    // Act
    playerServiceImpl.createNewPlayer(playerName, playerNumber);

    // Assert
    verify(playerRepository).save(captor.capture());
    assertEquals(playerName, captor.getValue().getName());
    assertEquals(playerNumber, captor.getValue().getNumber());
    assertEquals(0, captor.getValue().getScore());
  }

  @Test
  public void convertPlayerBeansToPlayer_providedPlayerBeans_shouldConvertCorrectly() {
    // Arrange
    PlayerBeans playerBeans = new PlayerBeans("A", 100, GameConstants.PLAYER_ONE_NUM);

    // Act
    Player player = playerServiceImpl.convertPlayerBeansToPlayer(playerBeans);

    // Assert
    assertEquals(player.getName(), playerBeans.getName());
    assertEquals(player.getNumber(), playerBeans.getNumber());
    assertEquals(player.getScore(), playerBeans.getScore());
  }

}
