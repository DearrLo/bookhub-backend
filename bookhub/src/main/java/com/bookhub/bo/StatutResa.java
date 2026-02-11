package com.bookhub.bo;

/**
 * Énumération des différents états possibles d'une réservation.
 */
public enum StatutResa {
    /** La demande est en file d'attente. */
    EN_ATTENTE,
    /** Le livre réservé est de nouveau disponible pour l'utilisateur. */
    DISPO,
    /** La réservation a été abandonnée ou annulée. */
    ANNULEE
}