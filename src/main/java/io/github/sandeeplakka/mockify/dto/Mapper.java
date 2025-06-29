package io.github.sandeeplakka.mockify.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Maps between YAML-side DTOs (ModelDTO/FieldDTO) and the UI form beans
 * (ModelForm/FieldForm) that Thymeleaf binds.
 */
//TODO: WIP
public final class Mapper {
    private Mapper() {
    }

    /**
     * Splits specifications like "int[min,max][20,60]" or "name" or "loremParagraphs[min,max]".
     */
    private static Parts split(String spec) {
        spec = spec == null ? "" : spec.trim();
        if (spec.isEmpty()) return new Parts("", "", "");

        int firstB = spec.indexOf('['),
                lastB = spec.lastIndexOf('['),
                space = spec.indexOf(' ');

        int cut = firstB == -1 ? space
                : space == -1 ? firstB
                : Math.min(firstB, space);
        String base = cut == -1 ? spec : spec.substring(0, cut);

        String finalSpec = spec;
        Function<Integer, String> slice = idx -> {
            if (idx < 0) return "";
            int end = finalSpec.indexOf(']', idx);
            return end < 0 ? "" : finalSpec.substring(idx, end + 1);
        };

        if (firstB != -1 && lastB != firstB) {
            return new Parts(base, slice.apply(firstB), slice.apply(lastB));
        }
        if (firstB != -1) {
            return new Parts(base, "", slice.apply(firstB));
        }
        if (space != -1) {
            return new Parts(base, "", " " + spec.substring(space + 1).trim());
        }
        return new Parts(base, "", "");
    }

    //DTO to Form
    public static SchemaForm dtoToForm(SchemaDTO dto) {
        SchemaForm form = new SchemaForm();

        for (ModelDTO m : dto.models()) {
            ModelForm mf = new ModelForm();
            mf.setName(m.name());
            mf.setCount(m.count());
            mf.setBelongsTo(m.belongsTo().isEmpty() ? "" : m.belongsTo().get(0));
            mf.setHasMany(new ArrayList<>(m.hasMany()));

            for (FieldDTO f : m.fields()) {
                Parts p = split(f.generator());
                String args = stripBrackets(p.value());
                mf.getFields().add(new FieldForm(
                        p.base(),    // generator name for the <select>
                        p.label(),   // hidden bracketed label (e.g. "[min,max]")
                        args,        // visible args (e.g. "20,60")
                        f.name()     // field key
                ));
            }
            form.getModels().add(mf);
        }
        return form;
    }

    //Form to DTO
    public static SchemaDTO formToDto(SchemaForm form) {
        var models = form.getModels().stream().map(mf -> {
            var fields = mf.getFields().stream().map(ff -> {
                String base = safe(ff.getBase());
                String label = safe(ff.getLabel());
                String val = safe(ff.getValue());

                String argPart = val.isBlank() ? ""
                        : val.startsWith(" ") ? val
                        : "[" + val + "]";

                return new FieldDTO(ff.getFieldKey(), base + label + argPart);
            }).toList();

            return new ModelDTO(
                    mf.getName(),
                    mf.getCount(),
                    fields,
                    mf.getHasMany(),
                    mf.getBelongsTo().isBlank() ? List.of() : List.of(mf.getBelongsTo())
            );
        }).toList();

        return new SchemaDTO(models);
    }

    private static String stripBrackets(String s) {
        if (s == null) return "";
        s = s.trim();
        return s.startsWith("[") && s.endsWith("]") ? s.substring(1, s.length() - 1) : s;
    }

    private static String safe(String s) {
        return s == null ? "" : s.trim();
    }

    private record Parts(String base, String label, String value) {
    }
}
