package com.fox.digifate.data.entity;

import com.fox.digifate.data.service.EnumConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SpecificationCard extends BaseCard{

    @Column
    private String firstQuestion;

    @Column(columnDefinition = "json")
    @Convert(converter = EnumConverter.class)
    private List<CardCategory> firstQuestionCategoryArray;

    @Column
    private String secondQuestion;

    @Column(columnDefinition = "json")
    @Convert(converter = EnumConverter.class)
    private List<CardCategory> secondQuestionCategoryArray;

}
