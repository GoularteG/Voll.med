package med.voll.api.domain.consulta.validacoes;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;

import java.time.DayOfWeek;

public class ValidadorHorarioDeFuncionamento {

    public void validar(DadosAgendamentoConsulta dados){
        var dataConsulta  = dados.data();
        var domingo= dataConsulta.getDayOfWeek().equals(DayOfWeek.SUNDAY);
        var antesDaAberturaDaClinica = dataConsulta.getHour() < 7;
        var depoisDoEnceramentoDaClinica= dataConsulta.getHour() > 18;
        if (domingo || antesDaAberturaDaClinica || depoisDoEnceramentoDaClinica){
            throw new ValidacaoException("Consulta fora do funcionamento da clínica");
        }
    }
}
