package com.mycompany.rentCar.Services;

import com.mycompany.rentCar.CarDTO.ReservationCarAgencyDTO;
import com.mycompany.rentCar.CarDTO.ReservationDTO;
import com.mycompany.rentCar.Entities.Agency;
import com.mycompany.rentCar.Entities.Cars;
import com.mycompany.rentCar.Entities.Reservation;
import com.mycompany.rentCar.Repositories.ReservationRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationService {

    List<Reservation> getAllReservation();

    public List<Reservation> getReservationsByCarId(long carId);

    @Transactional
    List<ReservationCarAgencyDTO> getReservationByUserId(Long userId);

    @Transactional
    List<Cars> getReservationByAgency(Long agencyId);

    void addReservation(Long carId, Reservation reservation, Long userId);

    void updateReservation(Long reservationId, String newStatus);

    Reservation getReservationById(Long reservationId);

    Optional<Agency> getAgencyById(Long agencyId);
}
