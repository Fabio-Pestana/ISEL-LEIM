from random import seed
from random import choice
seed(5445)

def e(d,a):
    p=(not d) or a
    return p

def f (d,a):
    p2=(e(d,a)) and (e(a,d))
    return p2

def w(d,a,q):
    p3= ((d or a)and f(a,q))
    p4 =  f(a,q)
    return e(p3,p4)
    


x = [True, False]
d = choice(x)
a = choice(x)
q = choice(x)
s_4 = e(d, a)
s_5 = f(d, a)
s_6 = w(d, a, q)
print('só para verificação dos valores pseudoaleatórios gerados')
print(d)
print(a)
print(q)
print(s_4)
print(s_5)
print(s_6)
print('exs')
## ex 1
d=False
a=False
q=False
print(w(d,a,q))
## ex 2
d = True
a = False
q = True
print(w(d,a,q))
## ex 3
d = True
a = False
q = False
print (w(d,a,q))
## ex 4
d = False
a = True
q = False
print (w(d,a,q))
## ex 5
d = True
a = True
q = False
print(w(d,a,q))
