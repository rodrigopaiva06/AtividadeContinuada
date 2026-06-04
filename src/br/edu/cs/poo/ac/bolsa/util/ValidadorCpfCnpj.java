package br.edu.cs.poo.ac.bolsa.util;

public class ValidadorCpfCnpj {
    public static ResultadoValidacao validarCpf(String cpf) {
        if (cpf == null || cpf.isEmpty()) {
            return ResultadoValidacao.NAO_INFORMADO;
        }

        cpf = cpf.replaceAll("[.\\-]", "");

        if (cpf.length() != 11 || cpf.chars().distinct().count() == 1) {
            return ResultadoValidacao.FORMATO_INVALIDO;
        }

        int soma_dv1 = 0;

        for (int i = 0; i < 9; i++) {
            soma_dv1 += (cpf.charAt(i) - '0') * (10 - i);
        }

        int dv1 = 11 - (soma_dv1 % 11);
        if (dv1 >= 10) {
            dv1 = 0;
        }

        int soma_dv2 = 0;

        for (int i = 0; i < 10; i++) {
            soma_dv2 += (cpf.charAt(i) - '0') * (11 - i);
        }

        int dv2 = 11 - (soma_dv2 % 11);
        if (dv2 >= 10) {
            dv2 = 0;
        }

        if (cpf.charAt(9) - '0' != dv1 || cpf.charAt(10) - '0' != dv2) {
            return ResultadoValidacao.DV_INVALIDO;
        }

        return null;
    }

    public static ResultadoValidacao validarCnpj(String cnpj) {
        if (cnpj == null || cnpj.isEmpty()) {
            return ResultadoValidacao.NAO_INFORMADO;
        }

        cnpj = cnpj.replaceAll("[.\\-\\/]", "");

        if (cnpj.length() != 14 || cnpj.chars().distinct().count() == 1) {
            return ResultadoValidacao.FORMATO_INVALIDO;
        }

        int[] pesos_dv1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] pesos_dv2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

        int soma_dv1 = 0;

        for (int i = 0; i < 12; i++) {
            soma_dv1 += (cnpj.charAt(i) - '0') * pesos_dv1[i];
        }

        int dv1 = 11 - (soma_dv1 % 11);
        if (dv1 >= 10) {
            dv1 = 0;
        }

        int soma_dv2 = 0;

        for (int i = 0; i < 13; i++) {
            soma_dv2 += (cnpj.charAt(i) - '0') * pesos_dv2[i];
        }

        int dv2 = 11 - (soma_dv2 % 11);
        if (dv2 >= 10) {
            dv2 = 0;
        }

        if (cnpj.charAt(12) - '0' != dv1 || cnpj.charAt(13) - '0' != dv2) {
            return ResultadoValidacao.DV_INVALIDO;
        }

        return null;
    }
}