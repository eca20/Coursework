import java.util.*;

public class Transaction implements Comparable<Transaction>{

   // public ArrayList<String> inputs= new ArrayList<String>();
   // public ArrayList<String> outputs= new ArrayList<String>();

    private String inputs;
    private String outputs;
    boolean coinbase = false;

    //constructor for potential use for coinbase transactions (no inputs)
    public Transaction(String o){
        outputs = o;
        inputs = null;
    }

    //main constructor for transactions
    public Transaction(String i, String o){
        inputs = i;
        outputs = o;
    }
    public Transaction(String i, String o, boolean c){
        inputs = i;
        outputs = o;
        coinbase = c;
    }


    public Transaction(String[] s){
        try {
            inputs = s[0];
            outputs = s[1];
        }
        catch(Exception e){
            System.out.println("Transaction array index out of bounds exceptions");
        }
    }

    //returns the total coins input from senders of transaction
    public int getInputTotal(){
        int total = 0;
        String[] utxos = new String[16];
        ArrayList<String[]> tx = new ArrayList<String[]>();

        utxos = this.inputs.split(",");

        for(String s : utxos){
            tx.add(s.split(">"));
        }

        for(String[] s : tx){
      //      System.out.println("Input: " + s[0] + " | " + s[1]);
            total += Integer.parseInt(s[1]);
        }

        return total;
    }

    //returns the total coins output to recipients of transaction
    public int getOutputTotal(){

        int total = 0;
        String[] utxos = new String[16];
        ArrayList<String[]> tx = new ArrayList<String[]>();

        utxos = this.outputs.split(",");

        for(String s : utxos){
            tx.add(s.split(">"));
        }

        for(String[] s : tx){
    //        System.out.println("Output: " + s[0] + " | " + s[1]);
            total += Integer.parseInt(s[1]);
        }

        return total;
    }

    // returns the total fee this transaction offers
    public int getFee(){
        return this.getInputTotal() - this.getOutputTotal();
    }

    public String toString(){
        if(coinbase == false) {
            return inputs + ";" + outputs;
        }
        else{
            return ";" + inputs + ">" + outputs;
        }
    }

    public int getIOCount(){
        String inputAndOutput = inputs + "," + outputs;
        int count = inputAndOutput.length() - inputAndOutput.replace(",", "").length();
        if(this.coinbase == false) {
            return count + 1;
        }
        else{
            return count;
        }
    }

    public double getFeePerTrans(){

        return this.getFee() / this.getIOCount();
    }

    public int compareTo(Transaction tx){
        if(this.getFee() == tx.getFee()){
            return 0;
        }
        else if (this.getFee() < tx.getFee()){
            return -1;
        }
        else{
            return 1;
        }
    }
}
