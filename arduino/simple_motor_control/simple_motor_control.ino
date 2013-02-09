#include <Servo.h> 
#include <SoftwareSerial.h>

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
   
  pinMode(ledPin, OUTPUT);
  
  pinMode (dirA, OUTPUT);
  pinMode (dirB, OUTPUT);
  pinMode (speedA, OUTPUT);
  pinMode (speedB, OUTPUT);
  
  
  myservo.attach(servoPin);
}

void loop()
{
  Serial.print(random(1024));
  
//
  digitalWrite(ledPin, HIGH); // set the LED on
  delay(flashdelay); // wait for two seconds
  digitalWrite(ledPin, LOW); // set the LED off
  delay(flashdelay); // wait for two seconds

// move the motor A to one direction increasing speed
  digitalWrite (dirA, HIGH);
  for (int j = 0; j <= 255; j += 5) {
    analogWrite (speedA, j);
    Serial.println(j);
    delay (loopdelay);
  }
  
 
  // stop the motor
  digitalWrite(speedA, LOW);
  digitalWrite(brakeA, HIGH);
  

  delay(1000); 

  // move the motor A to one direction increasing speed
  digitalWrite (dirA, LOW);
  for (int j = 0; j <= 255; j += 5) {
    analogWrite (speedA, j);
    Serial.println(j);
    delay (loopdelay);
  }

  digitalWrite(speedA, LOW);
  delay(1000); // keep the motor stopped for one second

  digitalWrite(speedA, HIGH);
  delay(10000); // keep the motor going for 10 seconds

  // move the motor A to one direction decreasing speed
  digitalWrite (dirA, LOW);
  for (int j = 255; j >= 0; j -= 10) {
    analogWrite (speedA, j);
    delay (loopdelay);
  }

  // stop the motor
  digitalWrite(speedA, LOW);

  delay(1000); // keep the motor stopped for one second

}

