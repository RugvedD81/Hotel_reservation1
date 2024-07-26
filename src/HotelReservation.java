import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;


public class HotelReservation {

    public static final String url="jdbc:mysql://127.0.0.1:3306/hotel_db";
    private static final String username="root";
    private static final String password="8767780674";

    public static void main(String[] args) throws ClassNotFoundException,SQLException{
    try{
        Class.forName("com.mysql.cj.jdbc.driver");

    }catch(ClassNotFoundException e){
        System.out.println(e.getMessage());
    }
        try{
            Connection connection= DriverManager.getConnection(url,username,password);
        while(true){
            System.out.println();
            System.out.println("Hotel Reservation System");
            Scanner sc=new Scanner(System.in);
            System.out.println("1. Reserve a room");
            System.out.println("2.View Reservation");
            System.out.println("3.Get a Room NUmber");
            System.out.println("4,Update Reservation");
            System.out.println("5.Delete Reservation");
            System.out.println("0.Exit");
            System.out.println("Enter yur choice");
            int choice=sc.nextInt();
            switch (choice){
                case 1:
                    reserveRoom(connection, sc);
                    break;
                case 2:
                    viewReservations(connection);
                    break;
                case 3:
                    getRoomNumber(connection, sc);
                    break;
                case 4:
                    updateReservation(connection, sc);
                    break;
                case 5:
                    deleteReservaton(connection, sc);
                    break;
                case 0:
                    exit();
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid choice, Try again...");


            }

        }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }catch(InterruptedException e){
            throw new RuntimeException (e);
        }


    }

    private static void deleteReservaton(Connection connection, Scanner sc) {
    }


    public static void reserveRoom(Connection connection,Scanner sc) {

        try {
            System.out.println("Enter guest name: ");
            String guestName = sc.next();
            System.out.println("Enter room number: ");
            int roomNumber = sc.nextInt();
            System.out.println("Enter contact number: ");
            int contactNumber = sc.nextInt();

            String sql = "INSERT INTO reservations(guest_name,room_number,contact_number)" +
                    "VALUES (" + guestName + ", " + roomNumber + " ," + contactNumber + ")";

            try (Statement statement = connection.createStatement()) {
                int affectedRows = statement.executeUpdate(sql);

                if (affectedRows > 0) {
                    System.out.println("Reservation Successfull: ");
                } else {
                    System.out.println(" Reservation Not Successfull:");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

        private  static void viewReservations(Connection connection) throws SQLException {
            String sql="SELECT reservation_id,guest_name, room_number,contact_number, reservation_date FROM reservations";

            try(Statement statement= connection.createStatement();
                ResultSet resultSet=statement.executeQuery(sql)){

                System.out.println("Current reservatons: ");


                while(resultSet.next()){
                    int reservationId= resultSet.getInt("reservation_id");
                    String guestName=resultSet.getString("guest_name");
                    int roomNumber=resultSet.getInt("room_number");
                    String cotactNumber=resultSet.getString("contact_number");
                    String reservationDate=resultSet.getTimestamp("reservation_date").toString();


                }
                System.out.println("  ");

            }

        }
        private static void getRoomNumber(Connection connection , Scanner sc){
         try{
             System.out.println("Enter reservation_Id: ");
             int reservationId=sc.nextInt();
             System.out.println("Enter guest name: ");
             String guestName=sc.next();

             String sql=" SELECT room_number FROM reservations "+
                     " WHERE reservationId="+reservationId+"AND guest_name="+guestName+" ";

             try (Statement statement = connection.createStatement();
                  ResultSet resultSet = statement.executeQuery(sql))
             {
                 if (resultSet.next()) {
                     int roomNumber = resultSet.getInt("room_number");
                     System.out.println("Room number for Reservation ID " + reservationId +
                             " and Guest " + guestName + " is: " + roomNumber);
                 } else {
                     System.out.println("Reservation not found for the given ID and guest name.");
                 }
             }
         } catch (SQLException e) {
             e.printStackTrace();
         }
        }
    private static void updateReservation(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter reservation ID to update: ");
            int reservationId = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            if (!reservationExists(connection, reservationId)) {
                System.out.println("Reservation not found for the given ID.");
                return;
            }

            System.out.print("Enter new guest name: ");
            String newGuestName = scanner.nextLine();
            System.out.print("Enter new room number: ");
            int newRoomNumber = scanner.nextInt();
            System.out.print("Enter new contact number: ");
            String newContactNumber = scanner.next();

            String sql = "UPDATE reservations SET guest_name = '" + newGuestName + "', " +
                    "room_number = " + newRoomNumber + ", " +
                    "contact_number = '" + newContactNumber + "' " +
                    "WHERE reservation_id = " + reservationId;

            try (Statement statement = connection.createStatement()) {
                int affectedRows = statement.executeUpdate(sql);

                if (affectedRows > 0) {
                    System.out.println("Reservation updated successfully!");
                } else {
                    System.out.println("Reservation update failed.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteReservation(Connection connection, Scanner sc) {
        try {
            System.out.print("Enter reservation ID to delete: ");
            int reservationId = sc.nextInt();

            if (!reservationExists(connection, reservationId)) {
                System.out.println("Reservation not found for the given ID.");
                return;
            }

            String sql = "DELETE FROM reservations WHERE reservation_id = " + reservationId;

            try (Statement statement = connection.createStatement()) {
                int affectedRows = statement.executeUpdate(sql);

                if (affectedRows > 0) {
                    System.out.println("Reservation deleted successfully!");
                } else {
                    System.out.println("Reservation deletion failed.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean reservationExists(Connection connection, int reservationId) {
        try {
            String sql = "SELECT reservation_id FROM reservations WHERE reservation_id = " + reservationId;

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                return resultSet.next(); // If there's a result, the reservation exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Handle database errors as needed
        }
    }


    public static void exit() throws InterruptedException {
        System.out.print("Exiting System");
        int i = 5;
        while(i!=0){
            System.out.print(".");
            Thread.sleep(1000);
            i--;
        }
        System.out.println();
        System.out.println("ThankYou For Using Hotel Reservation System!!!");
    }
}






