import { Component, Input, OnInit } from '@angular/core';
import { CarsService } from 'src/app/services/cars.service';
import { CarDTO } from 'src/app/Models/CarDTO';
import { ActivatedRoute, Router } from '@angular/router';
import { ca } from 'date-fns/locale';
import { AuthenticateService } from 'src/app/services/authenticate.service';


@Component({
  selector: 'app-list-voiture-user',
  templateUrl: './list-voiture-user.component.html',
  styleUrls: ['./list-voiture-user.component.css']
})
export class ListVoitureUserComponent implements OnInit {
  @Input() car!: CarDTO;
  cars: CarDTO[] = [];
  searchResults: CarDTO[] = [];
  showSearchResults: boolean = false;
  
  userRoles: string[] = [];

  constructor(private carService: CarsService, private route: ActivatedRoute, private router : Router, private authService: AuthenticateService) {}
  
  ngOnInit(): void {

    const userId=this.authService.getCurrentUserId();
    console.log("userid", userId);

    this.authService.getUserRolesById(userId).subscribe(
      (roles) => {
        this.userRoles = roles;
        console.log('Roles:', roles);
      },
      (error) => {
        console.error('Error fetching user roles:', error);
      }
    );

    this.route.paramMap.subscribe(()=>{
      this.listCars();
    });
  }

  listCars(){
    this.showSearchResults = this.route.snapshot.paramMap.has('keyword');
    if (this.showSearchResults){
      this.searchCars();
    }
    else{
      if (this.userRoles.includes('ROLE_AGENCY')) {
        this.getCarsByAgency();
      } else {
        this.getAllCars();
      }
    }
    
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

  searchCars(){
    const theKeyword: string | null = this.route.snapshot.paramMap.get('keyword');

    if (theKeyword !== null) {
      this.carService.searchCars(theKeyword).subscribe((data: CarDTO[]) => {
        this.cars = data;
        
      });
    } else {
      console.log("null")
      
    }
  }

  getAllCars(): void {
    this.carService.getAllCars().subscribe((data: CarDTO[]) => {
      this.cars = data;
    });
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
          this.listCars();
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

  reserveCar(car: CarDTO):void{
    if (car && car.id){
      this.router.navigate(['/reservation', car.id]);
    }else{
      console.error('Invalid car data for reserving.', car);
    }
  }

  detailCar(car: CarDTO):void{
    if (car && car.id){
      this.router.navigate(['/detail-car', car.id]);
    }else{
      console.error('Invalid car data for reserving.', car);
    }
  }
}

