import {Component, Input, ViewChild} from "@angular/core";
import {MatTableDataSource, MatTableModule} from "@angular/material/table";
import {MatPaginator, MatPaginatorModule} from "@angular/material/paginator";
import {Observable} from "rxjs";

@Component({
  selector: 'iot-logs', template: `
    <h4>Logovi</h4>
    <div style="height: 60vh; display: flex; flex-direction: column; justify-content: space-between">
      <table mat-table [dataSource]="dataSource">
        <ng-container matColumnDef="note">
          <th mat-header-cell *matHeaderCellDef> Komentar</th>
          <td mat-cell *matCellDef="let element"> {{element.note}} </td>
        </ng-container>

        <ng-container matColumnDef="date">
          <th mat-header-cell *matHeaderCellDef> Vrijeme</th>
          <td mat-cell *matCellDef="let element"> {{element.createdAt }} </td>
        </ng-container>

        <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
        <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>
      </table>

      <mat-paginator [pageSizeOptions]="[8]"
                     showFirstLastButtons
                     aria-label="Select page of periodic elements">
      </mat-paginator>
    </div>
  `, standalone: true, imports: [MatTableModule, MatPaginatorModule]
})
export class LogsComponent {
  dataSource = new MatTableDataSource<any>([]);
  displayedColumns = ['note', 'date'];

  @Input() logs!: Observable<any>;

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  ngAfterViewInit() {
    this.logs.subscribe(value => {
      let temp = [];

      for (let key of Object.keys(value)) {
        temp.push(value[key]);
      }

      temp = temp.sort((a: any, b: any): number => {
        return a.createdAt <= b.createdAt ? 1 : 0;
      });

      this.dataSource.data = temp;
    });

    this.dataSource.paginator = this.paginator;
  }
}
