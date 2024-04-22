package jagongadpro.autentikasi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "users")
public class User {
    @Id
    String email;

    String username;

    String password;

    String profileUrl;

    Integer saldo;

    String bio;

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
