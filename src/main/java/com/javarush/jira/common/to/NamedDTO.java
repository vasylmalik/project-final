package com.javarush.jira.common.to;

import com.javarush.jira.common.util.validation.NoHtml;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class NamedDTO extends BaseDTO {
    @NotBlank
    @Size(min = 2, max = 128)
    @NoHtml
    protected String name;

    public NamedDTO(Long id, String name) {
        super(id);
        this.name = name;
    }

    @Override
    public String toString() {
        return super.toString() + '[' + name + ']';
    }
}
