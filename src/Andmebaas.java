import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Andmebaas{
    private Connection connection;

    public Andmebaas(Connection connection) throws SQLException{ //konstruktoris loome ühenduse andmebaasiga
        this.connection = connection;
    }

    public String sqlÕpilaseAndmed(String nimi) throws SQLException{
        String[] jupid = nimi.trim().split(" ");
        String päring = "select * from Õpilased where eesnimi = '" + jupid[0] + "' and perenimi = '" + jupid[1] + "'";
        PreparedStatement õpilaseAndmed = connection.prepareStatement(päring);
        ResultSet tulemus = õpilaseAndmed.executeQuery();

        String vastus = "";
        while (tulemus.next()) {
                vastus = "Eesnimi: " + tulemus.getString("Eesnimi") + "\n" +
                    "Perenimi: " + tulemus.getString("Perenimi") + "\n" +
                    "Aadress: " + tulemus.getString("Aadress") + "\n" +
                    "Telefon: " + tulemus.getString("Telefon") + "\n" +
                    "E-mail: " + tulemus.getString("E-mail") + "\n" +
                    "Isikukood: " + tulemus.getString("Isikukood") + "\n" +
                    "Sünnikuupäev: " + tulemus.getString("Sünnikuupäev");
        }
        tulemus.close();
        õpilaseAndmed.close();
        return vastus;
    }

    public String sqlRühmaAndmed(String nimi) throws SQLException{
        String päring = "select Rühmad.nimetus, Õpilased.eesnimi, Õpilased.perenimi from Õpilased, Rühmad, " +
                "On_rühmas where On_rühmas.rühm_id = Rühmad.ID and On_rühmas.Õpilane_ID = Õpilased.ID " +
                "and rühmad.nimetus = '" + nimi + "'";
        PreparedStatement rühmaAndmed = connection.prepareStatement(päring);
        ResultSet tulemus = rühmaAndmed.executeQuery();
        String vastus = "";
        while (tulemus.next()){
            vastus = vastus + tulemus.getString("Eesnimi") + " " + tulemus.getString("Perenimi") + "\n";
        }
        tulemus.close();
        rühmaAndmed.close();
        return vastus;
    }


    //see on praegu ainult andmebaasi katsetamiseks, pärast kustutame ära
    public static void main(String[] args) throws SQLException {

        Connection connection = DriverManager.getConnection("jdbc:sqlanywhere:uid=OSP;pwd=sql;"
                + "Dbf=OSP.db");
        /*PreparedStatement õpilasteNimed = connection.prepareStatement("select Eesnimi, Perenimi from Õpilased");

        ResultSet tulemus = õpilasteNimed.executeQuery(); //päringu tulemus

        // Meetod ResultSet#next liigutab tulemuses järjehoidjat edasi.
        while (tulemus.next()) {
            // ResultSet'i meetodid getString, getInt jt. võimaldavad küsida
            // fookuses oleva kirje erinevaid komponente.
            System.out.println(tulemus.getString("Eesnimi") + " " + tulemus.getString("Perenimi"));
        }*/
        String nimi = "Veronika Lehesaar";
        String[] uus;
        uus = nimi.split(" ");
        System.out.println(uus[0] + uus[1]);

        PreparedStatement õpilaseAndmed = connection.prepareStatement("select * from Õpilased where eesnimi = '" + uus[0] +
                        "' and perenimi = '" + uus[1] + "'");

        ResultSet tulemus2 = õpilaseAndmed.executeQuery();

        while (tulemus2.next()){
            System.out.println(tulemus2.getString("Eesnimi")+ "\n" + tulemus2.getString("Perenimi") + "\n" +
            tulemus2.getString("Aadress") + "\n" + tulemus2.getString("Telefon") + "\n" + tulemus2.getString("E-mail") +
             "\n" + tulemus2.getString("Isikukood") + "\n" + tulemus2.getString("Sünnikuupäev"));
        }

        //tulemus.close();
        //õpilasteNimed.close();
        tulemus2.close();
        õpilaseAndmed.close();
        connection.close();
    }
}
