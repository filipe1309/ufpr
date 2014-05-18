

// Ultrasom
#define f_echoPin 3 //Pino 3 recebe o pulso do echo
#define f_trigPin 2 //Pino 2 envia o pulso para gerar o echo


// --> left
#define l_echoPin 11 
#define l_trigPin 4 


// --> right
#define r_echoPin 12 
#define r_trigPin 13 


int l,r,f; // Left, Right e Forward -> Variáveis das distancias obtidas pelo Ultrasom.

// Motor
#define ENA 10 // Azul - Dir
#define IN1 9  // Verde
#define IN2 8  // Amarelo
#define ENB 5 // Marrom - Esq
#define IN3 7  // Laranja
#define IN4 6  // Vermelho
#define INF 10000

int pinLDR = 5; //Porta analógica utilizada pelo LDR  
int state = 0;
char inData[30]; // Allocate some space for the string
char inChar=-1; // Where to store the character read
byte index = 0; // Index into array; where to store the character
 
 
int mapWork[5][5] = { 3, 6, 3, 10, 14,
5, 5, 1, 10, 6,
5, 5, 1, 14, 5,
5, 1, 8, 14, 5,
5, 9, 14, 11, 12 };
int path[5][5] = { 0, 0, 0, 0, 0,
0, 0, 0, 0, 0,
0, 0, 0, 0, 0,
0, 0, 0, 0, 0,
0, 0, 0, 0, 0 };
int x = 0;
int y = 4;
int mapPosition = 0; // 0 - N, 1 - D, 2 - E, 3 - S
int teta = 90;
void setup() 
{ 
  //myservo.attach(SERV);  // Vincula o servo no pino referente(SERV) ao servo object 
  Serial.begin(9600); //inicia a porta serial
  // Setando os pinos para o Ultrasom
  pinMode(f_echoPin, INPUT); // define o pino 3 como entrada (recebe)
  pinMode(f_trigPin, OUTPUT); // define o pino 2 como saida (envia)

  pinMode(l_echoPin, INPUT);
  pinMode(l_trigPin, OUTPUT);
  
  pinMode(r_echoPin, INPUT);
  pinMode(r_trigPin, OUTPUT);
  
  // Setando os pinos para o Motor
  pinMode (ENA, OUTPUT);
  pinMode (IN1, OUTPUT);
  pinMode (IN2, OUTPUT);
  pinMode (ENB, OUTPUT);
  pinMode (IN3, OUTPUT);
  pinMode (IN4, OUTPUT);
  pinMode (13, OUTPUT);  
} 
 
 
void loop() 
{
  delay(1000);
  if(!reachEnd()) {
    nextMovement(giveDirection());
  }
} 

boolean reachEnd()
{
  if(analogRead(pinLDR) > 500)
    return true;
  return false;
}

int giveDirection()
{
  int directions[4];
  int i,teta2;
  int  auxTeta, menor;
  
  for(i = 0; i < 3; i++){
    auxTeta = (teta + (i -1) * 90) % 360;
    if(auxTeta <= 0)
     auxTeta = 360;
     teta2 = haveWall(i);
	if(!teta2){
      directions[i] = path[y - (int)sin((auxTeta*PI)/180)][x + (int)cos((auxTeta*PI)/180)];
    }
    else
      directions[i] = INF;
      Serial.print("dir: ");
                  Serial.println(directions[i]);
  }
  directions[i] = INF / 2; 
  menor = 0;
  for(i = 1; i < 4; i++){
    if(directions[menor] > directions[i])
      menor = i;
  }

  teta = (teta + (menor - 1)*90) % 360;
  if(teta <= 0)
    teta = 360;
  path[y][x]++;
  y = y - (int)sin((teta*PI)/180);
  x = x + (int)cos((teta*PI)/180);
  
  Serial.println(menor);
  
   return menor;
}

boolean haveWall(int side){
  f = ultrSonic(f_trigPin, f_echoPin);
  delay(100);
  r = ultrSonic(r_trigPin, r_echoPin);
    delay(100);
  l = ultrSonic(l_trigPin, l_echoPin);
    delay(100);
  Serial.println("S: ");  
  Serial.println(f);
  Serial.println(l);  
  Serial.println(r);
  switch(side) 
   {
     case 2: if(l < 30) return true;
             else return false;
     case 1: if(f < 30) return true;
             else return false;
     case 0: if(r < 30) return true;
             else return false;       
   }
}

void initMap(){
  int i,j;
  for(i = 0; i < 5; i++)
    for(j = 0; j < 5; j++){
      mapWork[i][j] = 0;
    }
}

int ultrSonic(int trigPin,int echoPin){
  
  long duration;
  digitalWrite(trigPin, LOW);
  delayMicroseconds(2);
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPin, LOW);
  duration = pulseIn(echoPin, HIGH);
  duration = (((duration/2) / 2.91))/10;
  return (int)duration;   
}
 
void stepForward(){
    forward(200,200);
    delay(750);
    stope();
}

void forward(int r, int l)
{
    digitalWrite (IN1, HIGH);
    digitalWrite (IN2, LOW);
    digitalWrite (IN3, LOW);
    digitalWrite (IN4, HIGH);
    analogWrite (ENA, r);
    analogWrite (ENB, l);
}

void reverse()
{   
    digitalWrite (IN1, LOW);
    digitalWrite (IN2, HIGH);
    digitalWrite (IN3, HIGH);
    digitalWrite (IN4, LOW);
    digitalWrite (ENA, HIGH);
    digitalWrite (ENB, HIGH);
}

void rotate_right()
{
    digitalWrite (IN1, LOW);
    digitalWrite (IN2, HIGH);
    digitalWrite (IN3, LOW);
    digitalWrite (IN4, HIGH);
    digitalWrite (ENA, HIGH);
    digitalWrite (ENB, HIGH);
}

void rotate_left()
{
    digitalWrite (IN1, HIGH);
    digitalWrite (IN2, LOW);
    digitalWrite (IN3, HIGH);
    digitalWrite (IN4, LOW);
    digitalWrite (ENA, HIGH);
    digitalWrite (ENB, HIGH);
}

void left()
{
    digitalWrite (IN1, HIGH);
    digitalWrite (IN2, LOW);
    digitalWrite (IN3, LOW);
    digitalWrite (IN4, HIGH);
    analogWrite (ENA, 255);
    analogWrite (ENB, 150);
}

void right()
{
    digitalWrite (IN1, HIGH);
    digitalWrite (IN2, LOW);
    digitalWrite (IN3, LOW);
    digitalWrite (IN4, HIGH);
    analogWrite (ENA, 150);
    analogWrite (ENB, 255);
}

void stope()
{
    digitalWrite (ENA, LOW);
    digitalWrite (ENB, LOW);
}

void backward()
{
     rotate_left();
     delay(170);
     rotate_left();
     delay(150);
     correctForward();
}

void correctForward()
{
    int dif = 0;
    int dif_r = r;
    int dif_l = l;
    delay(100);
    f = ultrSonic(f_trigPin, f_echoPin);
         
    while( (dif < 20) && (f > 10) ){
         l = ultrSonic(l_trigPin, l_echoPin);
         r = ultrSonic(r_trigPin, r_echoPin);
         if(l < 30){
           while( l < 10 ){
             rotate_right();
             delay(10);
             stope();
             delay(50);
             l = ultrSonic(l_trigPin, l_echoPin);
             delay(100);
           }
         } else if( r < 30){
           if( r > 20 ){
             for(int i = 0; i < 2; i++)
               {
                 rotate_right();
                 delay(30);
                 stope();
                 delay(60);
               }
           }
         }
        
        if(r < 30) {
           while( r < 10 ){
             rotate_left();
             delay(10);
             stope();
             delay(50);
             r = ultrSonic(r_trigPin, r_echoPin);
             delay(100);
           }
        } else if( l < 30){
           if( l > 20 ){
             for(int i = 0; i < 2; i++)
               {
                 rotate_left();
                 delay(30);
                 stope();
                 delay(60);
               }
         }
        }   
         forward(150,220);
         delay(60);      
         stope();
         delay(100);
         f = ultrSonic(f_trigPin, f_echoPin); 
         dif++;
         delay(100);
         dif_r = ultrSonic(r_trigPin, r_echoPin);
         delay(100);
         dif_l = ultrSonic(l_trigPin, l_echoPin);
         delay(100);
     }
     stope();
}


void btSimpleCommand() {
  int c;
  
  if (Serial.available() > 0){
    c = Serial.read();
  
    if (c=='S') stope();
    if (c=='F') forward(200,200);
    if (c=='B') backward();
    if (c=='L') left();
    if (c=='R') right();
    if (c==69) rotate_left();    
    if (c==70) rotate_right();  
   }
}

void nextMovement(int nextPosition) {
  String answer = "";
  int aux;
    Serial.print("np: ");
    Serial.println(nextPosition);
  if(f < 34 && f > 25)
          correctForward();
   switch(nextPosition) 
   {
     case 4:
               stope();
               break;
     case 1:
               correctForward();
               break;
     case 3:
               Serial.println("back");
               backward();
               delay(500);
               break;
     case 2:
             Serial.println("ESQUERDA"); 
              l = ultrSonic(l_trigPin, l_echoPin);
              r = ultrSonic(r_trigPin, r_echoPin);
              aux = ultrSonic(f_trigPin, f_echoPin);
               for(int i = 0; i < 10; i++)
               {
                 rotate_left();
                 delay(30);
                 stope();
                 delay(60);
               }
               
               //Corret angle
               r = ultrSonic(r_trigPin, r_echoPin);
            
               forward(150,200);
               delay(30);
               stope();
               delay(30);
               aux = ultrSonic(r_trigPin, r_echoPin);
               while( (aux - r) < -2 )  {
                   rotate_right();
                   delay(30);
                   stope();
                   delay(60);
               }
               while( (aux - r) > 2 )  {
                   rotate_left();
                   delay(30);
                   stope();
                   delay(60);
               }
               correctForward();
               break;
     case 0:
 
               r = ultrSonic(r_trigPin, r_echoPin);
               for(int i = 0; i < 10; i++)
               {
                 rotate_right();
                 delay(30);
                 stope();
                 delay(60);
               }
               correctForward();
               delay(200);
               break;
     case 5:
              l = ultrSonic(l_trigPin, l_echoPin);
              answer += l;                
              f = ultrSonic(f_trigPin, f_echoPin);
              answer = answer + " " + f;
              r = ultrSonic(r_trigPin, r_echoPin);
              answer = answer + " " + r ;
              state = analogRead(pinLDR); 
              answer = answer + " " + state;
              Serial.println(answer);
              delay(200);
              break;
      default:

              break;
   
   
     }
  stope();
  
}

void btCommand() {
  char c;
  int i;
  String answer = "";
  while (Serial.available() > 0) // S efetua a leitura
                               // quando houver dados
  {
    if(index < 29) // Le ate 20 caracteres
    {
        inChar = Serial.read(); // Le um caracter
        inData[index] = inChar; // aramazena-o
        index++;
        inData[index] = '\0';
    }
  }
     
     for(i = 0; i < index;i++) 
     {
        f = ultrSonic(f_trigPin, f_echoPin);
        r = ultrSonic(r_trigPin, r_echoPin);
        l = ultrSonic(l_trigPin, l_echoPin);
        
       switch(inData[i]) 
       {
         case '4':
                   stope();
                   break;
         case '1':
                   correctForward();
                   break;
         case '3':
                   backward();
                   delay(500);
                   break;
         case '0':
                   for(int i = 0; i < 15; i++)
                   {
                     rotate_left();
                     delay(30);
                     stope();
                     delay(60);
                   }
                   correctForward();
                   break;
         case '2':
                   for(int i = 0; i < 18; i++)
                   {
                     rotate_right();
                     delay(30);
                     stope();
                     delay(60);
                   }
             	   correctForward();
                   delay(200);
                   break;
         case 'S':
                  l = ultrSonic(l_trigPin, l_echoPin);
                  answer += l;                
                  f = ultrSonic(f_trigPin, f_echoPin);
                  answer = answer + " " + f;
                  r = ultrSonic(r_trigPin, r_echoPin);
                  answer = answer + " " + r ;
                  state = analogRead(pinLDR); 
                  answer = answer + " " + state;
                  Serial.println(answer);
                  delay(200);
                  break;
          default:
                  Serial.println(inData[i]);
                  break;
       }
       
     }
     
  inChar=-1;
  index = 0;
  stope();
  
}

