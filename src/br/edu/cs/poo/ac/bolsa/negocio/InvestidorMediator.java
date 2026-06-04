package br.edu.cs.poo.ac.bolsa.negocio;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.edu.cs.poo.ac.bolsa.dao.DAOInvestidorEmpresa;
import br.edu.cs.poo.ac.bolsa.dao.DAOInvestidorPessoa;
import br.edu.cs.poo.ac.bolsa.entidade.Contatos;
import br.edu.cs.poo.ac.bolsa.entidade.ComparadorInvestidorPessoaRenda;
import br.edu.cs.poo.ac.bolsa.entidade.Endereco;
import br.edu.cs.poo.ac.bolsa.entidade.FaixaRenda;
import br.edu.cs.poo.ac.bolsa.entidade.Investidor;
import br.edu.cs.poo.ac.bolsa.entidade.InvestidorEmpresa;
import br.edu.cs.poo.ac.bolsa.entidade.InvestidorPessoa;
import br.edu.cs.poo.ac.bolsa.entidade.OrdenacaoInvestidorPessoa;
import br.edu.cs.poo.ac.bolsa.util.Comparador;
import br.edu.cs.poo.ac.bolsa.util.Comparavel;
import br.edu.cs.poo.ac.bolsa.util.ComparadorGenerico;
import br.edu.cs.poo.ac.bolsa.util.MensagensValidacao;
import br.edu.cs.poo.ac.bolsa.util.Ordenador;
import br.edu.cs.poo.ac.bolsa.util.ResultadoValidacao;
import br.edu.cs.poo.ac.bolsa.util.ValidadorCpfCnpj;

public class InvestidorMediator {

    private DAOInvestidorEmpresa daoInvEmp = new DAOInvestidorEmpresa();
    private DAOInvestidorPessoa daoInvPes = new DAOInvestidorPessoa();

    private MensagensValidacao validarEndereco(Endereco endereco) {
        MensagensValidacao msgs = new MensagensValidacao();
        if (endereco == null) {
            msgs.adicionar("Endereço é obrigatório.");
            return msgs;
        }
        if (endereco.getLogradouro() == null || endereco.getLogradouro().trim().isEmpty())
            msgs.adicionar("Logradouro é obrigatório.");
        if (endereco.getNumero() == null || endereco.getNumero().trim().isEmpty())
            msgs.adicionar("Número é obrigatório.");
        if (endereco.getCidade() == null || endereco.getCidade().trim().isEmpty())
            msgs.adicionar("Cidade é obrigatório.");
        if (endereco.getEstado() == null || endereco.getEstado().trim().isEmpty())
            msgs.adicionar("Estado é obrigatório.");
        if (endereco.getPais() == null || endereco.getPais().trim().isEmpty())
            msgs.adicionar("País é obrigatório.");
        return msgs;
    }

    private MensagensValidacao validarContatos(Contatos contatos, boolean ehPessoaJuridica) {
        MensagensValidacao msgs = new MensagensValidacao();
        if (contatos == null) {
            msgs.adicionar("Contatos é obrigatório.");
            return msgs;
        }
        if (contatos.getEmail() == null || contatos.getEmail().trim().isEmpty()) {
            msgs.adicionar("E-mail inválido.");
        } else if (!contatos.getEmail().contains("@") || !contatos.getEmail().contains(".")) {
            msgs.adicionar("E-mail inválido.");
        }
        boolean temTelefone =
                (contatos.getTelefoneFixo() != null && !contatos.getTelefoneFixo().trim().isEmpty()) ||
                (contatos.getTelefoneCelular() != null && !contatos.getTelefoneCelular().trim().isEmpty()) ||
                (contatos.getNumeroWhatsApp() != null && !contatos.getNumeroWhatsApp().trim().isEmpty());
        if (!temTelefone) {
            msgs.adicionar("Pelo menos um telefone deve ser informado.");
        } else {
            if (contatos.getTelefoneFixo() != null && !contatos.getTelefoneFixo().trim().isEmpty()
                    && !contatos.getTelefoneFixo().matches("\\d+"))
                msgs.adicionar("Telefone fixo deve conter apenas números.");
            if (contatos.getTelefoneCelular() != null && !contatos.getTelefoneCelular().trim().isEmpty()
                    && !contatos.getTelefoneCelular().matches("\\d+"))
                msgs.adicionar("Telefone celular deve conter apenas números.");
            if (contatos.getNumeroWhatsApp() != null && !contatos.getNumeroWhatsApp().trim().isEmpty()
                    && !contatos.getNumeroWhatsApp().matches("\\d+"))
                msgs.adicionar("Número WhatsApp deve conter apenas números.");
        }
        if (ehPessoaJuridica &&
                (contatos.getNomeParaContato() == null || contatos.getNomeParaContato().trim().isEmpty()))
            msgs.adicionar("Nome para contato é obrigatório para pessoa jurídica.");
        return msgs;
    }

    private MensagensValidacao validar(DadosInvestidor dadosInv) {
        MensagensValidacao msgs = new MensagensValidacao();
        if (dadosInv == null) {
            msgs.adicionar("Dados do investidor é obrigatório.");
            return msgs;
        }
        if (dadosInv.getNome() == null || dadosInv.getNome().trim().isEmpty())
            msgs.adicionar("Nome é obrigatório.");
        if (dadosInv.getEndereco() == null)
            msgs.adicionar("Endereço é obrigatório.");
        if (dadosInv.getDataCriacao() == null)
            msgs.adicionar("Data é obrigatória.");
        else if (dadosInv.getDataCriacao().isAfter(LocalDate.now()))
            msgs.adicionar("Data não pode ser futura.");
        if (dadosInv.getBonus() == null)
            msgs.adicionar("Bônus é obrigatório.");
        else if (dadosInv.getBonus().compareTo(BigDecimal.ZERO) < 0)
            msgs.adicionar("Bônus deve ser maior ou igual a zero.");
        if (dadosInv.getContatos() == null)
            msgs.adicionar("Contatos é obrigatório.");
        msgs.adicionar(validarEndereco(dadosInv.getEndereco()));
        msgs.adicionar(validarContatos(dadosInv.getContatos(), dadosInv.ehInvestidorEmpresa()));
        return msgs;
    }

    private MensagensValidacao validarInvestidorEmpresa(InvestidorEmpresa ie) {
        MensagensValidacao msgs = new MensagensValidacao();
        DadosInvestidor dados = new DadosInvestidor(ie, null);
        msgs.adicionar(validar(dados));
        ResultadoValidacao resultCnpj = ValidadorCpfCnpj.validarCnpj(ie.getCnpj());
        if (resultCnpj != null)
            msgs.adicionar(resultCnpj.getMensagem());
        if (ie.getFaturamento() < 100000.0)
            msgs.adicionar("Faturamento deve ser maior ou igual a 100000.");
        return msgs;
    }

    private MensagensValidacao validarInvestidorPessoa(InvestidorPessoa ip) {
        MensagensValidacao msgs = new MensagensValidacao();
        DadosInvestidor dados = new DadosInvestidor(null, ip);
        msgs.adicionar(validar(dados));
        ResultadoValidacao resultCpf = ValidadorCpfCnpj.validarCpf(ip.getCpf());
        if (resultCpf != null)
            msgs.adicionar(resultCpf.getMensagem());
        if (ip.getRenda() < 10000.0)
            msgs.adicionar("Renda deve ser maior ou igual a 10000.");
        for (FaixaRenda f : FaixaRenda.values()) {
            if (ip.getRenda() >= f.getValorInicial() && ip.getRenda() <= f.getValorFinal()) {
                ip.setFaixaRenda(f);
                break;
            }
        }
        return msgs;
    }

    public MensagensValidacao incluirInvestidorEmpresa(InvestidorEmpresa ie) {
        MensagensValidacao msgs = validarInvestidorEmpresa(ie);
        if (msgs.estaVazio()) {
            if (!daoInvEmp.incluir(ie))
                msgs.adicionar("Investidor Empresa já existente.");
        }
        return msgs;
    }

    public MensagensValidacao alterarInvestidorEmpresa(InvestidorEmpresa ie) {
        MensagensValidacao msgs = validarInvestidorEmpresa(ie);
        if (msgs.estaVazio()) {
            if (!daoInvEmp.alterar(ie))
                msgs.adicionar("Investidor Empresa não existente.");
        }
        return msgs;
    }

    public MensagensValidacao excluirInvestidorEmpresa(String cnpj) {
        MensagensValidacao msgs = new MensagensValidacao();
        ResultadoValidacao result = ValidadorCpfCnpj.validarCnpj(cnpj);
        if (result != null) {
            msgs.adicionar(result.getMensagem());
            return msgs;
        }
        if (!daoInvEmp.excluir(cnpj))
            msgs.adicionar("Investidor Empresa não existente.");
        return msgs;
    }

    public InvestidorEmpresa buscarInvestidorEmpresa(String cnpj) {
        if (ValidadorCpfCnpj.validarCnpj(cnpj) != null) return null;
        return daoInvEmp.buscar(cnpj);
    }

    public MensagensValidacao incluirInvestidorPessoa(InvestidorPessoa ip) {
        MensagensValidacao msgs = validarInvestidorPessoa(ip);
        if (msgs.estaVazio()) {
            if (!daoInvPes.incluir(ip))
                msgs.adicionar("Investidor Pessoa já existente.");
        }
        return msgs;
    }

    public MensagensValidacao alterarInvestidorPessoa(InvestidorPessoa ip) {
        MensagensValidacao msgs = validarInvestidorPessoa(ip);
        if (msgs.estaVazio()) {
            if (!daoInvPes.alterar(ip))
                msgs.adicionar("Investidor Pessoa não existente.");
        }
        return msgs;
    }

    public MensagensValidacao excluirInvestidorPessoa(String cpf) {
        MensagensValidacao msgs = new MensagensValidacao();
        ResultadoValidacao result = ValidadorCpfCnpj.validarCpf(cpf);
        if (result != null) {
            msgs.adicionar(result.getMensagem());
            return msgs;
        }
        if (!daoInvPes.excluir(cpf))
            msgs.adicionar("Investidor Pessoa não existente.");
        return msgs;
    }

    public InvestidorPessoa buscarInvestidorPessoa(String cpf) {
        if (ValidadorCpfCnpj.validarCpf(cpf) != null) return null;
        return daoInvPes.buscar(cpf);
    }

    public InvestidorPessoa[] consultarInvestidorPessoa(OrdenacaoInvestidorPessoa criterio) {
        InvestidorPessoa[] todos = daoInvPes.consultarTodos();
        if (todos == null) return null;
        Comparavel[] comparaveis = todos;
        Comparador comparador;
        if (criterio == OrdenacaoInvestidorPessoa.RENDA) {
            comparador = new ComparadorInvestidorPessoaRenda();
        } else {
            comparador = new ComparadorGenerico();
        }
        Ordenador.ordenar(comparaveis, comparador);
        return todos;
    }

    public Investidor buscarInvestidor(String identificador) {
        if (identificador == null || identificador.trim().isEmpty()) return null;
        if (ValidadorCpfCnpj.validarCpf(identificador) == null) {
            return buscarInvestidorPessoa(identificador);
        }
        if (ValidadorCpfCnpj.validarCnpj(identificador) == null) {
            return buscarInvestidorEmpresa(identificador);
        }
        InvestidorPessoa ip = daoInvPes.buscar(identificador);
        if (ip != null) return ip;
        return daoInvEmp.buscar(identificador);
    }

    public MensagensValidacao alterarInvestidor(Investidor investidor) {
        if (investidor instanceof InvestidorPessoa) {
            return alterarInvestidorPessoa((InvestidorPessoa) investidor);
        } else {
            return alterarInvestidorEmpresa((InvestidorEmpresa) investidor);
        }
    }
}
