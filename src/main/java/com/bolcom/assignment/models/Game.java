package com.bolcom.assignment.models;

import static com.bolcom.assignment.constants.GameConstants.*;
import java.util.UUID;
import java.util.stream.IntStream;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import com.bolcom.assignment.enums.GameStatus;
import org.springframework.lang.Nullable;

/**
 * Game
 */
@Entity
public class Game {

  @Id
  @GeneratedValue
  private UUID id;

  @NotNull
  @ManyToOne
  private Player playerOne;

  @NotNull
  @ManyToOne
  private Player playerTwo;

  @NotNull
  private GameStatus status;

  @NotNull
  private int[] board;

  @NotNull
  private int playerTurn;

  @NotNull
  private int totalTurn;

  @Nullable
  @ManyToOne
  private Player winner;

  public Game() {
    super();
    status = GameStatus.IN_PROGRESS;
    board = IntStream.of(new int[TOTAL_PITS]).map(i -> PITS_PER_ROW).toArray();
    totalTurn = 0;
  }

  public Game(Player playerOne, Player playerTwo) {
    this();
    this.playerOne = playerOne;
    this.playerTwo = playerTwo;
  }

  public UUID getId() {
    return this.id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Player getPlayerOne() {
    return this.playerOne;
  }

  public void setPlayerOne(Player playerOne) {
    this.playerOne = playerOne;
  }

  public Player getPlayerTwo() {
    return this.playerTwo;
  }

  public void setPlayerTwo(Player playerTwo) {
    this.playerTwo = playerTwo;
  }

  public GameStatus getStatus() {
    return this.status;
  }

  public void setStatus(GameStatus status) {
    this.status = status;
  }

  public int[] getBoard() {
    return this.board;
  }

  public void setBoard(int[] board) {
    this.board = board;
  }

  public int getPlayerTurn() {
    return this.playerTurn;
  }

  public void setPlayerTurn(int playerTurn) {
    this.playerTurn = playerTurn;
  }

  public int getTotalTurn() {
    return totalTurn;
  }

  public void setTotalTurn(int totalTurn) {
    this.totalTurn = totalTurn;
  }

  public void addTotalTurn(int totalTurn) {
    this.totalTurn += totalTurn;
  }

  public Player getWinner() {
    return this.winner;
  }

  public void setWinner(Player winner) {
    this.winner = winner;
  }

}
