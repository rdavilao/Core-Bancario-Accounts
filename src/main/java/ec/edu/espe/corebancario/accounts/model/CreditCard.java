/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.corebancario.accounts.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "credit_card", catalog = "corebancario-accounts", schema = "", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"NUMBER"})})
public class CreditCard implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COD_CARD", nullable = false)
    private Integer codigo;
    
    @Column(name = "NUMBER", nullable = false, length = 18)
    private String number;
    
    @Column(name = "LIMIT_ACCOUNT")
    private Integer limitAccount;
    
    @Column(name = "CVV", length = 3)
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
    @ManyToOne
    private Integer codAccount;

    public CreditCard() {
    }

    public CreditCard(Integer codCard) {
        this.codigo = codCard;
    }
    
    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getLimitAccount() {
        return limitAccount;
    }

    public void setLimitAccount(Integer limitAccount) {
        this.limitAccount = limitAccount;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCodAccount() {
        return codAccount;
    }

    public void setCodAccount(Integer codAccount) {
        this.codAccount = codAccount;
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
        if (!(object instanceof CreditCard)) {
            return false;
        }
        CreditCard other = (CreditCard) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.edu.espe.corebancario.accounts.model.CreditCard[ codCard=" + codigo + " ]";
    }
    
}
