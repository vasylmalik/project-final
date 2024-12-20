package com.javarush.jira.ref;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.javarush.jira.common.to.TitleDTO;
import com.javarush.jira.common.util.Util;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.lang.Nullable;

@Value
@EqualsAndHashCode(of = {"refType", "code"})
public class RefDTO extends TitleDTO {
    @NotNull
    RefType refType;
    @Nullable
    String aux;
    @Nullable
    @JsonIgnore
    String[] splittedAux;

    public RefDTO(Long id, RefType refType, String code, String title, @Nullable String aux) {
        super(id, code, title);
        this.refType = refType;
        this.aux = aux;
        this.splittedAux = (aux != null && aux.contains("|")) ? aux.split("\\|") : null;
    }

    @JsonIgnore
    public String getAux(int idx) {
        return splittedAux == null || splittedAux.length <= idx ? null : splittedAux[idx];
    }

    @JsonIgnore
    public long getLongFromAux() {
        return Long.parseLong(Util.notNull(aux, "MAIL_NOTIFICATION {0} has no aux(mask)", this));
    }
}
