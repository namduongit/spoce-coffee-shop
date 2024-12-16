package Ban;

import Utils.Function;
import Utils.IXuat;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;


@SuppressWarnings("resource")
public class QLBan implements IXuat {
    public ArrayList<Ban> tableList;

    public QLBan() {
        this.tableList = new ArrayList<>();
    }

    public QLBan(ArrayList<Ban> tableList) {
        this.tableList = tableList;
    }

    // Phương thức dùng để đọc dữ liệu bàn từ tập tin và ghi vào mảng quản lí bàn
    public void Init() {
        File tableFile = new File("../File/table.txt");
        try (Scanner sc = new Scanner(tableFile)) {
            while (sc.hasNextLine()) {
                String str = sc.nextLine();
                String[] arr = str.split("\\|");
                Ban ban = null;
                if (arr.length == 4) {
                    boolean status;
                    if (arr[2].compareTo("0") == 0) {
                        status = false;
                    } else {
                        status = true;
                    }
                    ban = new Ban(arr[0], Integer.parseInt(arr[1]), status, arr[3]);
                    this.tableList.add(ban);
                }
            }
            sc.close();
        } catch (Exception e) {
            System.out.println("Lỗi " + e.getMessage());
        }

        // Collision Sort bàn
        Collections.sort(this.tableList, new Comparator<Ban>() {
            @Override
            public int compare(Ban o1, Ban o2) {
                // Sắp xếp từ bé đến lớn theo numOfCustomersOfTable
                return o1.getCustomerPerTable() - o2.getCustomerPerTable();
            }
        });
    }

    // Phương thức dùng để ghi tất cả đối tượng Bàn trong Array List vào file
    public void writeAll() {
        try (FileWriter writer = new FileWriter("../File/table.txt", false)) {
            for (Ban ban : this.tableList) {
                writer.write(ban.makeString() + "\n");
            }
            writer.flush();
        } catch (Exception e) {
            System.out.println("Lỗi " + e.getMessage());
        }
    }

    // Phương thức dùng để thêm 1 bàn mới vào Array List
    public void addTable() {
        Ban ban = new Ban();
        ban.nhapThongTin();
        this.tableList.add(ban);
        this.writeAll();
        System.out.println("\tThêm bàn thành công!");
    }

    // Phương thức dùng để xóa 1 bàn ra khỏi Array List
    public void removeTable() {
        this.printTableList();
        Scanner sc = new Scanner(System.in);
        String str;
        boolean isDone = false;

        while (true) {
            System.out.print("\n\t=> Nhập ID bàn muốn xóa: ");
            str = sc.nextLine();

            if (Function.isEmpty(str)) {
                System.out.println("\tID bàn không được rỗng!");
                continue;
            }

            for (Ban ban : this.tableList) {
                if (ban.getTableID().equalsIgnoreCase(str)) {
                    this.tableList.remove(ban);
                    System.out.println("\tXóa bàn thành công!");
                    isDone = true;
                    break;
                }
            }

            if (!isDone) {
                System.out.println("\tKhông tìm thấy bàn!");
            } else {
                this.writeAll();
            }

            break;
        }
    }

    // Phương thức để kiểm tra ID bàn đã tồn tại hay chưa (Nếu đã tồn tại thì trả về true, chưa có thì trả về false)
    public boolean checkTableID(String tableID) {
        for (Ban table : this.tableList) {
            if (table.getTableID().compareTo(tableID) == 0) {
                return true;
            }
        }
        return false;
    }

    // Phương thức dùng để in ra danh sách bàn
    public void printTableList() {
        Function.clearScreen();
        System.out.println("\t=============================================[Danh sách bàn]================================================");
        System.out.printf("\t| %-20s | %-25s | %-25s | %-25s |\n", " Mã bàn", " Số chỗ ngồi", " View", " Tình trạng");
        System.out.println("\t|----------------------|---------------------------|---------------------------|---------------------------|");
        for (Ban table : this.tableList) {
            table.printString();
        }
        System.out.println("\t============================================================================================================");
    }

    public void printEmptyTableList() {
        Function.clearScreen();
        System.out.println("\t=============================================[Danh sách bàn]================================================");
        System.out.printf("\t| %-20s | %-25s | %-25s | %-25s |\n", " Mã bàn", " Số chỗ ngồi", " View", " Tình trạng");
        System.out.println("\t|----------------------|---------------------------|---------------------------|---------------------------|");
        for (Ban table : this.tableList) {
            if (table.getTableStatus() == true) {
                table.printString();
            }
        }
        System.out.println("\t============================================================================================================");
    }

    @Override
    public void xuatThongTin() {
        for (Ban table : this.tableList) {
            table.xuatThongTin();
        }
    }

    // Phương thức để chỉnh sửa thông tin của bàn
    public void modifyTable() {
        Scanner sc = new Scanner(System.in);
        String str;
        boolean isDone = false;

        this.printTableList();

        while (true) {
            System.out.print("\n\t=> Mời nhập ID bàn: ");
            str = sc.nextLine();

            if (Function.isEmpty(str)) {
                System.out.println("\tID bàn không được rỗng!");
                continue;
            }

            for (Ban ban : this.tableList) {
                if (ban.getTableID().equalsIgnoreCase(str)) {
                    ban.suaThongTin();
                    isDone = true;
                    break;
                }
            }

            if (!isDone) {
                System.out.println("\tKhông tìm thấy bàn!");
            } else {
                this.writeAll();
            }

            break;
        }
    }

    // Phương thức để đặt lại dữ liệu của array list
    public void resetList() {
        Scanner sc = new Scanner(System.in);
        String str;

        while (true) {
            // System.out.println("\n\tBạn có chắc chắn muốn xoá toàn bộ danh sách không?");
            // System.out.println("\t1. Có");
            // System.out.println("\t2. Không");
            // System.out.print("\t=> Nhập lựa chọn: ");
            System.out.println("\t===================================================================================");
            System.out.printf("\t| %-60s %-18s |%n", "Bạn có chắc chắn muốn xoá toàn bộ danh sách không ?", "");
            System.out.println("\t===================================================================================");
            System.out.printf("\t| %-10s %-68s |%n", "1.", "Có");
            System.out.printf("\t| %-10s %-68s |%n", "2.", "Không");
            System.out.println("\t===================================================================================");
            System.out.print("\t=> Mời nhập lựa chọn: ");
            str = sc.nextLine();

            if (Function.isEmpty(str)) {
                System.out.println("\tLựa chọn không được rỗng!");
                System.out.println("\tEnter để tiếp tục!");
                str = sc.nextLine();
                continue;
            }

            if (!Function.isTrueNumber(str)) {
                System.out.println("\tLựa chọn phải là số!");
                System.out.println("\tEnter để tiếp tục!");
                str = sc.nextLine();
                continue;
            }

            switch (str) {
                case "1":
                this.tableList.clear();
                System.out.println("\tLàm mới danh sách thành công!");
                break;

                case "2":
                System.out.println("\tHủy bỏ làm mới danh sách!");
                break;

                default:
                System.out.println("\tLựa chọn không hợp lệ!");
                continue;
            }

            break;
        }
    }

    // Phương thức để in ra số bàn trong quán
    public void countTable() {
        int twoSeatTable = 0;
        int fourSeatTable = 0;
        int eightSeatTable = 0;

        for (Ban ban : this.tableList) {
            switch (ban.getCustomerPerTable()) {
                case 2:
                twoSeatTable++;
                break;

                case 4:
                fourSeatTable++;
                break;

                case 8:
                eightSeatTable++;
                break;
            }
        }

        // System.out.println("\tTổng số lượng bàn: " + this.tableList.size());
        // System.out.printf("\t%-5s %-10s%n", "", "Số lượng bàn 2 chỗ: " + twoSeatTable);
        // System.out.printf("\t%-5s %-10s%n", "", "Số lượng bàn 4 chỗ: " + fourSeatTable);
        // System.out.printf("\t%-5s %-10s%n", "", "Số lượng bàn 8 chỗ: " + eightSeatTable);

        Function.clearScreen();
        System.out.println("\t============================================================================");
        System.out.printf("\t| %-50s %-21s |%n", "Số lượng bàn", "");
        System.out.println("\t============================================================================");
        System.out.printf("\t| %-30s %-41s |%n", "Tổng số lượng bàn:", this.tableList.size());
        System.out.printf("\t| %-30s %-41s |%n", "Bàn 2 chỗ", twoSeatTable);
        System.out.printf("\t| %-30s %-41s |%n", "Bàn 4 chỗ", fourSeatTable);
        System.out.printf("\t| %-30s %-41s |%n", "Bàn 8 chỗ", eightSeatTable);
        System.out.println("\t============================================================================");
    }

    // Phương thức để tìm bàn và in ra thông tin
    public void findTable() {
        Scanner sc = new Scanner(System.in);
        String str;
        Ban ban = null;

        while (true) {
            System.out.print("\n\tMời nhập ID bàn: ");
            str = sc.nextLine();

            if (Function.isEmpty(str)) {
                System.out.println("\tID bàn không được rỗng");
                continue;
            }

            for (Ban table : this.tableList) {
                if (table.getTableID().equalsIgnoreCase(str)) {
                    ban = table;
                    break;
                }
            }

            if (ban == null) {
                System.out.println("\tKhông tìm thấy bàn nào có ID: " + str);
            } else {
                System.out.println("\tKết quả tìm kiếm: ");
                ban.xuatThongTin1Ban();
            }

            break;
        }
    }

    public Ban getTableByID(String id) {
        Ban table = null;
        for (Ban ban : this.tableList) {
            if (ban.getTableID().equals(id) && !ban.getTableStatus()) {
                table = ban;
                break;
            }
        }
        return table;
    }

    public boolean printEmptyTable() {
        boolean isThereAnEmptyTable = false;
        for (Ban ban : this.tableList) {
            if (!ban.getTableStatus()) {
                isThereAnEmptyTable = true;
            }
        }

        if (isThereAnEmptyTable) {
            System.out.println("\t===========================================[Danh sách bàn trống]============================================");
            System.out.printf("\t| %-20s | %-25s | %-25s | %-25s |\n", "Mã bàn", "Số chỗ ngồi", "Địa điểm", "Tình trạng");
            for (Ban ban : this.tableList) {
                if (!ban.getTableStatus()) {
                    ban.printString();
                }
            }
            System.out.println("\t============================================================================================================");
        }

        return isThereAnEmptyTable;
    }

    public void tableFunctionMenu() {
        Scanner sc = new Scanner(System.in);
        String str;

        while (true) {
            Function.clearScreen();
            System.out.println("\t===============================[ Menu chức năng ]===========================");
            System.out.printf("\t| %-4s | %-65s |%n", "STT", "Chức năng");
            System.out.println(
                    "\t|------|-------------------------------------------------------------------|");
            System.out.printf("\t| %-4s | %-65s |%n", "1", "Tìm kiếm bàn");
            System.out.printf("\t| %-4s | %-65s |%n", "2", "In ra số lượng bàn trong quán");
            System.out.printf("\t| %-4s | %-65s |%n", "3", "Quay về menu chính");
            System.out.println("\t============================================================================");
            System.out.print("\t=> Nhập lựa chọn: ");
            str = sc.nextLine();

            if (Function.isEmpty(str)) {
                System.out.println("\tLựa chọn không được trống!");
                System.out.println("\tEnter để tiếp tục!");
                str = sc.nextLine();
                continue;
            }

            if (!Function.isTrueNumber(str)) {
                System.out.println("\tLựa chọn phải là số!");
                System.out.println("\tEnter để tiếp tục!");
                str = sc.nextLine();
                continue;
            }

            switch (str) {
                case "1":
                this.findTable();
                System.out.println("\tEnter để tiếp tục!");
                str = sc.nextLine();
                break;

                case "2":
                this.countTable();
                System.out.println("\tEnter để tiếp tục!");
                str = sc.nextLine();
                break;

                case "3":
                break;

                default:
                System.out.println("\tLựa chọn không hợp lệ!");
                System.out.println("\tEnter để tiếp tục!");
                str = sc.nextLine();
                continue;
            }

            break;
        }
    }

    public void addRemoveModifyMenu() {
        Scanner sc = new Scanner(System.in);
        String str;
        while (true) {
            Function.clearScreen();
            System.out.println("\t============================[ Menu thêm/sửa/xóa ]===========================");
            System.out.printf("\t| %-4s | %-65s |%n", "STT", "Chức năng");
            System.out.println(
                    "\t|------|-------------------------------------------------------------------|");
            System.out.printf("\t| %-4s | %-65s |%n", "1", "Thêm một bàn (Tự động lưu vào File)");
            System.out.printf("\t| %-4s | %-65s |%n", "2", "Xoá một bàn (Tự động load vào File)");
            System.out.printf("\t| %-4s | %-65s |%n", "3", "Sửa thông tin bàn");
            System.out.printf("\t| %-4s | %-65s |%n", "4", "Quay lại menu chính");
            System.out.println("\t============================================================================");
            System.out.print("\t=> Nhập lựa chọn: ");
            str = sc.nextLine();

            if (Function.isEmpty(str)) {
                System.out.println("\tLựa chọn không được trống!");
                continue;
            }

            if (!Function.isTrueNumber(str)) {
                System.out.println("\tLựa chọn phải là số!");
                continue;
            }

            switch (str) {
                case "1":
                    this.addTable();
                    break;

                case "2":
                    this.removeTable();
                    break;

                case "3":
                    this.modifyTable();
                    break;

                case "4":
                    break;

                default:
                    System.out.println("\tLựa chọn không hợp lệ! Hãy thử lại.");
                    System.out.println("\tEnter để tiếp tục!");
                    str = sc.nextLine();
                    continue;
            }
            break;
        }
    }

    public void updateMenu() {
        Scanner sc = new Scanner(System.in);
        String str;

        while (true) {
            Function.clearScreen();
            System.out.println("\t===============================[ Menu cập nhật ]============================");
            System.out.printf("\t| %-4s | %-65s |%n", "STT", "Chức năng");
            System.out.println(
                    "\t|------|-------------------------------------------------------------------|");
            System.out.printf("\t| %-4s | %-65s |%n", "1", "Cập nhật danh sách topping vào File");
            System.out.printf("\t| %-4s | %-65s |%n", "2", "Cập nhật lại danh sách topping từ File");
            System.out.printf("\t| %-4s | %-65s |%n", "3", "Làm mới danh sách topping (Reset dữ liệu)");
            System.out.printf("\t| %-4s | %-65s |%n", "4", "Quay về trang trước");
            System.out.println("\t============================================================================");
            System.out.print("\t=> Nhập lựa chọn: ");
            str = sc.nextLine();

            if (Function.isEmpty(str)) {
                System.out.println("\tLựa chọn không được trống!");
                continue;
            }

            if (!Function.isTrueNumber(str)) {
                System.out.println("\tLựa chọn phải là số!");
                continue;
            }

            switch (str) {
                case "1":
                this.writeAll();
                break;

                case "2":
                this.Init();
                break;

                case "3":
                this.resetList();
                break;

                case "4":
                break;

                default:
                continue;
            }

            break;
        }
    }

    public void menuQLBan() {
        Function.clearScreen();
        this.Init();
        Scanner sc = new Scanner(System.in);
        String str;

        while (true) {
            Function.clearScreen();
            // In tiêu đề
            System.out.println("\t==================================[ Menu Quản Lý Bàn ]==================================");

            // In tiêu đề các cột
            System.out.printf("\t| %-4s | %-77s |%n", "STT", "Chức năng");
            System.out.println(
                    "\t|------|-------------------------------------------------------------------------------|");

            // In danh sách các lựa chọn
            // System.out.printf("\t| %-4s | %-77s |%n", "1", "In danh sách bàn");
            // System.out.printf("\t| %-4s | %-77s |%n", "2", "Thêm một bàn (Tự động lưu vào File)");
            // System.out.printf("\t| %-4s | %-77s |%n", "3", "Xoá một bàn (Tự động load vào File)");
            // System.out.printf("\t| %-4s | %-77s |%n", "4", "Sửa thông tin bàn");
            // System.out.printf("\t| %-4s | %-77s |%n", "5", "Cập nhật lại bàn vào File từ danh sách");
            // System.out.printf("\t| %-4s | %-77s |%n", "6", "Cập nhật lại bàn vào danh sách từ File");
            // System.out.printf("\t| %-4s | %-77s |%n", "7", "Làm mới danh sách bàn (Reset dữ liệu nhưng không load vào File)");
            // System.out.printf("\t| %-4s | %-77s |%n", "8", "In ra số lượng bàn trong quán");
            // System.out.printf("\t| %-4s | %-77s |%n", "9", "Tìm kiếm bàn");
            // System.out.printf("\t| %-4s | %-77s |%n", "10", "Làm mới màn hình");
            // System.out.printf("\t| %-4s | %-77s |%n", "11", "Thoát chương trình quản lý");

            System.out.printf("\t| %-4s | %-77s |%n", "1", "In danh sách bàn");
            System.out.printf("\t| %-4s | %-77s |%n", "2", "Chức năng về bàn");
            System.out.printf("\t| %-4s | %-77s |%n", "3", "Thêm/sửa/xóa thông tin bàn");
            System.out.printf("\t| %-4s | %-77s |%n", "4", "Cập nhật và làm mới danh sách bàn");
            System.out.printf("\t| %-4s | %-77s |%n", "5", "Làm mới màn hình");
            System.out.printf("\t| %-4s | %-77s |%n", "6", "Thoát chương trình quản lý");

            // In dòng kẻ dưới cùng
            System.out.println("\t========================================================================================");
            System.out.print("\t[Manage] Nhập lựa chọn: ");
            str = sc.nextLine();

            if (Function.isEmpty(str)) {
                System.out.println("\tLựa chọn không được rỗng!");
                continue;
            }

            if (!Function.isTrueNumber(str)) {
                System.out.println("\tLựa chọn phải là số!");
                continue;
            }

            switch (str) {
                case "1":
                this.printTableList();
                System.out.print("\tEnter để tiếp tục!");
                str = sc.nextLine();
                continue;

                case "2":
                this.tableFunctionMenu();
                continue;

                case "3":
                this.addRemoveModifyMenu();
                continue;

                case "4":
                this.updateMenu();
                continue;

                case "5":
                Function.clearScreen();
                continue;

                case "6":
                break;

                default:
                System.out.println("\tLựa chọn không hợp lệ!");
                continue;
            }
            break;
        }
    }


    public static void main(String[] args) {
        QLBan ql = new QLBan();
        ql.menuQLBan();
    }
}
