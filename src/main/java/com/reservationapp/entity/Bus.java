package com.reservationapp.entity;


import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="bus")
public class  Bus {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @Column(name = "bus_number",unique = true)
        private String busNumber;
        private String busType;
        private double price;
        private int totalSeats;
        private int availableSeats;
}
