package projects.web.tictactoe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameStatsDto {
      private String player1;
      private String player2;
      private int won;
      private int loss;
      private int draw;
}
