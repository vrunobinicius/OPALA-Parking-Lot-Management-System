/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifnmg.poo.opala;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;
import javax.swing.JTextField;

public class TimeFormatFilter {

    public static void setHHMMFormat(JTextField textField) {
        PlainDocument doc = new PlainDocument();
        doc.setDocumentFilter(new TimeDocumentFilter());
        textField.setDocument(doc);
    }

    private static class TimeDocumentFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {
            StringBuilder sb = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
            sb.insert(offset, string);
            if (isValidTimeFormat(sb.toString())) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {
            StringBuilder sb = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
            sb.replace(offset, offset + length, text);
            if (isValidTimeFormat(sb.toString())) {
                super.replace(fb, offset, length, text, attrs);
            }
        }

        private boolean isValidTimeFormat(String text) {
            // Customize this validation based on your requirements
            return text.matches("^\\d{0,2}:?\\d{0,2}$");
        }
    }

    public static void main(String[] args) {
        // Example usage
        JTextField textField = new JTextField();
        setHHMMFormat(textField);
    }
}
