/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.corebancario.accounts.model;

import java.io.Serializable;
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

@Entity
@Table(name = "account", catalog = "corebancario-accounts", schema = "", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"NUMBER"})})

public class Account implements Serializable {

    private static final long serialVersionUID = 1L;
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
    @ManyToOne
    private Integer type;

    public Account() {
    }

    public Account(Integer codAccount) {
        this.codigo = codAccount;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getClientIdentification() {
        return clientIdentification;
    }

    public void setClientIdentification(String clientIdentification) {
        this.clientIdentification = clientIdentification;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<CreditCard> getCreditCardList() {
        return creditCardList;
    }

    public void setCreditCardList(List<CreditCard> creditCardList) {
        this.creditCardList = creditCardList;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigo != null ? codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Account)) {
            return false;
        }
        Account other = (Account) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.edu.espe.corebancario.accounts.model.Account[ codAccount=" + codigo + " ]";
    }
    
}
