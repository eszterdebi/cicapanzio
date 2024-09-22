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
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Cat {

    private String name;
    private String breed;
    private int age;
    private String owner;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Cat(String name, String breed, int age, String owner){
        this.name = name;
        this.breed = breed;
        this.age = age;
        this.owner = owner;
    }


    //a cats.xml fájlt beolvasó, tartalmát kilistázó metódus

    public static List<Cat> getCatsFromXml(){
        File f = new File("cats.xml");
        List<Cat> cats = new ArrayList<>();

        try{
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document dom = db.parse(f);
            dom.normalize();
            NodeList catNodes = dom.getElementsByTagName("cat");

            for(int i = 0; i < catNodes.getLength(); i++){
                Node n = catNodes.item(i);
                if(n.getNodeType() == Node.ELEMENT_NODE){
                    Element e = (Element)n;

                    String name = e.getElementsByTagName("name").item(0).getTextContent().trim();
                    String breed = e.getElementsByTagName("breed").item(0).getTextContent().trim();
                    int age = Integer.parseInt(e.getElementsByTagName("age").item(0).getTextContent().trim());
                    String owner = e.getElementsByTagName("owner").item(0).getTextContent().trim();

                    Cat cat = new Cat(name, breed, age, owner);
                    cats.add(cat);
                }
            }

        }catch (Exception e) {
            System.out.println("Hiba történt: " + e);
        }

        return cats;
    }

    //a cats.xml fájl tartalmát konzolra kiíró metódus

    public static void printCatsFromXml(){
        List<Cat> c = getCatsFromXml();

        System.out.println("Cicák:\n");
        for(int i = 0; i < c.size(); i++){
            System.out.println("Cica adatok:\nNév: " + c.get(i).getName() + "\nFajta: " + c.get(i).getBreed() +
                    "\nKora: " + c.get(i).getAge() + " éves\nGazdája: " + c.get(i).getOwner() + "\n");
        }
    }

    //’add’ metódus, amivel új elemeket adunk a két XML fájlhoz

    public static void addNewCatAndOwner(Scanner sc){
        File f = new File("cats.xml");
        File fl = new File("owners.xml");

        int spaceTaken;
        boolean wasHere = false;
        String ownerName = null;

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document dom = db.parse(f);
            dom.normalize();

            NodeList catsNode = dom.getElementsByTagName("cat");
            spaceTaken = catsNode.getLength();

            //ha van még hely
            if(spaceTaken < 20) {
                Document dom2 = db.parse(fl);
                dom2.normalize();

                Element newCat = dom.createElement("cat");

                Element newName = dom.createElement("name");
                System.out.println("Kérem az új cica nevét: ");
                String name = sc.nextLine();
                newName.setTextContent(name);

                Element newBreed = dom.createElement("breed");
                System.out.println("Kérem a cica fajtáját: ");
                String breed = sc.nextLine();
                newBreed.setTextContent(breed);

                Element newAge = dom.createElement("age");
                System.out.println("Kérem a cica életkorát: ");
                String age = sc.nextLine();
                newAge.setTextContent(age);

                NodeList ownersNode = dom2.getElementsByTagName("owner");

                //megvizsgálja a gazdák adatai között, hogy járt-e már itt a cica
                //ha igen akkor a gazda adatait nem kéri be mégegyszer
                for (int i = 0; i < ownersNode.getLength(); i++) {
                    Node owner = ownersNode.item(i);
                    if (owner.getNodeType() == Node.ELEMENT_NODE) {
                        Element eOwner = (Element) owner;

                        if (name.equals(eOwner.getElementsByTagName("cat").item(0).getTextContent().trim())) {
                            System.out.println("A cica gazdája " + eOwner.getElementsByTagName("name").item(0).getTextContent().trim() + "? (i/n)");
                            String answer = sc.nextLine().toLowerCase();
                            if (answer.equals("i")) {
                                wasHere = true;
                                ownerName = eOwner.getElementsByTagName("name").item(0).getTextContent().trim();
                                System.out.println("Szuper! Mivel visszatérő vendégről van szó, a gazdi adatait nem kell még egyszer rögzíteni.");
                                break;
                            }
                        }
                    }
                }

                Element newCatsOwnerName = dom.createElement("owner");
                //ha volt már itt a cica
                if (wasHere) {
                    newCatsOwnerName.setTextContent(ownerName);
                } else { //ha még nem volt, gazda adatok bekérése
                    System.out.println("Kérem a cica gazdájának a nevét: ");
                    ownerName = sc.nextLine();
                    newCatsOwnerName.setTextContent(ownerName);

                    Element newOwner = dom2.createElement("owner");

                    Element newOwnerName = dom2.createElement("name");
                    newOwnerName.setTextContent(ownerName);

                    Element newBirthdate = dom2.createElement("birthdate");
                    System.out.println("Kérem a gazdájának a születési idejét (éééé-hh-nn alakban): ");
                    String birthdate = sc.nextLine();
                    newBirthdate.setTextContent(birthdate);

                    Element newPhone = dom2.createElement("phone");
                    System.out.println("Kérem a gazdájának a telefonszámát (06/+36 előjel nélkül!): ");
                    String phone = sc.nextLine();
                    newPhone.setTextContent(phone);

                    Element newCatName = dom2.createElement("cat");
                    newCatName.setTextContent(name);

                    newOwner.appendChild(newOwnerName);
                    newOwner.appendChild(newBirthdate);
                    newOwner.appendChild(newPhone);
                    newOwner.appendChild(newCatName);

                    Element oRootElement = dom2.getDocumentElement();
                    oRootElement.appendChild(newOwner);
                }

                newCat.appendChild(newName);
                newCat.appendChild(newBreed);
                newCat.appendChild(newAge);
                newCat.appendChild(newCatsOwnerName);

                Element cRootElement = dom.getDocumentElement();
                cRootElement.appendChild(newCat);
                
                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer transformer = tf.newTransformer();

                DOMSource source = new DOMSource(dom);
                StreamResult result = new StreamResult(f);

                transformer.transform(source, result);

                if (!wasHere) {
                    DOMSource oSource = new DOMSource(dom2);
                    StreamResult oResult = new StreamResult(fl);

                    transformer.transform(oSource, oResult);
                }

                System.out.println("Sikeres becsekkolás!");
            }
            else{
                System.out.println("Sajnos jelenleg nincs szabad helyünk, kérem próbálja meg később!");
            }

        } catch (Exception e) {
            System.out.println("Hiba történt: " + e);
        }
    }

    //’delete’ metódus, amivel elemeket törlünk ki a cats.xml fájlból

    public static void deleteCatFromXml(Scanner sc) {
        File f = new File("cats.xml");

        System.out.println("Hogy hívják a cicát, aki szeretne kicsekkolni?");
        String name = sc.nextLine();
        String owner = null;
        int counter = 0;
        boolean match = false;

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document dom = db.parse(f);
            dom.normalize();

            NodeList catsNode = dom.getElementsByTagName("cat");

            //megszámolja hány cica van a megadott névvel
            for (int i = 0; i < catsNode.getLength(); i++) {
                Node cat = catsNode.item(i);
                if (cat.getNodeType() == Node.ELEMENT_NODE) {
                    Element eCat = (Element) cat;

                    if (name.equals(eCat.getElementsByTagName("name").item(0).getTextContent().trim())) {
                        counter++;
                    }
                }
            }

            if (counter >= 1) {

                if (counter > 1) {
                    System.out.println("Úgy látom több cica is be van jelentkezve ezzel a névvel.");
                    System.out.println("Kérem a cica gazdájának a nevét: ");
                    owner = sc.nextLine();
                }

                for (int i = 0; i < catsNode.getLength(); i++) {
                    Node cat = catsNode.item(i);
                    if (cat.getNodeType() == Node.ELEMENT_NODE) {
                        Element eCat = (Element) cat;
                        //ha 1 van cica név, ha több mint 1 van cica név és gazda név alapján megkeresi
                        if ((counter == 1 && name.equals(eCat.getElementsByTagName("name").item(0).getTextContent().trim())) ||
                                (counter > 1 && name.equals(eCat.getElementsByTagName("name").item(0).getTextContent().trim()) &&
                                        owner.equals(eCat.getElementsByTagName("owner").item(0).getTextContent().trim()))) {
                            match = true;
                            Node parentNode = eCat.getParentNode();
                            parentNode.removeChild(eCat);
                        }
                    }
                }
                if(!match){
                    System.out.println("Sajnos a megadott cica névhez nincs rendelve ilyen nevű gazda. " +
                            "Kérem ellenőrizze le, hogy helyesen gépelte-e be a kért adatokat és próbálkozzon újra!");
                }
            } else {
                System.out.println("Sajnos a megadott név nem található. " +
                        "Kérem ellenőrizze le, hogy helyesen gépelte-e be a cica nevét és próbálkozzon újra!");
            }

            DOMSource source = new DOMSource(dom);
            StreamResult result = new StreamResult("cats.xml");

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();


            transformer.transform(source, result);

            if (match) {
                System.out.println("Sikeres kicsekkolás!");
            }

        } catch (Exception e) {
            System.out.println("Hiba történt: " + e);
        }
    }

    //’update’ metódus, amivel módosítani tudjuk az elemeket a két XML fájlban

    public static void updateCatAndOwner(Scanner sc){
        File f = new File("cats.xml");
        File fl = new File("owners.xml");

        System.out.println("Kérem a cica nevét, akinek az adatait szereté módosítnai: ");
        String name = sc.nextLine();
        String owner = null;
        int counter = 0;
        boolean match = false;
        boolean ownerToo = false;
        boolean catNameChanged = false;
        boolean ownerNameChanged = false;
        String newName = null;
        String newOwner = null;
        String answer;

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document dom = db.parse(f);
            dom.normalize();

            NodeList catsNode = dom.getElementsByTagName("cat");

            Document dom2 = db.parse(fl);
            dom2.normalize();

            NodeList ownersNode = dom2.getElementsByTagName("owner");
            //megszámolja hány cica van a megadott névvel
            for (int i = 0; i < catsNode.getLength(); i++) {
                Node cat = catsNode.item(i);
                if (cat.getNodeType() == Node.ELEMENT_NODE) {
                    Element eCat = (Element) cat;

                    if (name.equals(eCat.getElementsByTagName("name").item(0).getTextContent().trim())) {
                        counter++;
                    }
                }
            }


            if (counter >= 1) {

                if (counter > 1) {
                    System.out.println("Úgy látom több cica is be van jelentkezve ezzel a névvel.");
                    System.out.println("Kérem a cica gazdájának a nevét: ");
                    owner = sc.nextLine();
                }

                for (int i = 0; i < catsNode.getLength(); i++) {
                    Node cat = catsNode.item(i);
                    if (cat.getNodeType() == Node.ELEMENT_NODE) {
                        Element eCat = (Element) cat;
                        //ha 1 van cica név, ha több mint 1 van cica név és gazda név alapján megkeresi
                        if ((counter == 1 && name.equals(eCat.getElementsByTagName("name").item(0).getTextContent().trim())) ||
                                (counter > 1 && name.equals(eCat.getElementsByTagName("name").item(0).getTextContent().trim()) &&
                                        owner.equals(eCat.getElementsByTagName("owner").item(0).getTextContent().trim()))) {
                            match = true;
                            NodeList childNodes = eCat.getChildNodes();

                            for (int j = 0; j < childNodes.getLength(); j++) {
                                Node childNode = childNodes.item(j);

                                if (childNode.getNodeType() == Node.ELEMENT_NODE) {

                                    switch (childNode.getNodeName()) {
                                        case "name":
                                            System.out.println("A cica nevét szeretné módosítani? (i/n)");
                                            answer = sc.nextLine().toLowerCase();
                                            if (answer.equals("i")) {
                                                System.out.println("Mire szeretné módosítani?");
                                                newName = sc.nextLine();
                                                childNode.setTextContent(newName);
                                                catNameChanged = true;
                                            }
                                            break;
                                        case "breed":
                                            System.out.println("A fajtáját szeretné módosítani? (i/n)");
                                            answer = sc.nextLine().toLowerCase();
                                            if (answer.equals("i")) {
                                                System.out.println("Mire szeretné módosítani?");
                                                String newBreed = sc.nextLine();
                                                childNode.setTextContent(newBreed);
                                            }
                                            break;
                                        case "age":
                                            System.out.println("Az életkorát szeretné módosítani? (i/n)");
                                            answer = sc.nextLine().toLowerCase();
                                            if (answer.equals("i")) {
                                                System.out.println("Mire szeretné módosítani?");
                                                String newAge = sc.nextLine();
                                                childNode.setTextContent(newAge);
                                            }
                                            break;
                                        case "owner":
                                            //ha szeretne gazda adatot is módosítani
                                            //csak akkor kéri be a későbbiekben a gazda adatok módosítását
                                            System.out.println("A gazdája adatait is szeretné módosítani? (i/n)");
                                            answer = sc.nextLine().toLowerCase();
                                            if (answer.equals("i")) {
                                                ownerToo = true;
                                                System.out.println("A gazdájának a nevét szeretné módosítani? (i/n)");
                                                answer = sc.nextLine().toLowerCase();
                                                if (answer.equals("i")) {
                                                    System.out.println("Mire szeretné módosítani?");
                                                    newOwner = sc.nextLine();
                                                    childNode.setTextContent(newOwner);
                                                    //ha módosítja a gazda nevét, át írja az owners.xml fájlban is
                                                    ownerNameChanged = true;
                                                }
                                            }
                                            break;
                                    }
                                }
                            }
                        }
                    }
                }
                if(!match){
                    System.out.println("Sajnos a megadott cica névhez nincs rendelve ilyen nevű gazda. " +
                            "Kérem ellenőrizze le, hogy helyesen gépelte-e be a kért adatokat és próbálkozzon újra!");
                }
            } else {
                System.out.println("Sajnos a megadott név nem található. " +
                        "Kérem ellenőrizze le, hogy helyesen gépelte-e be a cica nevét és próbálkozzon újra!");
            }
            //ha változott a cica neve vagy szeretnénk gazda adatot is módosítani
            if (catNameChanged || ownerToo) {
                for (int i = 0; i < ownersNode.getLength(); i++) {
                    Node ownerN = ownersNode.item(i);
                    if (ownerN.getNodeType() == Node.ELEMENT_NODE) {
                        Element eOwner = (Element) ownerN;
                        //ha 1 van cica név, ha több mint 1 van cica név és gazda név alapján megkeresi
                        if ((counter == 1 && name.equals(eOwner.getElementsByTagName("cat").item(0).getTextContent().trim())) ||
                                (counter > 1 && name.equals(eOwner.getElementsByTagName("cat").item(0).getTextContent().trim())
                                    && owner.equals(eOwner.getElementsByTagName("name").item(0).getTextContent().trim()))) {
                            NodeList childNodes = eOwner.getChildNodes();

                            for (int j = 0; j < childNodes.getLength(); j++) {
                                Node childNode = childNodes.item(j);

                                if (childNode.getNodeType() == Node.ELEMENT_NODE) {

                                    switch (childNode.getNodeName()) {
                                        case "cat":
                                            if (catNameChanged) {
                                                childNode.setTextContent(newName);
                                            }
                                            break;
                                        case "name":
                                            if (ownerNameChanged) {
                                                childNode.setTextContent(newOwner);
                                            }
                                            break;
                                        case "birthdate":
                                            if (ownerToo) {
                                                System.out.println("A születési idejét szeretné módosítani? (i/n)");
                                                answer = sc.nextLine().toLowerCase();
                                                if (answer.equals("i")) {
                                                    System.out.println("Mire szeretné módosítani? (éééé-hh-nn alakban)");
                                                    String newBirthdate = sc.nextLine();
                                                    childNode.setTextContent(newBirthdate);
                                                }
                                            }
                                            break;
                                        case "phone":
                                            if (ownerToo) {
                                                System.out.println("A telefonszámát szeretné módosítani? (i/n)");
                                                answer = sc.nextLine().toLowerCase();
                                                if (answer.equals("i")) {
                                                    System.out.println("Mire szeretné módosítani? (06/+36 előjel nélkül!)");
                                                    String newPhone = sc.nextLine();
                                                    childNode.setTextContent(newPhone);
                                                }
                                            }
                                            break;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            DOMSource source = new DOMSource(dom);
            StreamResult result = new StreamResult("cats.xml");

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();

            transformer.transform(source, result);

            DOMSource oSource = new DOMSource(dom2);
            StreamResult oResult = new StreamResult("owners.xml");

            transformer.transform(oSource, oResult);

            if(match) {
                System.out.println("Sikeres adat módosítás!");
            }

        }catch (Exception e) {
            System.out.println("Hiba történt: " + e);
        }
    }

    //cica számláló metódus, ami megszámolja, hogy hány cica van bejelentkezve jelenleg

    public static void catCounter(){
        File f = new File("cats.xml");
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document dom = db.parse(f);
            dom.normalize();

            NodeList catsNode = dom.getElementsByTagName("cat");

            System.out.println("Jelenleg " + catsNode.getLength() + " cica van bejelentkezve a panzióba.\n");

        }catch (Exception e) {
            System.out.println("Hiba történt: " + e);
        }
    }

    //a cats.xml fájlt széttagoló metódus

    public static void prettyPrintCatsXml(){
        List<Cat> catList = getCatsFromXml();

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document dom = db.newDocument();

            Element rootElement = dom.createElement("cats");
            dom.appendChild(rootElement);

            for (Cat c : catList) {
                Element eCat = dom.createElement("cat");
                Element eName = dom.createElement("name");
                Element eBreed = dom.createElement("breed");
                Element eAge = dom.createElement("age");
                Element eOwner = dom.createElement("owner");

                eCat.appendChild(eName);
                eCat.appendChild(eBreed);
                eCat.appendChild(eAge);
                eCat.appendChild(eOwner);

                eName.appendChild(dom.createTextNode(c.getName()));
                eBreed.appendChild(dom.createTextNode(c.getBreed()));
                eAge.appendChild(dom.createTextNode(Integer.toString(c.getAge())));
                eOwner.appendChild(dom.createTextNode(c.getOwner()));

                rootElement.appendChild(eCat);
            }

            DOMSource source = new DOMSource(dom);
            StreamResult result = new StreamResult("cats.xml");

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();

            //megfelelő formátumra alakítja az XML fájlt
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            transformer.transform(source, result);

            System.out.println("Sikeres széttagolás!\n");

        }catch (Exception ex) {
            System.out.println("Hiba történt: " + ex);
        }
    }
}