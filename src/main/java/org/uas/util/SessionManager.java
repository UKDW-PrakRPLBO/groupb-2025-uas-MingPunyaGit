package org.uas.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SessionManager implements Serializable {
    private static final String SESSION_FILE = "session.ser";

    private static SessionManager instance;
    private boolean isLoggedIn = false;

    private SessionManager() {loadSession();}

    // Static method to get the singleton instance
    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }


    // Method to check if the session file doesn't exist
    public void createSessionFile() {
        try {
            if (!Files.exists(Paths.get(SESSION_FILE))) {
                Files.createFile(Paths.get(SESSION_FILE));
            }
        } catch (Exception e) {
            System.out.println("Gagal membuat session: " + e);
        }
    }

    private void loadSession() {
        try (ObjectInputStream ois = new ObjectInputStream( new FileInputStream(SESSION_FILE))) {
            SessionManager loadSession = (SessionManager) ois.readObject();
            this.isLoggedIn = loadSession.isLoggedIn;
        }catch (FileNotFoundException e){
            isLoggedIn = false;
        }catch (Exception e){
            isLoggedIn = false;
        }
    }

    private void saveSession() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SESSION_FILE))) {
            oos.writeObject(this);
        }catch (Exception e) {
            System.out.println("Gagal menyimpan Session: " + e);
        }
    }

    // Method to check if user is logged in
    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    // Method to simulate login
    public void login() {
        isLoggedIn = true;
        saveSession();

    }

    // Method to simulate logout
    public void logout() {
        isLoggedIn = false;
        saveSession();

    }
}
