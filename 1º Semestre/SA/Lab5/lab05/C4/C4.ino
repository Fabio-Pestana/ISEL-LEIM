void setup() 
{
  Serial.begin(9600);
  pinMode(11,OUTPUT);
  pinMode(10,OUTPUT);
  pinMode(9,OUTPUT);
}

void loop() 
{
  led_rgb();
}

void led_rgb()
{ 
  int r=0,g=0,b=0;
  while(Serial.available()==0){}
  r=Serial.parseInt();
  while(Serial.available()==0){}
  g=Serial.parseInt();
  while(Serial.available()==0){}
  b=Serial.parseInt();
  if(r>100 || g>100 || b>100)
  {
    Serial.println("outside of scope");
  } else
  {
    analogWrite(9,map(r,0,100,0,255));
  	analogWrite(11,map(g,0,100,0,255));
    analogWrite(10,map(b,0,100,0,255));
  	Serial.print("R");
  	Serial.print(r);
  	Serial.print("G");
  	Serial.print(g);
  	Serial.print("B");
  	Serial.print(b);
  	Serial.println("");
  }
}