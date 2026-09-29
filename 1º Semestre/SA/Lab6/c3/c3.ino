void setup()
{
  Serial.begin(9600);
}


void receiveInt()
{
  static const int ESPERA = 0, EXECUTA_VALOR=1;
  static int state = ESPERA;
  static int n=0;
  switch (state)
  {
      case ESPERA:
        if(Serial.available() > 0)
        {
          n = Serial.parseInt();
          state= EXECUTA_VALOR;
        }
      break;
      case EXECUTA_VALOR:
        Serial.println(n);
        state = ESPERA;
      break;
  }
}

void loop() 
{
  receiveInt();
}
