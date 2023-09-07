import {Component, Inject} from "@angular/core";
import {MAT_DIALOG_DATA, MatDialogModule, MatDialogRef} from "@angular/material/dialog";
import {MatInputModule} from "@angular/material/input";
import {FormsModule} from "@angular/forms";
import {MatButtonModule} from "@angular/material/button";

interface ChangeSecurityPinDialogData {
  pin: number
}

@Component({
  template: `
    <h1 mat-dialog-title>Promjena sigurnosnog pina</h1>
    <div mat-dialog-content>
      <mat-form-field style="width: 100%">
        <mat-label>Novi pin</mat-label>
        <input matInput [(ngModel)]="data.pin">
      </mat-form-field>
    </div>
    <div mat-dialog-actions align="end">
      <button mat-button (click)="onCancel()">Odustani</button>
      <button mat-button [mat-dialog-close]="data.pin" cdkFocusInitial>Sačuvaj</button>
    </div>
  `, imports: [MatDialogModule, MatInputModule, FormsModule, MatButtonModule], standalone: true
})
export class ChangeSecurityPinDialog {
  constructor(public dialogRef: MatDialogRef<ChangeSecurityPinDialog>, @Inject(MAT_DIALOG_DATA) public data: ChangeSecurityPinDialogData,) {
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
