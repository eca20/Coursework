/**
 * Edmund Anderson
 * eca20@pitt.edu
 * CS 1699 - Deliverable2
 * Mine.java
 */
import java.io.*;
import java.util.*;
import java.math.BigInteger;
public class Mine {

    public static final String COINBASE_ADDRESS = "1333dGpHU6gQShR596zbKHXEeSihdtoyLb";
    public static final BigInteger MAX_DIFFICULTY = new BigInteger("115792089237316195423570985008687907853269984665640564039457584007913129639935");

    public static final int COINBASE = 50;
    public static void main(String[] args) throws IOException {
        ArrayList<String> lines = new ArrayList<String>();
        ArrayList<Transaction> transactions = new ArrayList<Transaction>();

        BigInteger diff = new BigInteger(args[1]);
        String pHash = args[2];
        Block b = new Block(pHash, Integer.parseInt(args[1]));
        BigInteger d1 = MAX_DIFFICULTY.divide(diff);
        BigInteger maxHex = new BigInteger(d1.toString(), 16);

        // Open the file
        Scanner fileScan = new Scanner(new FileInputStream(args[0]));


        while (fileScan.hasNext())
        {
            lines.add(fileScan.next());
        }

        for(String s : lines){
            Transaction t = new Transaction(s.split(";"));
            transactions.add(t);
        }

        //sort the array of transactions by (fee / transactions)
        List<Transaction> feeMaxList = new ArrayList<Transaction>();
        int feeTotal = 0;
        for(Transaction t : transactions){
            feeMaxList.add(t);
        }
        Collections.sort(feeMaxList);
        Collections.reverse(feeMaxList);

        //Array is sorted by total fee divided by number of transactions
        //we can now just add in order until we get to 15 total transactions
        //it will not be optimal, as it goes for largest fees first and does not remove
        //transactions, but is a quick and simple approximation

        //only add up to 15 txs, leaving 1 for coinbase transaction
        for(Transaction t : feeMaxList){
            if(b.transactionCount + t.getIOCount() >= 15){
                break;
            }
            else{
                b.add(t);
                feeTotal += t.getFee();
            }
        }

        //ensure that block meets difficulty
        while(b.getHash().compareTo(maxHex.toString()) > 0){
            b.changeNonce();
        }

        //add coinbase transaction
        Transaction coinbaseTrans = new Transaction(COINBASE_ADDRESS, Integer.toString(feeTotal+ COINBASE), true );
        b.add(coinbaseTrans);

        //output candidate block
        System.out.println("CANDIDATE BLOCK = Hash");
        System.out.println(b.getHash());

        System.out.println("---");

        System.out.println(b.getDataAsString());
        System.out.println(b.getConcatList());



    }

}
