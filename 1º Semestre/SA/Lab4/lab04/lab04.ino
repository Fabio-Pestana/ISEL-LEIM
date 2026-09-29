#define PINPOT A0
#define PINS1 2
#define PINS2 3
#define PINLDR A1
#define PIND1 4
#define PIND2 5

int V2, V5, brilho, contador=0, aux;
bool V3, V4;
float V2_analog, V3_analog, V5_analog, x, brilho_Ldr;


void setup()
{
  pinMode (2,INPUT);
  pinMode (3, INPUT_PULLUP);
  pinMode (4, OUTPUT);
  pinMode (5, OUTPUT);
  Serial.begin(9600);
}

void loop() 
{
  V2_analog = ler_pinpot ();
  x = calcularx (V2_analog);
  V5_analog = ler_V5();
  V3=V3e4(2);
  V4=V3e4(3);
  brilho_Ldr=brilhoLDR();
  if((V3e4(2)==1)||V3e4(3)==1) //contador do S1 E S2 
  {
    contador=contador+1;
    delay(1000);
  }
  pled(25); // valor entre 0.5 e 50 Hz
  rluz(50); // percentagem de luminosidade que queremos obter
  pled((49.5/5)*V2_analog+0.5);  // piscar led em função do V2
  lervalorserial();
  ciclomeioseg();
  aux =lervalorserial();
  if (aux>=0)
  {
    rluz(aux);
    //delay(100)--> se fosse para regular o LED num intervalo
  }

}

void ciclomeioseg()
{
  enviarconsola();
  delay(500);
}

float ler_pinpot()
{
  int aux1 = analogRead(PINPOT);
  return (aux1*5.0)/1023;
}

float calcularx(float T) 
{
  
  return T/5.0;
}

float ler_V5()
{
  int aux2 = analogRead(PINLDR);
  return (aux2*5.0)/1023;
}

float brilhoLDR()
{
  int R4=10; //valor otimo da resistencia R4
  float LDR = (5.00*R4)/(ler_V5()+R4); //divisor de tensão
  float brilho= pow((LDR/23.48), (-1/0.837)); //apartir da função do brilho
  return brilho;
}

bool V3e4(int x)
{
  bool aux3=digitalRead(x);
  return !aux3;
}

void pled(float freq)//piscar ledD1
{
  digitalWrite(PIND1, HIGH);
  delay(500/freq);
  digitalWrite(PIND1, LOW);
  delay(500/freq);
}

float rluz(int brilho)//regulador led D2, brilho entre 0-100%
{
  float aux4=brilho*255/100;            //map(brilho,0,100,0,255)--> outra execução
  analogWrite(PIND2, aux4);
}

int lervalorserial()
{
  if(Serial.available()>0)
  {
    int aux5=Serial.parseInt();
    if(aux5>=0 && aux5<=100)
    {
      return aux5;
    }
  }
  return -1;
}

void enviarconsola ()
{

  Serial.print(millis()/1000.00); // para o valor ficar em segundos
  Serial.print("\t");
  Serial.print(V2_analog, 3); //algarismos significativos
  Serial.print("\t");
  Serial.print(x, 3);
  Serial.print("\t");
	Serial.print(V3);
	Serial.print("\t");
	Serial.print(V4);
	Serial.print("\t");
	Serial.print(V5_analog, 3); //algarismos significativos
  Serial.print("\t");
  Serial.print(contador);
  Serial.print("\t");
  Serial.print(brilho_Ldr, 3); //algarismos significativos
  Serial.print("\t");
  Serial.print(aux);
  Serial.println("\t");
}
