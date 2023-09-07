import {Component} from '@angular/core';
import {FirebaseService} from "./services/Firebase.service";
import {MatDialog} from "@angular/material/dialog";
import {ChangeSecurityPinDialog} from "./components/change-security-pin.dialog";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'iot-root', template: `
    <mat-toolbar>
      <span>Simulacija kontrole ulaznih vrata</span>
      <span class="toolbar-spacer"></span>
      <button (click)="openChangeSecurityPinDialog()" mat-icon-button class="example-icon favorite-icon"
              aria-label="Example icon-button with heart icon">
        <mat-icon>lock</mat-icon>
      </button>
    </mat-toolbar>
    <main>
      <div class="door">
        <iot-door [opened]="isDoorOpened" (toggleDoorEvent)="toggleDoor()"></iot-door>
      </div>
      <div class="logs">
        <iot-logs [logs]="logs$"></iot-logs>
      </div>
    </main>
  `
})
export class AppComponent {
  isDoorOpened = false;

  constructor(private firebaseService: FirebaseService, public dialog: MatDialog, private snackBar: MatSnackBar) {
  }

  get logs$() {
    return this.firebaseService.logs$;
  }

  openChangeSecurityPinDialog() {
    const dialogRef = this.dialog.open(ChangeSecurityPinDialog, {
      data: {pin: null}, width: '360px'
    });

    dialogRef.afterClosed().subscribe(result => {
      this.firebaseService.changeSecurityPinPassword(result).then(value => {
        this.snackBar.open("Sigurnosni PIN je uspjesno ažuriran!")!
      }).catch(reason => {
        this.snackBar.open("Došlo je do greške!");
      })
    });
  }

  ngOnInit() {
    this.firebaseService.doorOpened$.subscribe(value => {
      if (value != null) this.isDoorOpened = value;
    });
  }

  toggleDoor() {
    this.firebaseService.openDoor();
  }
}
