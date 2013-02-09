#include <SoftwareSerial.h>
#include <string.h>

//const int rxPin = 5; // pin used to receive
//const int txPin = 4; // pin used to send to

const int rxPin = 0; // pin used to receive
const int txPin = 1; // pin used to send to
SoftwareSerial bluetooth(rxPin, txPin); // new serial port on given pins

int loopdelay = 100;
const int ledPin = 12;

char sval;
char bval;

int availBytes;
int availBytesSerial;

const int stringSize = 64;


char stringarr[stringSize];
//String stringstr[stringSize];
//String stringarr[stringSize];


//API: s:foward/reverse:speed:left/right:direction:headlights:brakeleft:brakeright
//ex   s:1:5:1:5:0:0:0


int apiStringSize = 15;
char apiStartChar = 's';
const int MAX_ARGS = 9;

char byteRead;

const int speedFactor = 28;
String CTRLStr = '\0';
int frBool = 0;
int speedVal = 0;
int lrBool = 0;
int dirVal = 0;
int headBool = 0;
int brakeLeftBool = 0;
int brakeRightBool = 0;


//String[] splitCmd(String text, char splitChar) {
//}

void setup()
{
  Serial.begin(9600);
  bluetooth.begin(9600);
  pinMode(ledPin, OUTPUT);
  
  //Not sure if these are needed
  pinMode(rxPin, INPUT);
  pinMode(txPin, OUTPUT);  
  Serial.print("Serial Works!");
}

void loop()
{
  //bluetooth.println("Bluetooth ready");
  //bluetooth.print(random(1024));
  //Serial.println(random(1024));
  for(int i=0; i<stringSize; i++)
  {
    stringarr[i] = '\0';
  }
  
  availBytesSerial = Serial.available();
  if(Serial.available() > 0) {
    Serial.print("Serial Available: ");
    Serial.println(availBytesSerial);
    
    for(int i=0; i<availBytesSerial; i++)
    {
      stringarr[i] = Serial.read();
    }
  }

    
  availBytes = bluetooth.available();
  //if(availBytes >= apiStringSize) {
  if(availBytes > 0) {
    Serial.print("Bluetooth Available: ");
    Serial.println(availBytes);
    
    for(int i=0; i<availBytes; i++)
    {
      stringarr[i] = bluetooth.read();
    }
    
    Serial.println(stringarr);
    
    CTRLStr=splitString(stringarr,':',0);
    
    
    if(CTRLStr[0] == 's') {
      
      frBool = splitString(stringarr,':',1).toInt();
      speedVal = splitString(stringarr,':',2).toInt();
      lrBool = splitString(stringarr,':',3).toInt();
      dirVal = splitString(stringarr,':',4).toInt();
      headBool = splitString(stringarr,':',5).toInt();
      brakeLeftBool = splitString(stringarr,':',6).toInt();
      brakeRightBool = splitString(stringarr,':',7).toInt();
  
      
      
      Serial.println(frBool);
      Serial.println(speedVal);
      Serial.println(lrBool);
      Serial.println(dirVal);
      Serial.println(headBool);
      Serial.println(brakeLeftBool);
      Serial.println(brakeRightBool);
      
      
      
      
      Serial.println("===============");
      
    }
    
    
    CTRLStr = '\0';
  }
  
  //LED control for Serial
  bval = stringarr[availBytesSerial -1];
  if( bval == 'h' ) {
    digitalWrite(ledPin, HIGH);  // turn ON the LED
  } else if ( bval == 'o') { 
    digitalWrite(ledPin, LOW);   // otherwise turn it OFF
  }
 
  //LED control for Bluetoo
  bval = stringarr[availBytes -1];
  if( bval == 'h' ) {
    digitalWrite(ledPin, HIGH);  // turn ON the LED
  } else if ( bval == 'o') { 
    digitalWrite(ledPin, LOW);   // otherwise turn it OFF
  }
  
  delay(loopdelay);   
  
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

