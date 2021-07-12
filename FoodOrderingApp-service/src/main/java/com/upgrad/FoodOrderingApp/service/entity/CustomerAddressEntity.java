package com.upgrad.FoodOrderingApp.service.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "customer_address")
@NamedQueries({
        @NamedQuery(name = "allCustomerAddresses", query = "select ca from CustomerAddressEntity ca where ca.customer=:customer"),
        @NamedQuery(name = "customerAddressByAddress", query = "select ca from CustomerAddressEntity ca where ca.address=:address")
})
public class CustomerAddressEntity implements Serializable {

    @Column(name = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "CUSTOMER_ID")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CustomerEntity customer;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "ADDRESS_ID")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private AddressEntity address;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

    public AddressEntity getAddress() {
        return address;
    }

    public void setAddress(AddressEntity address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "CustomerAddressEntity{" +
                "id=" + id +
                ", customer=" + customer +
                ", address=" + address +
                '}';
    }
}
