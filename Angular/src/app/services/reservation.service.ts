import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, tap, throwError } from 'rxjs';
import { ReservationCarUserDTO } from '../Models/reservation-car-user-dto.model';
import { Reservation } from '../Models/reservation';

import { AuthenticateService } from './authenticate.service';
import { ReservationCarAgencyDTO } from '../Models/reservation-car-agency-dto.model';

@Injectable({
  providedIn: 'root'
})
export class ReservationService {
  private apiUrl = 'http://localhost:8081/api/reservation';

  constructor(private http: HttpClient, private authService: AuthenticateService) { }

  addReservation(carId: number, userId: number, reservationData: any): Observable<any> {
    const url = `${this.apiUrl}/addReservation/${carId}?userId=${userId}`;
    return this.http.post(url, reservationData).pipe(
      tap(
        response => console.log('Error request HTTP :', response),
        error => console.error('Error request :', error)
      )
    );
  }  
  
  getAvailableDates(carId:number):Observable<any>{
    return this.http.get(`${this.apiUrl}/getReservation/${carId}`);
  }

  getReservationsByUserId(userId: number): Observable<ReservationCarAgencyDTO[]> {
    const url = `${this.apiUrl}/getReservationsByUserId/${userId}`;
    return this.http.get<ReservationCarAgencyDTO[]>(url);
  }

  getReservationsByAgency(agencyId: number): Observable<ReservationCarUserDTO[]> {
    const url = `${this.apiUrl}/reservations/${agencyId}`;
    return this.http.get<ReservationCarUserDTO[]>(url);
  }

  updateReservation(reservationId: number, newStatus: string): Observable<any> {
    const url = `${this.apiUrl}/updateStatus/${reservationId}?newStatus=${newStatus}`;
    return this.http.put(url, null);
  }

  getReservationById(id: number): Observable<ReservationCarUserDTO> {
    return this.http.get<ReservationCarUserDTO>(`${this.apiUrl}/getReservationById/${id}`);
  }

  getAgencyById(id: number): Observable<ReservationCarUserDTO> {
    return this.http.get<ReservationCarUserDTO>(`${this.apiUrl}/getReservationById/${id}`);
  }
  
}
