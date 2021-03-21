package ec.edu.espe.corebancario.accounts.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "credit_card", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"NUMBER"})})
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COD_CARD", nullable = false)
    private Integer codigo;

    @Column(name = "NUMBER", nullable = false, length = 18)
    private String number;

    @Column(name = "LIMIT_ACCOUNT")
    private Integer limitAccount;

    @Column(name = "CVV", length = 64)
    private String cvv;

    @Column(name = "EXPIRATION_DATE")
    @Temporal(TemporalType.DATE)
    private Date expirationDate;

    @Column(name = "CREATION_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Column(name = "STATUS", length = 3)
    private String status;

    @JoinColumn(name = "COD_ACCOUNT", referencedColumnName = "COD_ACCOUNT")
    private Integer codAccount;
}
