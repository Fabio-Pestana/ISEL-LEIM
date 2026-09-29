#include <Servo.h>

#define servo 9

Servo nameServo;
void setup() 
{
  Serial.begin(9600);
  nameServo.attach(9);

}

void loop() {
  unsigned long t=millis();
  nameServo.write(simuladorservo(t)); //varreamento do servo
  Serial.println(simuladorservo(t)); //ver no plotter a onda criada
  delay(15);

}
float simuladorservo(unsigned long t) // onda sinusoidal
{
  const float ydc=90, yac=90; 
  const int T=5; // T=5 segundos
  return ydc+yac*sin(6.28*t/1000/T); 
}