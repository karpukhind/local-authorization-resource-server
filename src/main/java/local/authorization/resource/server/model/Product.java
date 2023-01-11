package local.authorization.resource.server.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name")
public class Product {

	public Product(String name, BigDecimal price) {
		this.name = name;
		this.price = price;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String name;

	@Column
	private String description;

	@Column
	private BigDecimal price;

	/*@ManyToMany(cascade= CascadeType.ALL)
	@JoinTable(
			name = "Product_Store_mapping",
			joinColumns = { @JoinColumn(name = "product_id") },
			inverseJoinColumns = { @JoinColumn(name = "store_id") }
	)*/
	@ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
	private Store store;

	@ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
	private Brand brand;

}
