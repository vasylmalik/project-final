package com.javarush.jira.profile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.javarush.jira.common.to.BaseTo;
import com.javarush.jira.common.util.validation.Code;
import com.javarush.jira.common.util.validation.NoHtml;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(value = "id", allowSetters = true)
public class ContactTo extends BaseTo {
    @Code
    private String code;
    @NotBlank
    @Size(min = 2, max = 256)
    @NoHtml
    private String value;
}
