package com.benji.mpesastkpush.mpesa.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CallbackMetadata{

	@JsonProperty("Item")
	private List<ItemItem> item;
}