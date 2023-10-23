package projects.web.tictactoe.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import projects.web.tictactoe.dto.GameMoveDto;
import projects.web.tictactoe.dto.GameResponseDto;
import projects.web.tictactoe.exception.IllegalGameplayException;
import projects.web.tictactoe.model.Game;

import java.util.*;

@Service
public class GameService {
      private final Map<String, Game> games = new HashMap<>();
      private int currentGame = 68 * 67;

      public Game getGame(String game_id) {
            return games.get(game_id);
      }

      public String createRoom(String host, String password) {
            String game_id;
            do {
                  if(currentGame > 68 * 67 * 66) currentGame = 68 * 67;
                  game_id = generateGameId(currentGame++);
            } while (games.get(game_id) != null);

            char[] gameBoard = new char[9]; Arrays.fill(gameBoard, ' ');
            Game game = new Game(
                    gameBoard,
                    0,
                    host,
                    password,
                    "",
                    "",
                    true,
                    "CREATED",
                    System.currentTimeMillis() + 60 * 1000
            );
            games.put(game_id, game);
            return game_id;
      }

      public String generateGameId(int currGame) {
            final String randomAlphaNumericSpecialCharacters = "XO$jwIopPc01TsxS5JMi89#!%&abt2efyzD6rUZQdCqVEkGHm3ABFY74nvKLWNghl@Ru";
            StringBuilder characters = new StringBuilder(randomAlphaNumericSpecialCharacters);
            int len = characters.length();

            StringBuilder gameId = new StringBuilder();
            while (currGame > 0) {
                  int r = currGame % len;
                  gameId.insert(0, characters.charAt(r));
                  currGame /= len;

                  characters.deleteCharAt(r); len--;
            }
            return gameId.toString();
      }

      public Game RandomPlayerAssigner(String game_id) {
            Game game = games.get(game_id);
            if(game.getStatus().equals("STARTED")) return game;

            Random random = new Random();
            if(!random.nextBoolean()) {
                  String playerTemp = game.getPlayer1();
                  game.setPlayer1(game.getPlayer2());
                  game.setPlayer2(playerTemp);

                  if (!game.getStatus().equals("CREATED")) {
                        char[] gameBoard = new char[9]; Arrays.fill(gameBoard, ' ');

                        game.setGameBoard(gameBoard);
                        game.setCnt(0);
                        game.setPlayer1Turn(true);
                        game.setLastUpdatedInMilli(System.currentTimeMillis());
                  }
                  games.put(game_id, game);
            }

            return game;
      }

      public void validateMove(Game game, String playerName, int move) throws IllegalGameplayException {
            if(game == null) throw new IllegalGameplayException("Game not found");
            if(!game.isPlayer1Turn() && !game.getPlayer2().equals(playerName)) throw new IllegalGameplayException("Invalid player");
            if(game.getStatus().equals("WINNER") ||  game.getStatus().equals("DRAW")) throw new IllegalGameplayException("Game Already Completed");
            if(game.isPlayer1Turn() && !game.getPlayer1().equals(playerName)) throw new IllegalGameplayException("Not your turn");
            if(game.getGameBoard()[move] != ' ') throw new IllegalGameplayException("Illegal move");
      }

      public GameResponseDto makeMove(GameMoveDto gameMoveDto) throws IllegalGameplayException {
            String game_id = gameMoveDto.getGame_id();
            int move = gameMoveDto.getMove();
            Game game = games.get(game_id);

            try {
                  validateMove(game, gameMoveDto.getPlayer(), move);
            } catch (IllegalGameplayException e) {
                  games.remove(game_id);
                  throw new IllegalGameplayException(e.getMessage());
            }

            char[] updatedGameBoard = game.getGameBoard();
            updatedGameBoard[move] = game.isPlayer1Turn() ? 'X' : 'O';

            game.setGameBoard(updatedGameBoard);
            game.setCnt(game.getCnt() + 1);
            game.setPlayer1Turn(!game.isPlayer1Turn());
            game.setLastUpdatedInMilli(System.currentTimeMillis());

            if(isWinnerFound(game)) game.setStatus("WINNER");
            else if(isDraw(game)) game.setStatus("DRAW");

            games.put(game_id, game);
            return new GameResponseDto(game);
      }

      public boolean isWinnerFound(Game game) {
            if(game.getCnt() >= 5) {
                  int[][] lines = {
                          {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
                          {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
                          {0, 4, 8}, {2, 4, 6}
                  };

                  char[] gameBoard = game.getGameBoard();
                  for(int i = 0; i < 8; i++) {
                        int[] line = lines[i];
                        if(gameBoard[line[0]] == gameBoard[line[1]]
                                && gameBoard[line[1]] == gameBoard[line[2]]
                                && gameBoard[line[0]] != ' ') return true;
                  }
            }
            return false;
      }

      public boolean isDraw(Game game) {
            return game.getCnt() == 9;
      }

      @Scheduled(fixedRate = 10 * 1000)
      public void deleteExpiredGames() {
            long currTime = System.currentTimeMillis();
            games.entrySet().removeIf(entry -> (currTime - entry.getValue().getLastUpdatedInMilli()) >= 60000);
      }
}
