/* eslint-disable react-hooks/exhaustive-deps */
import { useEffect, useState } from "react";
import { getCookie } from "../../services/CookieService";

export const GameBoard = ({ game_id, host, stompClient, message }) => {
  const [game, setGame] = useState(null);
  const [gameStatus, setGameStatus] = useState('');
  const [isCompleted, setCompleted] = useState(false);

  useEffect(() => {
    if(message) {
      const body = message.body;
      if(body.charAt(0) === '{') {
        const data = JSON.parse(body);
        setGame(data);

        if(data.status === "DRAW") {
          setGameStatus('Game draw');
          setCompleted(true);
        }
        else if(data.status === "WINNER") {
          if(data.player1Turn) setGameStatus(data.player2 + ' won');
          else setGameStatus(data.player1 + ' won');
          setCompleted(true);
        } else {
          if(data.player1Turn) setGameStatus(data.player1 + ' turn');
          else setGameStatus(data.player2 + ' turn');
        }
      } else {
        const player = body.split(' ')[0];
        if(player !== getCookie('username')) {
          if(body.split(' ')[1] !== 'joined') {
            alert('Illegal play or access detected. check console for more');
            console.log(body);
          }
          else stompClient.send('/client/start/' + game_id, {}, '');
        }
      }
    }
  }, [message]);

  const handleClick = (square) => {
    if(game) {
      if(game.status === "WINNER" || game.status === "DRAW") alert("Game completed");
      else if(game.gameBoard.charAt(square) !== ' ') return;
      else if(game.player1Turn && getCookie('username') !== game.player1) return;
      else if(!game.player1Turn && getCookie('username') !== game.player2) return;
      else {
        const body = {
          game_id: game_id,
          player: getCookie('username'),
          move: square
        };
        stompClient.send('/client/move', {}, JSON.stringify(body));
      }
    }
  }

  const reset = () => {
    stompClient.send('/client/start/' + game_id, {}, '');
    setCompleted(false);
  }

  return (
    <>
      { game && (
        <div>
          <div className='board'>
            <h2 className="status"> {gameStatus} </h2>
            <div className='board-row'>
              <Square value={game.gameBoard.charAt(0)} onSquareClick={() => handleClick(0)} />
              <Square value={game.gameBoard.charAt(1)} onSquareClick={() => handleClick(1)} />
              <Square value={game.gameBoard.charAt(2)} onSquareClick={() => handleClick(2)} />
            </div>
            <div className='board-row'>
              <Square value={game.gameBoard.charAt(3)} onSquareClick={() => handleClick(3)} />
              <Square value={game.gameBoard.charAt(4)} onSquareClick={() => handleClick(4)} />
              <Square value={game.gameBoard.charAt(5)} onSquareClick={() => handleClick(5)} />
            </div>
            <div className='board-row'>
              <Square value={game.gameBoard.charAt(6)} onSquareClick={() => handleClick(6)} />
              <Square value={game.gameBoard.charAt(7)} onSquareClick={() => handleClick(7)} />
              <Square value={game.gameBoard.charAt(8)} onSquareClick={() => handleClick(8)} />
            </div>
          </div>
          { isCompleted && host === getCookie('username') &&  
            <button className='reset' onClick={reset}>Reset</button>
          }
        </div>
      )}
    </>
  );
};

const Square = ({ value, onSquareClick }) => {
  return (
    <button className="square" onClick={onSquareClick}>
      {value}
    </button>
  );
}