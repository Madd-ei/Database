import java.sql.*;
import java.util.Scanner;

public class Main {

    private static Connection con;
    public static String SQL;
    public static void main(String[] args) {
        Thread server = new Thread(new Server(con));
        server.start();

        Scanner scn = new Scanner(System.in);
        con = null;

        String url = "jdbc:mysql://localhost:3306/Fitness_Tracker";
        String user = "root";
        String pass = "pass";
        String driver = "com.mysql.cj.jdbc.Driver";

        for (int i = 0; i < args.length; ++i) {
            System.out.println(args[i]);
        }

        if (args.length >= 4) {
            url = args[0];
            user = args[1];
            pass = args[2];
            driver = args[3];

            try {
                System.setProperty("jdbc.drivers", driver);
                Class.forName(driver);

                con = DriverManager.getConnection(url, user, pass);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("You did not provide the necessary inputs to complete your command.");
        }

        boolean quit = false;
        while (!quit) {
            System.out.println("**Menu**");
            System.out.println("1. Insert");
            System.out.println("2. Update");
            System.out.println("3. Delete");
            System.out.println("4. Get Users From Gym");
            System.out.println("5. Get Trainers From Gym");
            System.out.println("6. Get Fitness Activities Taught By Trainer");
            System.out.println("7. Get Gym's Fitness Activities");
            System.out.println("8. Get Fitness Activities Logged By User");
            System.out.println("9. QUIT");

            switch (scn.nextLine()) {
                case "1":
                    insert(con, scn);
                    break;
                case "2":
                    break;
                case "3":
                    delete(con, scn);
                    break;
                case "4":
                    break;
                case "5":
                    break;
                case "6":
                    break;
                case "7":
                    break;
                case "8":
                    break;
                case "9":
                    quit = true;
                    try {
                        con.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    scn.close();
                    break;
            }
        }
    }

    public static PreparedStatement getColumns(Statement tables, String ans) {
        String columns = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = 'fitness_tracker' AND TABLE_NAME = '";
        PreparedStatement col = null;

        try {
            tables.getResultSet().first();
            tables.getResultSet().absolute(Integer.parseInt(ans));
            SQL += tables.getResultSet().getString(1);

            columns += tables.getResultSet().getString(1) + "'";
            col = con.prepareStatement(columns, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            col.executeQuery();
            tables.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return col;

    }

    public static Statement getTables() {
        String tables = "SELECT table_name FROM information_schema.tables WHERE table_type = 'BASE TABLE' AND table_schema = 'fitness_tracker'";
        Statement t = null;

        try {
            t = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            t.execute(tables);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return t;
    }

    public static void makeTableSelection() {

    }

    public static void insert(Connection con, Scanner scn) {
        Statement t;

        SQL = "INSERT INTO ";


        try {
            t = getTables();

            int i = 1;
            System.out.println("Choose a Table: ");
            while (t.getResultSet().next()) {
                System.out.println(i++ + ". " + t.getResultSet().getString(1));
            }

            String ans = scn.nextLine();

            PreparedStatement col = getColumns(t, ans);

            System.out.println("You need to provide the following inputs:");
            while (col.getResultSet().next()) {
                System.out.println(col.getResultSet().getString(1));
            }
            System.out.println("Please use this format: ('v1', 'v2', 'v3', ...)");
            col.close();

            SQL += " VALUES " + scn.nextLine();
            PreparedStatement ins = con.prepareStatement(SQL);
            ins.executeUpdate();
            ins.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void update() {}

    public static void delete(Connection con, Scanner scn) {
        Statement t = null;
        PreparedStatement pk = null;
        String tables = "SELECT table_name FROM information_schema.tables WHERE table_type = 'BASE TABLE' AND table_schema = 'fitness_tracker'";
        String delete = "DELETE FROM ";
        String primary_key = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE WHERE TABLE_NAME = ";


        try {
            t = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            t.execute(tables);

            int i = 1;
            System.out.println("Choose a Table: ");
            while (t.getResultSet().next()) {
                System.out.println(i++ + ". " + t.getResultSet().getString(1));
            }

            String ans = scn.nextLine();
            t.getResultSet().first();
            t.getResultSet().absolute(Integer.parseInt(ans));
            PreparedStatement del = con.prepareStatement(delete);
            delete += t.getResultSet().getString(1);

            primary_key += "'" + t.getResultSet().getString(1) + "' AND CONSTRAINT_NAME = 'PRIMARY' AND TABLE_SCHEMA = 'fitness_tracker'";
            pk = con.prepareStatement(primary_key, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pk.executeQuery();
            t.close();

            System.out.println("You need to provide the following inputs:");
            String primaryk_name = null;
            while (pk.getResultSet().next()) {
                primaryk_name = pk.getResultSet().getString(1);
                System.out.println(primaryk_name);
            }
            System.out.println("Please use this format: 'k1'");
            pk.close();

            delete += " WHERE " + primaryk_name + " = " + scn.nextLine();
            del = con.prepareStatement(delete);
            del.executeUpdate();
            del.close();

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("**This function cannot be completed due to the following reason: "
                    + e.getMessage() + "**");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void get_users() {}

    public static void get_trainers() {}

    public static void get_fitness_trainer() {}

    public static void get_fitness_gym() {}

    public static void get_fitness_user() {}

}