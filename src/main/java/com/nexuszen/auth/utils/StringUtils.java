package com.nexuszen.auth.utils;

/**
 * Utilidades para manejo y normalización de texto.
 */
public final class StringUtils {

  private StringUtils() {
    // Clase utilitaria, no instanciable
  }

  /**
   * Normaliza un correo electrónico convirtiéndolo a minúsculas y removiendo espacios.
   */
  public static String normalizeEmail(String email) {
    if (email == null) {
      return null;
    }
    return email.trim().toLowerCase();
  }

  /**
   * Normaliza un nombre de usuario convirtiéndolo a minúsculas y removiendo espacios.
   */
  public static String normalizeUsername(String username) {
    if (username == null) {
      return null;
    }
    return username.trim().toLowerCase();
  }
}
