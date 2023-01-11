package local.authorization.resource.server.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name")
public class Store {

    public Store(String name) {
        this.name = name;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    /*@Enumerated(EnumType.STRING)
    private ProductionType productionType;*/

    @ManyToMany(cascade= CascadeType.ALL)
	@JoinTable(
			name = "store_brand_mapping",
			joinColumns = { @JoinColumn(name = "store_id") },
			inverseJoinColumns = { @JoinColumn(name = "brand_id") }
	)
    private List<Brand> brands;

    @OneToMany(mappedBy = "store", fetch = FetchType.EAGER)
    private List<Product> products;


}
