package org.uas;

import org.uas.data.User;
import org.uas.repository.*;
import org.uas.util.DBConnectionManager;
import org.uas.util.SessionManager;

import java.util.List;
import java.util.Scanner;

public class UASApplication {
    UserRepository userRepository;
    private boolean isLogin = false;

    public UASApplication() {
        userRepository = new UserRepository(DBConnectionManager.getConnection());
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            isLogin = SessionManager.getInstance().isLoggedIn();
            System.out.println("\n=== UAS User Management System ===");
            System.out.println("0. Exit");
            if (isLogin) {
                System.out.println("2. Tampilkan Semua User");
                System.out.println("3. Tambah User ");
                System.out.println("4. Ubah User");
                System.out.println("5. Hapus User");
                System.out.println("6. Logout");
            } else {
                System.out.println("1. Login");
            }
            System.out.print("Tentukan Pilihan: ");

            int choice = 99;
            try {
                choice = Integer.parseInt(scanner.next());
                if (!isLogin) {
                    if (choice > 1) {
                        choice = -99;
                    }
                }
            } catch (NumberFormatException | NullPointerException e) {
                System.out.println("Pilihan harus berupa angka!");
            }

            switch (choice) {
                case 0:
                    exitApps();
                    break;
                case 1:
                    login(scanner);
                    break;
                case 2:
                    tampilkanSemuaUser();
                    break;
                case 3:
                    insertUser(scanner);
                    break;
                case 4:
                    updateUser(scanner);
                    break;
                case 5:
                    deleteUser(scanner);
                    break;
                case 6:
                    logout();
                    break;
                case -99:
                    System.out.println("Anda Belum Login.");
                    break;
                default:
                    System.out.println("Pilihan tidak sesuai. Coba lagi.");
            }
        }
    }

    private void logout() {
        SessionManager.getInstance().logout();
        System.out.println("Logout berhasil!");
    }

    private void deleteUser(Scanner scanner) {
        scanner.skip("\\R?");
        System.out.print("Masukan email user yang akan dihapus: ");
        String email = scanner.nextLine();

        if (userRepository.deleteUser(email)) {
            System.out.println("User berhasil dihapus!");
        } else {
            System.out.println("Gagal menghapus user atau user tidak ditemukan!");
        }
    }

    private void updateUser(Scanner scanner) {
        scanner.skip("\\R?");
        System.out.print("Masukan email user yang akan diupdate: ");
        String email = scanner.nextLine();
        System.out.print("Masukan username baru: ");
        String username = scanner.nextLine();
        System.out.print("Masukan password baru: ");
        String password = scanner.nextLine();

        if (userRepository.updateUser(email, username, password)) {
            System.out.println("User berhasil diupdate!");
        } else {
            System.out.println("Gagal mengupdate user atau user tidak ditemukan!");
        }
    }

    private void tampilkanSemuaUser() {
        List<User> users = userRepository.findAll();
        System.out.println("\n=== Daftar Semua User ===");
        if (users.isEmpty()) {
            System.out.println("Tidak ada user yang terdaftar.");
        } else {
            for (int i = 0; i < users.size(); i++) {
                User user = users.get(i);
                System.out.println((i + 1) + ". Email: " + user.getEmail() +
                        ", Username: " + user.getUsername());
            }
            System.out.println("Total users: " + users.size());
        }
    }

    private void exitApps() {
        System.out.println("Keluar aplikasi. Goodbye!");
        System.exit(0);
    }

    private void login(Scanner scanner) {
        scanner.skip("\\R?");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (userRepository.authenticateUser(username, password)) {
            SessionManager.getInstance().login();
            System.out.println("Login berhasil! Selamat datang, " + username + "!");
        } else {
            System.out.println("Username atau password salah!");
        }
    }

    private void insertUser(Scanner scanner) {
        scanner.skip("\\R?");
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (userRepository.insertUser(email, username, password)) {
            System.out.println("User berhasil ditambahkan!");
        } else {
            System.out.println("Gagal menambahkan user! Email atau username mungkin sudah digunakan.");
        }
    }

    public static void main(String[] args) {
        UASApplication uasApplication = new UASApplication();
        uasApplication.start();
    }
}