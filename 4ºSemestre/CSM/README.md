# Codificação de Sinais Multimédia — Trabalhos Práticos

Conjunto de **4 trabalhos práticos** desenvolvidos na unidade curricular **Codificação de Sinais Multimédia** (CSM) de LEIM, no **ISEL**, 2º semestre do ano letivo 2023/24.

- **Aluno:** Fábio Pestana — 50756
- **Aluno:** João Ramos — 50730
- **Aluno:** Miguel Alcobia — 50746
- **Turma:** 42D
- **Docente responsável:** Gonçalo Valadão

---

## Descrição

Trabalhos práticos, desenvolvidos em **Jupyter Notebook (Python)**, que abordam a **compressão de imagem, texto, áudio e vídeo**, desde o processamento básico de imagem até à codificação de vídeo, incluindo:

- Leitura, conversão e gravação de imagens com **OpenCV** (formatos TIFF, JPEG e BMP)
- Taxa de compressão, **SNR** e **PSNR**
- Conversão para tons de cinzento, histogramas e planos de bits
- *Dithering* de **Floyd-Steinberg**
- Compressão sem perdas com **códigos de Huffman**, entropia e eficiência
- Codificador e descodificador **JPEG sequencial** (DCT 8×8, quantificação, codificação DC diferencial, *zig-zag* e *run-length*)
- Codificação de vídeo com **I-frames** e **P-frames**
- **Estimação e compensação de movimento** com macroblocos 16×16

---

## Estrutura

```
CSM/
├── TP1/
│   ├── trab1_csm_2324.pdf
│   ├── dados do tp1
│   └── A50730A50746A50756_T1.ipynb
├── TP2/
│   ├── trab2_csm_2324.pdf
│   ├── dados do tp2
│   └── A50730A50746A50756_T2.ipynb
├── TP3/
│   ├── trab3_csm_2324.pdf
│   ├── Tabelas_jpeg.py
│   ├── dados do tp3
│   └── A50730A50746A50756_T3.ipynb
└── TP4/
    ├── trab4_csm_2324.pdf
    ├── dados do tp4
    └── A50730A50746A50756_T4.ipynb
```

Cada trabalho inclui o **enunciado** (em PDF), conjunto de dados necessários para a resolução dos trabalhos e a respetiva **resolução** em *notebook*, com as funções comentadas em células *Markdown*.

---

## Trabalho 1 — Processamento de imagem com OpenCV

- Abrir a imagem `lena.tif` e analisar `dtype` e `shape`
- Gravar em **JPEG** com diferentes qualidades (80 % e 10 %) e calcular a **taxa de compressão**, o **SNR** e o **PSNR**
- Conversão para **tons de cinzento** e justificação dos pesos da equação de luminância
- **Histograma** e número de níveis de cinzento
- Separação da imagem nos **8 planos de bits**
- Algoritmo de ***dithering* Floyd-Steinberg** (`dither`), com gravação da imagem a preto e branco num **ficheiro binário** (`imgBWToBin` / `binToImgBw`)
- Função `plot_setores` que desenha uma imagem 100×100 dividida em setores, com o ângulo dado por parâmetro

## Trabalho 2 — Compressão sem perdas (Huffman)

Implementação de um codificador de Huffman completo:

- `gen_huff_table`: gera a tabela de códigos a partir dos símbolos e das suas probabilidades
- `encode_huff` e `decode_huff`: codificação e descodificação de mensagens
- `encode_table`: codifica a própria tabela, com um protocolo de cabeçalho (bits de enchimento e tamanho da tabela)
- `write2file` e `read_file`: gravação e leitura da tabela e da mensagem em ficheiro

Testes com diferentes tipos de média:

- **Imagem:** `LenaColor.tif` e `LenaGray.tif`
- **Texto:** `DecUniversalDH.txt` e `DecUniversalDH.pdf`
- **Áudio:** `HenryMancini-PinkPanther30s.mp3` e `HenryMancini-PinkPanther.mid`

Para cada ficheiro medem-se o tempo de geração do código, a **entropia**, o número médio de bits por símbolo, a **eficiência**, os tempos de codificação e descodificação e o tamanho do ficheiro, e verifica-se que a mensagem descodificada é igual à original.

## Trabalho 3 — Codificador JPEG sequencial

Codificador e descodificador JPEG para imagens em tons de cinzento (`LenaGray.tif`):

1. **DCT 2D** por blocos de 8×8 (`codificador_DCT` / `descodificador_DCT`)
2. **Quantificação** com a tabela K1 e um fator de qualidade (`quantificar_dct` / `inversa_quantificacao`)
3. **Codificação diferencial** dos coeficientes DC (`codificador_dc` / `descodificador_dc`)
4. Ordenação em ***zig-zag*** dos coeficientes AC e pares *(zero run length, valor não nulo)* (`cod_cof_AC` / `decod_cof_AC`)
5. Codificação de Huffman com as **tabelas K3 e K5** da norma ITU T.81 (`tab_K3_K5` / `decode_K3_K5`), gravada e lida de ficheiro
6. Medição do **SNR** e da **taxa de compressão** para vários fatores de qualidade, com gráfico taxa de compressão em função do SNR e comparação com um conversor online (CloudConverter)

> **Nota do relatório:** os valores de SNR ficaram próximos dos do conversor usado na comparação, mas a taxa de compressão ficou bastante afastada. O grupo atribui o problema à codificação e/ou descodificação das tabelas K3 e K5.

## Trabalho 4 — Codificação de vídeo

Três codificadores de vídeo, testados com as sequências `bola_seq` e `carro_seq` e com JPEG do OpenCV a 50 % de qualidade:

1. **Todas as frames intra (I)**
2. **I-frame + P-frames** (diferença para a I-frame) **sem compensação de movimento**
3. **I-frame + P-frames com compensação de movimento**, com as funções `calcular_mae` (erro absoluto médio entre blocos 16×16), `pesquisar_macrobloco` (janela de −15 a +15) e `construir_frame_predita`, e visualização da frame predita, da frame diferença e dos **vetores de movimento** (`quiver`)

Para cada codificador medem-se, frame a frame, a **taxa de compressão**, o **SNR**, a **entropia**, a **energia média por pixel** e os **tempos de compressão e descompressão**, apresentados em gráficos.

---

## Tecnologias utilizadas

- Python 3
- Jupyter Notebook
- OpenCV
- NumPy
- Matplotlib
