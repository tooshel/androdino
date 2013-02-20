//Motor Pins
int dirIn1A = 8;
int dirIn2A = 9;
int dirIn1B = 12;
int dirIn2B = 13;
int speedA = 10;
int speedB = 11; 

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
  pinMode(dirIn1A, OUTPUT); 
  pinMode(dirIn2A, OUTPUT); 
  pinMode(dirIn1B, OUTPUT); 
  pinMode(dirIn2B, OUTPUT); 
  pinMode(speedA, OUTPUT); 
  pinMode(speedB, OUTPUT);
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
    }
    
    
    CTRLStr = '\0';
  }
  
  
  
  //Chances are nothing changes but I'll set the speed/direction of all the motors every loop
  
  if(frBool == 1) {
      digitalWrite(dirIn1A,LOW);
      digitalWrite(dirIn2A,HIGH);
  } else {
      digitalWrite(dirIn1A,HIGH);
      digitalWrite(dirIn2A,LOW);
  }
  analogWrite (speedA, (int)(speedVal*speedFactor));
  
  if(lrBool == 1) {
      digitalWrite(dirIn1B,LOW);
      digitalWrite(dirIn2B,HIGH);
  } else {
      digitalWrite(dirIn1B,HIGH);
      digitalWrite(dirIn2B,LOW);
  }
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

