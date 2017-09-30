package KPCB;

public class driver {
	public static void main(String[] args) {
		HashTable table = new HashTable(10);
		table.set("ILove", "something");
		table.set("ILove", "KPCB");
		table.set("IAlsoLove", "PIzza");
		table.set("IAlsoLove", "PIzza");

		table.remove("IAlsoLove");
		if (table.get("IAlsoLove") == null) {
			System.out.println("removal of key \"IAlsoLove\" successful!");
		} else {
			System.out.println("removal of key \"IAlsoLove\" successful unsuccessful!");
		}
		
		if (table.get("ILove").equals("KPCB")) {
			System.out.println("overwrite of key \"KPCB\" successful!");
		} else {
			System.out.println("overwrite of key \"KPCB\" failed!");
		}
		table.remove("ILove");
		if (table.get("ILove") == null) {
			System.out.println("Deletion of key \"ILove\" successful!");
		} else {
			System.out.println("Deletion of key \"ILove\" failed!");
		}
		table.set("ILove", "programming (or is this too cheesy?)");
		if (table.get("ILove").equals("programming (or is this too cheesy?)")) {
			System.out.println("Insertion of key \"ILove\" successful!");
		} else {
			System.out.println("Deletion of key \"ILove\" failed.");
		}
	}
}
