package local.authorization.resource.server.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDto {

    private String name;

    private String description;

    private Double price;

    private Long storeId;
}
