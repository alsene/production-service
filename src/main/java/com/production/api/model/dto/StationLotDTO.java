package com.production.api.model.dto;

import com.production.api.model.Lot;
import com.production.api.model.Station;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class StationLotDTO extends AbstractDTO {
    private Long id;
    private Station station;
    private Lot lot;
}

