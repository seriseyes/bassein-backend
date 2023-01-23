package com.seris.bassein.util;

/**
 * Constants
 *
 * @author Баярхүү.Лув 2022.04.12 20:19
 */
public class Constants {
    public static String[] httpSecurityUrlAntMatchers = new String[]{
            "/",
            "/error",
            "/assets/**",
            "/api/auth/**"
    };

    /*
     * Linux: /home/bayarkhuu/Documents/bassein
     * Windows: c:/bassein
     * */
    public static String path = "c:/bassein";
    public static String path_font = path + "/files/fonts";
    public static String path_pdf = path + "/files/pdfs";
    public static String path_temp = path + "/temp";
}
