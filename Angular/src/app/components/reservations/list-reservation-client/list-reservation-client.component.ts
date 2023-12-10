import { Component, OnInit } from '@angular/core';
import { CarReservationClient } from '../../../Models/list-reservation-client.model';
import { Reservation } from 'src/app/Models/reservation';
import { ReservationService } from 'src/app/services/reservation.service';
import { AuthenticateService } from 'src/app/services/authenticate.service';
import { ReservationCarAgencyDTO } from 'src/app/Models/reservation-car-agency-dto.model';

@Component({
  selector: 'app-list-reservation-client',
  templateUrl: './list-reservation-client.component.html',
  styleUrls: ['./list-reservation-client.component.css']
})
export class ListReservationClientComponent implements OnInit {

  reservations: ReservationCarAgencyDTO[] = [];

  constructor(private reservationService: ReservationService, private authService: AuthenticateService) {}

  ngOnInit(): void {
    const userId = this.authService.getCurrentUserId();
    console.log("id user", userId);
    this.reservationService.getReservationsByUserId(userId).subscribe(
      (reservations) => {
        this.reservations = reservations;
        console.log('Reservations:', this.reservations);
      },
      (error) => {
        console.error('Error fetching reservations:', error);
      }
    );
  }
}