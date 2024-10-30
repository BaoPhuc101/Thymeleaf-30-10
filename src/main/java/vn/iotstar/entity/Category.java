package vn.iotstar.entity;

import java.io.Serializable;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "Category")
public class Category implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "categoryid")
	private Long id;
	
	@NotEmpty(message = "Not null")
	@Column(name = "categoryname", columnDefinition = "NVARCHAR(50)")
	private String name;
	
	@Column(name = "images", columnDefinition = "NVARCHAR(500)")
	private String images;
	
	private int status;
	
}
