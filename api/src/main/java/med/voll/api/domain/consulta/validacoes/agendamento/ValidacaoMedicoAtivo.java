package med.voll.api.domain.consulta.validacoes.agendamento;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.domain.medico.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class ValidacaoMedicoAtivo implements ValidadorAgendamentoDeConsultas {

    @Autowired
    private MedicoRepository medicoRepository;

    public void validar(DadosAgendamentoConsulta dados){
    if (dados.idMedico()==null){
        return;
    }

    var medicoEstaAtivo = medicoRepository.findAtivoById(dados.idMedico());
    if (!medicoEstaAtivo){
        throw new ValidacaoException("Consulta não pode ser agendada com esse médico no momento");
    }

    }
}
