package com.bolcom.assignment.services;

import static com.bolcom.assignment.constants.GameConstants.*;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Optional;
import java.util.UUID;
import com.bolcom.assignment.enums.GameStatus;
import com.bolcom.assignment.models.Game;
import com.bolcom.assignment.models.Player;
import com.bolcom.assignment.repositories.GameRepository;
import com.bolcom.assignment.system.exceptions.GameNotFoundException;
import com.bolcom.assignment.system.exceptions.InvalidMoveException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * GameServiceTest
 */
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class GameServiceTest {

  @InjectMocks
  private GameService gameServiceImpl;

  @Mock
  private IPlayerService playerService;

  @Mock
  private GameRepository gameRepository;

  @Spy
  private ModelMapper modelMapper;

  /**
   * Helper method to generate Game with generic Players.
   * 
   * @return
   */
  private Game createGenericGame() {
    Player playerOne = new Player("A", PLAYER_ONE_NUM);
    Player playerTwo = new Player("B", PLAYER_TWO_NUM);

    Game game = new Game(playerOne);
    game.setPlayerTwo(playerTwo);
    game.setId(UUID.randomUUID());
    return game;
  }

  @Test
  public void playerOnePick_doesNotCrossOver_shouldPlacesStoneAndIncreasesScore() {
    // Arrange
    Game game = createGenericGame();
    Optional<Game> gameOptional = Optional.of(game);
    int playerNumber = PLAYER_ONE_NUM;
    int index = 0;
    int[] expectedBoard = new int[] {0, 7, 7, 7, 7, 7, 6, 6, 6, 6, 6, 6};
    int expectedScore = 1;

    when(gameRepository.findById(game.getId())).thenReturn(gameOptional);
    when(gameRepository.save(game)).thenReturn(game);
    when(playerService.getPlayerByGame(game, playerNumber)).thenReturn(game.getPlayerOne());

    // Act
    gameServiceImpl.pick(game.getId(), playerNumber, index);

    // Assert
    assertTrue(expectedScore == game.getPlayerOne().getScore());
    assertArrayEquals(expectedBoard, game.getBoard());
  }

  @Test
  public void playerTwoPick_doesNotCrossOver_shouldPlacesStoneAndIncreasesScore() {
    // Arrange
    Game game = createGenericGame();
    Optional<Game> gameOptional = Optional.of(game);
    int playerNumber = PLAYER_TWO_NUM;
    int index = 6;
    int[] expectedBoard = new int[] {6, 6, 6, 6, 6, 6, 0, 7, 7, 7, 7, 7};
    int expectedScore = 1;

    when(gameRepository.findById(game.getId())).thenReturn(gameOptional);
    when(gameRepository.save(game)).thenReturn(game);
    when(playerService.getPlayerByGame(game, playerNumber)).thenReturn(game.getPlayerTwo());

    // Act
    gameServiceImpl.pick(game.getId(), playerNumber, index);

    // Assert
    assertTrue(expectedScore == game.getPlayerTwo().getScore());
    assertArrayEquals(expectedBoard, game.getBoard());
  }

  @Test
  public void playerOnePick_crossOverToOpponentsBoard_shouldPlacesStoneAndIncreasesScore() {
    // Arrange
    Game game = createGenericGame();
    Optional<Game> gameOptional = Optional.of(game);
    int playerNumber = PLAYER_ONE_NUM;
    int index = 5;
    int[] expectedBoard = new int[] {6, 6, 6, 6, 6, 0, 7, 7, 7, 7, 7, 6};
    int expectedScore = 1;

    when(gameRepository.findById(game.getId())).thenReturn(gameOptional);
    when(gameRepository.save(game)).thenReturn(game);
    when(playerService.getPlayerByGame(game, playerNumber)).thenReturn(game.getPlayerOne());

    // Act
    gameServiceImpl.pick(game.getId(), playerNumber, index);

    // Assert
    assertTrue(expectedScore == game.getPlayerOne().getScore());
    assertArrayEquals(expectedBoard, game.getBoard());
  }

  @Test
  public void playerTwoPick_crossOverToOpponentsBoard_shouldPlacesStoneAndIncreasesScore() {
    // Arrange
    Game game = createGenericGame();
    Optional<Game> gameOptional = Optional.of(game);
    int playerNumber = PLAYER_TWO_NUM;
    int index = 11;
    int[] expectedBoard = new int[] {7, 7, 7, 7, 7, 6, 6, 6, 6, 6, 6, 0};
    int expectedScore = 1;

    when(gameRepository.findById(game.getId())).thenReturn(gameOptional);
    when(gameRepository.save(game)).thenReturn(game);
    when(playerService.getPlayerByGame(game, playerNumber)).thenReturn(game.getPlayerTwo());

    // Act
    gameServiceImpl.pick(game.getId(), playerNumber, index);

    // Assert
    assertTrue(expectedScore == game.getPlayerTwo().getScore());
    assertArrayEquals(expectedBoard, game.getBoard());
  }

  @Test
  public void playerOnePick_highNumberOnhand_shouldLoopBackToOwnPits() {
    // Arrange
    Game game = createGenericGame();
    Optional<Game> gameOptional = Optional.of(game);
    int playerNumber = PLAYER_ONE_NUM;
    int index = 0;
    int[] expectedBoard = new int[] {1, 8, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7};
    int expectedScore = 1;

    // Increases stones in picked pit
    game.getBoard()[index] = 14;

    when(gameRepository.findById(game.getId())).thenReturn(gameOptional);
    when(gameRepository.save(game)).thenReturn(game);
    when(playerService.getPlayerByGame(game, playerNumber)).thenReturn(game.getPlayerOne());

    // Act
    gameServiceImpl.pick(game.getId(), playerNumber, index);

    // Assert
    assertTrue(expectedScore == game.getPlayerOne().getScore());
    assertArrayEquals(expectedBoard, game.getBoard());
  }

  @Test
  public void playerTwoPick_highNumberOnhand_shouldLoopBackToOwnPits() {
    // Arrange
    Game game = createGenericGame();
    Optional<Game> gameOptional = Optional.of(game);
    int playerNumber = PLAYER_TWO_NUM;
    int index = 6;
    int[] expectedBoard = new int[] {7, 7, 7, 7, 7, 7, 1, 8, 7, 7, 7, 7};
    int expectedScore = 1;

    // Increases stones in picked pit
    game.getBoard()[6] = 14;

    when(gameRepository.findById(game.getId())).thenReturn(gameOptional);
    when(gameRepository.save(game)).thenReturn(game);
    when(playerService.getPlayerByGame(game, playerNumber)).thenReturn(game.getPlayerTwo());

    // Act
    gameServiceImpl.pick(game.getId(), playerNumber, index);

    // Assert
    assertTrue(expectedScore == game.getPlayerTwo().getScore());
    assertArrayEquals(expectedBoard, game.getBoard());
  }

  @Test
  public void playerOnePlace_lastStonePlacedInEmptyPit_shouldCaptureOpponentPit() {
    // Arrange
    Game game = createGenericGame();

    // Opponent: 6, 5, 4, 3, 2, 1
    // Player: 3, 6, 6, 0, 6, 6
    game.setBoard(new int[] {3, 6, 6, 0, 6, 6, 1, 2, 3, 4, 5, 6});

    Optional<Game> gameOptional = Optional.of(game);
    int playerNumber = PLAYER_ONE_NUM;
    int index = 0;
    int[] expectedBoard = new int[] {0, 7, 7, 0, 6, 6, 1, 2, 0, 4, 5, 6};
    int expectedScore = 4; // 3 from capture + 1 from last stone

    when(gameRepository.findById(game.getId())).thenReturn(gameOptional);
    when(gameRepository.save(game)).thenReturn(game);
    when(playerService.getPlayerByGame(game, playerNumber)).thenReturn(game.getPlayerOne());

    // Act
    gameServiceImpl.pick(game.getId(), playerNumber, index);

    // Assert
    assertTrue(expectedScore == game.getPlayerOne().getScore());
    assertArrayEquals(expectedBoard, game.getBoard());
  }

  @Test
  public void playerTwoPlace_lastStonePlacedInEmptyPit_shouldCaptureOpponentPit() {
    // Arrange
    Game game = createGenericGame();

    // Player: 6, 6, 0, 6, 6, 3
    // Opponent: 1, 2, 3, 4, 5, 6
    game.setBoard(new int[] {1, 2, 3, 4, 5, 6, 3, 6, 6, 0, 6, 6});

    Optional<Game> gameOptional = Optional.of(game);
    int playerNumber = PLAYER_TWO_NUM;
    int index = 6;
    int[] expectedBoard = new int[] {1, 2, 0, 4, 5, 6, 0, 7, 7, 0, 6, 6};
    int expectedScore = 4; // 3 from capture + 1 from last stone

    when(gameRepository.findById(game.getId())).thenReturn(gameOptional);
    when(gameRepository.save(game)).thenReturn(game);
    when(playerService.getPlayerByGame(game, playerNumber)).thenReturn(game.getPlayerTwo());

    // Act
    gameServiceImpl.pick(game.getId(), playerNumber, index);

    // Assert
    assertTrue(expectedScore == game.getPlayerTwo().getScore());
    assertArrayEquals(expectedBoard, game.getBoard());
  }

  @Test
  public void playerOnePlace_allPitsEmpty_shouldFinishGame() {
    // Arrange
    Game game = createGenericGame();

    // Player: 0, 0, 0, 0, 0, 1
    // Opponent: 1, 2, 3, 4, 5, 6
    game.setBoard(new int[] {0, 0, 0, 0, 0, 1, 1, 2, 3, 4, 5, 6});

    Optional<Game> gameOptional = Optional.of(game);
    Player playerOne = game.getPlayerOne();
    Player playerTwo = game.getPlayerTwo();
    int playerNumber = PLAYER_ONE_NUM;
    int index = 5;
    int expectedScore = 21;

    when(gameRepository.findById(game.getId())).thenReturn(gameOptional);
    when(gameRepository.save(game)).thenReturn(game);
    when(playerService.getPlayerByGame(game, playerNumber)).thenReturn(playerOne);

    // Act
    gameServiceImpl.pick(game.getId(), playerNumber, index);

    // Assert
    assertTrue(game.getStatus().equals(GameStatus.END));
    assertTrue(game.getWinner().equals(playerTwo));
    assertTrue(playerTwo.getScore() == expectedScore);
  }

  @Test
  public void startNewGame_shouldSaveGameAndReturnBean() {
    // Arrange
    String playerOneName = "A";
    String playerTwoName = "B";
    ArgumentCaptor<Game> captor = ArgumentCaptor.forClass(Game.class);

    when(gameRepository.save(ArgumentMatchers.any(Game.class))).thenReturn(new Game());
    when(playerService.createNewPlayer(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt()))
        .thenReturn(new Player());

    // Act
    gameServiceImpl.start(playerOneName);

    // Assert
    verify(gameRepository).save(captor.capture());
    assertNotNull(captor.getValue().getBoard());
    assertNotNull(captor.getValue().getPlayerOne());
    assertEquals(captor.getValue().getPlayerTwo(),null);
    assertEquals(GameStatus.IN_PROGRESS, captor.getValue().getStatus());
  }

  @Test(expected = InvalidMoveException.class)
  public void playerPick_invalidIndex_shouldThrowException() {
    // Arrange
    Game game = createGenericGame();
    Optional<Game> gameOptional = Optional.of(game);
    int playerNumber = PLAYER_ONE_NUM;
    int index = 6;

    when(gameRepository.findById(game.getId())).thenReturn(gameOptional);

    // Act
    gameServiceImpl.pick(game.getId(), playerNumber, index);
  }

  @Test(expected = InvalidMoveException.class)
  public void playerPick_emptyPit_shouldThrowException() {
    // Arrange
    Game game = createGenericGame();
    game.getBoard()[0] = 0;

    Optional<Game> gameOptional = Optional.of(game);
    int playerNumber = PLAYER_ONE_NUM;
    int index = 0;

    when(gameRepository.findById(game.getId())).thenReturn(gameOptional);

    // Act
    gameServiceImpl.pick(game.getId(), playerNumber, index);
  }

  @Test(expected = GameNotFoundException.class)
  public void loadGame_invalidGameId_shouldThrowException() {
    // Arrange
    UUID gameId = UUID.randomUUID();

    // Act
    gameServiceImpl.load(gameId,"player 2");
  }

}
