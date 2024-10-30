import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-adherant-layout',
  templateUrl: './adherant-layout.component.html',
  styleUrls: ['./adherant-layout.component.css'],
})
export class AdherantLayoutComponent implements OnInit {
  userconnect!: any;
  ngOnInit(): void {
    this.userconnect = JSON.parse(localStorage.getItem('userconnect')!);
  }

  logout() {
    localStorage.clear();
  }
}
