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
    private String flatBuilNo;

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
    private String pincode;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "STATE_ID")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private StateEntity state;

    @Column(name = "ACTIVE")
    @NotNull
    private int active;

    public AddressEntity() {
        super();
    }

    public AddressEntity(String uuid, String flatBuilNo, String locality, String city, String pincode, StateEntity state) {
        super();
        this.uuid = uuid;
        this.flatBuilNo = flatBuilNo;
        this.locality = locality;
        this.city = city;
        this.pincode = pincode;
        this.state = state;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFlatBuilNo() {
        return flatBuilNo;
    }

    public void setFlatBuilNo(String flatBuilNo) {
        this.flatBuilNo = flatBuilNo;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pinCode) {
        this.pincode = pinCode;
    }

    public StateEntity getState() {
        return state;
    }

    public void setState(StateEntity state) {
        this.state = state;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "AddressEntity{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", flatBuilNo='" + flatBuilNo + '\'' +
                ", locality='" + locality + '\'' +
                ", city='" + city + '\'' +
                ", pincode='" + pincode + '\'' +
                ", state=" + state +
                ", active=" + active +
                '}';
    }
}
