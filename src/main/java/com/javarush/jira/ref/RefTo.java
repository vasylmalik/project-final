package com.javarush.jira.ref;

import com.javarush.jira.common.to.TitleTo;
import com.javarush.jira.common.util.validation.Code;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.lang.Nullable;

@Value
@EqualsAndHashCode(of = {"refType", "code"})
public class RefTo extends TitleTo {
    @NotNull
    RefType refType;
    @Code
    String code;
    @Nullable
    String aux;

    public RefTo(Long id, RefType refType, String code, String title, String aux, boolean enabled) {
        super(id, title, enabled);
        this.refType = refType;
        this.code = code;
        this.aux = aux;
    }
}
