package com.bolcom.assignment.services;

import static com.bolcom.assignment.constants.GameConstants.*;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;
import com.bolcom.assignment.beans.GameBeans;
import com.bolcom.assignment.enums.GameStatus;
import com.bolcom.assignment.models.Game;
import com.bolcom.assignment.models.Player;
import com.bolcom.assignment.repositories.GameRepository;
import com.bolcom.assignment.system.exceptions.GameNotFoundException;
import com.bolcom.assignment.system.exceptions.InvalidGameStateException;
import com.bolcom.assignment.system.exceptions.InvalidMoveException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for Game related logics.
 * 
 * GameServiceImpl
 */
@Service
public class GameServiceImpl implements GameService {

  @Autowired
  private PlayerService playerService;

  @Autowired
  private GameRepository gameRepository;

  @Autowired
  private ModelMapper modelMapper;

  /**
   * Get Game by id.
   * 
   * @param gameId
   * @return
   */
  private Game getGame(UUID gameId) {
    Optional<Game> gameOptional = gameRepository.findById(gameId);

    if (gameOptional.isPresent()) {
      return gameOptional.get();
    } else {
      throw new GameNotFoundException("Invalid game Id.");
    }
  }

  /**
   * Return pit index based on player sides.
   * 
   * @param playerNumber
   * @param index
   * @return
   */
  private int getPlayerBoardIndex(int playerNumber, int index, boolean normalize) {
    if (normalize && index >= PITS_PER_ROW) {
      index -= PITS_PER_ROW;
    }
    return index + (playerNumber * PITS_PER_ROW);
  }

  /**
   * Gets opponent's pit index which directly opposite of current index.
   * 
   * @param index
   * @return
   */
  private int getOppositePitIndex(int index) {
    return TOTAL_PITS - index - 1;
  }

  /**
   * Return max index limit of player's board.
   * 
   * @param playerNumber
   * @param index
   * @return
   */
  private int getPlayerMaxBoardLimit(int playerNumber) {
    return PITS_PER_ROW + (playerNumber * PITS_PER_ROW) - 1;
  }

  /**
   * Return min index limit of player's board.
   * 
   * @param playerNumber
   * @param index
   * @return
   */
  private int getPlayerMinBoardLimit(int playerNumber) {
    return 0 + (playerNumber * PITS_PER_ROW);
  }

  /**
   * Validates whether the selected index is valid for current player.<br>
   * 1. Index must be within range of the player's board.<br>
   * 2. Selected pit must not be empty.<br>
   * 
   * @param playerNumber
   * @param index
   */
  private void validatePick(int playerNumber, int index, int[] board) {
    int maxBoardLimit = getPlayerMaxBoardLimit(playerNumber);
    int minBoardLimit = getPlayerMinBoardLimit(playerNumber);

    if (minBoardLimit > index || index > maxBoardLimit || board[index] == 0) {
      throw new InvalidMoveException(String.format("Index %d is not a valid move.", index));
    }
  }

  @Override
  public GameBeans pick(UUID gameId, int playerNumber, int index) {
    Game game = getGame(gameId);
    validateGameStatus(game.getId(), game.getStatus());
    validatePick(playerNumber, index, game.getBoard());

    // Proceed to place the stones
    game = place(game, playerNumber, index);
    return modelMapper.map(game, GameBeans.class);
  }

  /**
   * Second phase of the game.<br>
   * <br>
   * Distribute the stone picked earlier to other pits one by one.<br>
   * If finished iterating player's pit and there are still leftovers in hand,<br>
   * start iterating into opponent's pits instead.
   * 
   * @param game
   * @param playerNumber
   * @param pitIndex
   */
  private Game place(Game game, int playerNumber, int pitIndex) {
    Player player = playerService.getPlayerByGame(game, playerNumber);
    int[] board = game.getBoard();
    int scoreToAdd = 0;

    // Get all stones from the selected pit
    int hand = board[pitIndex];
    board[pitIndex] = 0;

    // Get current player max board index
    int boardLimit = getPlayerMaxBoardLimit(playerNumber);

    int i = pitIndex + 1;
    int currentPlayerNumber = playerNumber;
    int opponentNumber = playerNumber == PLAYER_ONE_NUM ? PLAYER_TWO_NUM : PLAYER_ONE_NUM;
    boolean isPlayerBoard = true;
    boolean isFinalOnLargePit = false;

    while (hand > 0) {
      // If current index is not a large pit
      if (i <= boardLimit) {
        // Check for Capture condition
        if (isPlayerBoard && hand == 1 && board[i] == 0) {
          scoreToAdd += capture(i, opponentNumber, board);
        } else {
          // Place 1 stone from hand
          board[i]++;
        }
        hand--;
      }
      // If board limit is reached and there's still leftover hand,
      // increase score (large pit) and continue to opponent's board
      if (i > boardLimit) {
        if (isPlayerBoard) {
          // Place to large pit
          scoreToAdd++;
          hand--;

          if (hand == 0) {
            isFinalOnLargePit = true;
          }
        }
        // Switch board
        isPlayerBoard = !isPlayerBoard;
        currentPlayerNumber = isPlayerBoard ? playerNumber : opponentNumber;
        boardLimit = getPlayerMaxBoardLimit(currentPlayerNumber);
        i = getPlayerBoardIndex(currentPlayerNumber, 0, true) - 1;
      }
      // Move to next pit
      i++;
    }

    // Calculate score for current turn
    player.addScore(scoreToAdd);

    // Increment turn and switch to next player's turn
    game.addTotalTurn(1);

    // If last stone landed on large pit, current player will get another turn
    if (!isFinalOnLargePit) {
      game.setPlayerTurn(opponentNumber);
    }

    // Check for finish condition
    finish(game);

    // Updates
    playerService.savePlayer(player);
    return gameRepository.save(game);
  }

  /**
   * Checks whether there's any player's board that is empty.
   * 
   * @param playerNumber
   * @param board
   * @return
   */
  private boolean anyEmptyBoard(int[] board) {
    boolean isPlayerOneEmpty = true;
    boolean isPlayerTwoEmpty = true;

    isPlayerOneEmpty = !IntStream
        .range(getPlayerMinBoardLimit(PLAYER_ONE_NUM), getPlayerMaxBoardLimit(PLAYER_ONE_NUM))
        .anyMatch(i -> board[i] != 0);
    isPlayerTwoEmpty = !IntStream
        .range(getPlayerMinBoardLimit(PLAYER_TWO_NUM), getPlayerMaxBoardLimit(PLAYER_TWO_NUM))
        .anyMatch(i -> board[i] != 0);
    return isPlayerOneEmpty || isPlayerTwoEmpty;
  }

  /**
   * Collects all stones in all player's pit and place it into large pit.<br>
   * Triggerred when finish condition is reached.
   * 
   * @param playerNumber
   * @param board
   * @return
   */
  private int collectStones(int playerNumber, int[] board) {
    int scoreToAdd = IntStream
        .rangeClosed(getPlayerMinBoardLimit(playerNumber), getPlayerMaxBoardLimit(playerNumber))
        .map(i -> board[i]).sum();
    return scoreToAdd;
  }

  /**
   * Checks and finishes the game.<br>
   * Finish condition: any player's board is totally empty.
   * 
   * @param game
   */
  private void finish(Game game) {
    int[] board = game.getBoard();

    // Check both player's board if there's any empty row.
    if (anyEmptyBoard(board)) {
      Player playerOne = game.getPlayerOne();
      Player playerTwo = game.getPlayerTwo();

      // Calculate final score
      playerOne.addScore(collectStones(playerOne.getNumber(), board));
      playerTwo.addScore(collectStones(playerTwo.getNumber(), board));

      // Determine winner and end the game
      game.setWinner(playerOne.getScore() > playerTwo.getScore() ? playerOne : playerTwo);
      game.setStatus(GameStatus.END);
    }
  }

  /**
   * Capture the stones in the opposite pit.<br>
   * Will trigger if the last stone on hand is placed to own's empty pit.
   * 
   * @param index
   * @param opponentNumber
   * @param board
   * @return
   */
  private int capture(int index, int opponentNumber, int[] board) {
    // Get opponent's pit index
    int oppositePitIndex = getOppositePitIndex(index);

    // Put all stones in current pit and opposite pit to large pit
    int scoreToAdd = board[oppositePitIndex] + 1;

    // Empty current pit and opposite pit
    board[index] = 0;
    board[oppositePitIndex] = 0;
    return scoreToAdd;
  }

  /**
   * Validate Game Status.<br>
   * Prevent ended game to be loaded or played.
   * 
   * @param gameId
   * @param gameStatus
   */
  private void validateGameStatus(UUID gameId, GameStatus gameStatus) {
    if (gameStatus.equals(GameStatus.END)) {
      throw new InvalidGameStateException(
          String.format("Game (%s) has ended. Please start a new game.", gameId));
    }
  }

  @Override
  public GameBeans getGameBeansById(UUID gameId) {
    return modelMapper.map(getGame(gameId), GameBeans.class);
  }

  @Override
  public Game saveGame(Game game) {
    return gameRepository.save(game);
  }

  @Override
  public GameBeans start(String playerOneName, String playerTwoName) {
    Player playerOne = playerService.createNewPlayer(playerOneName, PLAYER_ONE_NUM);
    Player playerTwo = playerService.createNewPlayer(playerTwoName, PLAYER_TWO_NUM);
    Game game = saveGame(new Game(playerOne, playerTwo));
    return modelMapper.map(game, GameBeans.class);
  }

  @Override
  public String load(UUID gameId) {
    GameBeans gameBeans = getGameBeansById(gameId);
    validateGameStatus(gameBeans.getId(), gameBeans.getStatus());
    return gameBeans.getId().toString();
  }

}
