from random import seed
from random import uniform

def s(a,b,c,d,e):
    x=(a+b+c+d+e)/5
    return (round(x,2))


p = s(1.0, 2.0, 3.0, 4.0, 5.0)
print(p)
seed(4948)
w_1 = round(uniform(1.0, 9.0), 2)
w_2 = round(uniform(1.0, 9.0), 2)
w_3 = round(uniform(1.0, 9.0), 2)
w_4 = round(uniform(1.0, 9.0), 2)
w_5 = round(uniform(1.0, 9.0), 2)
p = s(w_1, w_2, w_3, w_4, w_5)


print('só para verificação dos números pseudoaleatórios gerados')
print(w_1)
print(w_2)

print(round(p-w_2,2))
print (round(p-w_1,2))
print (w_4)
print (p)
print(w_3)


