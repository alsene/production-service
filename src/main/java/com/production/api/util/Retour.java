package com.production.api.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Retour {

	private RetourType retourType;

	private int httpCode;
	
	private String code;

	private String message;
}