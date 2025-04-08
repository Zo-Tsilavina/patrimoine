package school.hei.patrimoine.cas;

import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Dette;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.Devise;

import java.time.LocalDate;
import java.util.Set;
import java.util.function.Supplier;


import static java.time.Month.*;
import static school.hei.patrimoine.modele.Argent.ariary;

public class PatrimoineTiana implements Supplier<Patrimoine> {

    @Override
    public Patrimoine get() {
        var tiana = new Personne("Tiana");
        var dateActuelle = LocalDate.of(2025, APRIL, 8);
        var dateFinSimulation = LocalDate.of(2026, MARCH, 31);

        var compteBancaire = new Compte("Compte bancaire", dateActuelle, ariary(60_000_000));

        var terrainBati = new Materiel(
                "Terrain bâti",
                dateActuelle,
                dateActuelle,
                ariary(100_000_000),
                0.10);

        var depensesFamiliales = new FluxArgent(
                "Dépenses familiales",
                compteBancaire,
                dateActuelle,
                dateFinSimulation,
                1,
                ariary(-4_000_000));

        var debutProjet = LocalDate.of(2025, JUNE, 1);
        var finProjet = LocalDate.of(2025, DECEMBER, 31);

        var depensesProjet = new FluxArgent(
                "Dépenses projet",
                compteBancaire,
                debutProjet,
                finProjet,
                5,
                ariary(-5_000_000));

        var revenuInitialProjet = new FluxArgent(
                "Revenu initial projet (10%)",
                compteBancaire,
                LocalDate.of(2025, MAY, 1),
                ariary(7_000_000));

        var revenuFinalProjet = new FluxArgent(
                "Revenu final projet (90%)",
                compteBancaire,
                LocalDate.of(2026, JANUARY, 31),
                ariary(63_000_000));

        // Prêt bancaire
        var detteBancaire = new Dette("Prêt bancaire", dateActuelle, ariary(0));

        // 1. Prêt de 20_000_000 Ar le 27 juillet 2025
        var pretRecu = new FluxArgent(
                "Prêt bancaire reçu",
                compteBancaire,
                LocalDate.of(2025, JULY, 27),
                ariary(20_000_000));

        var creationDette = new FluxArgent(
                "Création dette bancaire",
                detteBancaire,
                LocalDate.of(2025, JULY, 27),
                ariary(-20_000_000));

        var remboursementPret = new FluxArgent(
                "Remboursement prêt",
                compteBancaire,
                LocalDate.of(2025, AUGUST, 27),
                LocalDate.of(2026, JULY, 27),
                27,
                ariary(-2_000_000));

        var reductionDette = new FluxArgent(
                "Réduction dette bancaire",
                detteBancaire,
                LocalDate.of(2025, AUGUST, 27),
                LocalDate.of(2026, JULY, 27),
                27,
                ariary(2_000_000));

        return Patrimoine.of(
                "Patrimoine de Tiana",
                Devise.MGA,
                dateActuelle,
                tiana,
                Set.of(
                        compteBancaire,
                        terrainBati,
                        depensesFamiliales,
                        depensesProjet,
                        revenuInitialProjet,
                        revenuFinalProjet,
                        detteBancaire,
                        pretRecu,
                        creationDette,
                        remboursementPret,
                        reductionDette
                ));
    }
}