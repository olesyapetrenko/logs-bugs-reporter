package telran.logs.bugs.dto;

import java.time.LocalDate;

import javax.validation.constraints.*;



public class BugAssignDto extends BugDto {
	@Min(1)
public long programmerId;

	public BugAssignDto(@NotNull Seriousness seriousness, @NotEmpty String discription, LocalDate dateOpen,
			@Min(1) long programmerId) {
		super(seriousness, discription, dateOpen);
		this.programmerId = programmerId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (int) (programmerId ^ (programmerId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		BugAssignDto other = (BugAssignDto) obj;
		if (programmerId != other.programmerId)
			return false;
		return true;
	}
	

}
