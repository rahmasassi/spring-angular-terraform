package com.mycompany.rentCar.Services.Impl;

import com.mycompany.rentCar.CarDTO.ReservationCarAgencyDTO;
import com.mycompany.rentCar.Entities.Agency;
import com.mycompany.rentCar.Entities.AppUser;
import com.mycompany.rentCar.Entities.Cars;
import com.mycompany.rentCar.Entities.Reservation;
import com.mycompany.rentCar.Repositories.AgencyRepository;
import com.mycompany.rentCar.Repositories.CarsRepository;
import com.mycompany.rentCar.Repositories.ReservationRepository;
import com.mycompany.rentCar.Repositories.UserRepository;
import com.mycompany.rentCar.Services.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
@AllArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;

    private final UserRepository userRepository;

    private final CarsRepository carsRepository;

    private final AgencyRepository agencyRepository;

    @Override
    public void addReservation(Long carId, Reservation reservation, Long userId) {
        try {
            Optional<Cars> optionalCar = carsRepository.findById(carId);

        if (optionalCar.isPresent()) {
            Cars car = optionalCar.get();

            Optional<AppUser> optionalUser = userRepository.findById(userId);
            if (optionalUser.isPresent()) {
                AppUser user = optionalUser.get();

                Reservation newReservation = new Reservation();
                newReservation.setDateDebut(reservation.getDateDebut());
                newReservation.setDateFin(reservation.getDateFin());
                newReservation.setAddress(reservation.getAddress());
                newReservation.setPhone(reservation.getPhone());
                newReservation.setRegistration(reservation.getRegistration());
                newReservation.setStatus("Pending ...");
                newReservation.setCar(car);
                newReservation.setUser(user);
                newReservation.setPriceTt(reservation.getPriceTt());

                reservationRepository.save(newReservation);
            }}} catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
    }
    @Override
    public List<Reservation> getAllReservation(){ return (List<Reservation>) reservationRepository.findAll();}

    @Transactional
    public List<Reservation> getReservationsByCarId(long carId) {
        return reservationRepository.findByCarId(carId);
    }

    @Override
    public List<ReservationCarAgencyDTO> getReservationByUserId(Long userId) {
        List<Reservation> reservations = reservationRepository.findByUserId(userId);
        List<ReservationCarAgencyDTO> reservationDTOs = new ArrayList<>();

        for (Reservation reservation : reservations) {
            Long agencyId = reservation.getCar().getAgencyId();
            Optional<Agency> agency = this.getAgencyById(agencyId);

            if (agency.isPresent()) {
                String agencyUsername = agency.get().getUsername();
                String agencyAddress = agency.get().getAddress();
                String agencyNumber = agency.get().getNumber();

                ReservationCarAgencyDTO reservationDTO = new ReservationCarAgencyDTO(reservation, agencyUsername, agencyAddress, agencyNumber);
                reservationDTOs.add(reservationDTO);
            }
        }
        return reservationDTOs;
    }

    @Override
    public List<Cars> getReservationByAgency(Long agencyId) {
        return carsRepository.findByAgencyId(agencyId);
    }

    @Override
    public void updateReservation(Long reservationId, String newStatus) {
        Optional<Reservation> reservation = reservationRepository.findById(reservationId);
        if (reservation.isPresent()) {
            Reservation reser = reservation.get();
            reser.setStatus(newStatus);
            reservationRepository.save(reser);
        }
    }

    @Override
    public Reservation getReservationById(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElse(null);
    }

    @Override
    public Optional<Agency> getAgencyById(Long agencyId) {
        return agencyRepository.findById(agencyId);
    }
}
