import java.sql.Connection;
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
    private List<String> päringus = new ArrayList<>();
    private PreparedStatement andmed;


    public Andmebaas(Connection connection) throws SQLException{ //konstruktoris loome ühenduse andmebaasiga
        this.connection = connection;
    }

    public void käivitaPäring(String päring) throws SQLException{

        int küsimärke = 0;
        int viimane = 0;
        while(viimane != -1){
            viimane = päring.indexOf("?", viimane);
            if (viimane != -1){
                küsimärke ++;
                viimane += 1;
            }
        }
        andmed = connection.prepareStatement(päring);
        for (int i = 0; i < küsimärke; i++){
            andmed.setString(i+1, päringus.get(i));
        }

        tulemus = andmed.executeQuery();
        päringus.clear();
    }

    public void korduv(String päring, boolean reas) throws SQLException{

        käivitaPäring(päring);

        vastus = "";
        while (tulemus.next()) {
            if (reas) {
                    for (String el : elemendid) {

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
        andmed.close();

    }

    public String sqlÕpilaseAndmed(String nimi) throws SQLException{
        int indeks = nimi.lastIndexOf(" "); //kõik enne viimast tühikut on eesnimi
        String eesnimi = nimi.substring(0, indeks);
        String perenimi = nimi.substring(indeks+1, nimi.length());
        String[] jupid = nimi.trim().split(" ");
        if (jupid.length < 2){
            Hoiatus.aken("Nimi on vigane!");
        }
        String päring = "select * from Õpilased where eesnimi = ? and perenimi = ?";
        Collections.addAll(päringus, eesnimi, perenimi);
        Collections.addAll(elemendid, "Eesnimi", "Perenimi", "Aadress", "Telefon", "E-Mail", "isikukood", "Sünnikuupäev");

        korduv(päring, true);
        return vastus;
    }

    public String sqlRühmaAndmed(String nimi) throws SQLException{
        String päring = "select Rühmad.nimetus, Õpilased.eesnimi, Õpilased.perenimi from Õpilased, Rühmad, " +
                "On_rühmas where On_rühmas.rühm_id = Rühmad.ID and On_rühmas.Õpilane_ID = Õpilased.ID " +
                "and rühmad.nimetus = ?";
        Collections.addAll(päringus, nimi);
        Collections.addAll(elemendid, "Eesnimi", "Perenimi");
        korduv(päring, false);
        return vastus;
    }

    public String sqlTrennisOsalejad(String trenn, String rühm) throws SQLException{
        String päring = "SELECT Distinct Õpilased.Eesnimi, Õpilased.Perenimi \n" +
                "from Kohalolu, Õpilased, Trennid, Rühmad, On_rühmas\n" +
                "where Õpilased.id = Kohalolu.Õpilane_ID \n" +
                "and Trennid.ID = Kohalolu.Trenn_ID \n" +
                "and Trennid.Rühm_ID = On_rühmas.Rühm_ID\n" +
                "and On_rühmas.Rühm_ID = Rühmad.ID\n" +
                "and date(Trennid.Toimumisaeg) = date(?) \n" +
                "and Rühmad.Nimetus = ?";

        Collections.addAll(päringus, trenn, rühm);
        Collections.addAll(elemendid, "Eesnimi", "Perenimi");
        korduv(päring, false);
        return vastus;
    }

    public String sqlSaavutused(String aasta) throws SQLException{
        String päring = "select nimi, aeg, asukoht, saavutatud_tulemus as Tulemus, nimetus as Rühm\n" +
                "from võistlused, võistleb, rühmad \n" +
                "where võistleb.rühm_id = rühmad.id and võistleb.võistlus_id = võistlused.id\n" +
                "and year(võistlused.aeg) = ?";
        Collections.addAll(päringus, aasta);
        Collections.addAll(elemendid, "Nimi", "Asukoht", "Aeg", "Rühm", "Tulemus");
        korduv(päring, true);
        return vastus;
    }

    public List<String> sqlRühmad()throws SQLException{ //comboboxis (aktiivsus) need rühmad, mis andmebaasis
        String päring = "select nimetus from rühmad";
        List<String> vastused = new ArrayList<>();
        PreparedStatement TrennisOsalejad = connection.prepareStatement(päring);
        ResultSet tulemus = TrennisOsalejad.executeQuery();
        while (tulemus.next()){
            vastused.add(tulemus.getString("Nimetus"));
        }
        tulemus.close();
        TrennisOsalejad.close();
        return vastused;
    }

    public List<String> sqlAastad()throws SQLException { //comboboxis (saavutused) need aastad, mis andmebaasis
        String päring = "select distinct year(aeg) as Aasta from võistlused";
        List<String> vastused = new ArrayList<>();
        PreparedStatement VõistlusteAastad = connection.prepareStatement(päring);
        ResultSet tulemus = VõistlusteAastad.executeQuery();
        while (tulemus.next()) {
            vastused.add(tulemus.getString("Aasta"));
        }
        tulemus.close();
        VõistlusteAastad.close();
        return vastused;
    }

    public String sqlAktiivsus(String rühm) throws SQLException{
        String päring = "select eesnimi + ' ' + perenimi as Nimi, count() as Trenne from Õpilased, Kohalolu, Trennid, Rühmad\n" +
                "where Õpilased.ID = Kohalolu.Õpilane_ID and Kohalolu.Trenn_ID = Trennid.ID\n" +
                "and Trennid.Rühm_ID = Rühmad.ID and Rühmad.Nimetus = ? group by eesnimi, perenimi order by Trenne desc";
        Collections.addAll(päringus, rühm);
        Collections.addAll(elemendid, "Nimi", "Trenne");
        korduv(päring, true);
        return vastus;
    }
    public void sqlLisaTrenn(String juhendaja, String rühm, String koht, String aeg) throws SQLException{
        String[] juh = juhendaja.split(" ");
        String päring = "insert into Trennid (Rühm_ID, Asukoht, Toimumisaeg, Juhendaja_ID)\n" +
                "values (f_rühmId(?), ?, datetime(?), f_juhendajaId(? , ?))";
        Collections.addAll(päringus, rühm, koht, aeg, juh[0], juh[1]);
        käivitaPäring(päring);
        andmed.close();
    }

    public void sqlLisaKohalolu(String nimi, String aeg, String rühm) throws SQLException{
        int indeks = nimi.lastIndexOf(" ");
        String eesnimi = nimi.substring(0, indeks);
        String perenimi = nimi.substring(indeks+1, nimi.length());
        String päring = "insert into Kohalolu (Õpilane_ID, Trenn_ID)\n" +
                "values (f_õpilaneID(?, ?), f_trennID(f_rühmId(?), datetime(?)))";
        Collections.addAll(päringus, eesnimi, perenimi, rühm, aeg);
        PreparedStatement lisa = connection.prepareStatement(päring);
        käivitaPäring(päring);
        andmed.close();

    }

    public void sqlLisaVõistlus(String koht, String aeg, String nimi) throws SQLException{
        String päring = "INSERT INTO Võistlused (Asukoht, Aeg, Nimi)\n" +
                "Values (? , date(?), ?)";
        Collections.addAll(päringus, koht, aeg, nimi);
        käivitaPäring(päring);
        andmed.close();
        }
    public void sqlLisaTulemus(String nimi, String rühm, String tulemus) throws SQLException{
        String päring = "INSERT INTO Võistleb (Rühm_ID, Võistlus_ID, Saavutatud_tulemus)\n" +
                "Values (f_rühmId(?), f_VõistlusId(?), ?)";
        Collections.addAll(päringus, rühm, nimi, tulemus);
        käivitaPäring(päring);
        andmed.close();

    }
    public void sqlLisaÕpilane(String nimi, String aadress, String isikukood, String email, String telefon, String vanem) throws SQLException{
        int indeks = nimi.lastIndexOf(" ");
        String eesnimi = nimi.substring(0, indeks);
        String perenimi = nimi.substring(indeks+1, nimi.length());

        int indeksLV = vanem.lastIndexOf(" ");
        String eesnimiLV = vanem.substring(0, indeksLV);
        String perenimiLV = vanem.substring(indeksLV+1, vanem.length());

        String aasta;
        if (isikukood.substring(0, 1).equals("5")||isikukood.substring(0, 1).equals("6")){
            aasta = "20";
        }
        else aasta = "19";

        String kuupäev = aasta + isikukood.substring(1, 3) + "-" + isikukood.substring(3, 5) + "-" + isikukood.substring(5,7);
        String päring = "insert into Õpilased (Lapsevanem_ID, Eesnimi, Perenimi, Aadress, Telefon, \"E-Mail\", Isikukood, Sünnikuupäev)\n" +
                "values (f_vanemId(?, ?), ?, ?, ?, cast(? as int), ?, ?, date(?))";
        Collections.addAll(päringus, eesnimiLV, perenimiLV, eesnimi, perenimi, aadress, telefon, email, isikukood, kuupäev);
        käivitaPäring(päring);
        andmed.close();
    }
    public void sqlLisaVanem(String nimi, String aadress, String kommentaar, String email, String telefon) throws SQLException{
        int indeks = nimi.lastIndexOf(" ");
        String eesnimi = nimi.substring(0, indeks);
        String perenimi = nimi.substring(indeks+1, nimi.length());

        String päring = "insert into Lapsevanemad (Eesnimi, Perenimi, Aadress, Telefon, \"E-Mail\", Kommentaar)\n" +
                "values (?, ?, ?, cast(? as int), ?, ?)";
        Collections.addAll(päringus, eesnimi, perenimi, aadress, telefon, email, kommentaar);
        käivitaPäring(päring);
        andmed.close();
    }
}
