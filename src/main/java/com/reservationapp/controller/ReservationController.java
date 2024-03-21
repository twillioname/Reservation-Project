package com.reservationapp.controller;

import com.reservationapp.entity.Bus;
import com.reservationapp.entity.Passenger;
import com.reservationapp.entity.Route;
import com.reservationapp.entity.SubRoute;
import com.reservationapp.exception.ResourceNotFound;
import com.reservationapp.payload.ReservationDto;
import com.reservationapp.repository.BusRepository;
import com.reservationapp.repository.PassengerRepository;
import com.reservationapp.repository.RouteRepository;
import com.reservationapp.repository.SubRouteRepository;
import com.reservationapp.util.EmailService;
import com.reservationapp.util.ExcelGeneratorService;
import com.reservationapp.util.PdfTicketGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservation")
public class ReservationController {
    @Autowired
    private PassengerRepository passengerRepository;
     @Autowired
    private BusRepository busRepository;
     @Autowired
     private RouteRepository routeRepository;
     @Autowired
     private PdfTicketGeneratorService pdfTicketGeneratorService;
     @Autowired
     private SubRouteRepository subRouteRepository;
     @Autowired
     private EmailService emailService;
     @Autowired
     private ExcelGeneratorService excelGeneratorService;

     //http://localhost:8080/api/reservation?busId=1&routeId=5

     @PostMapping
     public ResponseEntity<String> bookTicket(
             @RequestParam long busId,
             @RequestParam long routeId,

             @RequestBody Passenger passenger
     ){
//         Bus bus  = busRepository.findById(busId).orElseThrow(
//                 ()-> new ResourceNotFound("This booking is unavailable!!")
//         );
//
//         Route route  = routeRepository.findById(routeId).orElseThrow(
//                 ()-> new ResourceNotFound("This booking is unavailable!!")
//         );

         boolean busIsPresent = false;
         boolean routeIsPresent=false;
         boolean subRouteIsPresent=false;
         Optional<Bus> byId = busRepository.findById(busId);
         if (byId.isPresent()){
             busIsPresent=true;
             Bus bus = byId.get();
         }
         Optional<Route> byRouteId = routeRepository.findById(routeId);
         if (byRouteId.isPresent()){
             routeIsPresent=true;
             Bus bus = byId.get();
         }

         Optional<SubRoute> bySubRouteId = subRouteRepository.findById(routeId);
         if (byRouteId.isPresent()){
             subRouteIsPresent=true;
             Bus bus = byId.get();
         }

         if (busIsPresent&&routeIsPresent  || busIsPresent&&subRouteIsPresent){
             //Add passenger details
             Passenger p = new Passenger();
             p.setFirstName(passenger.getFirstName());
             p.setLastName(passenger.getLastName());
             p.setEmail(passenger.getEmail());
             p.setMobile(passenger.getMobile());
             p.setRouteId(routeId);
             p.setBusId(busId);
             Passenger savedPassenger = passengerRepository.save(p);
             byte[] pdfBytes = pdfTicketGeneratorService.generateTicket(savedPassenger,
                     byRouteId.get().getFromLocation(),
                     byRouteId.get().getToLocation(),
                     byRouteId.get().getFromDate());
             emailService.sendEmailWithAttachment(passenger.getEmail(),
                     "Booking Confirmed..", "You Reservation id"+savedPassenger.getId()
                     ,pdfBytes,"ticket");

         }


         return new ResponseEntity<>("Done....", HttpStatus.CREATED);

//http://localhost:8080/api/reservation/passengers/excel

     }
    @GetMapping("/passengers/excel")
    public ResponseEntity<byte[]> generateExcel(){
        try {
            //Assuming you have a method to fetch passengers from database
            List<Passenger> passengers = fetchPassengersFromDatabase();
            byte[] excelBytes = excelGeneratorService.generatePassengerExcel(passengers);

            HttpHeaders headers= new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment","passenger_data.xlsx");
            return new ResponseEntity<>(excelBytes,headers,HttpStatus.OK);

        } catch (IOException e) {
           e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private List<Passenger> fetchPassengersFromDatabase() {
         return passengerRepository.findAll();
    }
}
