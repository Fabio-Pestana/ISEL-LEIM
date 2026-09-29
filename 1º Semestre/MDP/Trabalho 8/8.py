from random import seed
from random import choices
from random import randint
seed(1903)

def c(l1,l2):
    mult=1
    s=0
    listaf=[]
    for linhas in range (len(l1)):
        for colunas in range (len(l1[0])):
            mult=1
            mult=l1[linhas][colunas]*l2[linhas][colunas]
            listaf.append(mult)
    for i in range (len(listaf)):
        s=s+listaf[i]
    return s

e_a = [
    [4, 3, 2],
    [2, 4, 3]
]
e_b = [
    [ 1, -1, 1],
    [-1, 1, -1]
]
a = c(e_a, e_b)
print(a)

def get_random_matrices():
    n_lines = randint(100, 200)
    n_columns = randint(100, 200)
    lines_1 = []
    lines_2 = []
    for x in range(n_lines):
        lines_1.append(choices(range(-100, 100), k=n_columns))
        lines_2.append(choices(range(-100, 100), k=n_columns))
    return (lines_1, lines_2)

(e_1, e_2) = get_random_matrices()
(e_3, e_4) = get_random_matrices()
(e_5, e_6) = get_random_matrices()
(e_7, e_8) = get_random_matrices()
(e_9, e_10) = get_random_matrices()

print('só para verificação dos valores pseudoaleatórios gerados')
print(e_10[0][0])
print(e_10[0][1])
print(e_10[1][0])
print(e_10[1][1])

print('exs')
print(c(e_1,e_2))
print(c(e_7,e_8))
print(c(e_5,e_6))
print(c(e_3,e_4))
print(c(e_9,e_10))
