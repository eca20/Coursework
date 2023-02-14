/**
 * Edmund Anderson
 * eca20@pitt.edu
 * CS 1699 - Deliverable1
 * StringCoin
 */
 import java.io.*;
 import java.util.*;

public class StringCoin {


    public static void main(String[] args) throws IOException{

        ArrayList<String> lines = new ArrayList<String>();
        ArrayList<Block> blocks = new ArrayList<Block>();
        Blockchain blockchain = new Blockchain();



        // Open the file
        Scanner fileScan = new Scanner(new FileInputStream(args[0]));
    		String st;
    		StringBuilder sb;

    		while (fileScan.hasNext())
    		{
              lines.add(fileScan.next());
        }

        for(String s : lines){
          blocks.add(blockchain.addBlock(s));
        }

        boolean blockchainGood = blockchain.iterateAndVerify();
        if (blockchainGood) {
            //Process Complete
      } else {
          System.out.println("Blockchain invalid!");
          System.exit(1);
      }

    }
}
