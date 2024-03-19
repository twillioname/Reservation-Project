package com.reservationapp.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "passengers")
public class Passenger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;

    @Column(name = "bus_id", unique = true)
    private long busId;

    @Column(name = "route_id",unique = true)
    private long routeId;



}
