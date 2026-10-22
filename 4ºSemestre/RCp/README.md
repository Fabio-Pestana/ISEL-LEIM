# Redes de Computadores — Projeto

Projeto desenvolvido na unidade curricular **Redes de Computadores** (RCp) de LEIM, no **ISEL**, ano letivo 2023/24.

O projeto foi dividido em **4 fases**, cada uma com o respetivo relatório, e tem como objetivo **construir uma rede de computadores**: começa num servidor Web e num cliente Web escrito de raiz, e evolui até uma **rede empresarial típica** simulada no **Cisco Packet Tracer**, com várias LANs, *routers*, rotas, DHCP, DNS e um servidor Web.

- **Aluno:** Fábio Pestana — 50756
- **Aluno:** João Ramos — 50730
- **Aluno:** Miguel Alcobia — 50746
- **Turma:** 42D
- **Docente responsável:** Nuno Garcia

---

## Descrição

| Fase | Tema | Ferramentas | Peso |
|------|------|-------------|------|
| 1 | Servidor Web e cliente Web em Java | XAMPP (Apache), Wireshark, Java | 25 % |
| 2 | LAN A e LAN B ligadas por um *router* | Cisco Packet Tracer | 25 % |
| 3 | Várias redes, redes de trânsito e rotas | Cisco Packet Tracer | 25 % |
| 4 | Serviços de rede: DHCP, DNS e Web | Cisco Packet Tracer | 25 % |

Conceitos abordados:

- Servidor Web, pedidos e respostas **HTTP** (cabeçalhos, códigos de estado e redirecionamentos)
- **Sockets TCP** e captura de tráfego com **Wireshark**
- Endereçamento **IPv4**, ***subnetting*** e endereços de *network* e de *broadcast*
- **Cache ARP**, ***ping*** e ***traceroute***
- **Routing** estático e tabelas de routing (`show ip route`)
- **DHCP** (com *relay* no *router*) e **DNS**

---

## Estrutura

```
RCp/
├── README.md
├── Enunciado.pdf
├── Fase1/
│   ├── grupo4_turma42D_relatorio.pdf
│   └── Tp1_redes/
│       └── src/
│           └── WebClient.java        # Cliente Web em Java
├── Fase2/
│   ├── grupo4_turma42D_relatorio.pdf
│   └── Projeto2324v.pka              # Projeto Packet Tracer configurado
├── Fase3/
│   ├── grupo4_turma42D_relatorio.pdf
│   └── Projeto2324v.pka
└── Fase4/
    ├── grupo4_turma42D_relatorio.pdf
    └── Projeto2324v.pka
```

Cada fase inclui o **relatório** (em PDF). A Fase 1 inclui o código Java e as Fases 2, 3 e 4 incluem o ficheiro do **Cisco Packet Tracer** (`.pka`) com a rede já configurada. O ficheiro `Projeto2324v.pka` original, com a topologia por configurar, foi fornecido pelos docentes.

---

## Fase 1 — Servidor Web e cliente Web

**Servidor:** instalado o servidor **Apache** através do **XAMPP**, testado com `http://127.0.0.1/` e com o IP do computador (obtido com `ipconfig`). Foi ainda testado com **dois computadores**, um a correr o servidor e outro o cliente.

**Wireshark:** capturados os acessos ao servidor com o filtro `http` e comparados os cabeçalhos enviados pelo *browser*, pelo cliente em Java e pelo servidor. O relatório explica os códigos `302 Found` e `200 OK` e os cabeçalhos `Date`, `Server`, `Last-Modified`, `ETag`, `Accept-Ranges`, `Content-Length`, `Connection` e `Content-Type`.

**Cliente Web (`WebClient.java`):** substitui o *browser*, sem usar nenhuma biblioteca HTTP. A ligação TCP é feita com `java.net.Socket`, o pedido HTTP é escrito à mão e a resposta é apresentada na consola.

- Menu inicial para usar os valores por omissão (`127.0.0.1`, porto `80`) ou indicar outro IP e porto, com validação do endereço e do porto (0–65535)
- Menu principal: **1** fazer um `GET`, **2** alterar o endereço e o porto, **3** sair
- Pedido `GET <caminho> HTTP/1.1` com os cabeçalhos `Host` e `Connection: close`
- Resposta a `301` e `302`: lê o cabeçalho `Location` e repete o pedido para o novo caminho
- Termina o pedido quando recebe `200 OK`, com outros códigos de estado volta a tentar ao fim de 1 s
- Código documentado linha a linha, com JavaDoc em cada método

---

## Fase 2 — LAN A e LAN B

Primeira fase no **Cisco Packet Tracer**, sobre o espaço de endereços do grupo, **192.168.4.0/24** (4 é o número do grupo).

1. **LAN A:** `Laptop0` com `192.168.4.1` e `PC0` com `192.168.4.2`, sem *gateway*. Verificação da **cache ARP** (`arp -a`) antes e depois do `ping`, e explicação das diferenças. Concluiu-se que o `traceroute` não ajuda a saber se um PC está ligado a um *switch*, porque este trabalha na camada 2 e o `traceroute` usa IP e ICMP (camada 3).
2. **LAN A e LAN B com um *router* (R1):** divisão em duas sub-redes **/25** (máscara `255.255.255.128`):

| LAN | Hosts | Gateway (R1) |
|-----|-------|--------------|
| A | `192.168.4.1` (Laptop0), `192.168.4.2` (PC0) | `192.168.4.126` |
| B | `192.168.4.129` (PC1), `192.168.4.130` (Laptop1) | `192.168.4.254` |

Testes de conectividade entre as duas LANs, nova análise da cache ARP e tabela de *routing* (`show ip route`) com as duas redes ligadas diretamente (`FastEthernet0/0` e `FastEthernet1/0`).

## Fase 3 — Várias redes e routing

Expansão para três *routers* (R0, R1 e R2), três **redes de trânsito** (/30) e uma **LAN Server**. O R0 é o *router* de saída para a Internet (`8.8.8.8/24`), simulada por um servidor externo (`8.8.8.1`).

**Dimensionamento:** soma dos números de aluno = 152 232, que dá 32 no módulo 100. A LAN A fica com 32 clientes e a LAN B com metade (16). A LAN A usa uma sub-rede /27, a LAN B uma /28, as redes de trânsito /30 e a LAN Server fica com o maior bloco restante (/26).

| Rede | Sub-rede | Network | Broadcast |
|------|----------|---------|-----------|
| LAN A | `192.168.4.0/27` | `192.168.4.0` | `192.168.4.31` |
| LAN B | `192.168.4.32/28` | `192.168.4.32` | `192.168.4.47` |
| Trânsito A (R1–R2) | `192.168.4.48/30` | | |
| Trânsito B (R0–R1) | `192.168.4.52/30` | | |
| Trânsito C (R0–R2) | `192.168.4.56/30` | | |
| LAN Server | `192.168.4.64/26` | `192.168.4.64` | `192.168.4.127` |

**Interfaces dos routers**

| Router | Interface | IP | Máscara |
|--------|-----------|----|---------|
| R0 | Fa0/0 | `8.8.8.8` | `255.255.255.0` |
| R0 | Fa4/0 | `192.168.4.58` | `255.255.255.252` |
| R0 | Fa5/0 | `192.168.4.54` | `255.255.255.252` |
| R1 | Fa0/0 | `192.168.4.30` | `255.255.255.224` |
| R1 | Fa1/0 | `192.168.4.46` | `255.255.255.240` |
| R1 | Fa4/0 | `192.168.4.49` | `255.255.255.252` |
| R1 | Fa5/0 | `192.168.4.53` | `255.255.255.252` |
| R2 | Fa0/0 | `192.168.4.126` | `255.255.255.192` |
| R2 | Fa4/0 | `192.168.4.57` | `255.255.255.252` |
| R2 | Fa5/0 | `192.168.4.50` | `255.255.255.252` |

**Dispositivos**

| Dispositivo | IP | Gateway |
|-------------|----|---------|
| PC0 / Laptop0 | `192.168.4.2` / `192.168.4.1` | `192.168.4.30` |
| PC1 / Laptop1 | `192.168.4.33` / `192.168.4.34` | `192.168.4.46` |
| Servidor DHCP | `192.168.4.65` | `192.168.4.126` |
| Servidor DNS | `192.168.4.66` | `192.168.4.126` |
| Servidor HTTP | `192.168.4.67` | `192.168.4.126` |
| Servidor externo | `8.8.8.1` | `8.8.8.8` |

**Rotas estáticas** (rede, máscara e *next hop* em cada *router*) para garantir que:

1. o tráfego das LANs A e B para a LAN Server usa sempre a ligação direta R1–R2;
2. o tráfego das LANs A e B para o exterior usa sempre a ligação direta R1–R0;
3. o tráfego da LAN Server para o exterior usa sempre a ligação direta R2–R0.

**Testes:** `ping` entre todos os dispositivos (PC0, Laptop0, PC1, Laptop1, servidores e *routers*) e `tracert` a partir de PC0, PC1, servidor DHCP e servidor externo para confirmar os caminhos escolhidos.

## Fase 4 — Serviços: DHCP, DNS e Web

- **DHCP:** os equipamentos das LANs A e B passam de IP estático a DHCP. Foram criadas duas *pools* no servidor DHCP (uma por LAN, com o *gateway* da respetiva interface do R1) e configurado o ***DHCP relay*** (`ip helper-address`) nas interfaces do R1 voltadas para as LANs A e B.
- **DNS:** configurado o IP do servidor DNS nos servidores da LAN Server e criado um registo **A** que associa o domínio `company.com` ao IP `8.8.8.1`.
- **Web:** acesso à página `company.com` pelo *browser* dos PCs da LAN A.
- **Testes:** `nslookup` (DNS), `ping` ao domínio, `ipconfig /release` e `ipconfig /renew` (DHCP) e `ipconfig` em PC0, Laptop0, PC1 e Laptop1.

---

## Tecnologias utilizadas

- Java
- Apache (XAMPP)
- Wireshark
- Cisco Packet Tracer
- HTTP, TCP/IP, ARP, ICMP, DHCP e DNS
- IntelliJ IDEA
