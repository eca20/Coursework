/*
*   Ed Anderson
*   CS 1699 - Privacy in Electronic Society
*   Project2
*/

import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class AccessControlDemo {



    public static void main(String[] args) {

        String accessFile = "DemoData.csv";
        BufferedReader br = null;
        String line = "";
        String delimiter = ",";

        AccessControl ac = new AccessControl();
        Scanner in = new Scanner(System.in); 

        try {

            br = new BufferedReader(new FileReader(accessFile));

            while ((line = br.readLine()) != null) {
                System.out.println(line);
                // use comma as separator
                String[] accessGrant = line.split(delimiter);
                System.out.println("User " + accessGrant[0] + " on file " + accessGrant[1] + " gets permission " + accessGrant[2] + ".");
                //add permission
                ac.addPermToUser(accessGrant[0], accessGrant[1], accessGrant[2]);

                ac.printAC();
    
            }

            System.out.println("<permission> can be \"read\" \"write\" or \"exec\"");
            while(true){

                System.out.println("Enter query: <username> <file> <permission>");
                String input = in.nextLine();
                String[] inputArray = input.split(" ");
                System.out.println(ac.queryAC(inputArray[0],inputArray[1],inputArray[2]));

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}