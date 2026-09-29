#include <Servo.h>
#define potpin A0
#define servo 9

Servo nameServo;
int pot=0;
int x,val;

void setup() 
{
  Serial.begin(9600);
  nameServo.attach(9);
}

void loop() 
{
  x = analogRead(potpin);
  val= map(x, 0,1023,-90, 90); //val= map(x, 0,1023,0, 180);
  nameServo.write(val);
  delay(15);
  Serial.println(x/1023.0);
  Serial.println(val);
}
