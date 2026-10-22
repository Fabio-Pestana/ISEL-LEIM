# Redes de Internet - Trabalhos Práticos

Trabalhos práticos da unidade curricular de **Redes de Internet** (RI) de LEIM, no ISEL, ano letivo 2024/2025.

- **Fábio Pestana** — 50756
- **João Ramos** — 50730
- **Turma:** 51D
- **Docente responsável:** Vítor Almeida

---

## Descrição

A unidade curricular aborda o funcionamento e a configuração de redes IP de dimensão empresarial e de operador (ISP). Foram realizados três trabalhos práticos, cada um com um relatório associado e um projeto de simulação de rede:

| Trabalho | Tema | Ferramenta |
|---|---|---|
| TP1 | VLANs, STP, routing estático, RIPv2, NAT e ACLs | Cisco Packet Tracer |
| TP2 | OSPF multi-área e configurações avançadas de routing | GNS3 |
| TP3 | BGP: routing entre Sistemas Autónomos (AS) | GNS3 |

---

## Estrutura do repositório

```
RI/
├── TP1/
│   ├── 1TP_RI_24_25_base.pkt            # Projeto (Packet Tracer)
│   └── TP1_G9_A50730_A50756.pdf         # Relatório do TP1
├── TP2/
│   ├── OSPF_T2_RI_24_25_v1.2.pdf        # Enunciado do TP2
│   ├── TP2_G9_A50730_A50756.pdf         # Relatório do TP2
│   └── Redes_internet_24_25_TP2_alunos/ # Projeto GNS3 (10 routers + 5 VPCS)
│       ├── README.txt
│       ├── Redes_internet_24_25_TP2_alunos.gns3
│       ├── images/IOS
│       └── project-files/
│           ├── dynamips/               
│           ├── vpcs/                    
│           └── images/
└── TP3/
    ├── BGP_TP3_RI_24_25_v1_3.pdf        # Enunciado do TP3
    ├── TP3_G9_A50730_A50756.pdf         # Relatório do TP3
    └── TP3_G9_51D.gns3project           # Projeto GNS3 exportado 
```

> **Nota:** o ficheiro `TP3/TP3_G9_51D.gns3project` **não se encontra no repositório**.

---

## TP1 — VLANs, STP, RIPv2, NAT e ACLs

Simulação, em Packet Tracer, da rede de duas empresas (Empresa A e Empresa B) ligadas a um ISP, a partir de um ficheiro base fornecido pelos docentes.

**Tarefas realizadas**
1. **Questões teóricas** sobre VLANs por omissão, formato da trama 802.1Q, portas *access* e *trunk*, VLAN nativa, *Spanning Tree* (Root Bridge, custos de porto, estados Root/Designated/Blocking, tempos Max Age e Forward Delay), Rapid-PVST e comparação entre *hub* e *switch*.
2. **Segmentação da Empresa A** em quatro VLANs (Contabilidade, Secretariado, Informática e Gestão da Rede), com *trunks* sem negociação DTP (`switchport nonegotiate`) e portas de acesso.
3. **Router A** com sub-interfaces 802.1Q (*router-on-a-stick*).
4. **Empresa B** com duas VLANs (Servidores e Engenharia) e endereçamento privado.
5. **Topologia do ISP** com ligações /30 públicas, PVST, *trunk pruning* e escolha do *root bridge* por VLAN nos *switches* de distribuição.
6. **Rotas estáticas** nos routers do ISP e rotas por omissão nas empresas.
7. **RIPv2** classless (`no auto-summary`) no núcleo do ISP, com `passive-interface`, redistribuição de rotas estáticas, `default-information originate` e uma *loopback* 8.8.8.8/32 a simular a Internet.
8. **NAT** (estático para o servidor web e dinâmico para os restantes equipamentos).
9. **ACLs** para implementar as restrições de comunicação entre departamentos, validadas com `ping`.

---

## TP2 — OSPF multi-área

Laboratório "OSPF Lab: Advanced Routing Configurations" em GNS3, com **10 routers Cisco 7200 (R1–R10)** e **5 PCs (VPCS)**, organizados em 4 áreas OSPF:

- **Área 0** (*backbone*): R1, R2, R3, R4
- **Área 1**: R7 e R5, ligada à Área 0 através de uma *virtual link*
- **Área 2**: R2, R8, R9, com redistribuição de rotas RIPv2
- **Área 3**: R1, R3, R5, R6

**Regras do enunciado:** sem rotas estáticas ou por omissão; `router ospf 1`, *loopbacks* /32 (R1 = 1.1.1.1 … R10 = 10.10.10.10) usadas como *router ID* e sem adjacências, redes /30 configuradas como *point-to-point* para convergência mais rápida.

**Conteúdo do relatório**
1. Configuração inicial dos routers (divisão de segmentos S1–S11, endereços dos PCs e das interfaces).
2. OSPF básico: Área 0 e Área 3, autenticação **MD5**, ligações *point-to-point* e controlo da eleição de **DR/BDR** (prioridades 200, 100 e 0 em R1, R3 e R6).
3. Área 1 e *virtual link*.
4. Área 2 e rotas externas: redistribuição de RIPv2, NSSA e ajuste da métrica externa.
5. OSPF avançado: sumarização de rotas no R5 e cenário de manutenção com alteração de custos em R1/R3.
6. Otimização: temporizadores de convergência (*hello* rápidos) e *traffic engineering*, verificado com `traceroute` entre PC11, PC22, PC33, PC44 e PC55.

---

## TP3 — BGP

Laboratório "BGP Lab: Routing Configurations" em GNS3, com **16 routers Cisco 7200** que representam um ecossistema de Sistemas Autónomos (ISPs Tier 1 e Tier 2 e respetivos clientes): AS 1, 701, 1273, 4637, 5511, 17390, 20717, 23344 e 64513 (privado).

**Regras do enunciado:** sem rotas estáticas ou por omissão (exceto indicação em contrário), OSPF (`router ospf 1`) dentro de cada AS, sem adjacências em *loopbacks* nem entre AS, *loopback0* = 10.ID.ID.ID usada como *router ID* e nas sessões iBGP, *loopback1* com IP público do AS, `next-hop-self` no iBGP, sessões eBGP sobre os IPs das interfaces físicas, endereços públicos fora do IGP, *route reflectors* (R3 e R4) no AS 1273.

**Conteúdo do relatório**
1. **Configuração inicial e IGP (OSPF):** endereçamento, redes /30 *point-to-point* e perguntas teóricas (Sistema Autónomo, Tier 1/Tier 2, peerings e IXP).
2. **BGP sem políticas:** AS com um só router (`network` + rota para `Null0`) e AS com vários routers (iBGP com *peer-groups*, route reflectors e `next-hop-self`), perguntas sobre o iBGP e captura Wireshark da sessão BGP.
3. **Escalabilidade e simplificação:** agregação com `aggregate-address ... summary-only`, *prefix-list* de saída (máscara até /24), `remove-private-as` para esconder o AS privado do AS-PATH e análise da seleção de caminho BGP.
4. **Influenciar o routing:** `as-path access-list` e *route-maps* com `local-preference` 200 nos AS 1273 e 5511, para que o AS 20717 seja o caminho preferido em ambos os sentidos.
5. **Segurança:** autenticação (`neighbor ... password`) nos peerings eBGP do AS 701, filtro de **bogons** (10.0.0.0/8 e 127.0.0.0/8) nos routers de fronteira do AS 1273 e **RTBH** (*Remote Triggered Black Hole*) com R4 como *trigger router*, `Null0`, comunidade `no-export` e uRPF.

---

## Tecnologias utilizadas

- **Cisco Packet Tracer** (TP1)
- **GNS3** com routers Cisco 7200 (Dynamips) e **VPCS** (TP2 e TP3)
- **Protocolos e tecnologias:** VLAN (802.1Q), STP/PVST/Rapid-PVST, RIPv2, OSPF (multi-área, MD5, virtual link, NSSA, sumarização), BGP (iBGP, eBGP, route reflectors, agregação, políticas, RTBH), NAT, ACLs, uRPF
- **Wireshark** (análise de pacotes BGP)
