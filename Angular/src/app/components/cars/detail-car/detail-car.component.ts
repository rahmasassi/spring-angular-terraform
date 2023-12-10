import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CarDTO } from 'src/app/Models/CarDTO';
import { CarsService } from 'src/app/services/cars.service';

@Component({
  selector: 'app-detail-car',
  templateUrl: './detail-car.component.html',
  styleUrls: ['./detail-car.component.css']
})
export class DetailCarComponent implements OnInit{

  car: any;

  constructor (private carsService: CarsService,
    private route: ActivatedRoute,
    private router: Router) {}

  ngOnInit(): void {
    const carId = this.route.snapshot.params['id'];
    this.carsService.getCarById(carId).subscribe(
      (response) => {
        this.car = response;
        console.log("carDetail : ",response);
      },
      (error) => {
        console.error('Error : ', error);
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

  reserveCar(car: CarDTO):void{
    if (car && car.id){
      this.router.navigate(['/reservation', car.id]);
    }else{
      console.error('Invalid car data for reserving.', car);
    }
  }

}
