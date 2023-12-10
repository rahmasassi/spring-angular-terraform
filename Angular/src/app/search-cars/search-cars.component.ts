import { Component, Input, OnInit, SimpleChanges } from '@angular/core';
import { CarDTO } from '../Models/CarDTO';
import { ActivatedRoute, Router } from '@angular/router';
import { CarsService } from '../services/cars.service';
import { DataSharingServiceService } from '../services/data-sharing-service.service';

@Component({
  selector: 'app-search-cars',
  templateUrl: './search-cars.component.html',
  styleUrls: ['./search-cars.component.css']
})
export class SearchCarsComponent implements OnInit{
  constructor(private router: Router){}
  ngOnInit(): void { 
  }
  doSearch(value: String){
    if (value.trim() === '') {
      this.router.navigate(['/search']);
    } else {
      this.router.navigate(['/search', value]);
    }
  } 
}


