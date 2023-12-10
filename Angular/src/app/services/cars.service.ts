import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable, catchError, tap ,map, throwError } from 'rxjs';
import { Cars } from '../Models/cars';
import { CarDTO } from '../Models/CarDTO';
import { ApiResponse } from '../Models/ApiResponse';
@Injectable({
  providedIn: 'root'
})
export class CarsService {
  private searchResultsSubject: BehaviorSubject<any> = new BehaviorSubject([]);
  public searchResults$: Observable<any> = this.searchResultsSubject.asObservable();
  private apiUrl = 'http://localhost:8081/api/cars';
  private searchResults: CarDTO[] = [];


  constructor(private http: HttpClient) { }
  addCarWithImage(formData: FormData, userId: number): Observable<ApiResponse> {
    const headers = new HttpHeaders();
    return this.http.post<ApiResponse>(`${this.apiUrl}/addCar?userId=${userId}`, formData, { headers });
  }

  getCarById(carId: number): Observable<CarDTO> {
    return this.http.get<CarDTO>(`${this.apiUrl}/getCarById/${carId}`);
  }
  
  updateCar(carId: number, formData: FormData): Observable<any> {
    const headers = new HttpHeaders({
      'enctype': 'multipart/form-data'
    });
  
    return this.http.put(`${this.apiUrl}/updateCar/${carId}`, formData, { headers });
  }
  

  getAllCars(): Observable<CarDTO[]> {
    return this.http.get<CarDTO[]>(`${this.apiUrl}/getAllCars`);
  }

  searchCars(searchTerm: string): Observable<CarDTO[]> {
    return this.http.get<CarDTO[]>(`${this.apiUrl}/search?searchTerm=${searchTerm}`);}
  delete(carId: number): Observable<void> {
    const url = `${this.apiUrl}/delete/${carId}`;
    return this.http.delete(url, { responseType: 'text' }).pipe(
      catchError((error: any) => {
        console.error('Error deleting car:', error);
        throw error; 
      }),
      map((response: any) => {
        console.log(response); 
        return; 
      })
    );
  }

  getCarsByAgencyId(agencyId: number): Observable<CarDTO[]> {
    const url = `${this.apiUrl}/getCarsByAgencyId/${agencyId}`;

    return this.http.get<CarDTO[]>(url).pipe(
      catchError((error) => {
        console.error('Error fetching cars by agency ID', error);
        return throwError(error);
      })
    );
  }
}
