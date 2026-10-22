# Processamento de Imagem e Visão — Trabalhos Laboratoriais

Conjunto de **2 trabalhos laboratoriais** desenvolvidos na unidade curricular **Processamento de Imagem e Visão** (PIV) de LEIM, no **ISEL**, ano letivo **2024/25**.

Os trabalhos usam **Python e OpenCV** para resolver dois problemas de visão por computador: **contar e classificar peças de Lego** numa imagem e **estimar e classificar o movimento de uma mão** num vídeo para animar um objeto gráfico em tempo real.

- **Aluno:** Fábio Pestana — 50756
- **Aluno:** João Ramos — 50730
- **Turma:** 51D
- **Docente responsável:** Pedro Mendes Jorge

---

## Descrição

| Trabalho | Tema | Resultado |
|----------|------|-----------|
| TP1 | Contagem e classificação de peças de Lego retangulares | *Notebook* que processa uma imagem e classifica as peças em 2x2, 2x4, 2x6 e 2x8 |
| TP2 | Estimação e classificação de movimento da mão | Aplicação em Pygame em que um cubo 3D é animado pelos movimentos da mão |

Cada trabalho inclui o enunciado, o relatório (em PDF) e a aplicação em *Jupyter Notebook*, com o código comentado em células *Markdown*.

---

## Estrutura

```
PIV/
├── README.md
├── TP1/
│   ├── Enunciado.pdf
│   ├── T51D-G07_relatorio.pdf
│   ├── T51D-G07_aplicacao.ipynb
│   └── treino/                 # 14 imagens de treino (lego01.jpg ... lego32.jpg)
└── TP2/
    ├── Enunciado.pdf
    ├── T51D-G07_relatorio.pdf
    ├── T51D-G07_aplicacao.ipynb
    └── videos/                 # 8 vídeos gravados com a aplicação (video_tp2_AAAAMMDD_HHMMSS.mp4)
```

---

## TP1 — Contagem e classificação de peças de Lego

Algoritmo que conta e classifica peças de Lego retangulares (**2x2, 2x4, 2x6 e 2x8**) sobre uma mesa azul, fotografadas por uma câmara montada num tripé, com o sensor paralelo à mesa. Deve ser robusto à presença de outras peças, a sombras e ao contacto entre peças.

**Pipeline**

1. **Leitura** da imagem com `cv.imread` (BGR) e conversão para RGB para visualização
2. **Histogramas** dos três canais, para escolher como eliminar o fundo azul
3. **Binarização:** média dos canais verde e vermelho (`addWeighted`), sem o azul do fundo, seguida do limiar automático de **Otsu**
4. **Melhoramento:** preenchimento dos contornos (`findContours` e `drawContours`) e **erosão** com elemento estruturante retangular 8×8 (valor escolhido por tentativa), que separa peças em contacto e remove ruído
5. **Componentes conexos:** `findContours` com `RETR_EXTERNAL` e `connectedComponents`, que dão o número de objetos
6. **Extração de propriedades:**
   - um contorno é **retangular** se `approxPolyDP` (ε = 2 % do perímetro) devolver 4 vértices
   - as dimensões (largura × altura) são obtidas com `minAreaRect`, o que dá independência da orientação da peça
7. **Classificação** por intervalos de dimensões, em píxeis, aprendidos nas imagens de treino:

| Peça | Largura | Altura |
|------|---------|--------|
| 2x2 | 78–91 | 78–91 |
| 2x4 | 78–91 | 168–180 |
| 2x6 | 78–91 | 258–270 |
| 2x8 | 78–91 | 345–355 |

Os objetos não retangulares, ou retangulares fora destes intervalos, são marcados como **"Indef."**

**Exemplo (`lego01.jpg`):** 9 objetos detetados, dos quais 3 peças 2x2, 3 peças 2x4, 1 peça 2x8 e 2 indefinidos.

## TP2 — Estimação e classificação de movimento

Aplicação interativa em tempo real que usa os movimentos de uma mão, vistos por uma câmara ou num vídeo, para animar um **cubo 3D** desenhado com **Pygame**. Distingue seis movimentos: **direita, esquerda, cima, baixo, *zoom in* e *zoom out***.

**Deteção da mão**

- Cor da pele no espaço **YCrCb** (`inRange`, de [0, 140, 97] a [255, 173, 127]), em alternativa ao RG normalizado proposto no enunciado
- Suavização gaussiana 5×5 e fecho morfológico com elemento elíptico 15×15
- Contornos com área mínima de 600 px, e retângulo delimitador desenhado sobre a mão

**Classificação de movimento**

- **Direção:** deslocamento (dx, dy) do centro do retângulo entre *frames* consecutivos. Domina a componente com maior valor absoluto, com limiar de 20 px, e dá direita, esquerda, cima ou baixo.
- ***Zoom*:** razão entre as áreas de pele de dois *frames* consecutivos. Acima de 1,2 é *zoom in* e abaixo de 0,8 é *zoom out*, com *cooldown* de 15 *frames* entre comandos.

**Cubo animado (`Cubo`)**

- Rotação contínua em torno do eixo Y e projeção 3D → 2D
- Animação em duas etapas: o cubo vai até à extremidade do ecrã correspondente ao movimento e regressa ao centro, sem aceitar novos comandos durante a animação
- *Zoom* em passos, até um passo para cada lado, com escala multiplicada por 1,5

**Aplicação**

| Tecla | Ação |
|-------|------|
| `C` | modo câmara (webcam) |
| `V` | modo vídeo, que abre uma janela para escolher um ficheiro |
| `R` | começa a gravar o vídeo da câmara (`video_tp2_AAAAMMDD_HHMMSS.mp4`) |
| `P` | pára a gravação |
| `Esc` | sai da aplicação |

Corre em ecrã inteiro, com instruções no ecrã e indicador "REC" durante a gravação. A pasta `videos/` contém gravações feitas com a própria aplicação.

> **Limitação do relatório:** o programa não distingue a mão da cara, porque ambas são deteções pela cor da pele. Como melhorias, o grupo aponta algoritmos de deteção de gestos mais avançados e uma interface mais sofisticada.

---

## Tecnologias utilizadas

- Python 3
- Jupyter Notebook
- OpenCV
- NumPy
- Matplotlib
- Pygame
- Tkinter
