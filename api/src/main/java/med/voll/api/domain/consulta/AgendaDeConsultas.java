package med.voll.api.domain.consulta;

import jakarta.validation.ValidationException;
import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.Paciente;
import med.voll.api.domain.paciente.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgendaDeConsultas {

    @Autowired
    private ConsultaRepository repository;
    @Autowired
    private MedicoRepository medicoR;
    @Autowired
    private PacienteRepository pacienteR;

    public void agendar( DadosAgendamentoConsulta dados){
        if(!pacienteR.existsById(dados.idPaciente())){
            throw new ValidacaoException("Id do Paciente informado não existe!");
        }
        if(dados.idMedico() != null && !medicoR.existsById(dados.idMedico())){
            throw new ValidacaoException("Id do Medico informado não existe!");
        }
        var paciente= pacienteR.findById(dados.idPaciente()).get();
        var medico = escolherMedicos(dados);
        var consulta = new Consulta(null, medico,paciente,dados.data());
        repository.save(consulta);
    }

    private Medico escolherMedicos(DadosAgendamentoConsulta dados) {
        if (dados.idMedico() != null){
            return medicoR.getReferenceById(dados.idMedico());
        }
        if (dados.especialidade() == null){
            throw new ValidacaoException("Especialidade é obrigatoria quando o médico não é escolhido!");
        }
        return  medicoR.escolherMedicoAleatorioLivreNaData(dados.especialidade(), dados.data());
    }
}
