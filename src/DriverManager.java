import java.util.ArrayList;


public class DriverManager {


    private static ArrayList<Driver> drivers =
            new ArrayList<>();



    static {


        drivers.add(
                new Driver(
                        1,
                        "Ahmed",
                        "99111111"
                )
        );


        drivers.add(
                new Driver(
                        2,
                        "Mohammed",
                        "99222222"
                )
        );


        drivers.add(
                new Driver(
                        3,
                        "Khalid",
                        "99333333"
                )
        );


    }







    public static ArrayList<Driver> getDrivers() {

        return drivers;

    }







    public static void addDriver(
            Driver driver
    ){

        drivers.add(driver);

    }







    public static void deleteDriver(
            int id
    ){


        drivers.removeIf(
                d -> d.getId() == id
        );


    }





    public static Driver findDriver(
            int id
    ){

        for(Driver d : drivers){

            if(d.getId()==id){

                return d;

            }

        }


        return null;

    }



}