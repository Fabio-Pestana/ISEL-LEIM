from random import seed
from random import choices
from random import randint
seed(8014)

def q(lista):
    max=0
    min=10000000000

    for i in range(len(lista)):
        if(len(lista[i])> max):
            max =len(lista[i])  
        if(len(lista[i])< min):
            min = len(lista[i])

    return (min, max)


def aux():
    result = []
    number_of_strings = randint(100, 200)
    for n in range(number_of_strings):
        min_len = randint(1, 500)
        max_len = min_len + randint(1, 500)
        string_len = randint(min_len, max_len)
        random_string = ''.join(choices('abcdefghij', k=string_len))
        result.append(random_string)
    return result


s = ['aa', 'bbbbb', 'ccc', 'd']
f = q(s)
print(f)


s_1 = aux()
s_2 = aux()
s_3 = aux()
s_4 = aux()
s_5 = aux()


print('só para verificação dos valores pseudoaleatórios gerados')
print(s_5[0][0])
print(s_5[1][0])
print(s_5[2][0])
print(s_5[3][0])

print('exs')
print(len(s_4[8]))
print(q(s_1))
print(q(s_2))
print(s_3[29][290])
print(len(s_5))
