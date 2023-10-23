package projects.web.tictactoe.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Game {
      private char[] gameBoard;
      private int cnt;
      private String host;
      private String password;
      private String player1;
      private String player2;
      private boolean isPlayer1Turn;
      private String status;
      private long lastUpdatedInMilli;
}
