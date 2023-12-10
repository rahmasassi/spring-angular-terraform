import { Component, OnInit } from '@angular/core';
import { Car } from '../../../Models/car.model';
import { CarDTO } from 'src/app/Models/CarDTO';
import { CarsService } from 'src/app/services/cars.service';
import { AuthenticateService } from 'src/app/services/authenticate.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-list-voiture-agence',
  templateUrl: './list-voiture-agence.component.html',
  styleUrls: ['./list-voiture-agence.component.css']
})
export class ListVoitureAgenceComponent implements OnInit {
  cars: CarDTO[] = [];

  constructor(private carService: CarsService, private authService: AuthenticateService, private router : Router) {}

  ngOnInit(): void {
    this.getCarsByAgency();
  }

  getCarsByAgency() {
    const agencyId = this.authService.getCurrentUserId();
    this.carService.getCarsByAgencyId(agencyId).subscribe(
      (cars) => {
        this.cars = cars;
      },
      (error) => {
        console.error('Error getting cars by agency ID', error);
      }
    );
  }

  getImageUrl(car: CarDTO): string {
    if (car.imageData) {
      const base64Image = 'data:image/' + car.fileType + ';base64,' + car.imageData;
      return base64Image;
    }
    return '';
  }

  deleteCar(car: CarDTO): void {
    if (car && car.id) {
      this.carService.delete(car.id).subscribe(
        () => {
          console.log(`Car with ID ${car.id} deleted successfully.`);
          this.getCarsByAgency();
        },
        (error) => {
          console.error('Error deleting car:', error);
        }
      );
    } else {
      console.error('Invalid car data for deletion.', car);
    }
  }
  
  editCar(car: CarDTO): void {
    if (car && car.id) {
      this.router.navigate(['/edit-car', car.id]);
    } else {
      console.error('Invalid car data for editing.', car);
    }
    
  }
}