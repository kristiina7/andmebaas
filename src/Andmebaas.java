import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Andmebaas{
    private Connection connection;
    private ResultSet tulemus;
    private String vastus = "";
    private List<String> elemendid= new ArrayList<>();


    public Andmebaas(Connection connection) throws SQLException{ //konstruktoris loome ühenduse andmebaasiga
        this.connection = connection;
    }
    public void korduv(String päring, List<String> kuvamine, boolean reas) throws SQLException{
        PreparedStatement Andmed = connection.prepareStatement(päring);
        tulemus = Andmed.executeQuery();
        vastus = "";
        while (tulemus.next()) {
            if (reas) {
                for (String el : kuvamine) {
                    vastus = vastus + el + ": " + tulemus.getString(el) + "\n";
                }
            }
            else{
                    vastus = vastus + tulemus.getString("Eesnimi") + " " + tulemus.getString("Perenimi") + "\n";
            }
        }
        elemendid.clear();
        tulemus.close();
        Andmed.close();

    }

    public String sqlÕpilaseAndmed(String nimi) throws SQLException{
        String[] jupid = nimi.trim().split(" ");
        String päring = "select * from Õpilased where eesnimi = '" + jupid[0] + "' and perenimi = '" + jupid[1] + "'";
        Collections.addAll(elemendid, "Eesnimi", "Perenimi", "Aadress", "Telefon", "E-Mail", "isikukood", "Sünnikuupäev");

        korduv(päring, elemendid, true);
/*
        PreparedStatement õpilaseAndmed = connection.prepareStatement(päring);
        tulemus = õpilaseAndmed.executeQuery();
        vastus = "";
        while (tulemus.next()) {
                vastus = vastus + "Eesnimi" + tulemus.getString("Eesnimi") + "\n";
            }

        tulemus.close();
        õpilaseAndmed.close();

  */      return vastus;
    }

    public String sqlRühmaAndmed(String nimi) throws SQLException{
        String päring = "select Rühmad.nimetus, Õpilased.eesnimi, Õpilased.perenimi from Õpilased, Rühmad, " +
                "On_rühmas where On_rühmas.rühm_id = Rühmad.ID and On_rühmas.Õpilane_ID = Õpilased.ID " +
                "and rühmad.nimetus = '" + nimi + "'";
        Collections.addAll(elemendid, "Eesnimi", "Perenimi");
        korduv(päring, elemendid, false);
        /*PreparedStatement rühmaAndmed = connection.prepareStatement(päring);
        ResultSet tulemus = rühmaAndmed.executeQuery();
        String vastus = "";
        while (tulemus.next()){
            vastus = vastus + tulemus.getString("Eesnimi") + " " + tulemus.getString("Perenimi") + "\n";
        }
        tulemus.close();
        rühmaAndmed.close();
        */return vastus;
    }

    public String sqlTrennisOsalejad(String trenn, String rühm) throws SQLException{
        String päring = "SELECT Distinct Õpilased.Eesnimi, Õpilased.Perenimi \n" +
                "from Kohalolu, Õpilased, Trennid, Rühmad, On_rühmas\n" +
                "where Õpilased.id = Kohalolu.Õpilane_ID \n" +
                "and Trennid.ID = Kohalolu.Trenn_ID \n" +
                "and Trennid.Rühm_ID = On_rühmas.Rühm_ID\n" +
                "and On_rühmas.Rühm_ID = Rühmad.ID\n" +
                "and Trennid.Toimumisaeg like '" +trenn +  "%'\n" +
                "and Rühmad.Nimetus = '" + rühm + "'";
        /*PreparedStatement TrennisOsalejad = connection.prepareStatement(päring);
        ResultSet tulemus = TrennisOsalejad.executeQuery();
        String vastus = "";
        while (tulemus.next()){
            vastus = vastus + tulemus.getString("Eesnimi") + " " + tulemus.getString("Perenimi") + "\n";
        }
        tulemus.close();
        TrennisOsalejad.close();
        */
        Collections.addAll(elemendid, "Eesnimi", "Perenimi");
        korduv(päring, elemendid, false);
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
