package com.github.oasis.craftprotect.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChallengeModel {

    private String name;
    private String description;

    private int goal;
    private int current;


}
