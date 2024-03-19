package com.reservationapp.payload;


import lombok.Data;



@Data
public class SubRouteDto {

    private Long Id;
    private String fromLocation;
    private String toLocation;
    private String fromDate;
    private String toDate;
    private String totalDuration;
    private String fromTime;
    private String toTime;

}
