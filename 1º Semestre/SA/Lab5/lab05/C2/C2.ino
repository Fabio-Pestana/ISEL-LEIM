const int TRIG = 7;
const int ECHO = 8;
unsigned long width;
unsigned long distance;

void setup() 
{
  Serial.begin(9600);
  pinMode(TRIG,OUTPUT);
  pinMode(ECHO,INPUT);
  pinMode(4,OUTPUT);
}

void loop() 
{
  sonar();
  piezoC2();
}

void sonar()
{
  digitalWrite(TRIG,HIGH);
  delayMicroseconds(10);
  digitalWrite(TRIG,LOW);
  width = pulseIn(ECHO,HIGH);
  distance = microtocenti(width);
  Serial.println(distance);
  delay(100);
}
long microtocenti (long micro)
{
  // v do som 340m/s = 29 micros por centi
  return micro/29/2;
}
void piezoC2()
{
  if(distance>=5 && distance <=50)
  {
    tone(4, map(distance,5,50, 5, 200)/10);
  } else
  {
    Serial.println("outside of scope");
    noTone(4);
  }
}