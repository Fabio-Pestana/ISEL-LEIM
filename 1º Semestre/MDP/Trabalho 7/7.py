from random import seed
from random import choices
from random import randint
seed(9796)

def a(lista):
    listaf=[]
    for i in range (len(lista[0])):
        s=0
        for j in range (len(lista)):
            s=s+ lista[j][i]
        listaf.append(s)
    return listaf
            
            
        

s = [
    [4, 3, 9, 1],
    [2, 6, 8, 2],
    [7, 5, 1, 3]
]

c = a(s)
print(c)

def get_random_matrix():
    n_lines = randint(100, 200)
    n_columns = randint(100, 200)
    lines = []
    for x in range(n_lines):
        lines.append(choices(range(-100, 100), k=n_columns))
    return lines

s_1 = get_random_matrix()
s_2 = get_random_matrix()
s_3 = get_random_matrix()
s_4 = get_random_matrix()
s_5 = get_random_matrix()

print('só para verificação dos valores pseudoaleatórios gerados')
print(s_5[0][0])
print(s_5[0][1])
print(s_5[1][0])
print(s_5[1][1])

print('exs')
print(len(s_2))
print(len(s_1))
print(a(s_3)[0])
print(a(s_5)[66])
print(a(s_4)[-1])

