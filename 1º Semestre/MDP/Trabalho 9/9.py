from random import seed
from random import choices
from random import randint
seed(1992)

def n(nome):
    lista=[]
    ficheiro= open(nome, 'r' , encoding='utf8')
    for i in ficheiro:
        lista.append(i)

    return lista
        


file = open('text.txt', 'w', encoding='utf8')
file.write('abcd\n')
file.write('efg\n')
file.write('hijkl\n')
file.close()

q = n('text.txt')
print(q)

def write_random_file(filename):
    file = open(filename, 'w', encoding='utf8')
    n_lines = randint(500, 1000)
    for x in range(n_lines):
        n_letters = randint(500, 1000)
        random_string = ''.join(choices('abcdefghij', k=n_letters)) + '\n'
        file.write(random_string)
    file.close()
    return random_string

write_random_file('v_1.txt')
write_random_file('v_2.txt')
write_random_file('v_3.txt')
write_random_file('v_4.txt')
aux = write_random_file('v_5.txt')

print('só para verificação dos valores pseudoaleatórios gerados')
print(aux[0])
print(aux[1])
print(aux[2])
print(aux[3])

print('exs')
print(len(n('v_2.txt')[139]))
print(len(n('v_1.txt')))
print(n('v_3.txt')[256][538])
print(n('v_5.txt')[476][153])
print(n('v_4.txt')[-1][0])
