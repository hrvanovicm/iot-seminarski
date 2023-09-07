import {Component, EventEmitter, Input, Output} from "@angular/core";
import {MatIconModule} from "@angular/material/icon";
import {CommonModule} from "@angular/common";
import {MatButtonModule} from "@angular/material/button";

@Component({
  selector: 'iot-door', template: `
    <div style="display: flex; flex-direction: column; background-color: aliceblue">
      <div style="position: relative">
        <img style="width: 100%;"
             [src]="isDoorOpened ? '/assets/door_opened.svg' : '/assets/door_closed.svg'"
        >
      </div>

      <div style="width: 100%">
        <button mat-button style="width: 100%; height: 65px" (click)="toggleDoor()" [disabled]="isDoorOpened">Otvori
          vrata
        </button>
      </div>
    </div>
  `, imports: [MatIconModule, CommonModule, MatButtonModule], standalone: true
})
export class DoorComponent {
  @Input('opened') isDoorOpened = false;
  @Output() toggleDoorEvent = new EventEmitter<void>();

  toggleDoor() {
    this.toggleDoorEvent.emit();
  }
}
