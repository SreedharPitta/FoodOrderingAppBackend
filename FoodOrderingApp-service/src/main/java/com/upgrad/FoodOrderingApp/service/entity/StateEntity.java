package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "state")
@NamedQueries({

})
public class StateEntity implements Serializable {

    @Column(name = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "UUID")
    @NotNull
    @Size(max = 200)
    private String uuid;

    @Column(name = "STATE_NAME")
    @NotNull
    @Size(max = 30)
    private String stateName;

    public StateEntity() {
        super();
    }

    public StateEntity(@NotNull @Size(max = 200) String uuid, @NotNull @Size(max = 30) String stateName) {
        super();
        this.uuid = uuid;
        this.stateName = stateName;
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

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    @Override
    public String toString() {
        return "StateEntity{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", stateName='" + stateName + '\'' +
                '}';
    }
}
