#include <string.h>

#define right1 5			//define I1 interface
#define speedPinRight 6  	//enable right motor (bridge A)
#define right2 7      		//define I2 interface 

#define left1 8      		//define I3 interface 
#define speedPinLeft 9  	//enable motor B
#define left2 10     		//define I4 interface 

//Motor Pins
//int dirA = 12;
//int speedA = 3;
//int brakeA = 9;
//
//int dirB = 13;
//int speedB = 11; 
//int brakeB = 8;

//Motor Pins
int dir1B = right1;
int speedB = speedPinRight;
int dir2B = right2;

int dir1A = left1;
int speedA = speedPinLeft; 
int dir2A = left2;


//Bluetooth Pins
const int rxPin = 0; // pin used to receive, though, on the itead card, PIN 0 is TX
const int txPin = 1; // pin used to send to, though, on the itead card, PIN 1 is RX
//SoftwareSerial bluetooth(rxPin, txPin); // new serial port on given pins

//Other Bluetooth related globals
int availBytes;
const int stringSize = 64;
char stringarr[stringSize];

//Used for the ledPin test
const int ledPin = 2;
char sval; 
char bval;

//Other Globals . . . love GLOBALS
int loopDelay = 50;

const float speedFactor = 28.3;
String CTRLStr = '\0';
int frBool = 0;
int speedVal = 0;
int lrBool = 0;
int dirVal = 0;
int headBool = 0;
int brakeLeftBool = 0;
int brakeRightBool = 0;

//This is a valid string
//API: s:foward/reverse:speed:left/right:direction:headlights:brakeleft:brakeright
//ex   s:1:5:1:5:0:0:0

int apiStringSize = 15;
char apiStartChar = 's';

//needed for splitString
const int MAX_ARGS = 9;
char byteRead;


void setup()
{
  //Serial is used for bluetooth
  Serial.begin(9600);

  //Testing Pin
  pinMode(ledPin, OUTPUT);
  
  //Motor Pins  
  pinMode (dir1A, OUTPUT);
  pinMode (dir2A, OUTPUT);
  pinMode (dir1B, OUTPUT);
  pinMode (dir2B, OUTPUT);
  pinMode (speedA, OUTPUT);
  pinMode (speedB, OUTPUT);
  
}

void loop()
{
  //Blank out the string array that will hold the data from Bluetooth
  for(int i=0; i<stringSize; i++)
  {
    stringarr[i] = '\0';
  }
  
  availBytes = Serial.available();
  //Only read in the string when it has all the commands in the queue
  if(availBytes >= apiStringSize) {  //Use this for the LED test: if(availBytes > 0) {
    //Every Serial.print/println/write is just a debug message to see in the serial console
    Serial.print("Bluetooth Serial Available: ");
    Serial.println(availBytes);
    
    //Read in everything . . . we only need the first apiStringSize but we want to 
    //clear the buffer of everything else, one char at a time
    for(int i=0; i<availBytes; i++)
    {
      stringarr[i] = Serial.read();
    }
    
    Serial.println(stringarr);
    
    //splitString is defined below . . . I borrowed it from SO
    CTRLStr=splitString(stringarr,':',0);
    
    
    //Check to see if the control string is set to s . . . IT WORKS   
    if(CTRLStr[0] == 's') {
      
      //Grab all the vars we need that came in from bluetooth that ".toInt()" trick isn't mentioned 
      //in the String docs (there when 3 hours) . . . not use to all this type safety
      frBool = splitString(stringarr,':',1).toInt();
      speedVal = splitString(stringarr,':',2).toInt();
      lrBool = splitString(stringarr,':',3).toInt();
      dirVal = splitString(stringarr,':',4).toInt();
      headBool = splitString(stringarr,':',5).toInt();
      brakeLeftBool = splitString(stringarr,':',6).toInt();
      brakeRightBool = splitString(stringarr,':',7).toInt();
       
//      Serial.print("fr: ");
//      Serial.println(frBool);
//      Serial.print("fr speed: ");
//      Serial.println((int)(speedVal*speedFactor));
//      Serial.print("lr: ");
//      Serial.println(lrBool);
//      Serial.print("lr speed: ");
//      Serial.println((int)(dirVal*speedFactor));
//      Serial.print("head: ");
//      Serial.println(headBool);
//      Serial.print("brake: ");
//      Serial.println(brakeLeftBool);
//      Serial.print("brake2: ");
//      Serial.println(brakeRightBool);
//      Serial.println("===============");
      
      
      
      bval = stringarr[availBytes - 1];
//      Serial.println(bval);
//      Serial.println("===============");
      
      
      
    }
    
    
    CTRLStr = '\0';
  }
  
  
  
  //Chances are nothing changes but I'll set the speed/direction of all the motors every loop
  if (frBool == 1) {
    digitalWrite (dir1A, 1);
    digitalWrite (dir2A, 0);
  } else {
    digitalWrite (dir1A, 0);
    digitalWrite (dir2A, 1);
  }
  
  if (lrBool == 1) {
    digitalWrite (dir1B, 1);
    digitalWrite (dir2B, 0);
  } else {
    digitalWrite (dir1B, 0);
    digitalWrite (dir2B, 1);
  }

  
  analogWrite (speedA, (int)(speedVal*speedFactor));
  
  analogWrite (speedB, (int)(dirVal*speedFactor));
  
  digitalWrite(ledPin, headBool);   
  
  //Everything works better if you give it a break between loops
  delay(loopDelay);   
}


String splitString(String s, char parser,int index) {
  String rs='\0';
  int parserCnt=0;
  int rFromIndex=0, rToIndex=-1;

  while(index>=parserCnt){
    rFromIndex = rToIndex+1;
    rToIndex = s.indexOf(parser,rFromIndex);

    if(index == parserCnt){
      if(rToIndex == 0 || rToIndex == -1){
        return '\0';
      }
      return s.substring(rFromIndex,rToIndex);
    }
    else{
      parserCnt++;
    }

  }
  return rs;
}

