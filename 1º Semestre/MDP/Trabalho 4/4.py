from random import seed
from random import choices
seed(4876)
def p(n,lista):
    cont=0
    for i in range (len(lista)):
        if(lista[i]== n):
            cont=cont+1

    if(cont==1):
        aux=True
    else:
        aux=False
        
    return aux
        
    
x = [3, 4, 5, 4, 7, 9]
w_1 = p(5, x)
w_2 = p(4, x)
w_3 = p(2, x)
print(w_1)
print(w_2)
print(w_3)
h_1 = choices(range(1000), k=1000)
h_2 = choices(range(1000), k=1000)
h_3 = choices(range(1000), k=1000)
h_4 = choices(range(1000), k=1000)
h_5 = choices(range(1000), k=1000)
print('só para verificação dos valores pseudoaleatórios gerados')
print(h_5[0])
print(h_5[1])
print(h_5[998])
print(h_5[999])
print('exs')
print(p(218,h_5))
print(p(966,h_3))
print(p(193,h_4))
print(p(610,h_2))
print(p(657,h_1))
