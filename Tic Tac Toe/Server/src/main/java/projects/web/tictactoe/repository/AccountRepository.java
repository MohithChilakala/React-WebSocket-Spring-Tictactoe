package projects.web.tictactoe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import projects.web.tictactoe.entity.Account;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
      Optional<Account> findByUsername(String username);

      @Modifying
      @Query(value = "UPDATE Account a SET a.won = a.won + 1 WHERE a.username = :username")
      void updateWon(String username);

      @Modifying
      @Query(value = "UPDATE Account a SET a.loss = a.loss + 1 WHERE a.username = :username")
      void updateLoss(String username);

      @Modifying
      @Query(value = "UPDATE Account a SET a.draw = a.draw + 1 WHERE a.username = :username")
      void updateDraw(String username);
}
