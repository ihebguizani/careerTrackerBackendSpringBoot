package CareerTracker.models;

import java.time.LocalDate;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="User")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="user_role", discriminatorType = DiscriminatorType.STRING)
public class User {
	@Id
	@GeneratedValue
	private Long userId ;
	@Column(unique=true)
	private String username;
	private String firstname;
	private String lastname;
	private String password;
	@JsonIgnore
	private boolean enabled;
	private String email;



	@ManyToOne(cascade= {CascadeType.PERSIST, CascadeType.REFRESH,CascadeType.MERGE}, fetch=FetchType.EAGER)
	private Role role;


}
