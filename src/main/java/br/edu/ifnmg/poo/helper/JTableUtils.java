/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifnmg.poo.helper;

import javax.swing.JTable;
import javax.swing.table.TableModel;

public class JTableUtils {
    
    public static int getColumnIndexByName(JTable table, String columnName) {
        TableModel model = table.getModel();
        int columnCount = model.getColumnCount();

        for (int i = 0; i < columnCount; i++) {
            if (model.getColumnName(i).equals(columnName)) {
                return i;
            }
        }

        // If the column is not found, return -1
        return -1;
    }
}
