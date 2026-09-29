from random import seed
from random import choices
seed(5964)

def n(n,lista):
    i=0
    cont=0
    while(i<len(lista)):
        if(lista[i]==n and lista[i+1]==n and lista[i+2]==n):
            cont=cont+1
            i=i+3
        else:
            i=i+1

    return cont


e = [3, 4, 4, 4, 4, 4, 3, 5, 5, 3, 4, 4, 4]
g_1 = n(5, e)
g_2 = n(4, e)
g_3 = n(2, e)
print(g_1)
print(g_2)
print(g_3)
h_1 = choices(range(10), k=5000)
h_2 = choices(range(10), k=5000)
h_3 = choices(range(10), k=5000)
h_4 = choices(range(10), k=5000)
h_5 = choices(range(10), k=5000)
print('só para verificação dos valores pseudoaleatórios gerados')
print(h_5[0])
print(h_5[1])
print(h_5[998])
print(h_5[999])
print('exs')

print(n(1,h_4))
print(n(3,h_2))
print(n(3,h_3))
print(n(12,h_5))
print(n(7,h_1))
