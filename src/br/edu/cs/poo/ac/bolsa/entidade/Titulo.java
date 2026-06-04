package br.edu.cs.poo.ac.bolsa.entidade;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import br.edu.cs.poo.ac.bolsa.util.Registro;

public class Titulo extends Registro {

    private Investidor investidor;
    private Ativo ativo;
    private BigDecimal valorInvestido;
    private BigDecimal valorAtual;
    private BigDecimal taxaDiaria;
    private LocalDate dataAplicacao;
    private LocalDate dataVencimento;
    private LocalDate dataUltimoRendimento;
    private StatusTitulo status;

    public Titulo() {}

    public Titulo(Investidor investidor,
                  Ativo ativo,
                  BigDecimal valorInvestido,
                  BigDecimal valorAtual,
                  BigDecimal taxaDiaria,
                  LocalDate dataAplicacao,
                  LocalDate dataVencimento,
                  LocalDate dataUltimoRendimento,
                  StatusTitulo status) {
        this.investidor = investidor;
        this.ativo = ativo;
        this.valorInvestido = valorInvestido;
        this.valorAtual = valorAtual;
        this.taxaDiaria = taxaDiaria;
        this.dataAplicacao = dataAplicacao;
        this.dataVencimento = dataVencimento;
        this.dataUltimoRendimento = dataUltimoRendimento;
        this.status = status;
    }

    @Override
    public String getIdentificador() {
        return getNumero();
    }

    public String getNumero() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd");
        String data = dataAplicacao.format(fmt) + "0000";
        String codigoAtivo = String.valueOf(ativo.getCodigo());
        String id = investidor.getIdentificador();
        if (id.length() <= 11) {
            return "000" + id + codigoAtivo + data;
        } else {
            return id + codigoAtivo + data;
        }
    }

    public Investidor getInvestidor() { return investidor; }
    public void setInvestidor(Investidor investidor) { this.investidor = investidor; }

    public Ativo getAtivo() { return ativo; }
    public void setAtivo(Ativo ativo) { this.ativo = ativo; }

    public BigDecimal getValorInvestido() { return valorInvestido; }
    public void setValorInvestido(BigDecimal valorInvestido) { this.valorInvestido = valorInvestido; }

    public BigDecimal getValorAtual() { return valorAtual; }
    public void setValorAtual(BigDecimal valorAtual) { this.valorAtual = valorAtual; }

    public BigDecimal getTaxaDiaria() { return taxaDiaria; }
    public void setTaxaDiaria(BigDecimal taxaDiaria) { this.taxaDiaria = taxaDiaria; }

    public LocalDate getDataAplicacao() { return dataAplicacao; }
    public void setDataAplicacao(LocalDate dataAplicacao) { this.dataAplicacao = dataAplicacao; }

    public LocalDate getDataVencimento() { return dataVencimento; }
    public void setDataVencimento(LocalDate dataVencimento) { this.dataVencimento = dataVencimento; }

    public LocalDate getDataUltimoRendimento() { return dataUltimoRendimento; }
    public void setDataUltimoRendimento(LocalDate dataUltimoRendimento) { this.dataUltimoRendimento = dataUltimoRendimento; }

    public StatusTitulo getStatus() { return status; }
    public void setStatus(StatusTitulo status) { this.status = status; }

    public boolean render() {
        LocalDate hoje = LocalDate.now();
        if (status != StatusTitulo.ATIVO) return false;
        if (!hoje.isBefore(dataVencimento)) return false;
        if (!hoje.isAfter(dataAplicacao)) return false;
        if (dataUltimoRendimento != null && !hoje.isAfter(dataUltimoRendimento)) return false;
        long dd;
        if (dataUltimoRendimento == null) {
            dd = ChronoUnit.DAYS.between(dataAplicacao, hoje);
        } else {
            dd = ChronoUnit.DAYS.between(dataUltimoRendimento, hoje);
        }
        if (dd == 0) return false;
        BigDecimal fator = BigDecimal.ONE
                .add(taxaDiaria.divide(new BigDecimal("100")))
                .pow((int) dd);
        this.valorAtual = this.valorAtual.multiply(fator);
        this.dataUltimoRendimento = hoje;
        return true;
    }
}
