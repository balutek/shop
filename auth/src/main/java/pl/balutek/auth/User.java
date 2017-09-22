package pl.balutek.auth;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue
    private long id;

    @NotNull
    private String login;

    // TODO: Use better hashing alghoritm like PBKDF2.
    // For now using SHA512 to simplify authentication task.
    @NotNull
    private String passwordHash;
}
