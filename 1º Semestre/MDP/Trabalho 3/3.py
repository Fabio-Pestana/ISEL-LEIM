from random import seed
from random import choices

def v(lista,n):
    for i in range (len (lista)):
        if(lista[i]>=n):
            aux= True
        else:
            return False

    return aux


seed(2354)
w = [3, 4, 5, 4, 7, 9]
g_1 = v(w, 2)
g_2 = v(w, 5)
print(g_1)
print(g_2)
e_1 = choices(range(10000, 100000), k=1000)
e_2 = choices(range(10000, 100000), k=1000)
e_3 = choices(range(10000, 100000), k=1000)
e_4 = choices(range(10000, 100000), k=1000)
e_5 = choices(range(10000, 100000), k=1000)
print('só para verificação dos valores pseudoaleatórios gerados')
print(e_5[0])
print(e_5[1])
print(e_5[998])
print(e_5[999])

print("exs:")
print(v(e_2,10066))
print(v(e_5,10017))
print(v(e_4,10079))
print(v(e_3,10098))
print(v(e_1,10011))
