package ifd.sparing.model;



import java.util.ArrayList;

public class User {
    private String mName;

    public User(String name) {
        mName = name;

    }

    public String getName() {
        return mName;
    }



    private static int lastContactId = 0;

    public static ArrayList<User> createContactsList(int numContacts) {
        ArrayList<User> contacts = new ArrayList<User>();

        for (int i = 1; i <= numContacts; i++) {
            contacts.add(new User("Nama Teman " + ++lastContactId));
        }

        return contacts;
    }
}