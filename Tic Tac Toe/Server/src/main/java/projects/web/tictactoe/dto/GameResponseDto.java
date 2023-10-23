package projects.web.tictactoe.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import projects.web.tictactoe.model.Game;

@Data
@NoArgsConstructor
public class GameResponseDto {
      private char[] gameBoard;
      private int cnt;
      private String player1;
      private String player2;
      private boolean isPlayer1Turn;
      private String status;

      public GameResponseDto(Game game) {
            this.gameBoard = game.getGameBoard();
            this.cnt = game.getCnt();
            this.player1 = game.getPlayer1();
            this.player2 = game.getPlayer2();
            this.isPlayer1Turn = game.isPlayer1Turn();
            this.status = game.getStatus();
      }
}
