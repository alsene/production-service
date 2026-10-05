package com.production.api.util;

import com.production.api.model.*;

public final class EnrichirLibelle {
    public static void libelleLot(Lot lot) {
        if (lot == null) {
            return;
        }
        if (lot.getNumeroProduction() != null && lot.getAnneeProduction()!= null) {
            lot.setLibelle(lot.getNumeroProduction() + " - " + lot.getAnneeProduction());
        }
    }

    public static void libelleStationLot(StationLot stationLot) {
        if (stationLot == null) {
            return;
        }
        Lot lot = stationLot.getLot();
        if (lot != null && lot.getNumeroProduction() != null && lot.getAnneeProduction() != null) {
            stationLot.getLot().setLibelle(lot.getNumeroProduction() + " - " + lot.getAnneeProduction());
            stationLot.setLibelle(stationLot.getLot().getLibelle());
        }
    }

    public static void libellePoidsProduit(PoidsProduit poidsProduit) {
        if (poidsProduit == null) {
            return;
        }
        if (poidsProduit.getPoids() != null && poidsProduit.getUnitePoids()  != null && poidsProduit.getCodePoids() != null) {
            poidsProduit.setLibelle(poidsProduit.getPoids() + poidsProduit.getUnitePoids() + " - " + poidsProduit.getCodePoids());
        }
    }

    public static void libelleSiloTypeProduit(SiloTypeProduit siloTypeProduit) {
        if (siloTypeProduit == null) {
            return;
        }
        if (siloTypeProduit.getCodeProduit() != null &&  siloTypeProduit.getTypeProduit() != null &&  siloTypeProduit.getTypeProduit().getLibelle() != null) {
            siloTypeProduit.setLibelle(siloTypeProduit.getCodeProduit() + " - " + siloTypeProduit.getTypeProduit().getLibelle());
        }
    }

    public static void libelleProduit(Produit produit) {
        if (produit == null) {
            return;
        }
        if (produit.getLot() != null) {
            libelleLot(produit.getLot());
        }
        if (produit.getPoidsProduit() != null) {
            libellePoidsProduit(produit.getPoidsProduit());
        }
        if (produit.getStationLot() != null ) {
            libelleStationLot(produit.getStationLot());
        }
        if (produit.getSiloTypeProduit() != null ) {
            libelleSiloTypeProduit(produit.getSiloTypeProduit());
        }
    }
}
