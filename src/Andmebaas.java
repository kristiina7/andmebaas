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
                    vastus = vastus + "\n"; //et näiteks erinevate võistluste vahele tuleks tühi rida
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
        return vastus;
    }

    public String sqlRühmaAndmed(String nimi) throws SQLException{
        String päring = "select Rühmad.nimetus, Õpilased.eesnimi, Õpilased.perenimi from Õpilased, Rühmad, " +
                "On_rühmas where On_rühmas.rühm_id = Rühmad.ID and On_rühmas.Õpilane_ID = Õpilased.ID " +
                "and rühmad.nimetus = '" + nimi + "'";
        Collections.addAll(elemendid, "Eesnimi", "Perenimi");
        korduv(päring, elemendid, false);
        return vastus;
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

        Collections.addAll(elemendid, "Eesnimi", "Perenimi");
        korduv(päring, elemendid, false);
        return vastus;
    }

    public String sqlSaavutused(int aasta) throws SQLException{
        String päring = "select nimi, aeg, asukoht, saavutatud_tulemus as Tulemus, nimetus as Rühm\n" +
                "from võistlused, võistleb, rühmad \n" +
                "where võistleb.rühm_id = rühmad.id and võistleb.võistlus_id = võistlused.id\n" +
                "and year(võistlused.aeg) = " + aasta;
        Collections.addAll(elemendid, "Nimi", "Asukoht", "Aeg", "Rühm", "Tulemus");
        korduv(päring, elemendid, true);
        return vastus;
    }

    public List<String> sqlRühmad()throws SQLException{
        String päring = "select nimetus from rühmad";
        List<String> vastused = new ArrayList<>();
        PreparedStatement TrennisOsalejad = connection.prepareStatement(päring);
        ResultSet tulemus = TrennisOsalejad.executeQuery();
        while (tulemus.next()){
            vastused.add(tulemus.getString("Nimetus"));
        }
        return vastused;
    }

    public String sqlAktiivsus(String rühm) throws SQLException{
        String päring = "select eesnimi + ' ' + perenimi as Nimi, count() as Trenne from Õpilased, Kohalolu, Trennid, Rühmad\n" +
                "where Õpilased.ID = Kohalolu.Õpilane_ID and Kohalolu.Trenn_ID = Trennid.ID\n" +
                "and Trennid.Rühm_ID = Rühmad.ID and Rühmad.Nimetus = '" + rühm + "' group by eesnimi, perenimi order by nimi asc";
        Collections.addAll(elemendid, "Nimi", "Trenne");
        korduv(päring, elemendid, true);
        return vastus;
    }
}
