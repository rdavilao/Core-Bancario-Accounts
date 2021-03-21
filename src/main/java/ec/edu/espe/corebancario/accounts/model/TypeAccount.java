package ec.edu.espe.corebancario.accounts.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "type_account")
public class TypeAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COD_TYPE_ACCOUNT", nullable = false)
    private Integer codigo;

    @Column(name = "NAME", nullable = false, length = 16)
    private String name;

    @Column(name = "TYPE", nullable = false, length = 12)
    private String type;
}
