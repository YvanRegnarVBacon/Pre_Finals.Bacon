package Java_Bacon;

import config.config;
import java.util.*;

public class Java_Bacon {

    // === MAIN METHOD WITH LOGIN & REGISTER ===
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        config.connectDB();

        int choice;

        do {
            System.out.println("\n==== MAIN MENU ====");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Enter Choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    // LOGIN
                    System.out.print("Enter Email: ");
                    String loginEmail = sc.nextLine();
                    System.out.print("Enter Password: ");
                    String loginPass = sc.nextLine();

                    String loginQry = "SELECT * FROM user_tbl WHERE u_email = ? AND u_password = ?";
                    List<Map<String, Object>> loginResult = conf.fetchRecords(loginQry, loginEmail, loginPass);

                    if (loginResult.isEmpty()) {
                        System.out.println("❌ INVALID CREDENTIALS");
                    } else {
                        Map<String, Object> user = loginResult.get(0);
                        String role = user.get("u_role").toString();
                        String name = user.get("u_name").toString();

                        System.out.println("\n✅ LOGIN SUCCESS! Welcome, " + name + " (" + role + ")");

                        if (role.equalsIgnoreCase("Admin")) {
                            adminMenu(conf, sc);
                        } else if (role.equalsIgnoreCase("Secretary")) {
                            userMenu(conf, sc); // Secretary → Add Purok
                        } else if (role.equalsIgnoreCase("Official")) {
                            officialMenu(conf, sc); // Official → Add Resident
                        } else {
                            System.out.println("Access limited to registered roles only!");
                        }
                    }
                    break;

                case 2:
                    // REGISTER
                    System.out.print("Enter Name: ");
                    String regName = sc.nextLine();
                    System.out.print("Enter Email: ");
                    String regEmail = sc.nextLine();

                    // Check if email already exists
                    while (true) {
                        String checkQry = "SELECT * FROM user_tbl WHERE u_email = ?";
                        List<Map<String, Object>> res = conf.fetchRecords(checkQry, regEmail);
                        if (res.isEmpty()) break;
                        System.out.print("Email already exists. Enter another: ");
                        regEmail = sc.nextLine();
                    }

                    System.out.println("Select User Role: ");
                    System.out.println("1. Admin");
                    System.out.println("2. Secretary");
                    System.out.println("3. Official");
                    System.out.print("Enter Role: ");
                    int roleChoice = sc.nextInt();
                    sc.nextLine();

                    String regRole = (roleChoice == 1) ? "Admin" : (roleChoice == 2 ? "Secretary" : "Official");

                    System.out.print("Enter Password: ");
                    String regPass = sc.nextLine();

                    String regSql = "INSERT INTO user_tbl(u_name, u_email, u_password, u_role) VALUES (?, ?, ?, ?)";
                    conf.addRecord(regSql, regName, regEmail, regPass, regRole);
                    System.out.println("✅ Account Registered!");
                    break;

                case 3:
                    System.out.println("Exiting system...");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid Choice!");
            }

        } while (true);
    }

    // === SECRETARY MENU ===
    public static void userMenu(config conf, Scanner sc) {
        while (true) {
            System.out.println("\n==== SECRETARY MENU ====");
            System.out.println("1. Add Purok");
            System.out.println("2. Back to Main Menu");
            System.out.print("Enter Choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter Purok Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Purok Address: ");
                    String address = sc.nextLine();
                    System.out.print("Enter Purok Offical Email: ");
                    String email = sc.nextLine();
                    System.out.print("Enter Purok Offical Phone No.: ");
                    String pn = sc.nextLine();

                    String sql = "INSERT INTO tbl_purok(purok_name, purok_address, purok_email, purok_pn) VALUES(?,?,?,?)";
                    conf.addRecord(sql, name, address, email, pn);
                    System.out.println("✅ Record added successfully!");
                    break;

                case 2:
                    return; // go back to main
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    // === OFFICIAL MENU ===
public static void officialMenu(config conf, Scanner sc) {
    while (true) {
        System.out.println("\n==== OFFICIAL MENU ====");
        System.out.println("1. Add Resident");
        System.out.println("2. Back to Main Menu");
        System.out.print("Enter Choice: ");
        int choice = sc.nextInt();
        sc.nextLine();

        switch (choice) {
            case 1:
                System.out.print("Enter Valid ID: ");
                String validID = sc.nextLine();
                System.out.print("Enter Full Name: ");
                String fullName = sc.nextLine();
                System.out.print("Enter Date of Birth (YYYY-MM-DD): ");
                String birthDate = sc.nextLine();
                System.out.print("Enter Civil Status: ");
                String civilStatus = sc.nextLine();
                System.out.print("Enter Occupation: ");
                String occupation = sc.nextLine();
                System.out.print("Enter Religion: ");
                String religion = sc.nextLine();
                System.out.print("Enter Educational Attainment: ");
                String education = sc.nextLine();
                System.out.print("Enter Contact Information: ");
                String contact = sc.nextLine();
                System.out.print("Enter Previous Address: ");
                String prevAddress = sc.nextLine();
                System.out.print("Enter Current Address: ");
                String currAddress = sc.nextLine();
                System.out.print("Enter Household Members (if applicable): ");
                String household = sc.nextLine();
                System.out.print("Enter Voter's Information (if applicable): ");
                String voter = sc.nextLine();

                // ✅ Display available Puroks before assigning
                System.out.println("\n--- Available Puroks ---");
                String purokSql = "SELECT * FROM tbl_purok";
                String[] headers = {"ID", "Name", "Address", "Email", "Phone No."};
                String[] columns = {"purok_id", "purok_name", "purok_address", "purok_email", "purok_pn"};
                conf.viewRecords(purokSql, headers, columns);

                System.out.print("Enter Purok ID to assign this resident: ");
                int purokId = sc.nextInt();
                sc.nextLine();

                // ✅ Updated SQL with purok_id
                String sql = "INSERT INTO tbl_residents(valid_id, full_name, birth_date, civil_status, occupation, religion, education, contact_info, prev_address, curr_address, household_members, voter_info, purok_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                conf.addRecord(sql, validID, fullName, birthDate, civilStatus, occupation, religion, education, contact, prevAddress, currAddress, household, voter, purokId);

                System.out.println("✅ Resident added successfully and assigned to Purok ID " + purokId + "!");
                break;

            case 2:
                return; // back to main
            default:
                System.out.println("Invalid choice!");
        }
    }
}


    // === ADMIN MENU ===
public static void adminMenu(config conf, Scanner sc) {
    int adminAction;
    do {
        System.out.println("\n==== ADMIN MENU ====");
        System.out.println("1. View Records");
        System.out.println("2. Update Record");
        System.out.println("3. Delete Record");
        System.out.println("4. Back");
        System.out.print("Enter Choice: ");
        adminAction = sc.nextInt();
        sc.nextLine();

        switch (adminAction) {
            // ===== VIEW RECORDS =====
            case 1:
                System.out.println("\nView which records?");
                System.out.println("1. Purok Records");
                System.out.println("2. Resident Records");
                System.out.print("Enter choice: ");
                int viewChoice = sc.nextInt();
                sc.nextLine();

                switch (viewChoice) {
                    case 1:
                        // ✅ Updated to show resident count per Purok
                        String sql = "SELECT * FROM tbl_purok";
                            String[] headers = {"ID", "Name", "Address", "Email", "Phone No."};
                            String[] columns = {"purok_id", "purok_name", "purok_address", "purok_email", "purok_pn"};
                            conf.viewRecords(sql, headers, columns);
                        break;

                    case 2:
                        String sql2 = "SELECT * FROM tbl_residents";
                        String[] headers2 = {"ID", "Valid ID", "Full Name", "Birth Date", "Civil Status", "Occupation", "Religion", "Education", "Contact", "Prev Address", "Curr Address", "Household Members", "Voter Info","Purok"};
                        String[] columns2 = {"resident_id", "valid_id", "full_name", "birth_date", "civil_status", "occupation", "religion", "education", "contact_info", "prev_address", "curr_address", "household_members", "voter_info","purok_id"};
                        conf.viewRecords(sql2, headers2, columns2);
                        break;

                    default:
                        System.out.println("Invalid choice!");
                        break;
                }
                break;

            // ===== UPDATE RECORD =====
            case 2:
                System.out.println("\nUpdate which records?");
                System.out.println("1. Purok Records");
                System.out.println("2. Resident Records");
                System.out.print("Enter choice: ");
                int updateChoice = sc.nextInt();
                sc.nextLine();

                switch (updateChoice) {
                    case 1: {
                        String sqlU = "SELECT * FROM tbl_purok";
                        String[] headersU = {"ID", "Name", "Address", "Email", "Phone No."};
                        String[] columnsU = {"purok_id", "purok_name", "purok_address", "purok_email", "purok_pn"};
                        conf.viewRecords(sqlU, headersU, columnsU);
                        System.out.print("Enter ID of record to update: ");
                        int updateId = sc.nextInt();
                        sc.nextLine();
                        System.out.println("Which field do you want to update?");
                        System.out.println("1. Name");
                        System.out.println("2. Address");
                        System.out.println("3. Email");
                        System.out.println("4. Phone No.");
                        System.out.print("Enter choice: ");
                        int fieldChoice = sc.nextInt();
                        sc.nextLine();
                        String sqlUpdate = "";
                        Object newValue = null;
                        switch (fieldChoice) {
                            case 1: System.out.print("Enter new Name: "); newValue = sc.nextLine(); sqlUpdate = "UPDATE tbl_purok SET purok_name = ? WHERE purok_id = ?"; break;
                            case 2: System.out.print("Enter new Address: "); newValue = sc.nextLine(); sqlUpdate = "UPDATE tbl_purok SET purok_address = ? WHERE purok_id = ?"; break;
                            case 3: System.out.print("Enter new Email: "); newValue = sc.nextLine(); sqlUpdate = "UPDATE tbl_purok SET purok_email = ? WHERE purok_id = ?"; break;
                            case 4: System.out.print("Enter new Phone No.: "); newValue = sc.nextLine(); sqlUpdate = "UPDATE tbl_purok SET purok_pn = ? WHERE purok_id = ?"; break;
                            default: System.out.println("Invalid choice!"); break;
                        }
                        if (!sqlUpdate.isEmpty()) {
                            conf.updateRecord(sqlUpdate, newValue, updateId);
                            System.out.println("✅ Purok record updated successfully!");
                        }
                        break;
                    }
                    case 2: {
                        String sqlR = "SELECT * FROM tbl_residents";
                        String[] headersR = {"ID", "Valid ID", "Full Name", "Birth Date", "Civil Status", "Occupation", "Religion", "Education", "Contact", "Prev Address", "Curr Address", "Household Members", "Voter Info"};
                        String[] columnsR = {"resident_id", "valid_id", "full_name", "birth_date", "civil_status", "occupation", "religion", "education", "contact_info", "prev_address", "curr_address", "household_members", "voter_info"};
                        conf.viewRecords(sqlR, headersR, columnsR);
                        System.out.print("Enter ID of resident to update: ");
                        int updateId = sc.nextInt();
                        sc.nextLine();
                        System.out.println("Which field do you want to update?");
                        System.out.println("1. Full Name");
                        System.out.println("2. Birth Date");
                        System.out.println("3. Civil Status");
                        System.out.println("4. Occupation");
                        System.out.println("5. Contact Info");
                        System.out.println("6. Valid ID");
                        System.out.println("7. Religion");
                        System.out.println("8. Education");
                        System.out.println("9. Prev-Address");
                        System.out.println("10. Curr-Address");
                        System.out.println("11. HouseHold Mem.");
                        System.out.println("12. Voter Info.");
                        System.out.print("Enter choice: ");
                        int fieldChoice = sc.nextInt();
                        sc.nextLine();
                        String sqlUpdateR = "";
                        Object newValueR = null;
                        switch (fieldChoice) {
                            case 1: System.out.print("Enter new Full Name: "); newValueR = sc.nextLine(); sqlUpdateR = "UPDATE tbl_residents SET full_name = ? WHERE resident_id = ?"; break;
                            case 2: System.out.print("Enter new Birth Date (YYYY-MM-DD): "); newValueR = sc.nextLine(); sqlUpdateR = "UPDATE tbl_residents SET birth_date = ? WHERE resident_id = ?"; break;
                            case 3: System.out.print("Enter new Civil Status: "); newValueR = sc.nextLine(); sqlUpdateR = "UPDATE tbl_residents SET civil_status = ? WHERE resident_id = ?"; break;
                            case 4: System.out.print("Enter new Occupation: "); newValueR = sc.nextLine(); sqlUpdateR = "UPDATE tbl_residents SET occupation = ? WHERE resident_id = ?"; break;
                            case 5: System.out.print("Enter new Contact Info: "); newValueR = sc.nextLine(); sqlUpdateR = "UPDATE tbl_residents SET contact_info = ? WHERE resident_id = ?"; break;
                            case 6: System.out.print("Enter new Valid ID: "); newValueR = sc.nextLine(); sqlUpdateR = "UPDATE tbl_residents SET valid_id = ? WHERE resident_id = ?"; break;
                            case 7: System.out.print("Enter new Religion: "); newValueR = sc.nextLine(); sqlUpdateR = "UPDATE tbl_residents SET religion = ? WHERE resident_id = ?"; break;
                            case 8: System.out.print("Enter new Education: "); newValueR = sc.nextLine(); sqlUpdateR = "UPDATE tbl_residents SET education = ? WHERE resident_id = ?"; break;
                            case 9: System.out.print("Enter new Prev-Address: "); newValueR = sc.nextLine(); sqlUpdateR = "UPDATE tbl_residents SET prev_address = ? WHERE resident_id = ?"; break;
                            case 10: System.out.print("Enter new Curr-Address: "); newValueR = sc.nextLine(); sqlUpdateR = "UPDATE tbl_residents SET curr_address = ? WHERE resident_id = ?"; break;
                            case 11: System.out.print("Enter new Household Mem.: "); newValueR = sc.nextLine(); sqlUpdateR = "UPDATE tbl_residents SET  household_members= ? WHERE resident_id = ?"; break;
                            case 12: System.out.print("Enter new Voters Info.: "); newValueR = sc.nextLine(); sqlUpdateR = "UPDATE tbl_residents SET voter_info = ? WHERE resident_id = ?"; break;
                            default: System.out.println("Invalid choice!"); break;
                        }
                        if (!sqlUpdateR.isEmpty()) {
                            conf.updateRecord(sqlUpdateR, newValueR, updateId);
                            System.out.println("✅ Resident record updated successfully!");
                        }
                        break;
                    }
                    default:
                        System.out.println("Invalid choice!");
                        break;
                }
                break;

            // ===== DELETE RECORD =====
            case 3:
                System.out.println("\nDelete from which records?");
                System.out.println("1. Purok Records");
                System.out.println("2. Resident Records");
                System.out.print("Enter choice: ");
                int deleteChoice = sc.nextInt();
                sc.nextLine();

                switch (deleteChoice) {
                    case 1: {
                        String sqlD = "SELECT * FROM tbl_purok";
                        String[] headersD = {"ID", "Name", "Address", "Email", "Phone No."};
                        String[] columnsD = {"purok_id", "purok_name", "purok_address", "purok_email", "purok_pn"};
                        conf.viewRecords(sqlD, headersD, columnsD);
                        System.out.print("Enter ID of record to delete: ");
                        int deleteId = sc.nextInt();
                        String deleteSql = "DELETE FROM tbl_purok WHERE purok_id = ?";
                        conf.deleteRecord(deleteSql, deleteId);
                        System.out.println("✅ Purok record deleted!");
                        break;
                    }
                    case 2: {
                        String sqlD = "SELECT * FROM tbl_residents";
                        String[] headersD = {"ID", "Valid ID", "Full Name", "Birth Date", "Civil Status", "Occupation", "Religion", "Education", "Contact", "Prev Address", "Curr Address", "Household Members", "Voter Info"};
                        String[] columnsD = {"resident_id", "valid_id", "full_name", "birth_date", "civil_status", "occupation", "religion", "education", "contact_info", "prev_address", "curr_address", "household_members", "voter_info"};
                        conf.viewRecords(sqlD, headersD, columnsD);
                        System.out.print("Enter ID of resident to delete: ");
                        int deleteId = sc.nextInt();
                        String deleteSql = "DELETE FROM tbl_residents WHERE resident_id = ?";
                        conf.deleteRecord(deleteSql, deleteId);
                        System.out.println("✅ Resident record deleted!");
                        break;
                    }
                    default:
                        System.out.println("Invalid choice!");
                        break;
                }
                break;

            case 4:
                System.out.println("Returning to Main Menu...");
                break;

            default:
                System.out.println("Invalid choice!");
        }
    } while (adminAction != 4);
}


}
