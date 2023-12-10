package com.mycompany.rentCar.CarDTO;

import com.mycompany.rentCar.Entities.Reservation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationCarAgencyDTO {
    private Long id;
    private Date dateDebut;
    private Date dateFin;
    private String Status;
    private float priceTt;

    private String model;

    private Long agencyId;

    private String agencyUsername;
    private String agencyAddress;

    private String agencyNumber;

    public ReservationCarAgencyDTO (Reservation reservation, String agencyUsername, String agencyAddress, String agencyNumber) {
        this.id=reservation.getId();
        this.dateDebut=reservation.getDateDebut();
        this.dateFin=reservation.getDateFin();
        this.Status= reservation.getStatus();
        this.priceTt= reservation.getPriceTt();

        this.model = reservation.getCar().getModel();

        this.agencyId=reservation.getCar().getAgencyId();
        this.agencyUsername = agencyUsername;
        this.agencyAddress = agencyAddress;
        this.agencyNumber = agencyNumber;


    }
}
