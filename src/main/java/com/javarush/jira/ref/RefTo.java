package com.javarush.jira.ref;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.javarush.jira.common.to.TitleTo;
import com.javarush.jira.common.util.Util;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(of = {"refType", "code"}, callSuper = false)
@com.fasterxml.jackson.annotation.JsonAutoDetect(
        fieldVisibility = com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY,
        getterVisibility = com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE,
        setterVisibility = com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE
)
public class RefTo extends TitleTo {

    @NotNull
    private final RefType refType;

    @Nullable
    private final String aux;

    @Nullable
    @JsonIgnore
    private final String[] splittedAux;

    @JsonCreator
    public RefTo(
            @JsonProperty("id") Long id,
            @JsonProperty("refType") RefType refType,
            @JsonProperty("code") String code,
            @JsonProperty("title") String title,
            @JsonProperty("aux") @Nullable String aux) {
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