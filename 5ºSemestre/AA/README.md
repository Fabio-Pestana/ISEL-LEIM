# Aprendizagem Automática — Trabalhos Práticos e Avaliações

Trabalhos desenvolvidos na unidade curricular **Aprendizagem Automática** (AA) de LEIM, no **ISEL**, ano letivo **2024/25**.

Inclui **2 trabalhos práticos** (classificação de dados clínicos e classificação, regressão e *clustering* de texto) e **5 fichas de avaliação**, feitas periodicamente em sala de aula.

- **Aluno:** Fábio Pestana — 50756
- **Aluno (TP1 e TP2):** João Ramos — 50730
- **Turma:** 51D
- **Docente responsável:** Gonçalo Marques

---

## Descrição

| Componente | Tema | Realização |
|------------|------|------------|
| TP1 | Diagnóstico de diabetes (*Pima Indians Diabetes*) com classificadores binários | Grupo (João Ramos e Fábio Pestana) |
| TP2 | Críticas de cinema do IMDb: classificação multi-classe, regressão e *clustering* | Grupo (João Ramos e Fábio Pestana) |
| Avaliações | Fichas 0 a 4, feitas em aula | Individual |

Todo o trabalho foi feito em **Jupyter Notebook (Python)**, com o código e as justificações das opções comentados em células *Markdown*.

---

## Estrutura

```
AA/
├── README.md
├── TP1/
│   ├── pimaDiabetesEnunciado_24-25.pdf
│   ├── pimaDiabetes.p
│   └── A50730A50756TP1.ipynb
├── TP2/
│   ├── TP_imdb_24-25.pdf
│   ├── A50730A50756TP2.ipynb
│   └── imdbFull.p                  # NÃO incluído no repositório (ver nota abaixo)
└── Avaliações/
    ├── A50756_Ficha0_ficheiros/
    ├── A50756_Ficha1_ficheiros/
    ├── A50756_Ficha2_ficheiros/
    ├── A50756_Ficha3_ficheiros/
    └── A50756_Ficha4_ficheiros/
```

> **Nota sobre o `imdbFull.p`:** o ficheiro de dados do TP2 (`imdbFull.p`, cerca de 66 MB) **não se encontra neste repositório**.

---

## TP1 — Pima Indians Diabetes

Determinar se uma paciente tem diabetes a partir de 8 indicadores clínicos (número de gravidezes, glicose, pressão arterial, espessura da pele, insulina, IMC, *Diabetes Pedigree Function* e idade). O *dataset* tem 768 mulheres: 500 sem diabetes e 268 com diabetes (`pimaDiabetes.p`).

**Classificadores:** Random Forest, Regressão Logística e SVM.

**Metodologia**

- **Divisão sequencial** em treino (50 %, 384), validação (20 %, 153) e teste (30 %, 231). Verificou-se que o *dataset* não está ordenado por classes.
- **Pesquisa de hiperparâmetros** com ciclos escritos pelo grupo (`rf_hiperparametros`, `lr_hiperparametros` e `svm_hiperparametros`), avaliados no conjunto de validação
- Treino final com treino + validação (537) e avaliação no conjunto de teste
- **Métricas:** matriz de confusão, probabilidade total de acertos, precisão, *recall* e F-score
- **Calibração:** o limiar de decisão é escolhido na curva ROC (`roc_curve`) para garantir um *recall* mínimo de 90 %, o que é prioritário num contexto de diagnóstico médico. Os modelos sem `predict_proba` (SVM) usam `decision_function`.
- **Normalização** com `StandardScaler` (ajustado só ao treino), repetindo a pesquisa de hiperparâmetros, a avaliação e a calibração

**Hiperparâmetros escolhidos**

| Modelo | Dados originais | Dados normalizados |
|--------|-----------------|--------------------|
| Random Forest | 200 árvores, sem profundidade máxima, `min_samples_split=10` | iguais |
| Logistic Regression | `C=0.01`, `lbfgs` | `C=0.1`, `lbfgs` |
| SVM | `C=1`, kernel linear | `C=10`, kernel linear |

**Resultados no conjunto de teste (dados originais)**

| Modelo | | Acertos | Precisão | Recall | F-score |
|--------|---|---------|----------|--------|---------|
| Random Forest | antes | 0,7965 | 0,7667 | 0,5823 | 0,6619 |
| | calibrado | 0,7229 | 0,5581 | 0,9114 | 0,6923 |
| Logistic Regression | antes | 0,7879 | 0,7500 | 0,5696 | 0,6475 |
| | calibrado | 0,7532 | 0,5902 | 0,9114 | 0,7164 |
| SVM | antes | 0,7749 | 0,7368 | 0,5316 | 0,6176 |
| | calibrado | 0,7532 | 0,5887 | 0,9241 | 0,7192 |

**Conclusões do notebook**

- Antes da calibração os três modelos são semelhantes, com a Random Forest à frente, mas o *recall* fica entre 53 % e 58 %, baixo para um diagnóstico médico.
- A calibração leva o *recall* a mais de 90 % à custa da precisão. Nos dados originais a SVM fica com o melhor F-score.
- A normalização não altera a Random Forest, melhora a Regressão Logística e deixa a SVM praticamente igual. Com dados normalizados e calibrados, a Regressão Logística tem o melhor desempenho (acertos 0,7619, *recall* 0,9241 e F-score 0,7264).

## TP2 — Críticas de cinema do IMDb

Trabalho sobre 50 000 críticas de filmes com pontuações de 1 a 10 (as críticas neutras, 5 e 6, foram excluídas), organizado em três tarefas.

**Pré-processamento e vocabulário**

- **Divisão sequencial** em treino (60 %, 30 000), validação (20 %, 10 000) e teste (20 %, 10 000)
- **Limpeza:** remoção de `<br />` e de todos os caracteres não alfabéticos (`clean_text`)
- ***Stemming*** com o `SnowballStemmer` para inglês
- **`TfidfVectorizer`** com `min_df=3`, unigramas e bigramas, `max_features` igual ao número de amostras de treino menos 1000, e *tokens* com 2 ou mais letras. As ***stopwords* não foram removidas** na classificação e na regressão, porque palavras como *not* mudam o sentido da crítica.
- Sem `GridSearchCV` nem validação cruzada, por causa do custo computacional

**I. Classificação multi-classe** (classes 1, 2, 3, 4, 7, 8, 9 e 10)

- **Logistic Regression** (`C=0.7`, `class_weight='balanced'`) e **LinearSVC** (`C=0.1`), com `C` escolhido no conjunto de validação (entre 0,1 e 1,0)
- Foram também testados Random Forest, Multinomial Naive Bayes e KNN, que foram descartados por terem pior desempenho ou não serem adequados a dados TF-IDF de dimensão elevada

**II. Regressão**

- **Ridge** (`alpha=1`), com MSE e R² como métricas
- As previsões contínuas são convertidas em classes (`continuo_para_discreto`) através de limiares ajustados empiricamente a partir da matriz de confusão, para se compararem com os classificadores

**Resultados no conjunto de teste** (treino final com 40 000 críticas e 39 000 *features*)

| Modelo | Acertos | Observações |
|--------|---------|-------------|
| Logistic Regression | 0,410 | melhores classes: 1 (F1 0,65) e 10 (F1 0,59) |
| LinearSVC | 0,438 | melhor modelo; também melhor nas classes 1 e 10 |
| Ridge (convertido em classes) | ≈ 0,31 | MSE 4,01 e R² 0,67 em valores contínuos |

Todos os modelos têm dificuldade nas classes intermédias (2, 3, 8 e 9) e funcionam melhor nas pontuações extremas (1 e 10).

**III. *Clustering***

- Amostra de 25 000 críticas, com TF-IDF e remoção de *stopwords* (aqui o objetivo é descobrir tópicos)
- Redução para 10 dimensões com `TruncatedSVD`
- **Clustering hierárquico** (ligação de Ward) e dendrograma, com dois cortes de distância:
  - **corte a 2,5**, que dá **20 *clusters***: tópicos específicos, como críticas negativas ("perda de tempo"), videojogos, histórias e personagens
  - **corte a 7,5**, que dá **3 *clusters***: temas gerais, como impressões gerais, histórias e personagens, e críticas emocionais, com visualização 2D
- ***K-Means*** com o número de *clusters* do corte do dendrograma, e identificação das 10 palavras com maior TF-IDF médio em cada *cluster*

---

## Avaliações (fichas)

Fichas feitas em sala de aula, **individuais**, com dados e enunciado próprios (`A50756_Q00X_data.p` e `AA_FichaN_A50756.pdf`). Têm questões de **resposta múltipla**, em que só uma resposta em cada alínea está correta e as erradas descontam. As respostas são guardadas em `A50756_FichaN_Respostas.p` e cada ficha tem o seu *notebook* de resolução.

| Ficha | Conteúdo |
|-------|----------|
| 0 | Estatística e álgebra linear sobre dados 2D com 4 classes: médias, matrizes de covariância, produtos matriciais, determinantes, distâncias de Manhattan e de cosseno |
| 1 | Idem, com 5 classes: probabilidades *a priori*, covariâncias por classe, normas e produtos internos |
| 2 | Métricas de classificação (matriz de confusão, *recall*, taxa de verdadeiros negativos) e **classificador de distância ao centroide** com métricas euclidiana, Manhattan e cosseno |
| 3 | **PCA** sobre dígitos do MNIST (`MNISTsmall.p`): variância explicada, valores e vetores próprios, matriz de covariância, correlações e produtos internos |
| 4 | **Regressão linear** de erro quadrático médio mínimo, classificador linear, regressão polinomial (2ª, 3ª e 4ª ordem), **Lasso** e R² com treino e teste por *folds* |

---

## Tecnologias utilizadas

- Python 3
- Jupyter Notebook
- NumPy e SciPy
- scikit-learn (Random Forest, Regressão Logística, SVM, LinearSVC, Ridge, Lasso, TF-IDF, TruncatedSVD e K-Means)
- NLTK (`SnowballStemmer`)
- Matplotlib
