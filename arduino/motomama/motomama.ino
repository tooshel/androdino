int dirIn1A = 8;
int dirIn2A = 9;

int dirIn1B = 12;
int dirIn2B = 13;

int speedA = 10;
int speedB = 11; 



void setup()
{
  pinMode(dirIn1A, OUTPUT); 
  pinMode(dirIn2A, OUTPUT); 
  pinMode(dirIn1B, OUTPUT); 
  pinMode(dirIn2B, OUTPUT); 
  pinMode(speedA, OUTPUT); 
  pinMode(speedB, OUTPUT);
  Serial.begin(9600);
}


void loop()
{

Serial.println("Hello");

int dirIn1A = 8;
int dirIn2A = 9;

int dirIn1B = 12;
int dirIn2B = 13;

int speedA = 10;
int speedB = 11; 
  int i;
  digitalWrite(dirIn1B,HIGH);
  digitalWrite(dirIn2B,LOW);
  
  digitalWrite(dirIn1A,HIGH);
  digitalWrite(dirIn2A,LOW);
  
  for(i = 0;i<256;i++)
  { 
    analogWrite(speedB, i);  // analogRead values go from 0 to 1023, analogWrite values from 0 to 255
    analogWrite(speedA, i);
    delay(10);
  }
  digitalWrite(dirIn2B,HIGH);
  digitalWrite(dirIn1B,LOW);
  
  digitalWrite(dirIn2A,HIGH);
  digitalWrite(dirIn1A,LOW);
  
  for(i = 0;i<256;i++)
  { 
    analogWrite(speedB, i);  // analogRead values go from 0 to 1023, analogWrite values from 0 to 255
    analogWrite(speedA, i);
    delay(10);
  }
}
