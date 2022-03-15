package net.therap.mealsystem.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author sheikh.ishrak
 * @since 10/03/2022
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "account_confirmation_token")
public class AccountConfirmationToken extends Persistent {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String token;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date expiration;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
