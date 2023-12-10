package com.mycompany.rentCar.Repositories;

import com.mycompany.rentCar.Entities.Cars;
import com.mycompany.rentCar.Entities.Reservation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarsRepository extends CrudRepository<Cars, Long> {
    List<Cars> findByName(String name);
    List<Cars> findByModel(String model);
    List<Cars> findByAddress(String address);
    List<Cars> findByModelAndAddress(String model, String address);
    List<Cars> findByAgencyId(Long userId);

    //List<Reservation> findAllByCarId(Long carId);

}
