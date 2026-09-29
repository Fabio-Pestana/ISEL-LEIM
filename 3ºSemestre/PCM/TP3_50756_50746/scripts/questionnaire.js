function Q1_1browser(elemento) {
    let firstOption = document.getElementById("primeiro"); 
    let secOption = document.getElementById("segundo");
    let thirdtOption = document.getElementById("terceiro");

    if((elemento.id.localeCompare("primeiro") !==0) && (elemento.value === firstOption.value))
    {
        firstOption.value="";
    }

    if((elemento.id.localeCompare("segundo") !==0) && (elemento.value === secOption.value))
    {
        secOption.value="";
    }

    if((elemento.id.localeCompare("terceiro") !==0) && (elemento.value === thirdtOption.value))
    {
        thirdtOption.value="";
    }
}

function Q1_Sites() {
    let aux = document.forms["questionnaire-p1"]["OutroSite"].value;
    if (aux == "nao") {
        document.forms["questionnaire-p1"]["TextOutroSite"].disabled = true;
        document.forms["questionnaire-p1"]["TextOutroSite"].value = "";
    } else {
        document.forms["questionnaire-p1"]["TextOutroSite"].disabled = false;
    }
}


const form1 = document.getElementById("questionnaire-p1");
if(form1){
    form1.addEventListener("submit", function(e){
        //e.preventDefault();

        const userId = crypto.randomUUID();
        localStorage.setItem("userId", userId);

        const idade = form1["idade"].value;
        //console.log("Idade " , idade);

        const genero = form1["genero"].value;
        //console.log("genero " , genero);

        const frequencia = form1["frequencia"].value;
        //console.log("frequencia " , frequencia);
        
        const browser1 = form1["primeiro"].value;
        //console.log("browser1 " , browser1);

        const browser2 = form1["segundo"].value;
        //console.log("browser2 " , browser2);

        const browser3 = form1["terceiro"].value;
        //console.log("browser3 " , browser3);
        
        const outros_sites = form1["OutroSite"].value;
        //console.log("outros sites: " , outros_sites);

        const outros_sites_quais = form1["TextOutroSite"].value;
        //console.log("Texto_outros sites: " , outros_sites_quais);

        const formData = {
            idade, 
            genero,
            frequencia,
            browser1,
            browser2,
            browser3,
            outros_sites,
            outros_sites_quais
        }

        //console.log("formData:" , formData);
        localStorage.setItem(userId, JSON.stringify(formData));
    })
}

const form2 = document.getElementById("questionnaire-p2");
if(form2){
    form2.addEventListener("submit", function(e){

        const userId = localStorage.getItem("userId");
        //console.log("userId " , userId);

        const Explorar = form2["Explorar"].value;
        //console.log("Explorar " , Explorar);

        const Categorias = form2["Categorias"].value;
        //console.log("Categorias " , Categorias);

        const Cor = form2["Cor"].value;
        //console.log("Cor " , Cor);

        const Imgs_Semelhantes = form2["Imgs_Semelhantes"].value;
        //console.log("Imgs_Semelhantes " , Imgs_Semelhantes);

        const Compare_Search = form2["Compare_Search"].value;
        //console.log("Compare_Search " , Compare_Search);

        const prevFormData = JSON.parse(localStorage.getItem(userId));

        // Adicione mais parâmetros ao objeto prevFormData
        prevFormData.Explorar = Explorar;
        prevFormData.Categorias = Categorias;
        prevFormData.Cor = Cor;
        prevFormData.Imgs_Semelhantes = Imgs_Semelhantes;
        prevFormData.Compare_Search = Compare_Search;

        //console.log("Submetido com mais informações:", prevFormData);
        localStorage.setItem(userId, JSON.stringify(prevFormData));
    })
}

const form3 = document.getElementById("questionnaire-p3");
if(form3){
    form3.addEventListener("submit", function(e){
        //e.preventDefault();

        const userId = localStorage.getItem("userId");
        //console.log("userId " , userId);

        const Fantastico_Horrivel = form3["Fantastico_Horrivel"].value;
        //console.log("Fantastico_Horrivel " , Fantastico_Horrivel);

        const Estimulante_Aborrecida = form3["Estimulante_Aborrecida"].value;
        //console.log("Estimulante_Aborrecida " , Estimulante_Aborrecida);

        const Gratificante_Frustante = form3["Gratificante_Frustante"].value;
        //console.log("Gratificante_Frustante " , Gratificante_Frustante);

        const Facil_Dificil = form3["Facil_Dificil"].value;
        //console.log("Facil_Dificil " , Facil_Dificil);

        const Categoria = form3["Categoria"].value;
        //console.log("Categoria " , Categoria);

        const Pesquisa_Cor = form3["Pesquisa_Cor"].value;
        //console.log("Pesquisa_Cor " , Pesquisa_Cor);
        
        const Pesquisa_Imagens = form3["Pesquisa_Imagens"].value;
        //console.log("Pesquisa_Imagens " , Pesquisa_Imagens);
        
        const Pesquisa_Utilidade = form3["Pesquisa_Utilidade"].value;
        //console.log("Pesquisa_Utilidade " , Pesquisa_Utilidade);
        
        const Sistema = form3["Sistema"].value;
        //console.log("Sistema " , Sistema);

        const Classf_Design = form3["Classf_Design"].value;
        //console.log("Classf_Design " , Classf_Design);

        const prevFormData = JSON.parse(localStorage.getItem(userId));

        // Adicione mais parâmetros ao objeto prevFormData
        prevFormData.Fantastico_Horrivel = Fantastico_Horrivel;
        prevFormData.Estimulante_Aborrecida = Estimulante_Aborrecida;
        prevFormData.Gratificante_Frustante = Gratificante_Frustante;
        prevFormData.Facil_Dificil = Facil_Dificil;
        prevFormData.Categoria = Categoria;
        prevFormData.Pesquisa_Cor = Pesquisa_Cor;
        prevFormData.Pesquisa_Imagens = Pesquisa_Imagens;
        prevFormData.Pesquisa_Utilidade = Pesquisa_Utilidade;
        prevFormData.Sistema = Sistema;
        prevFormData.Classf_Design = Classf_Design;

        //console.log("Submetido com mais informações:", prevFormData);
        localStorage.setItem(userId, JSON.stringify(prevFormData));
    })
}