import {Injectable} from "@angular/core";
import {AngularFireDatabase} from "@angular/fire/compat/database";

@Injectable()
export class FirebaseService {

  constructor(private firebaseClient: AngularFireDatabase) {
  }

  get doorOpened$() {
    return this.firebaseClient.object<boolean>("door/isOpened").valueChanges();
  }

  get logs$() {
    return this.firebaseClient.object<any>("logs").valueChanges();
  }

  openDoor() {
    this.firebaseClient.object<boolean>("door/isOpened").set(true);
  }

  changeSecurityPinPassword(pin: string) {
    return this.firebaseClient.object<number>("door/securityPin").set(parseInt(pin));
  }
}
