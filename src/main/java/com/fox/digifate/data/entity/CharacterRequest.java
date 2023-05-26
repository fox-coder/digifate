package com.fox.digifate.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CharacterRequest {

    private BaseCard character;
    private BaseCard origins;
    private BaseCard background;
    private SpecificationCard firstBond;
    private Map<String, List<BaseCard>> firstBondAnswers = new HashMap<>();
    private SpecificationCard secondBond;
    private Map<String, List<BaseCard>> secondBondAnswers = new HashMap<>();
    private SpecificationCard markOfThePast;
    private Map<String, List<BaseCard>> markOfThePastAnswers = new HashMap<>();
    private SpecificationCard impetus;
    private Map<String, List<BaseCard>> impetusAnswers = new HashMap<>();

}
