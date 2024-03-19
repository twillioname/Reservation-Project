package com.reservationapp.payload;


import com.reservationapp.entity.Driver;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusDto {
    private Long id;
    private String busNumber;
    private String busType;
    private double price;
    private int totalSeats;
    private int availableSeats;
   private RouteDto route;
//    private String FromDate;
//    private String ToDate;
    // Assuming you have a RouteDto class

    private List<SubRouteDto> subRoutes; // Assuming you have a SubRouteDto class

    // Constructors, getters, and setters
}
