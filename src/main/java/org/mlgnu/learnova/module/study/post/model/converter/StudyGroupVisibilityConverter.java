package org.mlgnu.learnova.module.study.post.model.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.mlgnu.learnova.module.study.joinrequest.model.enums.StudyGroupVisibility;

@Converter(autoApply = true)
public class StudyGroupVisibilityConverter implements AttributeConverter<StudyGroupVisibility, Integer> {

    @Override
    public Integer convertToDatabaseColumn(StudyGroupVisibility visibility) {
        return (visibility != null) ? visibility.getCode() : null;
    }

    @Override
    public StudyGroupVisibility convertToEntityAttribute(Integer dbValue) {
        return (dbValue != null) ? StudyGroupVisibility.fromCode(dbValue) : null;
    }
}
