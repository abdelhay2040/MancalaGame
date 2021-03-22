package com.bolcom.assignment.beans;

import java.util.UUID;
import com.bolcom.assignment.enums.GameStatus;

/**
 * GameBeans
 */
public class GameBeans {

  private UUID id;
  private GameStatus status;
  private int[] board;
  private int playerTurn;
  private int totalTurn;
  private int index;
  private PlayerBeans playerOne;
  private PlayerBeans playerTwo;

  public GameBeans() {
    super();
  }

  public GameBeans(UUID id, GameStatus status, int[] board, int playerTurn, int totalTurn) {
    this.id = id;
    this.status = status;
    this.board = board;
    this.playerTurn = playerTurn;
    this.totalTurn = totalTurn;
  }

  public UUID getId() {
    return this.id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public int getTotalTurn() {
    return totalTurn;
  }

  public void setTotalTurn(int totalTurn) {
    this.totalTurn = totalTurn;
  }

  public int getPlayerTurn() {
    return playerTurn;
  }

  public void setPlayerTurn(int playerTurn) {
    this.playerTurn = playerTurn;
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

  public int getIndex() {
    return this.index;
  }

  public void setIndex(int index) {
    this.index = index;
  }

  public PlayerBeans getPlayerOne() {
    return this.playerOne;
  }

  public void setPlayerOne(PlayerBeans playerOne) {
    this.playerOne = playerOne;
  }

  public PlayerBeans getPlayerTwo() {
    return this.playerTwo;
  }

  public void setPlayerTwo(PlayerBeans playerTwo) {
    this.playerTwo = playerTwo;
  }

}
