package util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XMLDoc2 {

    private static String xmlPath = "src/main/webapp/";
    
    
    /**
     * Cria um elemento com o nome especificado, define o conteúdo de texto do elemento e retorna o elemento criado.
     *
     * @param doc         O documento XML ao qual o elemento será adicionado.
     * @param tagName     O nome do elemento a ser criado.
     * @param textContent O conteúdo de texto a ser definido para o elemento.
     * @return O elemento criado com o nome e conteúdo de texto especificados.
     */
    public static Element createElement(Document doc, String tagName, String textContent) {
        Element element = doc.createElement(tagName);
        element.setTextContent(textContent);
        return element;
    }

    /**
     * Valida um documento XML em relação a um esquema XSD especificado.
     *
     * @param xmlDoc    O documento XML a ser validado.
     * @param vFileName O nome do arquivo XSD que contém o esquema de validação.
     * @return true se o documento XML for válido em relação ao esquema XSD, false caso contrário.
     */
    public static boolean validDocXSD(Document xmlDoc, String vFileName) {
        return validDoc(xmlDoc, vFileName, XMLConstants.W3C_XML_SCHEMA_NS_URI);
    }

    /**
     * Valida um documento XML em relação a um esquema XML especificado.
     *
     * @param document    O documento XML a ser validado.
     * @param xsdFileName O nome do arquivo XSD que contém o esquema de validação.
     * @param type        O tipo de esquema XML a ser utilizado (por exemplo, XMLConstants.W3C_XML_SCHEMA_NS_URI para esquemas XSD).
     * @return true se o documento XML for válido em relação ao esquema XML especificado, false caso contrário.
     */
    private static boolean validDoc(Document document, String xsdFileName, String type) {
        try {
            // Cria uma fábrica de esquema capaz de entender esquemas WXS
            SchemaFactory factory = SchemaFactory.newInstance(type);

            // Carrega um esquema WXS, representado por uma instância Schema
            Source schemaFile = new StreamSource(new File(xsdFileName));
            Schema schema = factory.newSchema(schemaFile);

            // Cria uma instância Validator, que pode ser usada para validar um documento
            Validator validator = schema.newValidator();

            // Valida a árvore DOM
            validator.validate(new DOMSource(document));

            return true; // A validação foi bem-sucedida
        } catch (SAXException | IOException e) {
            e.printStackTrace();
            return false; // Houve um erro durante a validação
        }
    }

    /**
     * Verifica se um dado elemnto existe no XML
     * @param fileXML Ficheiro xml (SEM EXTENSÃO .xml)
     * @param tagName elemento do dado desejado
     * @param value valor do dado desejado
     * @return true o dado existir no ficheiro, false caso contrário
     */
    public static boolean searchElementValue(String fileXML, String tagName, String value) {
        try {
            File xmlFile = new File(fileXML+".xml");

            if (!xmlFile.exists() && tagName == "username") {
                return false;
            }else if (!xmlFile.exists()){
                System.out.println("Não foi possível encontrar: "+ fileXML +".xml");
                return false;
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);

            NodeList nodeList = doc.getElementsByTagName(tagName);

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                String elementValue = element.getTextContent().trim();
                if (elementValue.equals(value)) {
                    return true;
                }
            }
        } catch (ParserConfigurationException | IOException | org.xml.sax.SAXException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static void RemoveExtraLinesFromXML() {
        String filePath = xmlPath + "users.xml"; // Substitua pelo caminho real do seu arquivo

        try {
            // Ler o arquivo XML
            Path path = Paths.get(filePath);
            List<String> lines = Files.readAllLines(path);

            // Encontrar a posição da segunda tag </users> contando de trás para frente
            int secondLastUsersTagIndex = -1;
            int count = 0;

            int penultimateUsersTagIndex = -1;
            for (int i = lines.size() - 1; i >= 0; i--) {
                if (lines.get(i).contains("</users>")) {
                    penultimateUsersTagIndex = i;
                    count++;
                    if (count == 2) {
                        secondLastUsersTagIndex = i;
                        break;
                    }
                }
            }

            // Remover as linhas após a penúltima tag </users>
            if (penultimateUsersTagIndex != -1) {
                lines.subList(penultimateUsersTagIndex + 1, lines.size()).clear();
            }

            // Remover o texto após a segunda tag </users>
            if (secondLastUsersTagIndex != -1) {
                String pattern = "</users>.*";
                Pattern r = Pattern.compile(pattern);
                Matcher m = r.matcher(lines.get(secondLastUsersTagIndex));
                if (m.find()) {
                    lines.set(secondLastUsersTagIndex, lines.get(secondLastUsersTagIndex).replaceAll(pattern, "</users>"));
                }
            }

            // Escrever as alterações de volta no arquivo
            Files.write(path, lines);

            System.out.println("Linhas extras removidas com sucesso!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        boolean teste = validDocXSD(XMLDoc.parseFile(xmlPath+"users.xml"), xmlPath+"users.xsd");
        //boolean teste = validDocXSD(XMLDoc.parseFile("src/users.xml"), "src/users.xsd");

        System.out.println("Teste Validação XSD: "+ teste);
    }
}
