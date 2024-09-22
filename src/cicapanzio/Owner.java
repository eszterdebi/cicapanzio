package cicapanzio;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Owner {

    private String name;
    private LocalDate birthdate;
    private long phone;
    private String cat;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public Owner(String name, LocalDate birthdate, long phone, String cat){
        this.name = name;
        this.birthdate = birthdate;
        this.phone = phone;
        this.cat = cat;
    }


    //az owners.xml fájlt beolvasó, tartalmát kilistázó metódus

    public static List<Owner> getOwnersFromXml(){
        File f = new File("owners.xml");
        List<Owner> owners = new ArrayList<>();

        try{
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document dom = db.parse(f);
            dom.normalize();
            NodeList ownerNodes = dom.getElementsByTagName("owner");

            for(int i = 0; i < ownerNodes.getLength(); i++){
                Node n = ownerNodes.item(i);
                if(n.getNodeType() == Node.ELEMENT_NODE){
                    Element e = (Element)n;

                    String name = e.getElementsByTagName("name").item(0).getTextContent().trim();
                    LocalDate birthdate = LocalDate.parse(e.getElementsByTagName("birthdate").item(0).getTextContent().trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    long phone = Long.parseLong(e.getElementsByTagName("phone").item(0).getTextContent().trim());
                    String cat = e.getElementsByTagName("cat").item(0).getTextContent().trim();

                    Owner owner = new Owner(name, birthdate, phone, cat);
                    owners.add(owner);
                }
            }

        }catch (Exception e) {
            System.out.println("Hiba történt: " + e);
        }

        return owners;
    }

    //az owners.xml fájl tartalmát konzolra kiíró metódus

    public static void printOwnersFromXml(){
        List<Owner> o = getOwnersFromXml();

        System.out.println("Gazdák:\n");
        for(int i = 0; i < o.size(); i++){
            System.out.println("Gazda adatok:\nNév: " + o.get(i).getName() + "\nSzületési idő: " + o.get(i).getBirthdate() +
                    "\nTelefonszám: +36" + o.get(i).getPhone() + "\nCicája: " + o.get(i).getCat() + "\n");
        }
    }

    //az owners.xml fájlt széttagoló metódus

    public static void prettyPrintOwnersXml(){
        List<Owner> ownerList = getOwnersFromXml();

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document dom = db.newDocument();

            Element rootElement = dom.createElement("owners");
            dom.appendChild(rootElement);

            for (Owner o : ownerList) {
                Element eOwner = dom.createElement("owner");
                Element eName = dom.createElement("name");
                Element eBirthdate = dom.createElement("birthdate");
                Element ePhone = dom.createElement("phone");
                Element eCat = dom.createElement("cat");

                eOwner.appendChild(eName);
                eOwner.appendChild(eBirthdate);
                eOwner.appendChild(ePhone);
                eOwner.appendChild(eCat);

                eName.appendChild(dom.createTextNode(o.getName()));
                eBirthdate.appendChild(dom.createTextNode(o.getBirthdate().toString()));
                ePhone.appendChild(dom.createTextNode(Long.toString(o.getPhone())));
                eCat.appendChild(dom.createTextNode(o.getCat()));

                rootElement.appendChild(eOwner);
            }

            DOMSource source = new DOMSource(dom);
            StreamResult result = new StreamResult("owners.xml");

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();

            //megfelelő formátumra alakítja az XML fájlt
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            transformer.transform(source, result);

            System.out.println("Sikeres széttagolás!\n");

        }catch (Exception e) {
            System.out.println("Hiba történt: " + e);
        }
    }
}