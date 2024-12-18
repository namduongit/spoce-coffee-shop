package FinalJava;

import Ban.Ban;
import Ban.QLBan;
import HoaDon.HoaDon;
import HoaDon.QLHoaDon;
import KhachHang.KHMangDi;
import KhachHang.KHTaiCho;
import KhachHang.KhachHang;
import KhachHang.MemberCard;
import KhachHang.QLKhachHang;
import NhanVien.NhanVienPhaChe;
import NhanVien.NhanVienThuNgan;
import NhanVien.Nhanvien;
import NhanVien.QLNhanVien;
import NuocUong.NuocUong;
import NuocUong.QLNuocUong;
import ThucDon.ThucDon;
import Topping.QLTopping;
import Topping.Topping;
import Utils.CreateTXT;
import Utils.Date;
import Utils.Function;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

/**
 *
 */

@SuppressWarnings({"resource", "unused"})
public class NguoiDung {

    public static String IDLink = "";
    // Các QLy
    public static QLNuocUong qlNuocUong = new QLNuocUong();
    public static QLBan qlBan = new QLBan();
    public static QLKhachHang qlKhachHang = new QLKhachHang();
    public static QLTopping qlTopping = new QLTopping();
    public static QLNhanVien qlNhanVien = new QLNhanVien();
    public static QLHoaDon qlHoaDon = new QLHoaDon();
    static {
        qlNuocUong.Init();
        qlBan.Init();
        qlKhachHang.Init();
        qlTopping.Init();
        qlNhanVien.Init();
        qlHoaDon.Init();
    }

    public static KhachHang getInfoCustomer() {
        KhachHang temp = null;
        MemberCard memberCard = null;
        boolean status = false;
        File customerFile = new File("../File/customer.txt");
        try (Scanner rd = new Scanner(customerFile)) {
            while (rd.hasNextLine()) {
                String line = rd.nextLine();
                String[] parts = line.split("\\|");
                if (parts[1].equals(IDLink)) {
                    if (parts[3].equals("1")) {
                        memberCard = new MemberCard(parts[4], new Date(parts[5], parts[6], parts[7]),
                                new Date(parts[8], parts[9], parts[10]), new Date(parts[11], parts[12], parts[13]),
                                Integer.parseInt(parts[14]));
                        status = true;
                    }
                    if (parts[0].equals("1")) {
                        temp = new KHMangDi(parts[1], parts[2], status, memberCard);
                    }
                    if (parts[0].equals("0")) {
                        temp = new KHTaiCho(parts[1], parts[2], status, memberCard);
                    }
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("\tLỗi: " + e.getMessage());
        }
        return temp;
    }

    // public static NhanVienThuNgan getInfoNhanVien() {
    // NhanVienThuNgan temp = null;
    // }

    public static void selectCustomer() {

    }

    public static void selectOne() {
        Scanner sc = new Scanner(System.in);
        String str;
        int number = 0;
        int soluongkhach = 0;
        String valString;

        loppMain:while (true) {
            Function.clearScreen();
            System.out.println(
                    "\t=============================[Chức năng người Dùng tại chỗ]===============================");
            System.out.printf("\t| %-87s |%n", "Chọn chức năng (Cần đăng nhập để chọn bàn)");
            System.out.printf("\t| %-5s %-81s |%n", "1.", "Đăng nhập tài khoản đã có");
            System.out.printf("\t| %-5s %-81s |%n", "2.", "Đăng ký nếu chưa có tài khoản");
            System.out.printf("\t| %-5s %-81s |%n", "3.", "Không dùng tài khoản");
            System.out.printf("\t| %-5s %-81s |%n", "4.", "Quay lại trang trước");
            System.out.println(
                    "\t==========================================================================================");
            System.out.print("\tNhập lựa chọn của bạn: ");
            str = sc.nextLine();
            if (Function.isEmpty(str)) {
                System.out.println("\tVui lòng không để trống !");
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                if (Function.isTrueNumber(str)) {
                    number = Integer.parseInt(str);
                    if (number >= 1 && number <= 4) {
                        if (number == 1) {
                            Account account = new Account("1");
                            account.Login();
                            IDLink = account.checkLogin();
                            if (IDLink != "") {
                                System.out.println("\tĐăng nhập thành công !");
                                KHTaiCho temp = null;

                                for (KhachHang kh : qlKhachHang.customerList) {
                                    if (kh.getCustomerID().equals(IDLink)) {
                                        temp = (KHTaiCho) kh;
                                    }
                                }
                                Function.clearScreen();

                                temp.xuatThongTin();

                                while (true) {
                                    System.out.print("\tNhập số lượng khách: ");
                                    str = sc.nextLine();

                                    if (Function.isEmpty(str)) {
                                        System.out.println("\tVui lòng không để trống !");
                                        continue;
                                    }

                                    if (!Function.isTrueNumber(str)) {
                                        System.out.println("\tVui lòng nhập số !");
                                        continue;
                                    }

                                    temp.setNumberOfCustomer(Integer.parseInt(str));
                                    break;
                                }

                                boolean isDone = qlBan.printEmptyTable();
                                Ban tmp = null;
                                if (isDone) {
                                    while (true) {
                                        System.out.print("\tNhập ID bàn bạn muốn chọn: ");
                                        str = sc.nextLine();

                                        if (Function.isEmpty(str)) {
                                            System.out.println("\tVui lòng không để trống !");
                                            continue;
                                        }

                                        tmp = qlBan.getTableByID(str);

                                        for (Ban ban : qlBan.tableList) {
                                            if (ban.getTableID().equals(tmp.getTableID())) {
                                                ban.changeTableStatus();
                                                qlBan.writeAll();
                                                break;
                                            }
                                        }
                                        if (tmp == null) {
                                            System.out.println("\tID không đúng, vui lòng nhập lại!");
                                            continue;
                                        }
                                        break;
                                    }

                                    order(temp);
                                    continue loppMain;
                                } else {
                                    System.out.println("\tKhông còn bàn trống!");
                                    try {
                                        Thread.sleep(1500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    continue loppMain;
                                }
                            } else {
                                System.out.println("\tĐăng nhập thất bại, đăng nhập lại ! !");
                                try {
                                    Thread.sleep(1500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                continue loppMain;
                            }
                        }
                        // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
                        else if (number == 2) {
                            Account account = new Account("1"); // tạo account với loại khách hàng (1)
                            account.nhapThongTin(); // sau đó nhập thông tin account bao gồm tk, mk, mã kh tự cấp phát

                            KHTaiCho temp = null;

                            qlKhachHang.Init();
                            for (KhachHang kh : qlKhachHang.customerList) {
                                if (kh.getCustomerID().equals(account.getIDLink())) {
                                    temp = (KHTaiCho) kh;
                                }
                            }

                            Function.clearScreen();
                            temp.xuatThongTin();
                            System.out.print("\tEnter để tiếp tục!");
                            str = sc.nextLine();

                            boolean isDone = qlBan.printEmptyTable();
                            Ban tmp = null;
                            if (isDone) {
                                while (true) {
                                    System.out.print("\tNhập ID bàn bạn muốn chọn: ");
                                    str = sc.nextLine();

                                    if (Function.isEmpty(str)) {
                                        System.out.println("\tVui lòng không để trống !");
                                        continue;
                                    }

                                    tmp = qlBan.getTableByID(str);
                                    for (Ban ban : qlBan.tableList) {
                                        if (ban.getTableID().equals(tmp.getTableID())) {
                                            ban.changeTableStatus();
                                            qlBan.writeAll();
                                            break;
                                        }
                                    }
                                    if (tmp == null) {
                                        System.out.println("\tID không đúng, vui lòng nhập lại!");
                                        continue;
                                    }
                                    break;
                                }

                                order(temp);
                                continue loppMain;
                            } else {
                                System.out.println("\tKhông còn bàn trống!");
                                try {
                                    Thread.sleep(1500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                continue loppMain;
                            }
                        }
                        // ----------------------------------------------------------------------------------------------------------------------------------------------------------------------
                        else if (number == 3) {
                                loopback:while (true) {
                                    Function.clearScreen();
                                    System.out.println("\t+--------------------------------------------------------------------------------+");
                                    System.out.println("\t|                   Bạn muốn dùng thẻ thành viên khoản không ?                   |");
                                    System.out.println("\t+--------------------------------------------------------------------------------+");
                                    System.out.println("\t| 1. Có                                                                          |");
                                    System.out.println("\t| 2. Không                                                                       |");
                                    System.out.println("\t| 3. Quay lại trang trước                                                        |");
                                    System.out.println("\t+--------------------------------------------------------------------------------+");
                                    System.out.print("\tNhập lựa chọn của bạn: ");
                                    valString = sc.nextLine();
                                    if (Function.isEmpty(valString)) {
                                        System.out.println("\tVui lòng không để trống !");
                                        try {
                                            Thread.sleep(1500);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        continue;
                                    }
                                    if (!Function.isTrueNumber(valString)) {
                                        System.out.println("\tVui lòng nhập số !");
                                        try {
                                            Thread.sleep(1500);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        continue;
                                    }
                                    if (Function.isTrueNumber(valString)) {
                                        number = Integer.parseInt(valString);
                                        if (number >= 1 && number <= 3) {
                                            KhachHang temp = null;
                                            if (number == 1) {
                                                while (true) {
                                                    System.out.print("\tNhập mã khách hàng của bạn: ");
                                                    str = sc.nextLine();
                                                    for (KhachHang kh : qlKhachHang.customerList) {
                                                        if (kh.getCustomerID().equals(str) && kh instanceof KHTaiCho) {
                                                            temp = kh;
                                                            break;
                                                        }
                                                    }
                                                    break;
                                                }

                                                if (temp == null) {
                                                    System.out.println("\tKhông tìm thấy khách hàng!");
                                                    try {
                                                        Thread.sleep(1500);
                                                    } catch (InterruptedException e) {
                                                        e.printStackTrace();
                                                    }
                                                    continue loopback;
                                                }
                                                else {
                                                    Function.clearScreen();
                                                    temp.xuatThongTin();

                                                    while (true) {
                                                        System.out.print("\tNhập số lượng khách: ");
                                                        str = sc.nextLine();

                                                        if (Function.isEmpty(str)) {
                                                            System.out.println("\tVui lòng không để trống !");
                                                            continue;
                                                        }

                                                        if (!Function.isTrueNumber(str)) {
                                                            System.out.println("\tVui lòng nhập số !");
                                                            continue;
                                                        }

                                                        ((KHTaiCho) temp).setNumberOfCustomer(Integer.parseInt(str));
                                                        break;
                                                    }
                                                    boolean isDone = qlBan.printEmptyTable();
                                                    Ban tmp = null;
                                                    if (isDone) {
                                                        while (true) {
                                                            System.out.print("\tNhập ID bàn bạn muốn chọn: ");
                                                            str = sc.nextLine();

                                                            if (Function.isEmpty(str)) {
                                                                System.out.println("\tVui lòng không để trống !");
                                                                continue;
                                                            }

                                                            tmp = qlBan.getTableByID(str);
                                                            for (Ban ban : qlBan.tableList) {
                                                                if (ban.getTableID().equals(tmp.getTableID())) {
                                                                    ban.changeTableStatus();
                                                                    qlBan.writeAll();
                                                                    break;
                                                                }
                                                            }
                                                            if (tmp == null) {
                                                                System.out.println("\tID không đúng, vui lòng nhập lại!");
                                                                continue;
                                                            }
                                                            break;
                                                        }

                                                        order(temp);
                                                        continue loppMain;
                                                    } else {
                                                        System.out.println("\tKhông còn bàn trống!");
                                                        try {
                                                            Thread.sleep(1500);
                                                        } catch (InterruptedException e) {
                                                            e.printStackTrace();
                                                        }
                                                        continue loppMain;
                                                    }
                                                }
                                            }
                                            if (number == 2) {
                                                temp = new KHTaiCho("KH1", "Không có tên", false, null, 0);
                                                Function.clearScreen();
                                                temp.xuatThongTin();

                                                while (true) {
                                                    System.out.print("\tNhập số lượng khách: ");
                                                    str = sc.nextLine();

                                                    if (Function.isEmpty(str)) {
                                                        System.out.println("\tVui lòng không để trống !");
                                                        continue;
                                                    }

                                                    if (!Function.isTrueNumber(str)) {
                                                        System.out.println("\tVui lòng nhập số !");
                                                        continue;
                                                    }

                                                    ((KHTaiCho) temp).setNumberOfCustomer(Integer.parseInt(str));
                                                    break;
                                                }

                                                boolean isDone = qlBan.printEmptyTable();
                                                Ban tmp = null;
                                                if (isDone) {
                                                    while (true) {
                                                        System.out.print("\tNhập ID bàn bạn muốn chọn: ");
                                                        str = sc.nextLine();

                                                        if (Function.isEmpty(str)) {
                                                            System.out.println("\tVui lòng không để trống !");
                                                            continue;
                                                        }

                                                        tmp = qlBan.getTableByID(str);
                                                        for (Ban ban : qlBan.tableList) {
                                                            if (ban.getTableID().equals(tmp.getTableID())) {
                                                                ban.changeTableStatus();
                                                                qlBan.writeAll();
                                                                break;
                                                            }
                                                        }
                                                        if (tmp == null) {
                                                            System.out.println("\tID không đúng, vui lòng nhập lại!");
                                                            continue;
                                                        }
                                                        break;
                                                    }

                                                    order(temp);
                                                    continue loppMain;
                                                } else {
                                                    System.out.println("\tKhông còn bàn trống!");
                                                    try {
                                                        Thread.sleep(1500);
                                                    } catch (InterruptedException e) {
                                                        e.printStackTrace();
                                                    }
                                                    continue loppMain;
                                                }
                                            }
                                            if (number == 3) {
                                                return;
                                            }
                                            break;
                                        } else {
                                            System.out.println("\tVui lòng chọn từ 1 đến 3 !");
                                            try {
                                                Thread.sleep(1500);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                                KhachHang temp = new KHTaiCho("KH1", "Không có tên", false, null, 0);
                                Function.clearScreen();
                                temp.xuatThongTin();

                                while (true) {
                                    System.out.print("\tNhập số lượng khách: ");
                                    str = sc.nextLine();

                                    if (Function.isEmpty(str)) {
                                        System.out.println("\tVui lòng không để trống !");
                                        continue;
                                    }

                                    if (!Function.isTrueNumber(str)) {
                                        System.out.println("\tVui lòng nhập số !");
                                        continue;
                                    }

                                    ((KHTaiCho)temp).setNumberOfCustomer(Integer.parseInt(str));
                                    break;
                                }

                                boolean isDone = qlBan.printEmptyTable();
                                Ban tmp = null;
                                if (isDone) {
                                    while (true) {
                                        System.out.print("\tNhập ID bàn bạn muốn chọn: ");
                                        str = sc.nextLine();

                                        if (Function.isEmpty(str)) {
                                            System.out.println("\tVui lòng không để trống !");
                                            continue;
                                        }

                                        tmp = qlBan.getTableByID(str);
                                        for (Ban ban : qlBan.tableList) {
                                            if (ban.getTableID().equals(tmp.getTableID())) {
                                                ban.changeTableStatus();
                                                qlBan.writeAll();
                                                break;
                                            }
                                        }

                                        if (tmp == null) {
                                            System.out.println("\tID không đúng, vui lòng nhập lại!");
                                            continue;
                                        }
                                        break;
                                    }

                                    order(temp);
                                    continue loppMain;
                                } else {
                                    System.out.println("\tKhông còn bàn trống!");
                                    try {
                                        Thread.sleep(1500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    continue loppMain;
                                }
                        }
                        // ----------------------------------------------------------------------------------------------------------------------------------------------------------------------
                        else if (number == 4) {
                            System.out.println("\tCảm ơn bạn đã sử dụng dịch vụ của chúng tôi !");
                            break;
                        } else {
                            System.out.println("\tVui lòng chọn từ 1 đến 4 !");
                            try {
                                Thread.sleep(1500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        System.out.println("\tVui lòng chọn từ 1 đến 3 !");
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    System.out.println("\tVui lòng nhập số !");
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    public static void selectTwo() {
        Scanner sc = new Scanner(System.in);
        String str;
        String selection;

        loop: while (true) {
            Function.clearScreen();
            System.out.println(
                    "\t=============================[Chức năng người Dùng mang đi]===============================");
            System.out.printf("\t| %-87s |%n", "Chọn chức năng");
            System.out.printf("\t| %-5s %-81s |%n", "1.", "Bắt đầu đặt món");
            System.out.printf("\t| %-5s %-81s |%n", "2.", "Quay lại trang trước");
            System.out.println(
                    "\t==========================================================================================");
            System.out.print("\tNhập lựa chọn của bạn: ");
            str = sc.nextLine();

            if (Function.isEmpty(str)) {
                System.out.println("\tVui lòng không để trống !");
                continue;
            }

            if (!Function.isTrueNumber(str)) {
                System.out.println("\tVui lòng nhập số !");
                continue;
            }

            switch (str) {
                case "1":
                    loop1: while (true) {
                        Function.clearScreen();
                        System.out.println(
                                "\t=============================[Chức năng người Dùng mang đi]===============================");
                        System.out.printf("\t| %-87s |%n", "Bạn đã từng mua hàng ở Spoce Coffee Shop chưa?");
                        System.out.printf("\t| %-5s %-81s |%n", "1.", "Rồi");
                        System.out.printf("\t| %-5s %-81s |%n", "2.", "Chưa từng");
                        System.out.printf("\t| %-5s %-81s |%n", "3.", "Quay lại trang trước");
                        System.out.println(
                                "\t==========================================================================================");
                        System.out.print("\tNhập lựa chọn của bạn: ");
                        str = sc.nextLine();

                        if (Function.isEmpty(str)) {
                            System.out.println("\tVui lòng không để trống !");
                            continue;
                        }

                        if (!Function.isTrueNumber(str)) {
                            System.out.println("\tVui lòng nhập số !");
                            continue;
                        }

                        switch (str) {
                            case "1":
                                while (true) {
                                    Function.clearScreen();
                                    System.out.println(
                                            "\t+--------------------------------------------------------------------------------+");
                                    System.out.println(
                                            "\t| Bạn muốn sử dụng tài khoản để đặt hàng và tích điểm hay không ?                |");
                                    System.out.println(
                                            "\t| 1. Có                                                                          |");
                                    System.out.println(
                                            "\t| 2. Không                                                                       |");
                                    System.out.println(
                                            "\t| 3. Quay lại trang trước                                                        |");
                                    System.out.println(
                                            "\t+--------------------------------------------------------------------------------+");
                                    System.out.print("\tNhập lựa chọn của bạn: ");
                                    selection = sc.nextLine();
                                    if (Function.isEmpty(selection)) {
                                        System.out.println("\tVui lòng không để trống !");
                                        try {
                                            Thread.sleep(1500);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        continue;
                                    }
                                    if (!Function.isTrueNumber(selection)) {
                                        System.out.println("\tVui lòng nhập số !");
                                        try {
                                            Thread.sleep(1500);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        continue;
                                    }
                                    switch (selection) {
                                        // Uống mang đi nhưng dùng tài khoản
                                        case "1":
                                            while (true) {
                                                System.out.print("\tNhập mã khách hàng của bạn: ");
                                                str = sc.nextLine();

                                                if (Function.isEmpty(str)) {
                                                    System.out.println("\tVui lòng không để trống !");
                                                    continue;
                                                }

                                                if (Function.isTrueNumber(str)) {
                                                    System.out.println("\tVui lòng nhập chữ !");
                                                    continue;
                                                }

                                                KhachHang temp = null;

                                                qlKhachHang.customerList.clear();
                                                qlKhachHang.Init();
                                                for (KhachHang kh : qlKhachHang.customerList) {
                                                    if (kh.getCustomerID().equals(str) && kh instanceof KHMangDi) {
                                                        temp = kh;
                                                        break;
                                                    }
                                                }

                                                if (temp == null) {
                                                    System.out.println("\tKhông tìm thấy khách hàng!");
                                                    try {
                                                        Thread.sleep(1500);
                                                    } catch (InterruptedException e) {
                                                        e.printStackTrace();
                                                    }
                                                    continue loop1;
                                                } else {
                                                    Function.clearScreen();
                                                    temp.xuatThongTin();

                                                    System.out.print("\tEnter để tiếp tục!");
                                                    str = sc.nextLine();

                                                    order(temp);
                                                }

                                                break;
                                            }
                                            break;
                                        // Uống mang đi nhưng không đùng tài khoản
                                        case "2":
                                            while (true) {
                                                KhachHang temp = new KHMangDi("KH1", "Không có tên", false, null);
                                                Function.clearScreen();
                                                temp.xuatThongTin();

                                                System.out.print("\tEnter để tiếp tục!");
                                                str = sc.nextLine();

                                                order(temp);
                                                break;
                                            }

                                            break;
                                        case "3":
                                            continue loop1;
                                        default:
                                            System.out.println("\tVui lòng chọn từ 1 đến 3 !");
                                            break;
                                    }
                                    break;
                                }
                                break;

                            case "2":
                                while (true) {
                                    Function.clearScreen();
                                    System.out.println(
                                            "\t+--------------------------------------------------------------------------------+");
                                    System.out.println(
                                            "\t| Bạn muốn đăng ký tài khoản để lấy mã khách hàng không ?                        |");
                                    System.out.println(
                                            "\t| 1. Có                                                                          |");
                                    System.out.println(
                                            "\t| 2. Không                                                                       |");
                                    System.out.println(
                                            "\t| 3. Quay lại trang trước                                                        |");
                                    System.out.println(
                                            "\t+--------------------------------------------------------------------------------+");
                                    System.out.print("\tNhập lựa chọn của bạn: ");
                                    selection = sc.nextLine();
                                    if (Function.isEmpty(selection)) {
                                        System.out.println("\tVui lòng không để trống !");
                                        try {
                                            Thread.sleep(1500);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        continue;
                                    }
                                    if (!Function.isTrueNumber(selection)) {
                                        System.out.println("\tVui lòng nhập số !");
                                        try {
                                            Thread.sleep(1500);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        continue;
                                    }
                                    KhachHang temp = null;
                                    HoaDon hd = null;
                                    switch (selection) {
                                        // Đăng ký tài khoản
                                        case "1":
                                            while (true) {
                                                Account acc = new Account("1");
                                                acc.nhapThongTin();
                                                temp = acc.getCustomer();


                                                Function.clearScreen();
                                                temp.xuatThongTin();

                                                System.out.print("\tEnter để tiếp tục!");
                                                str = sc.nextLine();

                                                order(temp);
                                                break;
                                            }
                                            break;
                                        case "2":
                                            while (true) {
                                                temp = new KHMangDi("KH1", "Không có tên", false, null);

                                                Function.clearScreen();
                                                temp.xuatThongTin();

                                                System.out.print("\tEnter để tiếp tục!");
                                                str = sc.nextLine();

                                                order(temp);
                                                break;
                                            }
                                            break;
                                        case "3":
                                            continue loop1;
                                        default:
                                            System.out.println("\tVui lòng chọn từ 1 đến 3 !");
                                            try {
                                                Thread.sleep(1500);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            break;
                                    }
                                    break;
                                }

                            case "3":
                                continue loop;

                            default:
                                System.out.println("\tVui lòng chọn từ 1 đến 3 !");
                                continue;
                        }

                        break;
                    }
                    break;

                case "2":
                    break;

                default:
                    System.out.println("\tVui lòng chọn từ 1 đến 3 !");
                    continue;
            }

            break;
        }
    }

    public static void order(KhachHang temp) {
        Scanner sc = new Scanner(System.in);
        String str;
        HoaDon hd = null;
        loop2: while (true) {
            NhanVienThuNgan nvtmp = null;
            ArrayList<NhanVienThuNgan> nvtnList = new ArrayList<>();
            for (Nhanvien nv : qlNhanVien.nhanVienList) {
                if (nv instanceof NhanVienThuNgan) {
                    nvtnList.add((NhanVienThuNgan) nv);
                }
            }

            NhanVienPhaChe nvpc = null;
            ArrayList<NhanVienPhaChe> nvpcList = new ArrayList<>();
            for (Nhanvien nv : qlNhanVien.nhanVienList) {
                if (nv instanceof NhanVienPhaChe) {
                    nvpcList.add((NhanVienPhaChe) nv);
                }
            }

            Random rd = new Random();
            nvtmp = nvtnList.get(rd.nextInt(nvtnList.size() - 0));
            nvpc = nvpcList.get(rd.nextInt(nvpcList.size() - 0));

            boolean isWantDrink = true;
            ThucDon order = new ThucDon();

            while (isWantDrink) {
                // boolean wantHot = false, wantCold = false, wantSugar = false, wantMilk =
                // false;

                Function.clearScreen();
                qlNuocUong.inMenuNuocUong();
                System.out.print("\tNhập ID nước bạn muốn đặt: ");
                str = sc.nextLine();

                if (Function.isEmpty(str)) {
                    System.out.println("\tVui lòng không để trống !");
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }

                NuocUong nuocuong = null;
                for (NuocUong drink : qlNuocUong.getWaterList()) {
                    if (drink.getId().equals(str)) {
                        nuocuong = drink;
                        break;
                    }
                }

                if (nuocuong == null) {
                    System.out.println("\tID không đúng, vui lòng nhập lại!");
                    continue;
                } else {
                    order.getDanhSachNuocUong().add(nuocuong);
                }

                // chọn size nước uống
                while (true) {
                    int countOption = 1;

                    Function.clearScreen();
                    System.out.println(
                            "\t=============================[Chức năng người Dùng tại chỗ]===============================");
                    System.out.printf("\t| %-87s |%n",
                            "Mời bạn chọn size cho món nước " + nuocuong.getId());
                    // ArrayList<String> sizeList = new ArrayList<>(nuocuong.getSizePrice().keySet());
                    // Collections.sort(sizeList);
                    // for (String key : sizeList) {
                    //     System.out.printf("\t| %-5s %-81s |%n", countOption++ + ".", key);
                    // }
                    for (String key : nuocuong.getSizePrice().keySet()) {
                        System.out.printf("\t| %-5s %-81s |%n", countOption++ + ".", key);
                    }
                    // System.out.printf("\t| ");
                    System.out.println(
                            "\t==========================================================================================");
                    System.out.print("\tNhập lựa chọn của bạn: ");
                    str = sc.nextLine();

                    if (Function.isEmpty(str)) {
                        System.out.println("\tVui lòng không để trống !");
                        continue;
                    }

                    if (!Function.isTrueNumber(str)) {
                        System.out.println("\tVui lòng nhập số !");
                        continue;
                    }

                    if (countOption - 1 == 3) {
                        switch (str) {
                            case "1":
                                order.getSize().add("S");
                                break;

                            case "2":
                                order.getSize().add("L");
                                break;

                            case "3":
                                order.getSize().add("M");
                                break;

                            default:
                                System.out.println("\tVui lòng chọn từ 1 đến 3!");
                                continue;
                        }
                    } else if (countOption - 1 == 4) {
                        switch (str) {
                            case "1":
                                order.getSize().add("S");
                                break;

                            case "2":
                                order.getSize().add("M");
                                break;

                            case "3":
                                order.getSize().add("L");
                                break;

                            case "4":
                                order.getSize().add("XL");
                                break;

                            default:
                                System.out.println("\tVui lòng chọn từ 1 đến 4!");
                                continue;
                        }
                    }
                    break;
                }

                // yêu cầu nóng, đá, sữa, đường
                ArrayList<String> trangThai = new ArrayList<>(); // mảng lưu trạng thái của
                                                                  // món nước hiện tại
                trangThai.add("Không"); // đá
                trangThai.add("Không"); // nóng
                trangThai.add("Không"); // đường
                trangThai.add("Không"); // sữa
                loop: while (true) {
                    Function.clearScreen();
                    System.out.println(
                            "\t=============================[Chức năng người Dùng tại chỗ]===============================");
                    System.out.printf("\t| %-87s |%n", "Bạn có yêu cầu gì cho món nước "
                            + nuocuong.getId() + " không?");
                    System.out.printf("\t| %-5s %-81s |%n", "1.", "Có");
                    System.out.printf("\t| %-5s %-81s |%n", "2.", "Không");
                    System.out.println(
                            "\t==========================================================================================");
                    System.out.print("\tNhập lựa chọn của bạn: ");
                    str = sc.nextLine();

                    if (Function.isEmpty(str)) {
                        System.out.println("\tVui lòng không để trống !");
                        continue;
                    }

                    if (!Function.isTrueNumber(str)) {
                        System.out.println("\tVui lòng nhập số !");
                        continue;
                    }

                    switch (str) {
                        case "1":
                            if (!nuocuong.isCold() && !nuocuong.isHot() && !nuocuong.isSugar() && !nuocuong.isMilk()) {
                                System.out.println("\tNước uống này không được điều chỉnh!");
                                System.out.println("\tEnter để tiếp tục!");
                                str = sc.nextLine();
                                break;
                            }
                            loop1: while (true) {
                                int countChangeOption = 1;
                                ArrayList<String> optionList = new ArrayList<>();
                                optionList.add("");
                                optionList.add("");
                                optionList.add("");
                                optionList.add("");
                                optionList.add("");
                                optionList.add("");
                                Function.clearScreen();
                                System.out.println(
                                        "\t=============================[Chức năng người Dùng tại chỗ]===============================");
                                System.out.printf("\t| %-87s |%n",
                                        "Bạn muốn yêu cầu gì cho món nước "
                                                + nuocuong.getId() + "?");
                                // System.out.printf("\t| %-5s %-81s |%n", "1.", "Đá");
                                // System.out.printf("\t| %-5s %-81s |%n", "2.", "Nóng");
                                // System.out.printf("\t| %-5s %-81s |%n", "3.", "Đường");
                                // System.out.printf("\t| %-5s %-81s |%n", "4.", "Sữa");
                                // System.out.printf("\t| %-5s %-81s |%n", "5.",
                                //         "Quay về trang trước");
                                if (nuocuong.isCold()) {
                                    System.out.printf("\t| %-5s %-81s |%n", countChangeOption + ".", "Đá");
                                    optionList.set(countChangeOption, "Đá");
                                    countChangeOption++;
                                }
                                if (nuocuong.isHot()) {
                                    System.out.printf("\t| %-5s %-81s |%n", countChangeOption + ".", "Nóng");
                                    optionList.set(countChangeOption, "Nóng");
                                    countChangeOption++;
                                }
                                if (nuocuong.isSugar()) {
                                    System.out.printf("\t| %-5s %-81s |%n", countChangeOption + ".", "Đường");
                                    optionList.set(countChangeOption, "Đường");
                                    countChangeOption++;
                                }
                                if (nuocuong.isMilk()) {
                                    System.out.printf("\t| %-5s %-81s |%n", countChangeOption + ".", "Sữa");
                                    optionList.set(countChangeOption, "Sữa");
                                    countChangeOption++;
                                }
                                optionList.set(countChangeOption, "Q");
                                System.out.printf("\t| %-5s %-81s |%n", countChangeOption + ".",
                                        "Quay về trang trước");
                                System.out.println(
                                        "\t==========================================================================================");
                                System.out.print("\tNhập lựa chọn của bạn: ");
                                str = sc.nextLine();

                                if (Function.isEmpty(str)) {
                                    System.out.println("\tVui lòng không để trống !");
                                    continue;
                                }

                                if (!Function.isTrueNumber(str)) {
                                    System.out.println("\tVui lòng nhập số !");
                                    continue;
                                }

                                switch (str) {
                                    case "1":
                                    if (optionList.get(1).equals("Đá")) {
                                        while (true) {
                                            Function.clearScreen();
                                            System.out.println(
                                                "\t=============================[Chức năng người Dùng tại chỗ]===============================");
                                            System.out.printf("\t| %-87s |%n",
                                                    "Bạn muốn chọn mức đá nào cho món nước "
                                                            + nuocuong.getId() + "?");
                                            System.out.printf("\t| %-5s %-81s |%n", "1.", "Đá ít");
                                            System.out.printf("\t| %-5s %-81s |%n", "2.", "Đá bình thường");
                                            System.out.printf("\t| %-5s %-81s |%n", "3.", "Đá nhiều");
                                            System.out.printf("\t| %-5s %-81s |%n", "4.", "Quay về trang trước");
                                            System.out.println(
                                                    "\t==========================================================================================");
                                            System.out.print("\tNhập lựa chọn của bạn: ");
                                            str = sc.nextLine();

                                            if (Function.isEmpty(str)) {
                                                System.out.println("\tVui lòng không để trống !");
                                                continue;
                                            }

                                            if (!Function.isTrueNumber(str)) {
                                                System.out.println("\tVui lòng nhập số !");
                                                continue;
                                            }

                                            switch (str) {
                                                case "1":
                                                trangThai.set(0, "Ít");
                                                break;

                                                case "2":
                                                trangThai.set(0, "Bình thường");
                                                break;

                                                case "3":
                                                trangThai.set(0, "Nhiều");
                                                break;

                                                case "4":
                                                continue loop1;

                                                default:
                                                continue;
                                            }
                                            break;
                                        }
                                    } else if (optionList.get(1).equals("Nóng")) {
                                        while (true) {
                                            Function.clearScreen();
                                            System.out.println(
                                                "\t=============================[Chức năng người Dùng tại chỗ]===============================");
                                            System.out.printf("\t| %-87s |%n",
                                                    "Bạn muốn chọn mức nóng nào cho món nước "
                                                            + nuocuong.getId() + "?");
                                            System.out.printf("\t| %-5s %-81s |%n", "1.", "Nóng ít");
                                            System.out.printf("\t| %-5s %-81s |%n", "2.", "Nóng bình thường");
                                            System.out.printf("\t| %-5s %-81s |%n", "3.", "Nóng nhiều");
                                            System.out.printf("\t| %-5s %-81s |%n", "4.", "Quay về trang trước");
                                            System.out.println(
                                                    "\t==========================================================================================");
                                            System.out.print("\tNhập lựa chọn của bạn: ");
                                            str = sc.nextLine();

                                            if (Function.isEmpty(str)) {
                                                System.out.println("\tVui lòng không để trống !");
                                                continue;
                                            }

                                            if (!Function.isTrueNumber(str)) {
                                                System.out.println("\tVui lòng nhập số !");
                                                continue;
                                            }

                                            switch (str) {
                                                case "1":
                                                trangThai.set(1, "Ít");
                                                break;

                                                case "2":
                                                trangThai.set(1, "Bình thường");
                                                break;

                                                case "3":
                                                trangThai.set(1, "Nhiều");
                                                break;

                                                case "4":
                                                continue loop1;

                                                default:
                                                continue;
                                            }
                                            break;
                                        }
                                    } else if (optionList.get(1).equals("Đường")) {
                                        while (true) {
                                            Function.clearScreen();
                                            System.out.println(
                                                "\t=============================[Chức năng người Dùng tại chỗ]===============================");
                                            System.out.printf("\t| %-87s |%n",
                                                    "Bạn muốn chọn mức đường nào cho món nước "
                                                            + nuocuong.getId() + "?");
                                            System.out.printf("\t| %-5s %-81s |%n", "1.", "Đường ít");
                                            System.out.printf("\t| %-5s %-81s |%n", "2.", "Đường bình thường");
                                            System.out.printf("\t| %-5s %-81s |%n", "3.", "Đường nhiều");
                                            System.out.printf("\t| %-5s %-81s |%n", "4.", "Quay về trang trước");
                                            System.out.println(
                                                    "\t==========================================================================================");
                                            System.out.print("\tNhập lựa chọn của bạn: ");
                                            str = sc.nextLine();

                                            if (Function.isEmpty(str)) {
                                                System.out.println("\tVui lòng không để trống !");
                                                continue;
                                            }

                                            if (!Function.isTrueNumber(str)) {
                                                System.out.println("\tVui lòng nhập số !");
                                                continue;
                                            }

                                            switch (str) {
                                                case "1":
                                                trangThai.set(2, "Ít");
                                                break;

                                                case "2":
                                                trangThai.set(2, "Bình thường");
                                                break;

                                                case "3":
                                                trangThai.set(2, "Nhiều");
                                                break;

                                                case "4":
                                                continue loop1;

                                                default:
                                                continue;
                                            }
                                            break;
                                        }
                                    } else if (optionList.get(1).equals("Sữa")) {
                                        while (true) {
                                            Function.clearScreen();
                                            System.out.println(
                                                "\t=============================[Chức năng người Dùng tại chỗ]===============================");
                                            System.out.printf("\t| %-87s |%n",
                                                    "Bạn muốn chọn mức sữa nào cho món nước "
                                                            + nuocuong.getId() + "?");
                                            System.out.printf("\t| %-5s %-81s |%n", "1.", "Sữa ít");
                                            System.out.printf("\t| %-5s %-81s |%n", "2.", "Sữa bình thường");
                                            System.out.printf("\t| %-5s %-81s |%n", "3.", "Sữa nhiều");
                                            System.out.printf("\t| %-5s %-81s |%n", "4.", "Quay về trang trước");
                                            System.out.println(
                                                    "\t==========================================================================================");
                                            System.out.print("\tNhập lựa chọn của bạn: ");
                                            str = sc.nextLine();

                                            if (Function.isEmpty(str)) {
                                                System.out.println("\tVui lòng không để trống !");
                                                continue;
                                            }

                                            if (!Function.isTrueNumber(str)) {
                                                System.out.println("\tVui lòng nhập số !");
                                                continue;
                                            }

                                            switch (str) {
                                                case "1":
                                                trangThai.set(3, "Ít");
                                                break;

                                                case "2":
                                                trangThai.set(3, "Bình thường");
                                                break;

                                                case "3":
                                                trangThai.set(3, "Nhiều");
                                                break;

                                                case "4":
                                                continue loop1;

                                                default:
                                                continue;
                                            }
                                            break;
                                        }
                                    }
                                    break;

                                    case "2":
                                    if (optionList.get(2).equals("Đá")) {
                                        while (true) {
                                            Function.clearScreen();
                                            System.out.println(
                                                "\t=============================[Chức năng người Dùng tại chỗ]===============================");
                                            System.out.printf("\t| %-87s |%n",
                                                    "Bạn muốn chọn mức đá nào cho món nước "
                                                            + nuocuong.getId() + "?");
                                            System.out.printf("\t| %-5s %-81s |%n", "1.", "Đá ít");
                                            System.out.printf("\t| %-5s %-81s |%n", "2.", "Đá bình thường");
                                            System.out.printf("\t| %-5s %-81s |%n", "3.", "Đá nhiều");
                                            System.out.printf("\t| %-5s %-81s |%n", "4.", "Quay về trang trước");
                                            System.out.println(
                                                    "\t==========================================================================================");
                                            System.out.print("\tNhập lựa chọn của bạn: ");
                                            str = sc.nextLine();

                                            if (Function.isEmpty(str)) {
                                                System.out.println("\tVui lòng không để trống !");
                                                continue;
                                            }

                                            if (!Function.isTrueNumber(str)) {
                                                System.out.println("\tVui lòng nhập số !");
                                                continue;
                                            }

                                            switch (str) {
                                                case "1":
                                                trangThai.set(0, "Ít");
                                                break;

                                                case "2":
                                                trangThai.set(0, "Bình thường");
                                                break;

                                                case "3":
                                                trangThai.set(0, "Nhiều");
                                                break;

                                                case "4":
                                                continue loop1;

                                                default:
                                                continue;
                                            }
                                            break;
                                        }
                                    } else if (optionList.get(2).equals("Nóng")) {
                                        while (true) {
                                            Function.clearScreen();
                                            System.out.println(
                                                "\t=============================[Chức năng người Dùng tại chỗ]===============================");
                                            System.out.printf("\t| %-87s |%n",
                                                    "Bạn muốn chọn mức nóng nào cho món nước "
                                                            + nuocuong.getId() + "?");
                                            System.out.printf("\t| %-5s %-81s |%n", "1.", "Nóng ít");
                                            System.out.printf("\t| %-5s %-81s |%n", "2.", "Nóng bình thường");
                                            System.out.printf("\t| %-5s %-81s |%n", "3.", "Nóng nhiều");
                                            System.out.printf("\t| %-5s %-81s |%n", "4.", "Quay về trang trước");
                                            System.out.println(
                                                    "\t==========================================================================================");
                                            System.out.print("\tNhập lựa chọn của bạn: ");
                                            str = sc.nextLine();

                                            if (Function.isEmpty(str)) {
                                                System.out.println("\tVui lòng không để trống !");
                                                continue;
                                            }

                                            if (!Function.isTrueNumber(str)) {
                                                System.out.println("\tVui lòng nhập số !");
                                                continue;
                                            }

                                            switch (str) {
                                                case "1":
                                                trangThai.set(1, "Ít");
                                                break;

                                                case "2":
                                                trangThai.set(1, "Bình thường");
                                                break;

                                                case "3":
                                                trangThai.set(1, "Nhiều");
                                                break;

                                                case "4":
                                                continue loop1;

                                                default:
                                                continue;
                                            }
                                            break;
                                        }
                                    } else if (optionList.get(2).equals("Đường")) {
                                        while (true) {
                                            Function.clearScreen();
                                            System.out.println(
                                                "\t=============================[Chức năng người Dùng tại chỗ]===============================");
                                            System.out.printf("\t| %-87s |%n",
                                                    "Bạn muốn chọn mức đường nào cho món nước "
                                                            + nuocuong.getId() + "?");
                                            System.out.printf("\t| %-5s %-81s |%n", "1.", "Đường ít");
                                            System.out.printf("\t| %-5s %-81s |%n", "2.", "Đường bình thường");
                                            System.out.printf("\t| %-5s %-81s |%n", "3.", "Đường nhiều");
                                            System.out.printf("\t| %-5s %-81s |%n", "4.", "Quay về trang trước");
                                            System.out.println(
                                                    "\t==========================================================================================");
                                            System.out.print("\tNhập lựa chọn của bạn: ");
                                            str = sc.nextLine();

                                            if (Function.isEmpty(str)) {
                                                System.out.println("\tVui lòng không để trống !");
                                                continue;
                                            }

                                            if (!Function.isTrueNumber(str)) {
                                                System.out.println("\tVui lòng nhập số !");
                                                continue;
                                            }

                                            switch (str) {
                                                case "1":
                                                trangThai.set(2, "Ít");
                                                break;

                                                case "2":
                                                trangThai.set(2, "Bình thường");
                                                break;

                                                case "3":
                                                trangThai.set(2, "Nhiều");
                                                break;

                                                case "4":
                                                continue loop1;

                                                default:
                                                continue;
                                            }
                                            break;
                                        }
                                    } else if (optionList.get(2).equals("Sữa")) {
                                        while (true) {
                                            Function.clearScreen();
                                            System.out.println(
                                                "\t=============================[Chức năng người Dùng tại chỗ]===============================");
                                            System.out.printf("\t| %-87s |%n",
                                                    "Bạn muốn chọn mức sữa nào cho món nước "
                                                            + nuocuong.getId() + "?");
                                            System.out.printf("\t| %-5s %-81s |%n", "1.", "Sữa ít");
                                            System.out.printf("\t| %-5s %-81s |%n", "2.", "Sữa bình thường");
                                            System.out.printf("\t| %-5s %-81s |%n", "3.", "Sữa nhiều");
                                            System.out.printf("\t| %-5s %-81s |%n", "4.", "Quay về trang trước");
                                            System.out.println(
                                                    "\t==========================================================================================");
                                            System.out.print("\tNhập lựa chọn của bạn: ");
                                            str = sc.nextLine();

                                            if (Function.isEmpty(str)) {
                                                System.out.println("\tVui lòng không để trống !");
                                                continue;
                                            }

                                            if (!Function.isTrueNumber(str)) {
                                                System.out.println("\tVui lòng nhập số !");
                                                continue;
                                            }

                                            switch (str) {
                                                case "1":
                                                trangThai.set(3, "Ít");
                                                break;

                                                case "2":
                                                trangThai.set(3, "Bình thường");
                                                break;

                                                case "3":
                                                trangThai.set(3, "Nhiều");
                                                break;

                                                case "4":
                                                continue loop1;

                                                default:
                                                continue;
                                            }
                                            break;
                                        }
                                    } else if (optionList.get(2).equals("Q")) {
                                        break;
                                    }
                                    break;

                                    case "3":
                                    if (optionList.get(3).equals("Đá")) {
                                        while (true) {
                                            Function.clearScreen();
                                            System.out.println(
                                                "\t=============================[Chức năng người Dùng tại chỗ]===============================");
                                            System.out.printf("\t| %-87s |%n",
                                                    "Bạn muốn chọn mức đá nào cho món nước "
                                                            + nuocuong.getId() + "?");
                                            System.out.printf("\t| %-5s %-81s |%n", "1.", "Đá ít");
                                            System.out.printf("\t| %-5s %-81s |%n", "2.", "Đá bình thường");
                                            System.out.printf("\t| %-5s %-81s |%n", "3.", "Đá nhiều");
                                            System.out.printf("\t| %-5s %-81s |%n", "4.", "Quay về trang trước");
                                            System.out.println(
                                                    "\t==========================================================================================");
                                            System.out.print("\tNhập lựa chọn của bạn: ");
                                            str = sc.nextLine();

                                            if (Function.isEmpty(str)) {
                                                System.out.println("\tVui lòng không để trống !");
                                                continue;
                                            }

                                            if (!Function.isTrueNumber(str)) {
                                                System.out.println("\tVui lòng nhập số !");
                                                continue;
                                            }

                                            switch (str) {
                                                case "1":
                                                trangThai.set(0, "Ít");
                                                break;

                                                case "2":
                                                trangThai.set(0, "Bình thường");
                                                break;

                                                case "3":
                                                trangThai.set(0, "Nhiều");
                                                break;

                                                case "4":
                                                continue loop1;

                                                default:
                                                continue;
                                            }
                                            break;
                                        }
                                    } else if (optionList.get(3).equals("Nóng")) {
                                        while (true) {
                                            Function.clearScreen();
                                            System.out.println(
                                                "\t=============================[Chức năng người Dùng tại chỗ]===============================");
                                            System.out.printf("\t| %-87s |%n",
                                                    "Bạn muốn chọn mức nóng nào cho món nước "
                                                            + nuocuong.getId() + "?");
                                            System.out.printf("\t| %-5s %-81s |%n", "1.", "Nóng ít");
                                            System.out.printf("\t| %-5s %-81s |%n", "2.", "Nóng bình thường");
                                            System.out.printf("\t| %-5s %-81s |%n", "3.", "Nóng nhiều");
                                            System.out.printf("\t| %-5s %-81s |%n", "4.", "Quay về trang trước");
                                            System.out.println(
                                                    "\t==========================================================================================");
                                            System.out.print("\tNhập lựa chọn của bạn: ");
                                            str = sc.nextLine();

                                            if (Function.isEmpty(str)) {
                                                System.out.println("\tVui lòng không để trống !");
                                                continue;
                                            }

                                            if (!Function.isTrueNumber(str)) {
                                                System.out.println("\tVui lòng nhập số !");
                                                continue;
                                            }

                                            switch (str) {
                                                case "1":
                                                trangThai.set(1, "Ít");
                                                break;

                                                case "2":
                                                trangThai.set(1, "Bình thường");
                                                break;

                                                case "3":
                                                trangThai.set(1, "Nhiều");
                                                break;

                                                case "4":
                                                continue loop1;

                                                default:
                                                continue;
                                            }
                                            break;
                                        }
                                    } else if (optionList.get(3).equals("Đường")) {
                                        while (true) {
                                            Function.clearScreen();
                                            System.out.println(
                                                "\t=============================[Chức năng người Dùng tại chỗ]===============================");
                                            System.out.printf("\t| %-87s |%n",
                                                    "Bạn muốn chọn mức đường nào cho món nước "
                                                            + nuocuong.getId() + "?");
                                            System.out.printf("\t| %-5s %-81s |%n", "1.", "Đường ít");
                                            System.out.printf("\t| %-5s %-81s |%n", "2.", "Đường bình thường");
                                            System.out.printf("\t| %-5s %-81s |%n", "3.", "Đường nhiều");
                                            System.out.printf("\t| %-5s %-81s |%n", "4.", "Quay về trang trước");
                                            System.out.println(
                                                    "\t==========================================================================================");
                                            System.out.print("\tNhập lựa chọn của bạn: ");
                                            str = sc.nextLine();

                                            if (Function.isEmpty(str)) {
                                                System.out.println("\tVui lòng không để trống !");
                                                continue;
                                            }

                                            if (!Function.isTrueNumber(str)) {
                                                System.out.println("\tVui lòng nhập số !");
                                                continue;
                                            }

                                            switch (str) {
                                                case "1":
                                                trangThai.set(2, "Ít");
                                                break;

                                                case "2":
                                                trangThai.set(2, "Bình thường");
                                                break;

                                                case "3":
                                                trangThai.set(2, "Nhiều");
                                                break;

                                                case "4":
                                                continue loop1;

                                                default:
                                                continue;
                                            }
                                            break;
                                        }
                                    } else if (optionList.get(3).equals("Sữa")) {
                                        while (true) {
                                            Function.clearScreen();
                                            System.out.println(
                                                "\t=============================[Chức năng người Dùng tại chỗ]===============================");
                                            System.out.printf("\t| %-87s |%n",
                                                    "Bạn muốn chọn mức sữa nào cho món nước "
                                                            + nuocuong.getId() + "?");
                                            System.out.printf("\t| %-5s %-81s |%n", "1.", "Sữa ít");
                                            System.out.printf("\t| %-5s %-81s |%n", "2.", "Sữa bình thường");
                                            System.out.printf("\t| %-5s %-81s |%n", "3.", "Sữa nhiều");
                                            System.out.printf("\t| %-5s %-81s |%n", "4.", "Quay về trang trước");
                                            System.out.println(
                                                    "\t==========================================================================================");
                                            System.out.print("\tNhập lựa chọn của bạn: ");
                                            str = sc.nextLine();

                                            if (Function.isEmpty(str)) {
                                                System.out.println("\tVui lòng không để trống !");
                                                continue;
                                            }

                                            if (!Function.isTrueNumber(str)) {
                                                System.out.println("\tVui lòng nhập số !");
                                                continue;
                                            }

                                            switch (str) {
                                                case "1":
                                                trangThai.set(3, "Ít");
                                                break;

                                                case "2":
                                                trangThai.set(3, "Bình thường");
                                                break;

                                                case "3":
                                                trangThai.set(3, "Nhiều");
                                                break;

                                                case "4":
                                                continue loop1;

                                                default:
                                                continue;
                                            }
                                            break;
                                        }
                                    } else if (optionList.get(3).equals("Q")) {
                                        break;
                                    }
                                    break;

                                    case "4":
                                    if (optionList.get(4).equals("Đá")) {
                                        while (true) {
                                            Function.clearScreen();
                                            System.out.println(
                                                "\t=============================[Chức năng người Dùng tại chỗ]===============================");
                                            System.out.printf("\t| %-87s |%n",
                                                    "Bạn muốn chọn mức đá nào cho món nước "
                                                            + nuocuong.getId() + "?");
                                            System.out.printf("\t| %-5s %-81s |%n", "1.", "Đá ít");
                                            System.out.printf("\t| %-5s %-81s |%n", "2.", "Đá bình thường");
                                            System.out.printf("\t| %-5s %-81s |%n", "3.", "Đá nhiều");
                                            System.out.printf("\t| %-5s %-81s |%n", "4.", "Quay về trang trước");
                                            System.out.println(
                                                    "\t==========================================================================================");
                                            System.out.print("\tNhập lựa chọn của bạn: ");
                                            str = sc.nextLine();

                                            if (Function.isEmpty(str)) {
                                                System.out.println("\tVui lòng không để trống !");
                                                continue;
                                            }

                                            if (!Function.isTrueNumber(str)) {
                                                System.out.println("\tVui lòng nhập số !");
                                                continue;
                                            }

                                            switch (str) {
                                                case "1":
                                                trangThai.set(0, "Ít");
                                                break;

                                                case "2":
                                                trangThai.set(0, "Bình thường");
                                                break;

                                                case "3":
                                                trangThai.set(0, "Nhiều");
                                                break;

                                                case "4":
                                                continue loop1;

                                                default:
                                                continue;
                                            }
                                            break;
                                        }
                                    } else if (optionList.get(4).equals("Nóng")) {
                                        while (true) {
                                            Function.clearScreen();
                                            System.out.println(
                                                "\t=============================[Chức năng người Dùng tại chỗ]===============================");
                                            System.out.printf("\t| %-87s |%n",
                                                    "Bạn muốn chọn mức nóng nào cho món nước "
                                                            + nuocuong.getId() + "?");
                                            System.out.printf("\t| %-5s %-81s |%n", "1.", "Nóng ít");
                                            System.out.printf("\t| %-5s %-81s |%n", "2.", "Nóng bình thường");
                                            System.out.printf("\t| %-5s %-81s |%n", "3.", "Nóng nhiều");
                                            System.out.printf("\t| %-5s %-81s |%n", "4.", "Quay về trang trước");
                                            System.out.println(
                                                    "\t==========================================================================================");
                                            System.out.print("\tNhập lựa chọn của bạn: ");
                                            str = sc.nextLine();

                                            if (Function.isEmpty(str)) {
                                                System.out.println("\tVui lòng không để trống !");
                                                continue;
                                            }

                                            if (!Function.isTrueNumber(str)) {
                                                System.out.println("\tVui lòng nhập số !");
                                                continue;
                                            }

                                            switch (str) {
                                                case "1":
                                                trangThai.set(1, "Ít");
                                                break;

                                                case "2":
                                                trangThai.set(1, "Bình thường");
                                                break;

                                                case "3":
                                                trangThai.set(1, "Nhiều");
                                                break;

                                                case "4":
                                                continue loop1;

                                                default:
                                                continue;
                                            }
                                            break;
                                        }
                                    } else if (optionList.get(4).equals("Đường")) {
                                        while (true) {
                                            Function.clearScreen();
                                            System.out.println(
                                                "\t=============================[Chức năng người Dùng tại chỗ]===============================");
                                            System.out.printf("\t| %-87s |%n",
                                                    "Bạn muốn chọn mức đường nào cho món nước "
                                                            + nuocuong.getId() + "?");
                                            System.out.printf("\t| %-5s %-81s |%n", "1.", "Đường ít");
                                            System.out.printf("\t| %-5s %-81s |%n", "2.", "Đường bình thường");
                                            System.out.printf("\t| %-5s %-81s |%n", "3.", "Đường nhiều");
                                            System.out.printf("\t| %-5s %-81s |%n", "4.", "Quay về trang trước");
                                            System.out.println(
                                                    "\t==========================================================================================");
                                            System.out.print("\tNhập lựa chọn của bạn: ");
                                            str = sc.nextLine();

                                            if (Function.isEmpty(str)) {
                                                System.out.println("\tVui lòng không để trống !");
                                                continue;
                                            }

                                            if (!Function.isTrueNumber(str)) {
                                                System.out.println("\tVui lòng nhập số !");
                                                continue;
                                            }

                                            switch (str) {
                                                case "1":
                                                trangThai.set(2, "Ít");
                                                break;

                                                case "2":
                                                trangThai.set(2, "Bình thường");
                                                break;

                                                case "3":
                                                trangThai.set(2, "Nhiều");
                                                break;

                                                case "4":
                                                continue loop1;

                                                default:
                                                continue;
                                            }
                                            break;
                                        }
                                    } else if (optionList.get(4).equals("Sữa")) {
                                        while (true) {
                                            Function.clearScreen();
                                            System.out.println(
                                                "\t=============================[Chức năng người Dùng tại chỗ]===============================");
                                            System.out.printf("\t| %-87s |%n",
                                                    "Bạn muốn chọn mức sữa nào cho món nước "
                                                            + nuocuong.getId() + "?");
                                            System.out.printf("\t| %-5s %-81s |%n", "1.", "Sữa ít");
                                            System.out.printf("\t| %-5s %-81s |%n", "2.", "Sữa bình thường");
                                            System.out.printf("\t| %-5s %-81s |%n", "3.", "Sữa nhiều");
                                            System.out.printf("\t| %-5s %-81s |%n", "4.", "Quay về trang trước");
                                            System.out.println(
                                                    "\t==========================================================================================");
                                            System.out.print("\tNhập lựa chọn của bạn: ");
                                            str = sc.nextLine();

                                            if (Function.isEmpty(str)) {
                                                System.out.println("\tVui lòng không để trống !");
                                                continue;
                                            }

                                            if (!Function.isTrueNumber(str)) {
                                                System.out.println("\tVui lòng nhập số !");
                                                continue;
                                            }

                                            switch (str) {
                                                case "1":
                                                trangThai.set(3, "Ít");
                                                break;

                                                case "2":
                                                trangThai.set(3, "Bình thường");
                                                break;

                                                case "3":
                                                trangThai.set(3, "Nhiều");
                                                break;

                                                case "4":
                                                continue loop1;

                                                default:
                                                continue;
                                            }
                                            break;
                                        }
                                    } else if (optionList.get(4).equals("Q")) {
                                        break;
                                    }
                                    break;

                                    case "5":
                                        continue loop;

                                    default:
                                        System.out.println("\tVui lòng chọn từ 1 đến 5!");
                                        continue;
                                }

                                while (true) {
                                    Function.clearScreen();
                                    System.out.println(
                                            "\t=============================[Chức năng người Dùng tại chỗ]===============================");
                                    System.out.printf("\t| %-87s |%n",
                                            "Bạn còn yêu cầu gì thêm cho món nước "
                                                    + nuocuong.getId() + " không?");
                                    System.out.printf("\t| %-5s %-81s |%n", "1.", "Có");
                                    System.out.printf("\t| %-5s %-81s |%n", "2.", "Không");
                                    System.out.println(
                                            "\t==========================================================================================");
                                    System.out.print("\tNhập lựa chọn của bạn: ");
                                    str = sc.nextLine();

                                    if (Function.isEmpty(str)) {
                                        System.out.println("\tVui lòng không để trống !");
                                        continue;
                                    }

                                    if (!Function.isTrueNumber(str)) {
                                        System.out.println("\tVui lòng nhập số !");
                                        continue;
                                    }

                                    switch (str) {
                                        case "1":
                                            continue loop1;

                                        case "2":
                                            break;

                                        default:
                                            System.out
                                                    .println("\tVui lòng chọn từ 1 đến 2!");
                                            try {
                                                Thread.sleep(1500);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            continue;
                                    }
                                    break;
                                }

                                break;
                            }
                            break;

                        case "2":
                            break;

                        default:
                            System.out.println("\tVui lòng chọn từ 1 đến 2 !");
                            try {
                                Thread.sleep(1500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            continue;
                    }

                    break;
                }
                order.getTrangThaiNuocUong().add(trangThai);

                boolean isWantTopping = false;
                boolean askTopping = true;
                if (nuocuong.getTopping().size() == 0) {
                    askTopping = false;
                }
                while (true && askTopping) {
                    Function.clearScreen();
                    System.out.println(
                            "\t=============================[Chức năng người Dùng tại chỗ]===============================");
                    System.out.printf("\t| %-87s |%n",
                            "Bạn có muốn thêm topping cho món nước " + nuocuong.getId()
                                    + " không?");
                    System.out.printf("\t| %-5s %-81s |%n", "1.", "Có");
                    System.out.printf("\t| %-5s %-81s |%n", "2.", "Không");
                    System.out.println(
                            "\t==========================================================================================");
                    System.out.print("\tNhập lựa chọn của bạn: ");
                    str = sc.nextLine();

                    if (Function.isEmpty(str)) {
                        System.out.println("\tVui lòng không để trống !");
                        continue;
                    }

                    if (!Function.isTrueNumber(str)) {
                        System.out.println("\tVui lòng nhập số !");
                        continue;
                    }

                    switch (str) {
                        case "1":
                            isWantTopping = true;
                            break;

                        case "2":
                            isWantTopping = false;
                            break;

                        default:
                            System.out.println("\tVui lòng chọn từ 1 đến 3 !");
                            try {
                                Thread.sleep(1500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            continue;
                    }

                    break;
                }

                ArrayList<Topping> tplist = new ArrayList<>();
                while (isWantTopping) {
                    Function.clearScreen();
                    if (nuocuong.getTopping().size() == 0) {
                        System.out.println("\tNước uống này không được thêm topping!");
                        System.out.print("\tEnter để tiếp tục!");
                        str = sc.nextLine();
                        break;
                    }
                    nuocuong.printToppingOfDrink();
                    while (true) {
                        System.out.print("\tNhập ID topping bạn muốn đặt: ");
                        str = sc.nextLine();

                        if (Function.isEmpty(str)) {
                            System.out.println("\t Vui lòng không để trống!");
                            continue;
                        }

                        Topping tp = qlTopping.getToppingByID(str);
                        if (tp == null) {
                            System.out.println("\t Vui lòng nhập đúng mã topping!");
                            continue;
                        } else {
                            tplist.add(tp);
                            System.out.println("\tĐã thêm topping thành công!");
                        }

                        while (true) {
                            Function.clearScreen();
                            System.out.println(
                                    "\t=============================[Chức năng người Dùng tại chỗ]===============================");
                            System.out.printf("\t| %-87s |%n",
                                    "Bạn có muốn thêm topping nào nữa không?");
                            System.out.printf("\t| %-5s %-81s |%n", "1.", "Có");
                            System.out.printf("\t| %-5s %-81s |%n", "2.", "Không");
                            System.out.println(
                                    "\t==========================================================================================");
                            System.out.print("\tNhập lựa chọn của bạn: ");
                            str = sc.nextLine();

                            if (Function.isEmpty(str)) {
                                System.out.println("\tVui lòng không để trống !");
                                continue;
                            }
                            if (!Function.isTrueNumber(str)) {
                                System.out.println("\tVui lòng nhập số !");
                                continue;
                            }

                            switch (str) {
                                case "1":
                                    isWantTopping = true;
                                    break;

                                case "2":
                                    isWantTopping = false;
                                    break;

                                default:
                                    System.out.println("\tVui lòng chọn từ 1 đến 2 !");
                                    try {
                                        Thread.sleep(1500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    continue;
                            }
                            break;
                        }
                        break;
                    }

                }
                order.getDanhSachTopping().add(tplist);
                Function.clearScreen();
                order.xuatThongTin();
                while (true) {
                    // Function.clearScreen();
                    System.out.println(
                            "\t=============================[Chức năng người Dùng tại chỗ]===============================");
                    System.out.printf("\t| %-87s |%n",
                            "Bạn có muốn đặt thêm món nước nào nữa không?");
                    System.out.printf("\t| %-5s %-81s |%n", "1.", "Có");
                    System.out.printf("\t| %-5s %-81s |%n", "2.", "Không");
                    System.out.println(
                            "\t==========================================================================================");
                    System.out.print("\tNhập lựa chọn của bạn: ");
                    str = sc.nextLine();

                    if (Function.isEmpty(str)) {
                        System.out.println("\tVui lòng không để trống !");
                        continue;
                    }
                    if (!Function.isTrueNumber(str)) {
                        System.out.println("\tVui lòng nhập số !");
                        continue;
                    }

                    switch (str) {
                        case "1":
                            isWantDrink = true;
                            break;

                        case "2":
                            isWantDrink = false;
                            break;

                        default:
                            System.out.println("\tVui lòng chọn từ 1 đến 2 !");
                            try {
                                Thread.sleep(1500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            continue;
                    }
                    break;
                }
            }

            while (true) {
                Function.clearScreen();
                order.xuatThongTin();
                System.out.println(
                        "\t=============================[Chức năng người Dùng tại chỗ]===============================");
                System.out.printf("\t| %-87s |%n", "Mời bạn xác nhận đơn hàng");
                System.out.printf("\t| %-5s %-81s |%n", "1.", "Xác nhận");
                System.out.printf("\t| %-5s %-81s |%n", "2.", "Không xác nhận");
                System.out.println(
                        "\t==========================================================================================");
                System.out.print("\tNhập lựa chọn của bạn: ");
                str = sc.nextLine();

                if (Function.isEmpty(str)) {
                    System.out.println("\tVui lòng không để trống !");
                    continue;
                }
                if (!Function.isTrueNumber(str)) {
                    System.out.println("\tVui lòng nhập số !");
                    continue;
                }

                switch (str) {
                    case "1":
                        double tongTien = order.tinhTongTien();
                        if (temp.IsMember()) {
                            if (temp.getMemberCard().getPoint() > tongTien
                                    / 100) {
                                while (true) {
                                    System.out.println(
                                            "\tBạn có muốn sử dụng điểm tích lũy để giảm 10% tổng hóa đơn không?");
                                    System.out.println("\t1. Có");
                                    System.out.println("\t2. Không");
                                    System.out
                                            .print("\tNhập lựa chọn của bạn: ");
                                    str = sc.nextLine();
                                    if (Function.isEmpty(str)) {
                                        System.out.println(
                                                "\tVui lòng không để trống !");
                                        continue;
                                    }
                                    if (!Function.isTrueNumber(str)) {
                                        System.out.println(
                                                "\tVui lòng nhập số !");
                                        continue;
                                    }
                                    switch (str) {
                                        case "1":
                                            tongTien -= tongTien * 0.1;
                                            temp.getMemberCard().subPoint((int) (tongTien / 100));
                                            System.out.println(
                                                    "\tĐã giảm 10% tổng hóa đơn !");
                                            break;

                                        case "2":
                                            break;

                                        default:
                                            System.out.println(
                                                    "\tVui lòng chọn từ 1 đến 2 !");
                                            try {
                                                Thread.sleep(1500);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            continue;
                                    }
                                    break;
                                }
                            }
                        }
                        // Lấy ngày và tháng hiện tại
                        LocalDate today = LocalDate.now();
                        String day = String.format("%02d",
                                today.getDayOfMonth()); // Ngày
                        String month = String.format("%02d",
                                today.getMonthValue()); // Tháng

                        if (temp.IsMember()) {
                            if (temp.getMemberCard().getBirthDay().getDay()
                                    .equals(day)
                                    && temp.getMemberCard().getBirthDay()
                                            .getMonth().equals(month)) {
                                System.out.println(
                                        "\tChúc mừng sinh nhật bạn được giảm 10% hoá đơn !");
                                tongTien -= tongTien * 0.1;
                            }
                        }
                        double tienKhachDua;
                        while (true) {
                            // System.out.println("\tSố tiền phải trả là: " + tongTien);
                            System.out.printf("\tSố tiền phải trả là:  %.0f VNĐ%n",
                                    tongTien);
                            System.out.print("\tMời nhập số tiền khách đưa: ");
                            str = sc.nextLine();

                            if (Function.isEmpty(str)) {
                                System.out.println("\tVui lòng không để trống!");
                                continue;
                            }

                            if (!Function.isTrueNumber(str)) {
                                System.out.println("\tVui lòng nhập số!");
                                continue;
                            }

                            if (Double.parseDouble(str) < tongTien) {
                                System.out.println(
                                        "\tQuý khách đưa không đủ tiền. Vui lòng nhập lại!");
                                continue;
                            }

                            tienKhachDua = Double.parseDouble(str);
                            break;
                        }

                        hd = new HoaDon(temp, nvtmp, order, tongTien, tienKhachDua);
                        nvtmp.setSoBillDaXuLy(nvtmp.getSoBillDaXuLy() + 1);
                        nvtmp.setTongTienDaXuLy(nvtmp.getTongTienDaXuLy() + tongTien);
                        nvpc.setSoDonDaPhaChe(nvpc.getSoDonDaPhaChe() + 1);
                        if (temp.IsMember()) {
                            temp.getMemberCard().point((int) tongTien / 1000);
                        }

                        break;

                    case "2":
                        continue loop2;

                    default:
                        System.out.println("\tVui lòng chọn từ 1 đến 2 !");
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        continue;
                }
                break;
            }

            break;
        }
        Function.clearScreen();
        hd.xuatThongTin();
        System.out.print("\t Bạn muốn in hoá đơn không ?");
        System.out.print("\t 1. Có");
        System.out.print("\t 2. Không");
        str = sc.nextLine();
        if (str.equals("1")) {
            CreateTXT.createTextFile(hd);
        }
        else if (str.equals("2")) {
            System.out.println("\tCảm ơn bạn đã sử dụng dịch vụ của chúng tôi !");
        }
        qlHoaDon.billList.add(hd);
        qlHoaDon.writeAll();
        qlNhanVien.writeFile();
        qlKhachHang.writeAll();
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        str = sc.nextLine();
        // xử lí phần chọn nước
    }

    public static void Menu() {
        Scanner sc = new Scanner(System.in);
        int number = 0;
        String str;
        while (true) {
            Function.clearScreen();
            System.out.println(
                    "\t===================================[Trang người dùng]====================================");
            System.out.printf("\t| %-87s |%n", "Bạn là người dùng gì ?");
            System.out.printf("\t| %-5s %-81s |%n", "1.", "Dùng tại chỗ");
            System.out.printf("\t| %-5s %-81s |%n", "2.", "Dùng mang đi");
            System.out.printf("\t| %-5s %-81s |%n", "3.", "Thoát chương trình");
            System.out.println(
                    "\t==========================================================================================");
            System.out.print("\tNhập lựa chọn của bạn: ");
            str = sc.nextLine();
            if (Function.isEmpty(str)) {
                System.out.println("\tVui lòng không để trống !");
            } else {
                if (Function.isTrueNumber(str)) {
                    number = Integer.parseInt(str);
                    if (number >= 1 && number <= 3) {
                        if (number == 1) {
                            selectOne();
                            continue;
                        } else if (number == 2) {
                            selectTwo();
                            continue;
                        } else if (number == 3) {
                            // Thoát
                            System.out.println("\tCảm ơn bạn đã sử dụng dịch vụ của chúng tôi !");
                            break;
                        } else {
                            System.out.println("\tVui lòng chọn từ 1 đến 3 !");
                            continue;
                        }
                    } else {
                        System.out.println("\tVui lòng chọn từ 1 đến 2 !");
                        continue;
                    }
                } else {
                    System.out.println("\tVui lòng nhập số !");
                    continue;
                }
            }
        }
    }

    public static void main(String[] args) {
        Menu();
    }
}
