# Produção de Conteúdos Multimédia — Trabalhos Práticos

Conjunto de **4 trabalhos práticos e 1 projeto final** desenvolvidos na unidade curricular
**Produção de Conteúdos Multimédia** (PCM) de LEIM, no **ISEL**, ano letivo 2023/24.

- **Aluno:** Fábio Pestana — 50756
- **Aluno:** Miguel Alcobia — 50746
- **Docente responsável:** Pedro Albuquerque Santos

---

## Descrição

Trabalhos práticos em **HTML5, CSS3 e JavaScript** que abordam o desenvolvimento de
**aplicações web multimédia**, incluindo:

- Estrutura semântica do HTML5, CSS3 e **Bootstrap** (design responsivo)
- Programação em **JavaScript** e padrão de design **MVC**
- **Formulários** com validação (Web Forms) e **LocalStorage**
- Desenho no **canvas** e programação orientada a objetos
- Pesquisa e visualização de imagens (histogramas de cor e momentos de cor)
- Áudio, animações e usabilidade (escala **SUS**)

---

## Estrutura dos trabalhos

| Trabalho      | Tema                                |
|---------------|-------------------------------------|
| TP1           | Portfólio Web Responsivo            |
| TP2           | Jogo de Cartas — Blackjack          |
| TP3           | Questionário de Usabilidade         |
| TP4           | FotoPrint (canvas)                  |
| Projeto Final | Image Gallery                       |

---

## TP1 — Portfólio Web Responsivo

Website de portfólio construído com HTML5, CSS3 e Bootstrap, dividido em sete partes:

1. Navegação (barra superior com redes sociais)
2. Carrossel / *slideshow*
3. My Skills
4. About me
5. Projects
6. Contact me
7. Rodapé

Utiliza as *tags* de estrutura do HTML5 e propriedades do CSS3.

## TP2 — Blackjack

Jogo de cartas **Blackjack** em JavaScript, seguindo o padrão **MVC**:

- **Model** (`blackjack_object.js`): classe `Blackjack`, com baralho, baralhar, pontuação e
  jogada do *dealer*
- **View** (`blackjack_oop.html` e CSS): interface do jogo
- **Controller** (`blackjack_manager.js`): ligação entre a interface e o modelo

## TP3 — Questionário de Usabilidade

Dois *websites* no mesmo domínio:

- **Recolha de dados:** questionário com várias páginas (introdução, caracterização, tarefas
  e avaliação global), com validação em HTML (`required`, `pattern`) e JavaScript, e dados
  guardados no **LocalStorage**
- **Resultados:** página que lê o LocalStorage e apresenta as respostas recolhidas
- Exportação e importação dos dados em ficheiros **JSON**

## TP4 — FotoPrint

Aplicação para criar fotografias para impressão, com imagens e objetos desenhados no
**canvas**, em programação orientada a objetos (subclasses de `DrawingObjects`):

- Objetos desenhados: `Bear` (urso, com instâncias de `Oval`), `Ghost` (fantasma, com
  `arcTo()` ou `quadraticCurveTo()`) e um objeto à escolha do grupo
- Cor dos objetos ajustável e alteração da cor de fundo
- Inserir e remover imagens, objetos e texto
- *Drag and drop* (`setPos(x, y)`) e duplicação de objetos com duplo clique (`cloneObj()`)
- Descarregar a imagem final (*Save Image*)

## Projeto Final — Image Gallery

Galeria de imagens em HTML5 e JavaScript, semelhante a uma versão simplificada do Google
Photos, com imagens e meta-informação fornecidas pelo docente (ficheiro JSON).

**Funcionalidades:**

- Pesquisa por **palavra-chave** (categoria da imagem)
- Pesquisa por **cor**, com o histograma de 12 cores e a distância de Manhattan
- Pesquisa por **imagem semelhante** (momentos de cor no espaço HSV)
- Visualização dos resultados no **canvas**, em grelha, por relevância
- **Ecrã de loading** animado
- **Dark mode** com animação sol/lua
- **Música de fundo** com botão para ligar e desligar

**Nota do relatório:** a pesquisa por imagens semelhantes não ficou com o resultado
pretendido.

---

## Tecnologias utilizadas

- HTML5, CSS3 e JavaScript
- Bootstrap
