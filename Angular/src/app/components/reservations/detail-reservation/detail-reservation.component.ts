import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ReservationCarUserDTO } from 'src/app/Models/reservation-car-user-dto.model';
import { ReservationService } from 'src/app/services/reservation.service';

@Component({
  selector: 'app-detail-reservation',
  templateUrl: './detail-reservation.component.html',
  styleUrls: ['./detail-reservation.component.css']
})
export class DetailReservationComponent implements OnInit{

  reservation: any;

  constructor (private reservationService: ReservationService,
    private route: ActivatedRoute,
    private router: Router) {}

  ngOnInit(): void {
    const id = this.route.snapshot.params['id'];
    this.reservationService.getReservationById(id).subscribe(
      (response) => {
        this.reservation = response;
        console.log("reservationDetail : ",response);
      },
      (error) => {
        console.error('Error : ', error);
      }
    );
  }

  getImageUrl(car: ReservationCarUserDTO): string {
    if (car.imageData) {
      const base64Image = 'data:image/' + car.fileType + ';base64,' + car.imageData;
      return base64Image;
    }
    return '';
  }

  updateStatus(): void {
    this.reservationService.updateReservation(this.reservation.id, this.reservation.status)
      .subscribe(
        () => {
          console.log('Reservation status updated successfully');
        },
        (error) => {
          console.error('Error updating reservation status:', error);
        }
      );
  }

}
