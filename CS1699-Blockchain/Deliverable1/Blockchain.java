/**
*   Ed Anderson
*   CS 1699
*   Due Oct 4th
*   Deliverable 1
*   Modified from code provided by: https://github.com/laboon/CS1699_Fall2018
*/



import java.util.*;
public class Blockchain {
    final String bill_pk = "3081f03081a806072a8648ce38040130819c024100fca682ce8e12caba26efccf7110e526db078b05edecbcd1eb4a208f3ae1617ae01f35b91a47e6df63413c5e12ed0899bcd132acd50d99151bdc43ee737592e17021500962eddcc369cba8ebb260ee6b6a126d9346e38c50240678471b27a9cf44ee91a49c5147db1a9aaf244f05a434d6486931d2d14271b9e35030b71fd73da179069b32e2935630e1c2062354d0da20a6c416e50be794ca403430002405b0656317dd257ec71982519d38b42c02621290656eba54c955704e9b5d606062ec663bdeef8b79daa2631287d854da77c05d3e178c101b2f0a1dbbe5c7d5e10";
    final String bill_sk = "3081c60201003081a806072a8648ce38040130819c024100fca682ce8e12caba26efccf7110e526db078b05edecbcd1eb4a208f3ae1617ae01f35b91a47e6df63413c5e12ed0899bcd132acd50d99151bdc43ee737592e17021500962eddcc369cba8ebb260ee6b6a126d9346e38c50240678471b27a9cf44ee91a49c5147db1a9aaf244f05a434d6486931d2d14271b9e35030b71fd73da179069b32e2935630e1c2062354d0da20a6c416e50be794ca404160214556d46e1888b30bccf9c4a5ea71b41c107b5d219";

    private HashPointer _head = null;
    private HashPointer _current = null;

    /**
     * Generates a block and adds it to the end of the blockchain.
     * NOTE: Ordinarily we would need to have a direct reference
     * to the generated block.  We should only reference blocks
     * via HashPointer.  I am including it so that I can
     * directly modify it later and show what happens when a HashPointer
     * is invalid..
     * @param data - The data to be added to the block
     * @return A direct reference to the generated block
     */

    public Block addBlock(String data) {
        Block b = null;
        if (_head == null) {
            b = new Block(null, data);
        } else {
            b = new Block((Block) _head.getReference(), data);
        }

        _head = new HashPointer(b);
        return b;
    }


    public boolean iterateAndVerify() {
         ArrayList<Transaction> transactions = new ArrayList<Transaction>();
         HashMap<String, String> accounts;
         accounts = new HashMap<String, String>();

        // Trivial case - no blocks in blockchain, it's valid
        if (_head == null) {
            return true;
        }
        // Otherwise, iterate through all of the blocks until you get to null
        // If hashes don't match up, blockchain is invalid
        Block _current = (Block) _head.getReference();

        try {
            while (_current != null) {
              //verify previous hash matches hash of previous block
              if(_current._type.equals("CREATE")){
                //verify coinID is signed by bill
                try{
                    boolean valid = PublicKeyDemo.verifyMessage(_current._coin, _current._coinsig_or_pk, bill_pk);
                    if(!valid){
                      System.out.println("Created coin not signed by Bill. Exiting..");
                      System.exit(1);
                    }
                } catch (Exception ex){
                  System.err.println("Exception: " + ex);
                }

                //verify hash of coin to this point is signed by bill

                try{
                  boolean valid = PublicKeyDemo.verifyMessage(_current.getPrevHash() + "," + _current._type+ "," + _current._coin + "," + _current._coinsig_or_pk, _current._sig , bill_pk);
                  if(!valid){
                    System.out.println("Blockchain Invalid. Exiting..");
                    System.exit(1);
                  }
                } catch (Exception ex){
                  System.err.println("Exception: " + ex);
                }



                //get name of coin signed will bill secret key - verifying creation
                //we can then add block to potential transactions
                try{
                  String bsig = PublicKeyDemo.signMessage(_current._coin, bill_sk);
                  Transaction t1 = new Transaction(_current._coin, _current._type, bill_pk, _current._coinsig_or_pk, _current._sig, _current.getPrevHash());
                  transactions.add(t1);
                } catch(Exception ex){
                  System.err.println("Exception: " + ex);
                  System.exit(1);
                }
              }
              else if(_current._type.equals("TRANSFER")){
                //add to list of potential transactions
                Transaction t = new Transaction(_current._coin, _current._type, "owner", _current._coinsig_or_pk, _current._sig, _current.getPrevHash());
                transactions.add(t);
              }
              else{
                System.out.println("Invalid Transaction Type. Exiting..");
                System.exit(1);
              }
              _current = _current.previousBlock();
            }


        } catch (InvalidHashException ihex) {
            System.out.println(ihex);
            return false;
        }


        //Validate and process transactions

        for(int i = transactions.size() - 1; i >= 0; i--){
      //    System.out.println("Validating: " + transactions.get(i).toString());
          if (transactions.get(i).type.equals("CREATE")){
            if (accounts.containsKey(transactions.get(i).coin)){
              System.out.println("Duplicate Coin creation. Exiting");
              System.exit(1);
            }
            else{
              accounts.put(transactions.get(i).coin, bill_pk);
            }
          }
          else if (transactions.get(i).type.equals("TRANSFER")){
            //verify coin exists
            if (!accounts.containsKey(transactions.get(i).coin)){
              System.out.println("Coin to xfer doesnt exists. Exiting");
              System.exit(1);
            }
            //verify block is signed by sender
          try{
            if (PublicKeyDemo.verifyMessage(transactions.get(i).getVerifyString(), transactions.get(i).signature, accounts.get(transactions.get(i).coin))){
              //update owner to recipient
    //          System.out.println("Replacing " + transactions.get(i).coin + " to " + transactions.get(i).destination.substring(transactions.get(i).destination.length()-5, transactions.get(i).destination.length()) );
              accounts.replace(transactions.get(i).coin, transactions.get(i).destination);
            }

          } catch(Exception ex){
            System.err.println("Exception: " + ex);
          }



          }
          else{
            System.out.println("Invalid transaction type, exiting..");
          }

        }


        //sort the hashmap of coin/owners by coin ID and print coins
        Map<String, String> map = sortByKeys(accounts);
        Set set = map.entrySet();
        Iterator iterator = set.iterator();
        while(iterator.hasNext()) {
          Map.Entry me = (Map.Entry)iterator.next();
          System.out.println("Coin " + me.getKey() + " / Owner = \n" + me.getValue());
        }


        // We have iterated through the entire blockchain without an error, thus
        // it is valid.
        return true;
    }



    private static HashMap sortByKeys(HashMap map) {
       List list = new LinkedList(map.entrySet());
       // Defined Custom Comparator here
       Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
               return ((Comparable) ((Map.Entry) (o1)).getKey())
                  .compareTo(((Map.Entry) (o2)).getKey());
            }
       });

       // Here I am copying the sorted list in HashMap
       // using LinkedHashMap to preserve the insertion order
       HashMap sortedHashMap = new LinkedHashMap();
       for (Iterator it = list.iterator(); it.hasNext();) {
              Map.Entry entry = (Map.Entry) it.next();
              sortedHashMap.put(entry.getKey(), entry.getValue());
       }
       return sortedHashMap;
  }

}
