package med.voll.api.medico;

import jakarta.validation.constraints.NotNull;
import med.voll.api.endereco.DadosEndereco;

public record DadosAtualizcaoMedicio(
        @NotNull
        Long id,
        String nome,
        String telefone,
        DadosEndereco endereco) {
}
