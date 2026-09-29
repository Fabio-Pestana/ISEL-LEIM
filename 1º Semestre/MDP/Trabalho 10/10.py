from random import seed
from random import choices
from random import randint

seed(3509)

def p(text):
    d = {}
    for letter in text:
        if letter in d:
            d[letter] += 1
        else:
            d[letter] = 1
    return d


text = 'aaabccddddefabb'
m = p(text)
print(m)

aux = 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ'
i_1 = ''.join(choices(aux, k=randint(5000, 10000)))
i_2 = ''.join(choices(aux, k=randint(5000, 10000)))
i_3 = ''.join(choices(aux, k=randint(5000, 10000)))
i_4 = ''.join(choices(aux, k=randint(5000, 10000)))
i_5 = ''.join(choices(aux, k=randint(5000, 10000)))

print('só para verificação dos valores pseudoaleatórios gerados')
print(i_5[0])
print(i_5[1])
aux = len(i_5)
print(i_5[aux-2])
print(i_5[aux-1])

print('exs')
occurrences = p(i_3)
min_letter = min(occurrences, key=occurrences.get)
print(min_letter)

occurrences = p(i_2)
max_letter = max(occurrences, key=occurrences.get)
print(max_letter)

print(p(i_4)['F']) #verdade(3)
print(p(i_4)['B']) #verdade (3)
print(p(i_1)['n']) #verdade (4)
print(len(p(i_5))) #verdade (5)
