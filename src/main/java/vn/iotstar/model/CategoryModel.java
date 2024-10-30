package vn.iotstar.model;

import java.io.Serializable;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryModel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	private Long id;
	
	@NotEmpty(message = "Not null")
	private String name;
	
	private String images;
	
	private int status;
	
	private Boolean isEdit = false;
	
}
