# Nutikas Restorani Reserveerimissüsteem

Laudade broneerimise ja sobiva laua soovitamise veebirakendus.

---

## Projekti ülevaatus

Süsteem haldab nelja peamist olemit, mille vahel on Many-to-many seosed.

| Table               | Fields                                                                                                                         |
|:--------------------|:-------------------------------------------------------------------------------------------------------------------------------|
| **Area**            | `id` (PK), `name` (Varchar)                                                                                                    |
| **Booking**         | `id` (PK), `restaurantTable` (RestaurantTable), `client` (Client), `date` (Varchar), `time` (Varchar), `tableIsFree` (boolean) |
| **Client**          | `id` (PK), `name` (Varchar), `phoneNumber` (Varchar)                                                                           |
| **Preferences**     | `id` (PK), `preference` (Varchar)                                                                                              |
| **RestaurantTable** | `id` (PK), `size` (Int), `area` (Area), `preferences` (Preference)                                                             |

- **RestaurantTable** saab olla üks **Area**.
- **RestaurantTable** saab olla mitu **Preferences**.
- Igal Cliendil saab olla mitu **Booking**.

*Note: Kõik Id-sid on unikaalsed ja autogenereeritud.*

### Põhifunktsioonid
- **Error Handling:** Globaalne erindite haldus puuduvate andmete puhul.

---

## Project structure:
```text
    demo/
    ├── .idea/
    ├── .mvn/
    └── src/
        └── main/
            ├── java/
            │   └── com.example.demo/
            │       ├── controller/
            │       │   ├── AreaController.java
            │       │   ├── BookingController.java
            │       │   ├── ClientController.java
            │       │   ├── PreferenceController.java
            │       │   └── TableController.java
            │       ├── exceptions/
            │       │   ├── GlobalExceptionHandler.java
            │       │   └── ResourceNotFoundException.java
            │       ├── repository/
            │       │   ├── Area.java
            │       │   ├── AreaRepository.java
            │       │   ├── Booking.java
            │       │   ├── BookingRepository.java
            │       │   ├── Client.java
            │       │   ├── ClientRepository.java
            │       │   ├── Preference.java
            │       │   ├── PreferenceRepository.java
            │       │   ├── RestaurantTable.java
            │       │   └── TableRepository.java
            │       ├── service/
            │       │   ├── AreaService.java
            │       │   ├── BookingService.java
            │       │   ├── ClientService.java
            │       │   ├── PreferenceService.java
            │       │   └── TableService.java
            │       └── RestaurantApplication.java
            └── resources/
                ├── static/
                │   ├── css/
                │   │   └── style.css
                │   ├── js/
                │   │   └── main.css
                │   └── index.html
                ├── templates/
                │   └── map.html
                └── application.properties
    
```

---

## Seadistamine ja paigaldamine

### Eeltingimused
- **Java JDK 17** või uuem.
- **Maven**
- **IntellIJ IDEA**.

### Paigladamine

1. **Klooni repositoorium:**
   ```bash
   git clone <https://github.com/denissmirno8-blip/restaurantSystem>
   ```

2. **Seadista andmebaasi asukoht:**
   Ava fail `src/main/resources/application.properties` ja määra oma kohaliku andmebaasifaili absoluutne tee:
    ```bash
    spring.datasource.url=jdbc:sqlite:C:/path/to/your/restaurant.db
   ```
3. **Käivita rakendus:**
    ```bash
    ./mvnw spring-boot:run   
   ```

---


### Andmebaasi seadistamine (SQLite)
Rakendus on seadistatud kasutama **SQLite** mis on kergepõhine andmebaas. Veendu, et failis `src/main/resources/application.properties` on määratud järgmised parameetrid:

- `spring.datasource.url`: Sinu kohaliku andmebaasifaili asukoht (n.t., `jdbc:sqlite:C:/path/to/restaurant.db`).
- `spring.datasource.driver-class-name`: Peab olema `org.sqlite.JDBC`.
- `spring.jpa.database-platform`: Peab olema `org.hibernate.community.dialect.SQLiteDialect`.

*Note: Since SQLite is a file-based engine, `username` and `password` fields are not required.*

### Broneerimisprotsess:

1.  **Otsingu alustamine:**
    Pealehel on kohustuslik valida **kuupäev**, **kellaaeg** ja **inimeste arv**. Pärast andmete sisestamist vajuta nupule **"Find Tables"** (Otsi laudu).

2.  **Laua valimine ja filtreerimine:**
    Uuel lehel saad valida sobiva laua piirkonna ja määrata täiendavad **eelistused**. Filtrite all kuvatakse nimekiri vabadest laudadest:
    *   **Roheline värv:** Kõige sobivamad lauad vastavalt sinu valikutele.
    *   **Punane värv:** Lauad, mis ei sobi seltskonnale oma suuruse tõttu.

3.  **Registreerimine:**
    Broneeringu vormistamiseks sisesta oma **nimi** ja **telefoninumber** ning vajuta nupule **"Register"** (Registreeri).

4.  **Kinnitus:**
    Kui kõik andmed on õiged, kuvatakse ekraanil teade edukast broneeringust.

> **Märkus:** Kui mõni väli on täitmata või sisaldab vigu, kuvatakse andmete saatmisel vastav selgitav veateade.
---
## Töökäik 

Alguses koostasin andmebaasi struktuuri: määrasin põhitabelid ja nende tulbad. Pärast andmebaasi disainimist lõin standardse Spring Boot rakenduse, kus defineerisin klassid ja peamised CRUD-meetodid, võttes eeskujuks oma varasema movies-api projekti (mis koosnes vaid back-end osast). Lisaks visandasin filtrite loogika ja restorani plaani paigutuse.

Testisin CRUD-meetodeid Postmanis ja parandasin ilmnenud vead, seejärel initsialiseerisin GitHubi repositooriumi.

Front-end osas lisasin esmased filtrid ja registreerimisvormi (nimi + telefon) ning valisin sobivad sisendtüübid (inputid). Kontrollisin üle fetch-meetodid ja alustasin laudade paigutusega. Määrasin kindlaks, millised andmed lähevad broneeringute (booking) tabelisse ja kuidas neid kuvada. Interaktiivse tabeli ja "checkbox as tiles" lahenduse loomisel kasutasin abiinfona seda YouTube'i videot.

Jaotasin projekti kaheks leheküljeks:

- Esimesel lehel on üldised filtrid, mida kasutaja tihti ei muuda.
- Teisel lehel on detailsemad filtrid võrdlemiseks ja tabelivaade.
Seoses sellega lisasin vajalikud muudatused ka kontrolleris.

- Lisasin laudade värvimise loogika, seadistasin registreerimise ja filtreerimise ning lisasin lihtsad teavitused (alerts). Pärast edukat broneerimist suunatakse kasutaja tagasi pealehele.

Edasiarendused ja õppetunnid:

- Vaja on lisada Regex kontroll nimele ja telefoninumbrile, et välistada sobimatud sümbolid.
- Lisada legend, mis selgitab laudade värvide tähendust.
- Töö kestis kokku umbes 20 tundi.

Järgmisel korral optimeeriksin back-end'i: alguses kirjutasin liiga palju erinevaid mapping-meetodeid, kuigi piisanuks vaid GET ja POST päringutest. Säästetud aja kulutaksin front-end'ile, et proovida rohkem paigutusvariante ja luua dünaamilisemat kasutajaliidest.

---
## Author

Denis Smirnov