package org.mlgnu.learnova.module.user.model.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.mlgnu.learnova.module.user.model.enums.AccountStatus;

@Converter(autoApply = true)
public class AccountStatusConverter implements AttributeConverter<AccountStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(AccountStatus status) {
        return (status != null) ? status.getCode() : null;
    }

    @Override
    public AccountStatus convertToEntityAttribute(Integer dbValue) {
        return (dbValue != null) ? AccountStatus.fromCode(dbValue) : null;
    }
}
