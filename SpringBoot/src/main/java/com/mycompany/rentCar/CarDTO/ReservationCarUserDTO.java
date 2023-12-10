package com.mycompany.rentCar.CarDTO;

import com.mycompany.rentCar.Entities.Image;
import com.mycompany.rentCar.Entities.Reservation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationCarUserDTO {

    private Long id;
    private Date dateDebut;
    private Date dateFin;

    private String username;

    private String model;
    private String Status;

    private float priceTt;
    private String name;
    private String address;
    private String phone;

    private long imageId;
    private String imageName;
    private String imageFileType;
    private byte[] imageData;

    public ReservationCarUserDTO(Reservation reservation) {
        this.id=reservation.getId();
        this.dateDebut=reservation.getDateDebut();
        this.dateFin=reservation.getDateFin();
        this.model = reservation.getCar().getModel();
        this.username = reservation.getUser().getUsername();
        this.Status= reservation.getStatus();
        this.priceTt= reservation.getPriceTt();
        this.name=reservation.getCar().getName();
        this.phone=reservation.getPhone();
        this.address=reservation.getAddress();

        Image image = reservation.getCar().getImage();
        if (image != null) {
            this.imageId = image.getId();
            this.imageName = image.getFileName();
            this.imageFileType = image.getFileType();
            this.imageData= image.getData();
        }
    }
}
