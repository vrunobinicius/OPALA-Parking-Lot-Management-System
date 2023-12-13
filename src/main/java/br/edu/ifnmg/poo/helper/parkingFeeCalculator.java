package br.edu.ifnmg.poo.helper;

import java.time.LocalTime;
import java.time.Duration;

public class parkingFeeCalculator {

    private static final Double VALOR_HORA = 6.0; // TODO: valores diferentes para cada tipo de veículo
    private static final int FRACIONAMENTO_MINUTOS = 15;
    private static final int MINUTOS_GRATIS = 5;

    public static double calcularValor(LocalTime entrada, LocalTime saida) {
        if (saida.isBefore(entrada)) {
            return 0.0;
        }

        Duration duracao = Duration.between(entrada, saida);
        long minutos = duracao.toMinutes();

        if (minutos <= MINUTOS_GRATIS) {
            return 0.0;
        }
        
        double valor = 0.0;

        while (minutos >= 60) {
            valor += VALOR_HORA;
            minutos -= 60;
        }

        while (minutos >= FRACIONAMENTO_MINUTOS) {
            valor += (double) VALOR_HORA / 4.0;
            minutos -= FRACIONAMENTO_MINUTOS;
        }

        if (minutos > 0) {
            valor += (double) VALOR_HORA / 4;
        }

        return valor;
    }
}
