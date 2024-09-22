package cicapanzio;

import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        String input;
        do {
            System.out.println(
                    "** FŐMENÜ **\n" +
                    "1 - Cicák listázása\n" +
                    "2 - Gazdik listázása\n" +
                    "3 - Becsekkolás\n" +
                    "4 - Kicsekkolás\n" +
                    "5 - Adat módosítás\n" +
                    "6 - Jelenleg becsekkolt cicák száma\n" +
                    "7 - Cica adatok széttagolása\n" +
                    "8 - Gazdi adatok széttagolása\n" +
                    "0 - Kilépés\n");

            System.out.print("A kiválasztott menüpont: ");
            input = sc.nextLine();
            System.out.println();

            switch (input) {
                case "1": Cat.printCatsFromXml();
                break;
                case "2": Owner.printOwnersFromXml();
                break;
                case "3": Cat.addNewCatAndOwner(sc);
                break;
                case "4": Cat.deleteCatFromXml(sc);
                break;
                case "5": Cat.updateCatAndOwner(sc);
                break;
                case "6": Cat.catCounter();
                break;
                case "7": Cat.prettyPrintCatsXml();
                break;
                case "8": Owner.prettyPrintOwnersXml();
                break;
                case "0": break;
                default: System.out.println("Kérem csak 0 és 8 közötti számot írjon be!\n");
            }
        } while(!input.equals("0"));

        sc.close();
    }
}
