package br.edu.cs.poo.ac.bolsa.testes;

import br.edu.cs.poo.ac.bolsa.entidade.Ativo;
import br.edu.cs.poo.ac.bolsa.entidade.FaixaRenda;
import br.edu.cs.poo.ac.bolsa.entidade.InvestidorEmpresa;
import br.edu.cs.poo.ac.bolsa.entidade.InvestidorPessoa;
import br.edu.cs.poo.ac.bolsa.entidade.StatusTitulo;
import br.edu.cs.poo.ac.bolsa.entidade.Titulo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class TituloTest {

    private InvestidorPessoa pessoa;
    private InvestidorEmpresa empresa;
    private Ativo ativo;

    @BeforeEach
    void setup() {
        pessoa = new InvestidorPessoa(
                "MARCOS", null, null, BigDecimal.ZERO, null,
                "12345678901", 30000.00, FaixaRenda.REGULAR);
        empresa = new InvestidorEmpresa(
                "ACME", null, null, BigDecimal.ZERO, null,
                "12345678901234", 3000000.00);
        ativo = new Ativo(
                123456, "C-BONDS", 10000.00, 1000000000.00,
                0.10, 1.00, FaixaRenda.REGULAR, 24);
    }

    private Titulo criarTituloPadrao() {
        return new Titulo(
                pessoa, empresa, ativo,
                new BigDecimal("1000.00"),
                new BigDecimal("1000.00"),
                new BigDecimal("1.0"),
                LocalDate.now().minusDays(10),
                LocalDate.now().plusDays(10),
                null,
                StatusTitulo.ATIVO
        );
    }

    @Test
    void deveRetornarFalseQuandoStatusNaoAtivo() {
        Titulo t = criarTituloPadrao();
        t.setStatus(StatusTitulo.CANCELADO);
        assertFalse(t.render());
    }

    @Test
    void deveRetornarFalseQuandoHojeNaoEhAntesDoVencimento() {
        Titulo t = criarTituloPadrao();
        t.setDataVencimento(LocalDate.now());
        assertFalse(t.render());
    }

    @Test
    void deveRetornarFalseQuandoHojeNaoEhDepoisDaAplicacao() {
        Titulo t = criarTituloPadrao();
        t.setDataAplicacao(LocalDate.now());
        assertFalse(t.render());
    }

    @Test
    void deveRetornarFalseQuandoUltimoRendimentoEhHoje() {
        Titulo t = criarTituloPadrao();
        t.setDataUltimoRendimento(LocalDate.now());
        assertFalse(t.render());
    }

    @Test
    void deveRetornarFalseQuandoDiasCalculadosSaoZero() {
        Titulo t = criarTituloPadrao();
        t.setDataUltimoRendimento(LocalDate.now().minusDays(0));
        assertFalse(t.render());
    }

    @Test
    void deveCalcularRendimentoCorretamenteQuandoPrimeiroRendimento() {
        Titulo t = criarTituloPadrao();
        BigDecimal valorAntes = t.getValorAtual();
        boolean resultado = t.render();
        assertTrue(resultado);
        assertTrue(t.getValorAtual().compareTo(valorAntes) > 0);
    }

    @Test
    void deveAtualizarDataUltimoRendimento() {
        Titulo t = criarTituloPadrao();
        t.render();
        assertEquals(LocalDate.now(), t.getDataUltimoRendimento());
    }

    @Test
    void deveCalcularRendimentoComUltimoRendimentoDefinido() {
        Titulo t = criarTituloPadrao();
        t.setDataUltimoRendimento(LocalDate.now().minusDays(5));
        BigDecimal valorAntes = t.getValorAtual();
        boolean resultado = t.render();
        assertTrue(resultado);
        assertTrue(t.getValorAtual().compareTo(valorAntes) > 0);
    }

    @Test
    void deveAplicarFormulaCorretamenteParaDiasEspecificos() {
        Titulo t = criarTituloPadrao();
        t.setValorAtual(new BigDecimal("1000.00"));
        t.setTaxaDiaria(new BigDecimal("1.0"));
        t.setDataUltimoRendimento(LocalDate.now().minusDays(3));
        t.render();
        BigDecimal esperado = new BigDecimal("1000")
                .multiply(new BigDecimal("1.01").pow(3));
        assertEquals(0, t.getValorAtual().compareTo(esperado));
    }

    @Test
    void testGetNumeroParaInvestidorPessoa() {
        InvestidorPessoa p = new InvestidorPessoa("MARCUS", null, null,
                BigDecimal.ZERO, null, null, 100000.0, FaixaRenda.DIFERENCIADA);
        p.setCpf("12345678901");
        LocalDate dataAplicacao = LocalDate.of(2024, 3, 10);
        Titulo titulo = new Titulo(p, null, ativo, BigDecimal.TEN,
                BigDecimal.TEN, BigDecimal.ONE, dataAplicacao,
                dataAplicacao.plusDays(30), null, StatusTitulo.ATIVO);
        String esperado = "000" + p.getCpf() + ativo.getCodigo() + "202403100000";
        assertEquals(esperado, titulo.getNumero());
    }

    @Test
    void testGetNumeroParaInvestidorEmpresa() {
        InvestidorEmpresa emp = new InvestidorEmpresa("ACME", null, null,
                BigDecimal.ZERO, null, null, 100000.0);
        emp.setCnpj("11222333444455");
        LocalDate dataAplicacao = LocalDate.of(2024, 5, 20);
        Titulo titulo = new Titulo(null, emp, ativo, BigDecimal.TEN,
                BigDecimal.TEN, BigDecimal.ONE, dataAplicacao,
                dataAplicacao.plusDays(60), null, StatusTitulo.ATIVO);
        String esperado = emp.getCnpj() + ativo.getCodigo() + "202405200000";
        assertEquals(esperado, titulo.getNumero());
    }
}