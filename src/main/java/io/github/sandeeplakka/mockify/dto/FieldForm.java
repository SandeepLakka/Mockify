package io.github.sandeeplakka.mockify.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FieldForm {

    private String base;       // password
    private String label;      // [lower,upper,…] or ""
    private String value;      // true,true,…,8  or "6"
    private String fieldKey;   // YAML field name

}

