package local.authorization.resource.server.controller.rest.forms;

import java.math.BigDecimal;

public class ProductForm {
	private String name;

	private BigDecimal price;

	public String getName() {
		return name;
	}

	public BigDecimal getPrice() {
		return price;
	}
}
