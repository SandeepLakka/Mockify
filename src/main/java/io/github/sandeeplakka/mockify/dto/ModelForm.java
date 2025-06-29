package io.github.sandeeplakka.mockify.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModelForm {

    private String name;
    private Integer count = 10;
    private List<FieldForm> fields = new ArrayList<>();
    private List<String> hasMany = new ArrayList<>();
    private String hasOne;
    private String belongsTo;
}
