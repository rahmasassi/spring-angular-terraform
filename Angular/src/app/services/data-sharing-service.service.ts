import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DataSharingServiceService {
  
  private searchTermSubject = new BehaviorSubject<string>('');
  searchTerm$ = this.searchTermSubject.asObservable();

  updateSearchTerm(searchTerm: string) {
    this.searchTermSubject.next(searchTerm);
  }
}
