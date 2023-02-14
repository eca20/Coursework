import java.util.*;
import java.time.Instant;



public class Block implements HashableObject{
    String prevHash;
    int transactionCount;
    long timestamp;
    int difficulty;

    private byte[] nonce = new byte[4];
    public ArrayList<Transaction> tx_list = new ArrayList<Transaction>(16);

    public Block(){
        timestamp = Instant.now().toEpochMilli();
        transactionCount = 0;
        changeNonce();

    }
    public Block(String prev, int d ){
        timestamp = Instant.now().toEpochMilli();
        transactionCount = 0;
        changeNonce();
        prevHash = prev;
        difficulty = d;
    }

    public boolean add(Transaction t){
        if(transactionCount + t.getIOCount() <= 16) {
            tx_list.add(t);
            transactionCount += t.getIOCount();
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public String getDataAsString() {
        StringBuilder s = new StringBuilder();
        s.append(prevHash + "\n");
        s.append(transactionCount + "\n");
        s.append(timestamp + "\n");
        s.append(difficulty + "\n");
        s.append(getNonceAsString() + "\n");
        s.append(this.getConcatRoot() + "\n");
      //  s.append(this.getConcatList());
        return s.toString();
    }

    public String getConcatList(){
        StringBuilder result = new StringBuilder();
        for(Transaction t : this.tx_list){
            result.append(t.toString() + "\n");
        }
        return result.toString();
    }
    public String getConcatRoot(){
        StringBuilder result = new StringBuilder();
        for(Transaction t : this.tx_list){
            result.append(t.toString());
        }
        return Sha256Hash.calculateHash(result.toString());
    }

    public void changeNonce(){
        Random r = new Random();
        int[] x = new int[4];

        for(int z = 0; z < 4; z++){
            x[z] = 32 + r.nextInt(94);
            nonce[z] = (byte) x[z];
        }
    }

    public String getNonceAsString(){
        StringBuilder sb = new StringBuilder();
       try {
           String s = new String(nonce, "US-ASCII");
           sb.append(s);
       }
       catch(Exception e){
           System.out.println("encoding exception in nonce generation");
           System.exit(1);
        }
        return sb.toString();
    }

    public String getHash(){
        return Sha256Hash.calculateHash(this.getDataAsString().replace("\n", ""));
    }
}
