import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*siit kustutab pärast main meetodi ära, praegu lihtsalt selleks, et andmebaasi tööd kontrollida.
Tegelikult saaks kõik klassid: Aktiivsus, Saavutused, Lisamine, Otsing ka siia panna, sest tegelikult kasutame igast
klassist ainult ühte meetodit. Lihtsam on orienteeruda, kui on erinevas klassis, aga otseselt vajadust selleks pole.
*/
public class Andmebaas {
    public static void main(String[] args) throws SQLException {
        // Ühenduse attribuut Dbf viitab andmebaasi failile.
        // uid tähistab kasutajanime ja pwd parooli.
        Connection connection = DriverManager.getConnection("jdbc:sqlanywhere:uid=dba;pwd=sql;"
                + "Dbf=C:\\andmebaas\\ope.db");

        // PreparedStatement tüüpi objektid esitavad SQL lauset andmebaasi jaoks mugaval kujul
        PreparedStatement stmt = connection.prepareStatement("select Eesnimi, Perenimi from Isikud");

        // ResultSet tüüpi objektid esitavad päringu tulemusi
        ResultSet rs = stmt.executeQuery();

        // Meetod ResultSet#next liigutab tulemuses järjehoidjat edasi.
        while (rs.next()) {
            // ResultSet'i meetodid getString, getInt jt. võimaldavad küsida
            // fookuses oleva kirje erinevaid komponente.
            System.out.println(rs.getString("Eesnimi") + " " + rs.getString("Perenimi"));
        }

        // Peale töö lõpetamist on soovitav objektid sulgeda,
        // see vabastab teatavad ühenduse ja päringutega seotud andmebaasi ressursid.
        rs.close();
        stmt.close();
        connection.close();
    }
}
