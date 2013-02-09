#include <Servo.h> 
#include <SoftwareSerial.h>
const int rxPin = 5; // pin used to receive
const int txPin = 4; // pin used to send to
SoftwareSerial bluetooth(rxPin, txPin); // new serial port on given pins

int dirA = 12;
int dirB = 13;  // not used in this example
int speedA = 3;
int speedB = 11; // not used in this example

int brakeA = 9;
int brakeB = 8;

int loopdelay = 100;


Servo myservo;
int servoPin = 5;
int pos = 0;

const int ledPin = 13;

int flashdelay = 9;

char sval;
char bval;

int availBytes;

void setup()
{
  Serial.begin(9600);
  bluetooth.begin(9600);
  //bluetooth.println("Bluetooth ready");
  pinMode(rxPin, INPUT);
  pinMode(txPin, OUTPUT);  
   
  pinMode(ledPin, OUTPUT);
  
  pinMode (dirA, OUTPUT);
  pinMode (dirB, OUTPUT);
  pinMode (speedA, OUTPUT);
  pinMode (speedB, OUTPUT);
  
  
  myservo.attach(servoPin);
}

void loop()
{
  bluetooth.println("Bluetooth ready");
  
  //bluetooth.print(random(1024));
  //Serial.print(random(1024));
  
  
  
  availBytes = bluetooth.available();
  if( bluetooth.available() )       // if data is available to read
  {
    Serial.println("Bluetooth Read");
    Serial.println(availBytes);
    Serial.write(bluetooth.read());
    bval = bluetooth.read();         // read it and store it in 'val'
    Serial.println(bval);
  }
  if( bval == 'h' )               // if 'H' was received
  {
    digitalWrite(ledPin, HIGH);  // turn ON the LED
  } else if ( bval == 'o') { 
    digitalWrite(ledPin, LOW);   // otherwise turn it OFF
  }
  
  if( Serial.available() )       // if data is available to read
  {
    sval = Serial.read();         // read it and store it in 'val'
  }
  if( sval == 'h' )               // if 'H' was received
  {
    digitalWrite(ledPin, HIGH);  // turn ON the LED
  } else if ( sval == 'o') { 
    digitalWrite(ledPin, LOW);   // otherwise turn it OFF
  }
  
  
  delay(500);   
  

//  if (bluetooth.available()) {
//    int c = (int)bluetooth.read();
//    flashdelay = c;
//    //Serial.write(c);
//  }
//
//  digitalWrite(ledPin, HIGH); // set the LED on
//  delay(flashdelay); // wait for two seconds
//  digitalWrite(ledPin, LOW); // set the LED off
//  delay(flashdelay); // wait for two seconds

// move the motor A to one direction increasing speed
//  digitalWrite (dirA, HIGH);
//  for (int j = 0; j <= 255; j += 5) {
//    analogWrite (speedA, j);
//    Serial.println(j);
//    delay (loopdelay);
//  }
//myservo.write(135);

//  for(pos = 0; pos < 600; pos += 100)  // goes from 0 degrees to 180 degrees 
//  {                                  // in steps of 1 degree 
//    myservo.write(pos);              // tell servo to go to position in variable 'pos' 
//    //delay(15);                       // waits 15ms for the servo to reach the position 
//  } 
//  for(pos = 180; pos>=1; pos-=1)     // goes from 180 degrees to 0 degrees 
//  {                                
//    myservo.write(pos);              // tell servo to go to position in variable 'pos' 
//    //delay(15);                       // waits 15ms for the servo to reach the position 
//  } 
  
  // stop the motor
  //digitalWrite(speedA, LOW);
  
  //digitalWrite(brakeA, HIGH);
  

  //delay(1000); 

//  // move the motor A to one direction increasing speed
//  digitalWrite (dirA, LOW);
//  for (int j = 0; j <= 255; j += 5) {
//    analogWrite (speedA, j);
//    Serial.println(j);
//    delay (loopdelay);
//  }

  //digitalWrite(speedA, LOW);
  //delay(1000); // keep the motor stopped for one second

//  digitalWrite(speedA, HIGH);
//  delay(10000); // keep the motor going for 10 seconds

//  // move the motor A to one direction decreasing speed
//  digitalWrite (dirA, LOW);
//  for (int j = 255; j >= 0; j -= 10) {
//    analogWrite (speedA, j);
//    delay (idelay);
//  }

  // stop the motor
  //digitalWrite(speedA, LOW);

  //delay(1000); // keep the motor stopped for one second

}

