package org.mlgnu.learnova.module.study.joinrequest.model.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.mlgnu.learnova.module.study.joinrequest.model.enums.JoinRequestStatus;

@Converter(autoApply = true)
public class JoinRequestStatusConverter implements AttributeConverter<JoinRequestStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(JoinRequestStatus status) {
        return status != null ? status.getCode() : null;
    }

    @Override
    public JoinRequestStatus convertToEntityAttribute(Integer dbValue) {
        return dbValue != null ? JoinRequestStatus.fromCode(dbValue) : null;
    }
}
