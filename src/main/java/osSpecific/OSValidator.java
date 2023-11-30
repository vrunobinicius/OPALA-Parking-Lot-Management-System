/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package osSpecific;

/**
 *
 * @author Paulo Filipe Moreira da Silva &lt;pfms at ifnmg.edu.br&gt;
 */
public class OSValidator {
    private static final String OS = System.getProperty("os.name").toLowerCase();
    public static final boolean IS_WINDOWS = (OS.contains("win"));
    public static final boolean IS_MAC = (OS.contains("mac"));
    public static final boolean IS_UNIX = (OS.contains("nix") || OS.contains("nux") || OS.indexOf("aix") > 0);
    public static final boolean IS_SOLARIS = (OS.contains("sunos"));
}
