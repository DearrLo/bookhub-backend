package com.bookhub.bo;

/**
 * Énumération des différents états possibles d'un emprunt.
 */
public enum StatutEmprunt {
    /** Le livre est actuellement détenu par un lecteur. */
    EMPRUNTE,
    /** Le livre a été restitué à la bibliothèque. */
    RENDU
}