
#include <Keypad.h>
#include <Servo.h>

Servo myservo;

char keys[4][4] = {
  {'1', '2', '3', 'A'},
  {'4', '5', '6', 'B'},
  {'7', '8', '9', 'C'},
  {'*', '0', '#', 'D'}
};

byte rowPins[4] = {9, 8, 7, 6};
byte colPins[4] = {5, 4, 3, 1};

const char validPinCharacters[] = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '#'};

const int RED_LIGHT_PIN = 10;
const int GREEN_LIGHT_PIN = 11;
const int BLUE_LIGHT_PIN = 12;
const int SERVO_PIN = 13;

bool isDoorOpened = false;
bool isConnected = false;

Keypad keypad = Keypad(makeKeymap(keys), rowPins, colPins, 4, 4); 

void setup() {
    pinMode(RED_LIGHT_PIN, OUTPUT);
    pinMode(GREEN_LIGHT_PIN, OUTPUT);
    pinMode(BLUE_LIGHT_PIN, OUTPUT);
    myservo.attach(SERVO_PIN); 
    Serial.begin(9600);
}

void loop() {

  if(!isConnected) {
      digitalWrite(GREEN_LIGHT_PIN, HIGH);
      digitalWrite(RED_LIGHT_PIN, HIGH);
      digitalWrite(BLUE_LIGHT_PIN, HIGH);
      myservo.write(0);
  } 


  if(isConnected && isDoorOpened) {
      digitalWrite(GREEN_LIGHT_PIN, HIGH);
      digitalWrite(RED_LIGHT_PIN, LOW);
      digitalWrite(BLUE_LIGHT_PIN, LOW);
      myservo.write(180); 
  } else if(isConnected) {
      digitalWrite(GREEN_LIGHT_PIN, LOW);
      digitalWrite(RED_LIGHT_PIN, HIGH);
      digitalWrite(BLUE_LIGHT_PIN, LOW);
      myservo.write(0); 
  } 
  
  if (Serial.available() > 0) {
        char receivedChar = Serial.read();
        // Do something when "PIN" is received

        if(receivedChar == '1' || receivedChar == 1) {
              isConnected = true;
        } else if(receivedChar == '2' || receivedChar == 2) {
              isDoorOpened = true;
        }  else if(receivedChar == '3' || receivedChar == 3) {
                isDoorOpened = false;
        }  else if(receivedChar == '4' || receivedChar == 4) {
              onFail();
        }  else if(receivedChar == '5' || receivedChar == 5) {
              onKeyReceived();
        }
    
        // Clear the serial buffer
        while (Serial.available() > 0) {
          Serial.read();
      }
  }
  
  char input = keypad.getKey();

  if(isConnected && !isDoorOpened && input) {
    sendChar(input);
  }
}

void onFail() {
  Serial.flush();
  for(int i = 0; i < 3; i++) {
    digitalWrite(RED_LIGHT_PIN, HIGH);
    delay(500);
    digitalWrite(RED_LIGHT_PIN, LOW);
    delay(500);
  }

  digitalWrite(RED_LIGHT_PIN, LOW);
}

void onKeyReceived() {
    for(int i = 0; i < 1; i++) {
        delay(100);
        digitalWrite(BLUE_LIGHT_PIN, HIGH);
        delay(100);
        digitalWrite(BLUE_LIGHT_PIN, LOW);
    }
}

void sendChar(char input) {
    for (size_t i = 0; i < strlen(validPinCharacters); ++i) {
        if (input == validPinCharacters[i]) {
             Serial.print(input); 
             break;
        }
    }
}