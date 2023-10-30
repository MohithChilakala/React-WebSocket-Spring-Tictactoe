/* eslint-disable react-hooks/exhaustive-deps */
import { useParams } from "react-router-dom";
import { GameBoard } from "../component/game/GameBoard";
import { joinRoom } from "../services/GameService";
import { getCookie } from "../services/CookieService";
import { getApiUrl } from "../services/GetApiUrl";
import { over } from "stompjs";
import SockJS from "sockjs-client";
import { useEffect, useState } from "react";
import '../styles/gameBoard.css';

const Game = () => {
  const { game_id } = useParams();
  const [message, setMessage] = useState(null);
  const [stompClient, setStompClient] = useState(null);

  const urlParams = new URLSearchParams(window.location.search);
  const host = urlParams.get('host');
  const password = urlParams.get('password');

  const connectToGame = async () => {
    const response = await joinRoom(game_id, password, getCookie("username"), getCookie("authorization"));
    if (response.status === 404) alert("game not found");
    else if (response.status === 401) alert("invalid credentials");
    else if (response.status === 406) alert("game is full");
    else {
      if (response.status === 200) alert("joined");

      var stompClient = null;
      const connect = () => {
        let Sock = new SockJS(getApiUrl('ws?game_id=' + game_id), null, {
          transports: ["websocket", "xhr-streaming"],
        });
        stompClient = over(Sock);
        stompClient.connect({}, onConnected);

        stompClient.debug = (log) => {
          if (log.includes("ExecutorSubscribableChannel")) alert("Invalid player");
          else if (log.includes("Lost connection")) alert("Connection lost");
          else return;
        };

        setStompClient(stompClient);
      };

      const onConnected = () => {
        stompClient.subscribe("/game/" + game_id, onMessageReceived, {
          host: host,
          password: password,
          player: getCookie("username"),
        });
      };

      const onMessageReceived = (payload) => {
        setMessage(payload, () => {});
      };

      connect();
    }
  }

  useEffect(() => {
    connectToGame(setStompClient);
  }, []);

  return (
    <div>
      <h1>Game Page</h1>
      <p>Game ID: {game_id}</p>
      <p>Host: {host} </p>
      <p>Password: {password} </p>

      <GameBoard 
        game_id={game_id} 
        host={host} 
        stompClient={stompClient} 
        message={message} 
      />
    </div>
  );
};

export default Game;
