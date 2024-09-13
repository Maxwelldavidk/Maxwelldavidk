import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ChainingHashTable <K,V> implements DeletelessDictionary<K,V>{
    private List<Item<K,V>>[] table; // the table itself is an array of linked lists of items.
    private int size;
    private static int[] primes = {11, 23, 47, 97, 197, 397, 797, 1597, 3203, 6421, 12853};
    private int prime_index = 0;

    public ChainingHashTable(){
        table = (LinkedList<Item<K,V>>[]) Array.newInstance(LinkedList.class, primes[0]);
        for(int i = 0; i < table.length; i++){
            table[i] = new LinkedList<>();
        }
        size = 0;
    }

    private int hash(K key){
        return Math.abs(key.hashCode()) % table.length;
    }


    public boolean isEmpty(){
        return size == 0;
    }    

    public int size(){
        return size;
    }

    public V insert(K key, V value) {
        int index = hash(key);// index of the key
        List<Item<K, V>> bucket = table[index]; //bucket at the index of the key

        for(Item<K, V> item : bucket){ // Search for the key in the bucket
            if(item.key.equals(key)){ // if the key exist update its value and return the old value
                V oldValue = item.value;
                item.value = value;
                return oldValue;
            }
        }
        bucket.add(new Item<>(key, value)); // Key does not exist so add new <K,V> to the bucket
        size += 1; //increment
        if(loadFactor() > 0.75){ //check for if resizing is needed
            resize();
        }
        return null;

    }

    public V find(K key){ // find the value assigned to the key
        int index = hash(key); //index of the key
        List<Item<K, V>> bucket = table[index]; // bucket at the index

        for(Item<K, V> item : bucket){ //search for the key in the bucket
            if(item.key.equals(key)){
                return item.value; //return the value if the key is found
            }
        }
        return null; // return null if key is not found
    }

    public boolean contains(K key){ // check if the hash table contains the given key
        return find(key) != null; // return true if key is found
    }

    public List<K> getKeys(){
        List<K> keys = new ArrayList<>();
        for(List<Item<K,V>> bucket : table){ //iterate through each bucket and collect all keys
            for(Item<K,V> item : bucket){
                keys.add(item.key);
            }
        }
        return keys;
    }

    public List<V> getValues(){
        List<V> values = new ArrayList<>();
        for(List<Item<K,V>> bucket : table){ // iterat through each bucket collecting all values
            for(Item<K,V> item : bucket){
                values.add(item.value);
            }
        }
        return values;
    }

    private void resize(){
        int newSize = getPrime(); // collect the next prime number
        LinkedList<Item<K,V>>[] newTable = (LinkedList<Item<K,V>>[]) Array.newInstance(LinkedList.class, newSize);
        for(int i = 0; i < newSize; i++){ //create new buckets for the new table
            newTable[i] = new LinkedList<>();
        }
        List<Item<K,V>>[] oldTable = table; //store the oldTable
        table = newTable; // update table to the new table
        reHash(oldTable); // reHash all items from the old table to the new table
    }
    private int getPrime() {
        if(prime_index < primes.length){ // return the next prime number from the prime array or create a new one
            return primes[prime_index++];
        } else {
            return table.length*2+3;
        }
    }
    private void reHash(List<Item<K,V>>[] oldTable){
        // Iterate over all buckets in the old table.
        for (int i = 0; i < oldTable.length; i++) {
            List<Item<K, V>> bucket = oldTable[i];

            // Iterate over each item in the bucket.
            for (Item<K, V> item : bucket) {
                int newIndex = hash(item.key);
                table[newIndex].add(item);
            }
        }
    }
    private double loadFactor(){
        return (double) size / table.length; // calculate the load factor

    }

    public String toString(){
        String s = "{";
        s += table[0];
        for(int i = 1; i < table.length; i++){
            s += "," + table[i];
        }
        return s+"}";
    }

}
