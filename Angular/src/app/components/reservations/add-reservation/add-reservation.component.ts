import { formatDate } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CarDTO } from 'src/app/Models/CarDTO';
import { Reservation } from 'src/app/Models/reservation';
import { AuthenticateService } from 'src/app/services/authenticate.service';
import { CarsService } from 'src/app/services/cars.service';
import { ReservationService } from 'src/app/services/reservation.service';


@Component({
  selector: 'app-add-reservation',
  templateUrl: './add-reservation.component.html',
  styleUrls: ['./add-reservation.component.css']
})
export class AddReservationComponent implements OnInit{
  listDateReserv:string[]=[];
  numberOfDays: number = 0;
  totalPrice: number = 0;

  minDate= new Date(2023, 9, 9);
  maxDate= new Date(2023, 11, 31);

  myDateFilter = (m: Date| null): boolean => {
    let list :number[]=[];
    this.listDateReserv.forEach(element => {

    let xx=new Date(element);
    let reservation=new Date(xx.getFullYear(), xx.getMonth(),xx.getDate())
    list.push(Date.parse(reservation.toISOString()))

  });
    return !list.includes(Date.parse(m!.toISOString()));
  }

  reservation: any = {};

  constructor(private reservationService: ReservationService,
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthenticateService,
    private carService: CarsService) { }

    addReservation() {
      const reservation: Reservation = {
        address: this.reservation.address,
        registration: this.reservation.registration,
        phone: this.reservation.phone,
        dateDebut: this.reservation.dateDebut,
        dateFin: this.reservation.dateFin,
        status: this.reservation.Status,
        priceTt: this.totalPrice
      };
      const userId = this.authService.getCurrentUserId();
      console.log("id user", userId);
      const carId = this.route.snapshot.params['id'];

      this.reservationService.addReservation(carId, userId, reservation).subscribe(
      (response) => {
          console.log('Reservation added successfully:', reservation.dateDebut);
          console.log('Réponse de la requête HTTP :', response);
          this.reservation = {};
          
        },
        (error) => {
          console.error('Erreur de la requête HTTP :', error);
          this.router.navigate(['/list-voiture-user']);
        }
      );

  }

  ngOnInit(): void {

    const carId = this.route.snapshot.params['id'];
    this.listDateReserv=[];
    this.reservationService.getAvailableDates(carId).subscribe(
      (response) => {
        console.log('Dates How are Not available :', response);
          this.listDateReserv=response;
      },
      (error) => {
        console.error('Error getting dates :', error);
      }
    );
  }

  calculateTotalPrice(): string {
    if (this.reservation.dateDebut && this.reservation.dateFin) {
      const startDate = new Date(this.reservation.dateDebut);
      const endDate = new Date(this.reservation.dateFin);
      const timeDifference = Math.abs(endDate.getTime() - startDate.getTime());
      this.numberOfDays = Math.ceil(timeDifference / (1000 * 3600 * 24)) + 1;
  
      const carId = this.route.snapshot.params['id'];
  
      this.carService.getCarById(carId).subscribe(
        (car: CarDTO) => {
          let carPrice = car.price_per_day;
          this.totalPrice = carPrice * this.numberOfDays;
        },
        (error) => {
          console.error('Error getting car details:', error);
        }
      );
  
      return this.totalPrice.toFixed(2);
    } else {
      return 'Please select start and end dates';
    }
  }
  
}
