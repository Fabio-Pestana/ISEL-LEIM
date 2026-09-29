void setup() 
{
  Serial.begin(9600);
  pinMode(2,OUTPUT);
}

void loop() 
{
  
  float brilho=ldr();
  Serial.println(brilho);
 
  piezo(brilho);
}

void piezo(float brilho)
{
 	tone(2,(map(brilho,0,100,200,10000)));
}
float ldr()
{
  float v=5.00*analogRead(A1)/1023;
  float RLDR=10.0*(5.00/v-1);
  float brilho = pow(RLDR/23.48,-1/0.837);
  return brilho;
}