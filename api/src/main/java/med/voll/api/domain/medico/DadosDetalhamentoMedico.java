package med.voll.api.domain.medico;

import med.voll.api.domain.endereco.Endereco;

public record DadosDetalhamentoMedico(Long id, String nome, String email, String crm, Especialidade especialidade, Endereco endereco) {

    public DadosDetalhamentoMedico(Medico medico){
        this(medico.getId(),medico.getEmail(), medico.getCrm(), medico.getNome(),medico.getEspecialidade(),medico.getEndereco());
    }
}
