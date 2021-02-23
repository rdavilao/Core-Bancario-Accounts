/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.corebancario.accounts.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Table(name = "account", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"NUMBER"})})
public class Account{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COD_ACCOUNT", nullable = false)
    private Integer codigo;
    
    @Column(name = "CLIENT_IDENTIFICATION", nullable = false, length = 13)
    private String clientIdentification;
    
    @Column(name = "CREATION_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    
    @Column(name = "NUMBER", nullable = false, length = 12)
    private String number;
    
    @Column(name = "BALANCE", nullable = false)
    private BigDecimal balance;
    
    @Column(name = "STATUS", nullable = false, length = 3)
    private String status;
    
    @OneToMany(mappedBy = "codAccount")
    private List<CreditCard> creditCardList;
    
    @JoinColumn(name = "TYPE", referencedColumnName = "COD_TYPE_ACCOUNT")
    private Integer type;
}
