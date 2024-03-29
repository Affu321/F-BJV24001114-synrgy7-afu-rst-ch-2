import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Restaurant {
    private static final ArrayList<MenuItem> MENU_ITEMS = new ArrayList<>();

    static {
        // Initialize menu items
        MENU_ITEMS.add(new MenuItem("Nasi Goreng", 15000.0));
        MENU_ITEMS.add(new MenuItem("Mie Goreng", 13000.0));
        MENU_ITEMS.add(new MenuItem("Nasi + Ayam", 18000.0));
        MENU_ITEMS.add(new MenuItem("Es Teh", 3000.0));
        MENU_ITEMS.add(new MenuItem("Es Jeruk", 5000.0));
    }

    public void startRestaurantApp() {
        Scanner input = new Scanner(System.in);
        boolean inginPesanLagi = true;
        double totalHarga = 0;
        ArrayList<String> pesanan = new ArrayList<>();
        ArrayList<Integer> kuantitas = new ArrayList<>();
        int totalItems = 0;

        StringBuilder menuOptions = new StringBuilder("==========================\n");
        menuOptions.append("Selamat Datang di BinarFud\n");
        menuOptions.append("==========================\n");
        menuOptions.append("Silahkan Pilih Makanan :\n");
        for (int i = 0; i < MENU_ITEMS.size(); i++) {
            MenuItem menuItem = MENU_ITEMS.get(i);
            menuOptions.append((i + 1)).append(". ").append(menuItem.getName()).append(" | Rp. ").append(menuItem.getPrice()).append("\n");
        }
        menuOptions.append("99. Pesan dan Bayar\n");
        menuOptions.append("0. Keluar Aplikasi\n");
        menuOptions.append("=>");

        System.out.print(menuOptions);

        try {
            while (inginPesanLagi) {
                int pilih = input.nextInt();

                if (pilih == 0) {
                    System.out.println("Terima kasih telah menggunakan layanan kami.");
                    break;
                }

                if (pilih != 99 && (pilih < 1 || pilih > MENU_ITEMS.size())) {
                    System.out.println("Pilihan tidak valid. Silakan pilih lagi.\n" + menuOptions);
                    continue;
                }

                MenuItem selectedItem = null;
                if (pilih != 99) {
                    selectedItem = MENU_ITEMS.get(pilih - 1);
                }

                if (pilih != 0 && pilih != 99 && selectedItem != null) {
                    // Request quantity
                    System.out.println("====================");
                    System.out.println("Berapa pesanan anda");
                    System.out.println("====================");
                    System.out.println(selectedItem.getName() + " | Harga: Rp." + selectedItem.getPrice());
                    System.out.println("(Input 0 untuk Kembali)");
                    System.out.print("qty=>");
                    int qty = input.nextInt();

                    if (qty < 0) {
                        System.out.println("Jumlah pesanan tidak valid.");
                        continue;
                    }

                    pesanan.add(selectedItem.getName());
                    kuantitas.add(qty);
                    totalHarga += selectedItem.getPrice() * qty;
                    totalItems += qty;
                } else if (pilih == 99) {
                    inginPesanLagi = false;
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Mohon masukkan input yang valid.");
            input.nextLine(); // Clear the input buffer
        }

        if (totalItems > 0) {
            // Display order details
            StringBuilder orderDetails = new StringBuilder("========================\n");
            orderDetails.append("Konfirmasi & Pembayaran\n");
            orderDetails.append("========================\n");
            orderDetails.append("Pesanan Anda:\n");
            for (int i = 0; i < pesanan.size(); i++) {
                String menu = pesanan.get(i);
                int qty = kuantitas.get(i);
                double hargaMenu = MENU_ITEMS.stream().filter(item -> item.getName().equals(menu)).findFirst().get().getPrice();
                double subtotal = hargaMenu * qty;
                orderDetails.append(menu).append(" | Qty: ").append(qty).append(" | Total: Rp.").append(subtotal).append("\n");
            }

            // Payment options
            orderDetails.append("Total Harga: Rp.").append(totalHarga).append("\n");
            orderDetails.append("========================\n");
            orderDetails.append("Menu Pembayaran:\n");
            orderDetails.append("1. Konfirmasi dan Bayar\n");
            orderDetails.append("2. Kembali ke Menu Utama\n");
            orderDetails.append("0. Keluar Aplikasi\n");
            orderDetails.append("=>");

            System.out.print(orderDetails);

            int pilihanPembayaran = input.nextInt();
            switch (pilihanPembayaran) {
                case 1:
                    // Generate receipt
                    try {
                        FileWriter myWriter = new FileWriter("struk_pembelian.txt");
                        myWriter.write("========================\n");
                        myWriter.write("Struk Pembelian:\n");
                        for (int i = 0; i < pesanan.size(); i++) {
                            String menu = pesanan.get(i);
                            int qty = kuantitas.get(i);
                            double hargaMenu = MENU_ITEMS.stream().filter(item -> item.getName().equals(menu)).findFirst().get().getPrice();
                            double subtotal = hargaMenu * qty;
                            myWriter.write(menu + " | Qty: " + qty + " | Total: Rp." + subtotal + "\n");
                            System.out.println(menu + " | Qty: " + qty + " | Total: Rp." + subtotal); // Menampilkan isi struk di output
                        }
                        myWriter.write("Total Harga: Rp." + totalHarga + "\n");
                        myWriter.write("Terima kasih telah menggunakan layanan kami.\n");
                        myWriter.close();
                        System.out.println("Struk pembelian telah disimpan dalam file 'struk_pembelian.txt'");
                    } catch (IOException e) {
                        System.out.println("Terjadi kesalahan dalam menyimpan struk pembelian.");
                        System.out.println("Mohon maaf, pembayaran tidak berhasil. Silakan coba lagi.");
                    }
                    break;
                case 2:
                    break;
                case 0:
                    System.out.println("Terima kasih telah menggunakan layanan kami.");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        } else {
            System.out.println("Tidak ada pesanan yang diproses.");
        }
    }
}
