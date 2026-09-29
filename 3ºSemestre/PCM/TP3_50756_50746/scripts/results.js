function getdataForm() {
    let keys = [];
    let todo_index = window.localStorage.length;

    for (let i = 0; i < todo_index; i++) {
        let currentKey = localStorage.key(i);

        // Ignorar a chave "userId"
        if (currentKey !== "userId") {
            let participantData = JSON.parse(localStorage.getItem(currentKey));

            // Por cada pagina vejo se um dos parametros obrigatorios foi preenchido, 
            // isto garante que durante o questionario a pessoa nao fechou o site sem preencher parte dele,
            // tendo assim uma resposta valida.
            // .hasOwnProperty() método que verifica se um objeto tem um certo atributo (nao herdado)
            let pagina1Concluida = participantData.hasOwnProperty("idade"); 
            let pagina2Concluida = participantData.hasOwnProperty("Explorar");
            let pagina3Concluida = participantData.hasOwnProperty("Classf_Design");

            let paginasPreenchidas = pagina1Concluida && pagina2Concluida && pagina3Concluida;
            //console.log(currentKey , paginasPreenchidas);

            // Verificar se todas as páginas foram preenchidas
            if (paginasPreenchidas) { 
                keys.push(currentKey);
            }
        }
    }
    //console.log(keys);
    return keys;
}


function createTable() {
    let keys = getdataForm();

    // Criar a tabela
    let table = document.createElement("table");
    table.className = "table";

    // coluna das keys (participantes do questionario)
    let headerRow = table.insertRow(0);
    let colunaParticipantes = headerRow.insertCell(0);
    colunaParticipantes.innerHTML = "<b>Participantes</b>";

    // Adicionar colunas das perguntas
    let userData = JSON.parse(window.localStorage.getItem(keys[0]));
    for (let info in userData) {
        let colunaInfo = headerRow.insertCell();
        colunaInfo.innerHTML = "<b>" + info + "</b>";
        //console.log(info);
    }

    // Preencher a tabela com as respostas dos participantes
    for (let i = 0; i < keys.length; i++) {
        let userRow = table.insertRow(i + 1);
        let userCell = userRow.insertCell(0);
        userCell.innerHTML = keys[i];

        let userData = JSON.parse(window.localStorage.getItem(keys[i]));
        for (let respostas in userData) {
            let cell = userRow.insertCell();
            cell.innerHTML = userData[respostas];
        }
    }
    // Adicionar a tabela a div - TabelaInfo
    document.getElementById("TabelaInfo").append(table);
}

function graficoCircular() {
    let keys = getdataForm();
    const nTotal = keys.length;

    let masculino = 0;
    let feminino = 0;
    let naoBinario = 0;
    let nResponde = 0;

    //contador de cada genero
    for (let i = 0; i < nTotal; i++) {
        let userData = JSON.parse(window.localStorage.getItem(keys[i]));

        // obter a info do genero na key
        let gender = userData["genero"];

        if (gender == "Masculino") {
            masculino++;
        } else if (gender == "Feminino") {
            feminino++;
        } else if (gender == "nbinario") {
            naoBinario++;
        } else {
            nResponde++;
        }
    }

    let c = document.createElement("canvas");
    c.id = "graficoCircular";
    c.width = 400;
    c.height = 400;
    document.getElementById("caracterizacaoGenero").appendChild(c);

    let ctx = c.getContext("2d");

    let startAngle = 0;
    let endAngle;

    let nPorGenero = [masculino, feminino, naoBinario, nResponde]; //array com o numero de pessoas por genero
    let generos = ["Masculino", "Feminino", "Não Binário", "Não Respondeu"];
    let colors = ["SkyBlue", "pink", "green", "gray"]; // cores do grafico circular 

    for (let i = 0; i < nPorGenero.length; i++) {
        endAngle = startAngle + (nPorGenero[i] / nTotal) * 2 * Math.PI;

        // Desenhar a "fatia" do grafico circular
        ctx.fillStyle = colors[i];
        ctx.beginPath();
        ctx.moveTo(200, 200); // centro do canvas
        ctx.arc(200, 200, 150, startAngle, endAngle);
        ctx.closePath();
        ctx.stroke();
        ctx.fill();

        if(nPorGenero[i]!== 0){
            // texto com o numero de pessoas num genero 
            let angle = (startAngle + endAngle) / 2; // descobrir angulo "medio" para posicionar o texto
            let x = 170 + Math.cos(angle) * 80; // menos da metade da largura do canvas * angulo * um valor para distanciar dos "eixos centrais"
            let y = 200 + Math.sin(angle) * 75; // metade da altura do canvas * angulo * um valor para distanciar dos "eixos centrais"
            ctx.fillStyle = "black";
            ctx.font = "bold 11px Arial"; 
            let strLegend = nPorGenero[i] + " " +  generos[i]; 
            ctx.fillText(strLegend, x, y);
        }
        startAngle = endAngle;
    }
}

function graficoDeBarras() {
    let keys = getdataForm();
    const nTotal = keys.length;

    let resposta_valor5 = 0;
    let resposta_valor4 = 0;
    let resposta_valor3 = 0;
    let resposta_valor2 = 0;
    let resposta_valor1 = 0;

    // Contador de cada classificação
    for (let i = 0; i < keys.length; i++) {
        let userData = JSON.parse(window.localStorage.getItem(keys[i]));

        // Obter a info da classificação na key
        let classf = userData["Classf_Design"];
        if (classf == "5") {
            resposta_valor5++;
        } else if (classf == "4") {
            resposta_valor4++;
        } else if (classf == "3") {
            resposta_valor3++;
        } else if (classf == "2") {
            resposta_valor2++;
        } else {
            resposta_valor1++;
        }
    }

    // Criar elemento canvas
    let canvas = document.createElement("canvas");
    canvas.id = "graficoBarras";
    canvas.width = 400; 
    canvas.height = 400;
    document.getElementById("avaliacaoGlobal").appendChild(canvas);

    let ctx = canvas.getContext("2d");

    let barWidth = 50;
    let barSpacing = 20;
    let maxY = 100;
    let margemX = 40;
    let margemY = 40;

    // Desenhar as barras
    drawBar(ctx, 1, resposta_valor1 / nTotal * maxY, "red", canvas, barWidth, barSpacing, margemY, maxY);
    drawBar(ctx, 2, resposta_valor2 / nTotal * maxY, "orange", canvas, barWidth, barSpacing, margemY, maxY);
    drawBar(ctx, 3, resposta_valor3 / nTotal * maxY, "yellow", canvas, barWidth, barSpacing, margemY, maxY);
    drawBar(ctx, 4, resposta_valor4 / nTotal * maxY, "green", canvas, barWidth, barSpacing, margemY, maxY);
    drawBar(ctx, 5, resposta_valor5 / nTotal * maxY, "blue", canvas, barWidth, barSpacing, margemY, maxY);

    // Desenhar eixos
    ctx.beginPath();
    ctx.moveTo(margemX, canvas.height - margemY);
    ctx.lineTo(canvas.width, canvas.height - margemY);
    ctx.moveTo(margemX, canvas.height - margemY);
    ctx.lineTo(margemX, 0);
    ctx.strokeStyle = "black"; 
    ctx.stroke();

    ctx.fillStyle = "black";
    ctx.font = "bold 12px Arial"; 

    // Adicionar valores em y (percentagens de 10 em 10)
    for (let i = 0; i <= maxY; i += 10) {
        let aux = canvas.height - (i / maxY) * (canvas.height - margemY) - margemY; 
        ctx.fillText(i, 5, aux + 15);

        ctx.beginPath();
        ctx.moveTo(5, aux); 
        ctx.lineTo(canvas.width, aux);
        ctx.stroke();
    }

    // Adicionar valores em x (1-5)
    for (let i = 1; i <= 5; i++) {
        const xPosition = i * (barWidth + barSpacing) - barWidth / 2 + margemX;
        const yPosition = canvas.height - margemY/2; 
        ctx.fillText(i, xPosition, yPosition);
    }
}

function drawBar(ctx, x, height, color, canvas, barWidth, barSpacing, margemY, maxY) {
    // para calcular a escala para multiplicar pela altura
    // devido as margens adicionadas para os numeros laterais e abaixo 
    // do grafico e necessario fazer este calculo (400 - 40)/100 = 3.6
    let scale = (canvas.height - margemY) / maxY;
    
    height = height * scale;

    ctx.fillStyle = color;
    let aux = canvas.height - height - margemY;
    ctx.fillRect(x * (barWidth + barSpacing), aux, barWidth, height);
}

//chamar métodos para "criar" a página html
createTable();
graficoCircular();
graficoDeBarras();

