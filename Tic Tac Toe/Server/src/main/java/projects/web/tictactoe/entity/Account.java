package projects.web.tictactoe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Account implements UserDetails {
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private long id;

      @Column(unique = true)
      private String username;
      private String password;
      private int won;
      private int loss;
      private int draw;

      public Account(String username, String password) {
            this.username = username;
            this.password = password;
            this.won = 0;
            this.loss = 0;
            this.draw = 0;
      }

      @Override
      public Collection<? extends GrantedAuthority> getAuthorities() {
            return null;
      }

      @Override
      public boolean isAccountNonExpired() {
            return true;
      }

      @Override
      public boolean isAccountNonLocked() {
            return true;
      }

      @Override
      public boolean isCredentialsNonExpired() {
            return true;
      }

      @Override
      public boolean isEnabled() {
            return true;
      }
}
