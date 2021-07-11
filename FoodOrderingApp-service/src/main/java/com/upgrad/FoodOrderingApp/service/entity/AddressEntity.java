package com.upgrad.FoodOrderingApp.service.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "address")
@NamedQueries({
})
public class AddressEntity implements Serializable {

    @Column(name = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "UUID")
    @NotNull
    @Size(max = 200)
    private String uuid;

    @Column(name = "FLAT_BUIL_NUMBER")
    @NotNull
    @Size(max = 255)
    private String flatBuildingNumber;

    @Column(name = "LOCALITY")
    @NotNull
    @Size(max = 255)
    private String locality;

    @Column(name = "CITY")
    @NotNull
    @Size(max = 30)
    private String city;

    @Column(name = "PINCODE")
    @NotNull
    @Size(max = 30)
    private String pinCode;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "STATE_ID")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private StateEntity state;

    @Column(name = "ACTIVE")
    @NotNull
    private int active;

}
