package KPCB;

public class HashTable {
	private HashItem[] table;
	private float numberOfElements;
	private double THRESHOLD = 0.7; // For resize
	public HashTable(int size) {
		table = new HashItem[size];
		numberOfElements = 0;
	}
	/*
	 * Functionality: Given a key, get will find the value associated with a given key.
	 * @input: String key
	 * @output: generic Object
	 * author: Kaveh Khorram
	 */
	public Object get(String key) {
		// Given a key, find its corresponding value.
		int hash = Math.abs(key.hashCode() % table.length);
		
		int probeCount = 0;
		// Worst case O(n), we'll need to iterate through the entire array.
		while (probeCount < table.length) {
			if (table[hash] == null) { 
				probeCount++; // still continue moving forward
				hash++;
				continue;
			}
			if (table[hash].getKey().equals(key) && !table[hash].DELETED) {
				// Keys match.
				return table[hash].value;
			}
			hash = (hash + 1) % table.length;
			probeCount++;
		}
		return null;
	}
	/*
	 * Functionality: Given a key, remove will (technically) remove that key:value pair from our table.
	 * In reality, we're simply just activating a "DELETED" flag so we don't need to rehash all elements.
	 * Returns the object that was removed, or null if the object didn't exist.
	 * @input: String key
	 * @output: generic Object
	 * author: Kaveh Khorram
	 */
	public Object remove(String key) {
		int hash = Math.abs(key.hashCode() % table.length);
		
		int probeCount = 0;
		
		while (probeCount < table.length) {
			
			if (table[hash] == null) { // skip null spots
				probeCount++; // still continue moving forward
				hash++;
				continue;
			}
			
			if (table[hash].getKey().equals(key) && !table[hash].DELETED) {
				// Found it, now remove and return.
				table[hash].DELETED = true;
				numberOfElements--;
				return table[hash].getValue();
			}
			hash = (hash + 1) % table.length;
			probeCount++;
		}
		return null;
	}
	/*
	 * Functionality: Returns the load factor (numberOfElements / table length).
	 * Typically used for resizing, and typical resizes occur when the load factor is >= 0.7.
	 * @author: Kaveh Khorram
	 */
	public float load() {
		// returns the load factor for our table.
		return this.numberOfElements / this.table.length;
	}
	/*
	 * Functionality: Performs resize when threshold is met. Creates a new "temporary" array of 2x the size of our original, rehashes all elements to that, and then sets 
	 * itself to our current table.
	 * @input: String key
	 * @output: generic Object
	 * @author: Kaveh Khorram
	 */
	public void resize() {
//		System.out.println("resize triggered");
		// Hit threshold, resize.
		HashTable largerTable = new HashTable(table.length * 2); // increase by a factor of 2.
		for (int i = 0; i < table.length; i++) {
			if (table[i] == null) continue;
			String key = table[i].getKey();
			Object value = table[i].getValue();
			largerTable.set(key, value); // will rehash for us.
		}
		// Set other table to main table.
		table = largerTable.table;
	}
	
	/*
	 * Functionality: Adds a new key:value pair to the table. 
	 * There are three possible outputs here: 1) the load factor is already too large and we need to rehash everything before adding. Or
	 * 2) where the user is updating a value for an already existing key and 3) the user is actually adding something new.
	 * @input: String key, Object value
	 * @output: T/F
	 * @author: Kaveh Khorram
	 */
	public boolean set(String key, Object value) {
		if (this.load() >= THRESHOLD) { // 0.7
			resize();
		}
		
		// compute index.
		int hash = Math.abs(key.hashCode() % table.length);
		// Find spot via Linear Probing
		// Case 1: User is trying to update a key:value pair that already exists.
		// Case 2: Use is inserting something new.
		int probeCount = 0; // Variable used for linear probing, so if something already exists in that spot we can move on.
		int probe = hash;
		HashItem entry;
		int possiblePosition = hash;
		while (probeCount < table.length) {
			entry = table[probe];
			if (entry == null) {
				// Just insert, there has never existed any other HashItem objects here.
				table[probe] = new HashItem(key, value);
				numberOfElements++;
				return true;
			} else if (table[probe].key.equals(key) && !table[probe].DELETED) {
				// Update set, we've found a duplicate key. 
				table[probe].setValue(value);
				return true;
			} else {
				boolean isDeleted = table[probe].DELETED;
				if (isDeleted && possiblePosition != hash) {
					// Meaning the space was technically empty, so we _could_ put it here, but that means a duplicate key _may_ exist as well. Save this spot if not.
					possiblePosition = probe;
				}
				probe = (probe + 1) % table.length;
				probeCount++;
			}
		}
		numberOfElements++;
		table[possiblePosition] = new HashItem(key, value);
		return true;
	}
}
/*
 * Our table will consist of HashItem objects.
 */
class HashItem {
	Object value;
	String key;
	boolean DELETED;
	public HashItem(String key, Object value) {
		this.value = value;
		this.key = key;
		this.DELETED = false;
	}
	
	public String getKey() {
		return key;
	}
	
	// Don't need a setter for key since we won't need to ever change its value.
	// We will only either a) change its value or b) remove.
	
	public void deleteItem() {
		DELETED = true;
		// So we don't need to shift the table, which is an expensive operation.
	}
	
	public Object getValue() {
		return value;
	}
	
	public boolean setValue(Object value) {
		this.value = value;
		// This is probably not necessary, but why not? ;)
		if (this.value.equals(value)) return true;
		return false;
	}
}
