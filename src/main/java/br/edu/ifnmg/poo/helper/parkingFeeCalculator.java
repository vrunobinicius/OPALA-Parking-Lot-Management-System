/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifnmg.poo.helper;

/**
 *
 * @author Paulo Filipe Moreira da Silva &lt;pfms at ifnmg.edu.br&gt;
 */
import java.time.LocalTime;
import java.time.Duration;

public class parkingFeeCalculator {

    private static final int VALOR_HORA = 6;
    private static final int FRACIONAMENTO_MINUTOS = 15;
    private static final int MINUTOS_GRATIS = 5;

    public static double calcularValor(LocalTime entrada, LocalTime saida) {
        Duration duracao = Duration.between(entrada, saida);
        long minutosEstacionado = duracao.toMinutes();

        // Se o veículo ficou menos de 5 minutos, o valor é zero
        if (minutosEstacionado < MINUTOS_GRATIS) {
            return 0.0;
        }

        // Calcular o valor com base no fracionamento de 15 minutos
        double valorPago = Math.ceil(minutosEstacionado / (double) FRACIONAMENTO_MINUTOS) * VALOR_HORA / 4;
        
        return valorPago;
    }
}
