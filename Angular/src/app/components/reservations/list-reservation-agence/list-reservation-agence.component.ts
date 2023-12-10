import { Component, OnInit } from '@angular/core';
import { CarReservationAgence } from '../../../Models/list-reservation-agence.model';
import { format } from 'date-fns';
import { Reservation } from 'src/app/Models/reservation';
import { ReservationService } from 'src/app/services/reservation.service';
import { AuthenticateService } from 'src/app/services/authenticate.service';
import { ReservationCarUserDTO } from 'src/app/Models/reservation-car-user-dto.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-list-reservation-agence',
  templateUrl: './list-reservation-agence.component.html',
  styleUrls: ['./list-reservation-agence.component.css']
})
export class ListReservationAgenceComponent implements OnInit {

  reservations: ReservationCarUserDTO[] = [];

  constructor(private reservationService: ReservationService, private authService: AuthenticateService,
    private router: Router) {}

  ngOnInit(): void {
    const agencyId = this.authService.getCurrentUserId();
    console.log("id user", agencyId);
    this.loadReservations(agencyId);
  }

  loadReservations(agencyId: number): void {
    this.reservationService.getReservationsByAgency(agencyId).subscribe(
      (reservations) => {
        this.reservations = reservations;
        console.log('Reservations:', this.reservations);
      },
      (error) => {
        console.error('Error fetching reservations:', error);
      }
    );
  }
  
  detailReservation(reservation: ReservationCarUserDTO):void{
    if (reservation && reservation.id){
      this.router.navigate(['/detail-reservation', reservation.id]);
      console.log("objet", reservation);
      console.log("id", reservation.id);
      
      
    }else{
      console.error('Invalid reservation data.', reservation);
    }
  }
  
}