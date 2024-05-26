package jagongadpro.autentikasi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@NoArgsConstructor
@Table(name = "users")
public class User implements UserDetails {
    @Getter
    @Setter
    @Id
    String email;

    @Setter
    String username;

    @Getter
    @Setter
    String password;

    @Getter
    @Setter
    String profileUrl;

    @Getter
    @Setter
    Integer saldo;

    @Getter
    @Setter
    String bio;

    @Setter
    @Getter
    String status = "PEMBELI";

    public User(Builder builder){
        this.email = builder.email;
        this.username = builder.username;
        this.password = builder.password;
        this.profileUrl = builder.profileUrl;
        this.saldo = builder.saldo;
        this.bio = builder.bio;
        this.status = builder.status;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }


    //buat jwt aja yaaa
    @Override
    public String getUsername() {
        return email;
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

    public String getUsernameReal() {
        return username;
    }

    public static class Builder{
        String email;

        String username;

        String password;
        String profileUrl;
        Integer saldo;
        String bio;
        String status="PEMBELI";

        public Builder email(String email){
            this.email = email;
            return this;
        }
        public Builder username(String username){
            this.username = username;
            return this;
        }
        public Builder password(String password){
            this.password = password;
            return this;
        }
        public Builder profileUrl(String profileUrl){
            this.profileUrl = profileUrl;
            return this;
        }
        public Builder status(String status){
            this.status = status;
            return this;
        }
        public Builder bio(String bio){
            this.bio = bio;
            return this;
        }
        public Builder saldo(Integer saldo){
            this.saldo = saldo;
            return this;
        }
        public User build() {
            User user=  new User(this);
            return user;
        }


    }
}
