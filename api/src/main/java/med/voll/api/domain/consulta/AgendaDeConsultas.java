package med.voll.api.domain.consulta;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.validacoes.agendamento.ValidadorAgendamentoDeConsultas;
import med.voll.api.domain.consulta.validacoes.cancelamento.ValidadorCancelamentoDeConsulta;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

    @Service
    public class AgendaDeConsultas {

        @Autowired
        private ConsultaRepository repository;
        @Autowired
        private MedicoRepository medicoR;
        @Autowired
        private PacienteRepository pacienteR;

        @Autowired
        private List<ValidadorAgendamentoDeConsultas> validadores;

        @Autowired
        private List<ValidadorCancelamentoDeConsulta> validadoresCancelamento;

        public DadosDetalhamentoConsulta agendar(DadosAgendamentoConsulta dados) {
            if (!pacienteR.existsById(dados.idPaciente())) {
                throw new ValidacaoException("Id do Paciente informado não existe!");
            }
            if (dados.idMedico() != null && !medicoR.existsById(dados.idMedico())) {
                throw new ValidacaoException("Id do Medico informado não existe!");
            }

            validadores.forEach(v -> v.validar(dados));

            var paciente = pacienteR.findById(dados.idPaciente()).get();
            var medico = escolherMedicos(dados);
            if (medico == null) {
                throw new ValidacaoException("Não existe medico disponivel nessa data!");
            }
            var consulta = new Consulta(null, medico, paciente, dados.data(), null);
            repository.save(consulta);

            return new DadosDetalhamentoConsulta(consulta);
        }

        private Medico escolherMedicos(DadosAgendamentoConsulta dados) {
            if (dados.idMedico() != null) {
                return medicoR.getReferenceById(dados.idMedico());
            }
            if (dados.especialidade() == null) {
                throw new ValidacaoException("Especialidade é obrigatoria quando o médico não é escolhido!");
            }
            return medicoR.escolherMedicoAleatorioLivreNaData(dados.especialidade(), dados.data());
        }

        public void cancelar(DadosCancelamentoConsulta dados) {
            if (!repository.existsById(dados.idConsulta())) {
                throw new ValidacaoException("Id da consulta informado não existe!");
            }

            validadoresCancelamento.forEach(v -> v.validar(dados));

            var consulta = repository.getReferenceById(dados.idConsulta());
            consulta.cancelar(dados.motivoCancelamento());
        }
}
