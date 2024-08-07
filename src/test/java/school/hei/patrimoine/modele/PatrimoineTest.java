package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.*;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PatrimoineTest {

  @Test
  void patrimoine_vide_vaut_0() {
    var ilo = new Personne("Ilo");

    var patrimoineIloAu13mai24 = new Patrimoine(
        "patrimoineIloAu13mai24",
        ilo,
        LocalDate.of(2024, MAY, 13),
        Set.of(),
            null
    );

    assertEquals(0, patrimoineIloAu13mai24.getValeurComptable());
  }

  @Test
  void patrimoine_a_de_l_argent() {
    var ilo = new Personne("Ilo");

    var au13mai24 = LocalDate.of(2024, MAY, 13);
    var patrimoineIloAu13mai24 = new Patrimoine(
        "patrimoineIloAu13mai24",
        ilo,
        au13mai24,
        Set.of(
            new Argent("Espèces", au13mai24, 400_000),
            new Argent("Compte epargne", au13mai24, 200_000),
            new Argent("Compte courant", au13mai24, 600_000)),
            null);

    assertEquals(1_200_000, patrimoineIloAu13mai24.getValeurComptable());
  }

  @Test
  void patrimoine_possede_un_train_de_vie_financé_par_argent() {
    var ilo = new Personne("Ilo");
    var au13mai24 = LocalDate.of(2024, MAY, 13);
    var financeur = new Argent("Espèces", au13mai24, 600_000);
    var trainDeVie = new FluxArgent(
        "Vie courante",
        financeur, au13mai24.minusDays(100), au13mai24.plusDays(100), -100_000,
        15);

    var patrimoineIloAu13mai24 = new Patrimoine(
        "patrimoineIloAu13mai24",
        ilo,
        au13mai24,
        Set.of(financeur, trainDeVie), null);

    assertEquals(500_000, patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(10)).getValeurComptable());
    assertEquals(200_000, patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(100)).getValeurComptable());
    assertEquals(200_000, patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(1_000)).getValeurComptable());
  }

  @Test
  void patrimoine_possede_groupe_de_train_de_vie_et_d_argent() {
    var ilo = new Personne("Ilo");
    var au13mai24 = LocalDate.of(2024, MAY, 13);
    var financeur = new Argent("Espèces", au13mai24, 600_000);
    var trainDeVie = new FluxArgent(
        "Vie courante",
        financeur, au13mai24.minusDays(100), au13mai24.plusDays(100), -100_000,
        15);

    var patrimoineIloAu13mai24 = new Patrimoine(
        "patrimoineIloAu13mai24",
        ilo,
        au13mai24,
        Set.of(new GroupePossession("Le groupe", au13mai24, Set.of(financeur, trainDeVie))), null);

    assertEquals(500_000, patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(10)).getValeurComptable());
    assertEquals(200_000, patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(100)).getValeurComptable());
    assertEquals(200_000, patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(1_000)).getValeurComptable());
  }

  @Test
  void patrimoine_zety_le_17_sept_24(){
    var zety = new Personne("zety");
    var dateDebut = LocalDate.of(2024, JULY, 3);
    var dateFin = LocalDate.of(2024, SEPTEMBER, 17);

    Possession ordinateur = new Materiel("Ordinateur", dateDebut, 1200000, dateDebut, -0.10);
    Possession vetement = new Materiel("Vetement", dateDebut, 1500000, dateDebut, -0.50);
    var argentEspece = new Argent("Espece", dateDebut, 800000);

    Argent compteBancaire = new Argent("Compte bancaire", dateDebut, 100000);
    FluxArgent fraisDeCompte = new FluxArgent("Frais de tenue de compte", compteBancaire, dateDebut, dateFin.plusMonths(1), -20000, 25);
    TransfertArgent fraisScolaire = new TransfertArgent("Frais de scolarite", argentEspece, compteBancaire, LocalDate.of(2023, 11, 27), LocalDate.of(2024, 8, 27), -200000, 27);

    var patrimoineZetyAu17Sept27 = new Patrimoine(
            "Patrimoine de Zety",
            zety,
            dateDebut,
            Set.of(ordinateur, vetement, argentEspece, compteBancaire, fraisDeCompte, fraisScolaire),
            null
    );
    int valeurAttendueOrdinateur = (int) (1200000 * Math.pow((1 - 0.10), 1.5 / 12));
    int valeurAttendueVetements = (int) (1500000 * Math.pow((1 - 0.50), 1.5 / 12));
    int valeurAttendueEspeces = 800000 - 200000 * 10;
    int valeurAttendueCompteBancaire = 100000 - 20000 * 2;
    int valeurComptableAttendue = valeurAttendueOrdinateur + valeurAttendueVetements + valeurAttendueEspeces + valeurAttendueCompteBancaire;

    assertEquals(valeurComptableAttendue, patrimoineZetyAu17Sept27.getValeurComptable());
  }

  @Test
  void diminution_patrimoine_zety_entre_17_et_18_sept_2024(){
    LocalDate dateDebut = LocalDate.of(2024, JULY, 3);
    LocalDate dateFin = LocalDate.of(2024, SEPTEMBER, 17);

    Possession ordinateur = new Materiel("Ordinateur", dateDebut, 1200000, dateDebut, -0.10);
    Possession vetement = new Materiel("Vetement", dateDebut, 1500000, dateDebut, -0.50);
    Possession argentEspece = new Argent("Espece", dateDebut, 800000);

    Argent compteBancaire = new Argent("Compte bancaire", dateDebut, 100000);
    FluxArgent fraisDeCompte = new FluxArgent("Frais de tenue de compte", compteBancaire, dateDebut, dateFin.plusMonths(1), -20000, 25);
    TransfertArgent fraisScolaire = new TransfertArgent("Frais de scolarite", (Argent) argentEspece, compteBancaire, LocalDate.of(2023, 11, 27), LocalDate.of(2024, 8, 27), -200000, 27);

    Personne zety = new Personne("Zety");

    Patrimoine patrimoineAvantEndettement = new Patrimoine(
            "Patrimoine de Zety",
            zety,
            dateDebut,
            Set.of(ordinateur, vetement, argentEspece,compteBancaire, fraisDeCompte, fraisScolaire), null
    );

    Argent empruntBancaire = new Argent("Emprunt bancaire", LocalDate.of(2024, SEPTEMBER, 18), 10000000);
    Argent coutEmprunt = new Argent("Coût du prêt", LocalDate.of(2024, SEPTEMBER, 18), 1000000);

    Patrimoine patrimoineApresEndettement = new Patrimoine(
            "Patrimoine de Zety après endettement",
            zety,
            dateDebut,
            Set.of(ordinateur, vetement, argentEspece, compteBancaire, fraisDeCompte, fraisScolaire, empruntBancaire, coutEmprunt), null
            );

    int diminutionAttendue = patrimoineAvantEndettement.getValeurComptable() - patrimoineApresEndettement.getValeurComptable();

    assertEquals(10000000, empruntBancaire.getValeurComptable());
    assertEquals(1000000, coutEmprunt.getValeurComptable());
    assertEquals(diminutionAttendue, -11000000);
  }

  @Test
  void date_fin_espece_zety_24_25(){
    LocalDate dateDebut = LocalDate.of(2024, JULY, 3);
    LocalDate dateFin = LocalDate.of(2025, FEBRUARY, 13);

    Argent argentEspece = new Argent("Espèces", dateDebut, 800000);
    Argent compteBancaire = new Argent("Compte bancaire", dateDebut, 100000);
    FluxArgent fraisScolarite = new FluxArgent("Frais de scolarité 2024-2025", compteBancaire, LocalDate.of(2024, 9, 21), null, -2500000, 0);
    TransfertArgent donMensuel = new TransfertArgent("Don mensuel des parents", argentEspece, compteBancaire, LocalDate.of(2024, 1, 15), null, 100000, 30);
    FluxArgent trainDeVie = new FluxArgent("Train de vie mensuel", argentEspece, LocalDate.of(2024, 10, 1), LocalDate.of(2025, 2, 13), -250000, 30);

    Personne zety = new Personne("Zety");

    Patrimoine patrimoineZety = new Patrimoine(
            "patrimoine de zety",
            zety,
            dateDebut,
            Set.of(argentEspece, compteBancaire, fraisScolarite, donMensuel, trainDeVie), null
    );

    LocalDate dateActuelle = dateDebut;
    boolean plusDEspece = false;

    while (dateActuelle.isBefore(dateFin) || dateActuelle.equals(dateFin)) {
      Patrimoine patrimoineFutur = patrimoineZety.projectionFuture(dateActuelle);
      if (patrimoineFutur.getValeurComptable() <= 0) {
        plusDEspece = true;
        break;
      }
      dateActuelle = dateActuelle.plusDays(1);
    }

    assertEquals(LocalDate.of(2025, JANUARY, 31), dateActuelle);
  }

  @Test
  void testConversionEnEUR() {
    LocalDate dateDebut = LocalDate.of(2024, 7, 3);

    Argent argentEspece = new Argent("Espèces", dateDebut, 800000);
    Argent compteBancaire = new Argent("Compte bancaire", dateDebut, 100000);

    Patrimoine patrimoineZety = new Patrimoine(
            "patrimoine de zety",
            new Personne("Zety"),
            dateDebut,
            Set.of(argentEspece, compteBancaire),
            "Ar"
    );

    double tauxChangeEURtoAr = 1 / 4821.0;
    double tauxAppreciationAnnuelAr = -0.10;

    double valeurEnEUR = patrimoineZety.getValeurEnDevise("EUR", LocalDate.of(2025, 2, 14), tauxChangeEURtoAr, tauxAppreciationAnnuelAr);

    assertEquals(800000 * tauxChangeEURtoAr * Math.pow(1 + tauxAppreciationAnnuelAr, -1.5 / 365), valeurEnEUR, 0.01);
  }

}