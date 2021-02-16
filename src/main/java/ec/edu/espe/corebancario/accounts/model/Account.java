/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.corebancario.accounts.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "account", catalog = "corebancario", schema = "", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"NUMBER"})})

public class Account implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COD_ACCOUNT", nullable = false)
    private Integer code;
    @Column(name = "CLIENT_IDENTIFICATION", nullable = false, length = 13)
    private String clientIdentification;
    @Column(name = "TYPE", nullable = false, length = 32)
    private String type;
    @Column(name = "STATUS", nullable = false, length = 3)
    private String status;
    @Column(name = "NUMBER", nullable = false, length = 12)
    private String number;
    @Column(name = "CREATION_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "CURRENT_BALANCE", nullable = false, precision = 14, scale = 2)
    private BigDecimal currentBalance;

    public Account() {
    }

    public Account(Integer codAccount) {
        this.code = codAccount;
    } 

    public Integer getCodAccount() {
        return code;
    }

    public void setCodAccount(Integer codAccount) {
        this.code = codAccount;
    }

    public String getClientIdentification() {
        return clientIdentification;
    }

    public void setClientIdentification(String clientIdentification) {
        this.clientIdentification = clientIdentification;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (code != null ? code.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Account)) {
            return false;
        }
        Account other = (Account) object;
        if ((this.code == null && other.code != null) || (this.code != null && !this.code.equals(other.code))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Account{" + "code=" + code + ", clientIdentification=" + clientIdentification + ", type=" + type + ", status=" + status + ", number=" + number + ", creationDate=" + creationDate + ", currentBalance=" + currentBalance + '}';
    }

    
}
