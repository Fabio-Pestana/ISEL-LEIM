# Importar o módulo pygame
# se a execução deste import em Python3 ou Python2 der algum erro
# é porque o pygame não está bem instalado
import pygame, sys , pygame.mixer
from pygame.locals import *
from math import cos, sin, sqrt


# inicialização do módulo pygame
pygame.init()
pygame.mixer.init()


# criação de uma janela
largura = 676
altura  = 503
tamanho = (largura, altura)
janela  = pygame.display.set_mode(tamanho)
pygame.display.set_caption('CarRace do Fábio - A50756') #nome da janela
#Nesta janela o ponto (0,0) é o canto superior esquerdo


# número de imagens por segundo
frame_rate = 30

# relógio para controlo do frame rate
clock = pygame.time.Clock()

# ler uma imagem em formato bmp
pista = pygame.image.load("PistaSA.jpg")

#música

pygame.mixer.music.load("Sonic The Hedgehog.mp3")

pygame.mixer.music.play(-1)
    
#Inicializa o tempo
t=0.0


#########################
#Para escrever o tempo e velocidade:
font_size = 25
font = pygame.font.Font(None, font_size) # fonte pré-definida
antialias = True # suavização
BLACK = (0, 0, 0)# cor (terno com os valores Red, Green, Blue entre 0 e 255)

##################################

##parametrização:

def parametrizacao (t):
    if t>=0:
        resultado=(262,11)
    if 0<t<=1:
        resultado=(262+201*t, 11-7*t) #done  201.12/2=100.56   ###
    if 1<t<=1.5:
        resultado=(464+67*cos(1.81*(t+220.2)),50+44*sin(1.81*(t+220.2))) #done   
    if 1.5<t<=2.4:
        resultado=(516+114*(t-1.5), 21+65*(t-1.5)) #done 
    if 2.4<t<=2.8:
        resultado=(613 + 28*cos(4.37*(t+446)), 92 + 18* sin(4.37*(t+446)) ) #done
    if 2.8<t<=3.8:
        resultado=(628-228*(t-2.8), 114+77*(t-2.8)) #done 
    if 3.8<t<=4.4:
        resultado=(400-86*(t-3.8), 191+11*(t-3.8)) #done    
    if 4.4<t<=5:
        resultado =(309-28*cos(4.90*(t+143.8)),185-13*sin(4.90*(t+143.8))) #done    
    if 5<t<=5.6:
        resultado = (252 + 29*cos(4.47*(-t+144.2)), 180 + 16* sin(4.47*(-t+144.2))) #done    
    if 5.6<t<=6.3:
        resultado = (236 + 36*cos(2.76*(-t+143.7)), 203 + 37* sin(2.76*(-t+143.7)))  #done
    if 6.3<t<=7.7:
        resultado = (170 + 63*cos(1.52*(t+67.5)), 276 + 69* sin(1.52*(t+67.5)))  #done 
    if 7.7<t<=8.2:
        resultado=(208-316*(t-7.7), 344+240*(t-7.7)) #done 
    if 8.2<t<=8.6:
        resultado = (28 + 12*cos(8.04*(t+251.4)), 448 + 13*sin(8.04*(t+251.4)))  #done 
    if 8.6<t<=9.6:
        resultado=(27+161*(t-8.6), 447-381*(t-8.6)) #done 
    if 9.6<t<=10.6:
        resultado = (293 + 117*cos(t+100.56), 85 + 84*sin(t+100.56))  #done ####
    if t>10.6:
        resultado = (262,11)
    
    return resultado



#################################

#funções auxiliares para medir as velocidades e derivadas das curvas

def auxsen (a, b , c , t):
    return (-a*b*sin(b*(t+c)))

def auxcos (a,b,c,t):
    return (a*b*cos(b*(t+c)))

def auxder (a,b):
    return (((a)*(a))+((b)*(b)))

def v (t):
    a=0
    b=0
    c=0
    if t>=0:
        resultado= 0
    if 0<t<=1:
        resultado= (sqrt(auxder(201,-7)))
    if 1<t<=1.5:
        a= auxsen (67, 1.81 , 220.2 , t)
        b= auxcos (44, 1.81, 220.2, t)
        resultado = (sqrt(auxder (a,b)))   
    if 1.5<t<=2.4:      
        resultado = (sqrt(auxder(114,65)))
    if 2.4<t<=2.8:
        a= auxsen (28, 4.37 , 446 , t)
        b= auxcos (18, 4.37, 446, t)
        resultado = (sqrt(auxder (a,b)))  
    if 2.8<t<=3.8:
        resultado= (sqrt(auxder(-228,77)))
    if 3.8<t<=4.4:
        resultado= (sqrt(auxder(-86,11))) 
    if 4.4<t<=5:
        a= auxsen (-28, 4.90 , 143.8 , -t)
        b= auxcos (-13, 4.90 , 143.8, -t)
        resultado = (sqrt(auxder (a,b)))  #curva   
    if 5<t<=5.6:
        a= auxsen (29, 4.47 , 144.2 , -t)
        b= auxcos (16, 4.47 , 144.2, -t)
        resultado = (sqrt(auxder (a,b)))  #done    
    if 5.6<t<=6.3:
        a= auxsen (36, 2.76 , 143.7 , -t)
        b= auxcos (37, 2.76, 143.7, -t)
        resultado = (sqrt(auxder (a,b)))   #done    
    if 6.3<t<=7.7:
        a= auxsen (63, 1.52 , 67.5 , t)
        b= auxcos (69, 1.52, 67.5, t)
        resultado = (sqrt(auxder (a,b)))   #done  
    if 7.7<t<=8.2:
        resultado=  (sqrt(auxder(-316,240))) 
    if 8.2<t<=8.6:
        a= auxsen (12, 8.04 , 251.4 , t)
        b= auxcos (13, 8.04, 251.4, t)
        resultado = (sqrt(auxder (a,b)))   #done 
    if 8.6<t<=9.6:
        resultado=  (sqrt(auxder(161,-381)))
    if 9.6<t<=10.6:
        a= auxsen (117, 100.56 , 1 , t)
        b= auxcos (85, 100.56, 1, t)
        resultado = (sqrt(auxder (a,b)))   #done ####  
    if t>10.6:
        resultado = 0
    
    return resultado




##### orientação do carro
def orient (t):
    
    if t>=0:
        image = "carro.png"
    if 0<t<=1:
        image = "carro.png"
    if 1<t<=1.2:
        image = "carro.png"
    if 1.2<t<=2.4:
        image = "carro_r.png"
    if 2.4<t<=2.6:
        image = "carro_r.png"
    if 2.6<t<=2.8:
        image = "carro_l.png"
    if 2.8<t<=3.8:
        image = "carroinv.png"
    if 3.8<t<=4.4:
        image = "carroinv.png" 
    if 4.4<t<=4.8:
        image = "carroinv.png" 
    if 4.8<t<=5.2:
        image = "carroinvu.png"
    if 5.2<t<=5.4:
        image = "carroinv.png"
    if 5.4<t<=5.7:
        image = "carro_l.png"
    if 5.7<t<=6.3:
        image = "carro_d.png"
    if 6.3<t<=7.2:
        image = "carro_d.png"
    if 7.2<t<=8.2:
        image = "carro_l.png"
    if 8.2<t<=8.3:
        image = "carro_l.png"
    if 8.3<t<=8.6:
        image = "carro_up.png"
    if 8.6<t<=9.6:
        image = "carro_up.png"
    if 9.6<t<=10.6:
        image = "carro_r2.png"
    if t>10.6:
        image = "carro.png"
    
    return image



###############

#(A) Se descomentar aqui (e comentar B) vejo onde passou/ rasto da trajetória
# Pois neste caso só junta a pista uma vez,
#no outro caso está sempre a juntar/desenhar a pista
#janela.blit(pista, (0, 0)) 



#Ciclo principal do jogo
    

while True:
    tempo = font.render("t="+str(int(t)), antialias, BLACK) 
    janela.blit(pista, (0, 0))  #(B) se descomentar aqui (e comentar (A)) vejo movimento
    carro = pygame.image.load(orient(t))
    janela.blit(carro, parametrizacao(t))
    intensidadev = v(t)
    velocidade = font.render("v="+str(int(intensidadev)), antialias, BLACK)
    janela.blit(tempo, (10, 10))
    janela.blit(velocidade, (10,30))
    pygame.display.update()
    clock.tick(frame_rate)
    t = t+0.1

    

    
    for event in pygame.event.get():
        #Para sair...
        if event.type == QUIT:
            pygame.quit()
            sys.exit()

        #Ao clicar em qualquer local, o tempo recomeça com t=0
        # evento mouse click botão esquerdo (código = 1)
        elif event.type== pygame.MOUSEBUTTONUP and event.button == 1:
            t = 0
                       

##        #Quando queremos saber as coordenadas de um ponto: 
##        # descomentar isto e comentar o "evento mouse click"...
##        #"clicar" nesse ponto... o python print as coordenadas.
##        # evento mouse click botão esquerdo (código = 1)
        #elif event.type== pygame.MOUSEBUTTONUP and event.button == 1:
        #    (x, y) = event.pos
        #    localizacao="posicao=(" + str(x) + "," + str(y) + ")"
         #   print(localizacao)


##FAQs:
##            (1)
##            Quando parametrização (ou velocidade) não está definida
##            para algum valor de t, dá o erro:
##                "local variable "result/resultado" referenced before assignment"
##            
         




