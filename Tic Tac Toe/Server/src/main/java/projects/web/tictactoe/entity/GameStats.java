package projects.web.tictactoe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class GameStats {
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private long id;

      @ManyToOne private Account player1;
      @ManyToOne private Account player2;

      private int won;
      private int loss;
      private int draw;

      public GameStats(Account player1, Account player2) {
            this.player1 = player1;
            this.player2 = player2;
            this.won = 0;
            this.loss = 0;
            this.draw = 0;
      }
}
