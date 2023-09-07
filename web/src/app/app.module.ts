import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {getDatabase, provideDatabase} from '@angular/fire/database';
import {DoorComponent} from "./components/door.component";
import {LogsComponent} from "./components/logs.component";
import {FirebaseService} from "./services/Firebase.service";
import {environment} from "../environments/environment";
import {AngularFireModule} from "@angular/fire/compat";
import {AngularFireDatabaseModule} from "@angular/fire/compat/database";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {MAT_SNACK_BAR_DEFAULT_OPTIONS, MatSnackBarModule} from "@angular/material/snack-bar";
import {MatDialogModule} from "@angular/material/dialog";

@NgModule({
  declarations: [AppComponent],
  imports: [BrowserModule, BrowserAnimationsModule, AngularFireModule.initializeApp(environment.firebase), AngularFireDatabaseModule, provideDatabase(() => getDatabase()), DoorComponent, LogsComponent, MatToolbarModule, MatIconModule, MatButtonModule, MatSnackBarModule, MatDialogModule],
  providers: [FirebaseService, {
    provide: MAT_SNACK_BAR_DEFAULT_OPTIONS, useValue: {
      duration: 3000
    }
  }],
  bootstrap: [AppComponent]
})
export class AppModule {
}
