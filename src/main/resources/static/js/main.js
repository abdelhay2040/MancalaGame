import { POST, JSON_CONTENT_TYPE } from "./util.js";

const BASE_URL = `${window.location.href}game`;

const newGame = async () => {
  const playerOneName = prompt("Enter name for Player One:", "Player Name");
  if (!playerOneName) {
    return;
  }

  const url = `${BASE_URL}/start/${playerOneName}`;
  const response = await fetch(url, {
    headers: {
      "Content-Type": JSON_CONTENT_TYPE
    },
    method: POST
  });
  const data = await response.json();

  if (response.ok) {
    const newGameId = data.gameId;
    const gameUrl = `${BASE_URL}/play/${newGameId}`;
    window.location.href = gameUrl;
  } else {
    alert(data.message);
  }
};

const loadGame = async () => {
  const gameId = prompt("Enter Game ID:", "Game ID");
  const playerTwoName = prompt("Enter name for Player Two:", "Player Name");
  if (!playerTwoName) {
    return;
  }
  if (gameId) {
    const url = `${BASE_URL}/load/${gameId}/${playerTwoName}`;
    const response = await fetch(url);
    const data = await response.json();

    if (response.ok) {
      const gameUrl = `${BASE_URL}/play/${gameId}`;
      window.location.href = gameUrl;
    } else {
      alert(data.message);
    }
  }
};

document.getElementById("new-game-button").addEventListener("click", newGame);
document.getElementById("load-game-button").addEventListener("click", loadGame);
