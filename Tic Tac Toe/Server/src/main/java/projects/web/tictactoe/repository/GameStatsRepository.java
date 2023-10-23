package projects.web.tictactoe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import projects.web.tictactoe.entity.GameStats;

import java.util.Optional;

public interface GameStatsRepository extends JpaRepository<GameStats, Long> {

      @Query("SELECT g FROM GameStats g WHERE g.player1.username = :player1 AND g.player2.username = :player2 " +
              "OR g.player1.username = :player2 AND g.player2.username = :player1 ")
      Optional<GameStats> findGameStats(String player1, String player2);

      @Modifying
      @Query("Update GameStats g SET g.won = g.won + 1 WHERE g.player1.username = :player1 AND g.player2.username = :player2")
      void GameWon(String player1, String player2);

      @Modifying
      @Query("Update GameStats g SET g.loss = g.loss + 1 WHERE g.player1.username = :player1 AND g.player2.username = :player2")
      void GameLoss(String player1, String player2);

      @Modifying
      @Query("Update GameStats g SET g.draw = g.draw + 1 WHERE g.player1.username = :player1 AND g.player2.username = :player2")
      void GameDraw(String player1, String player2);
}
