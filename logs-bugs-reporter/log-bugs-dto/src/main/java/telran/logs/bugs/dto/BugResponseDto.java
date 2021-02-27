package telran.logs.bugs.dto;

import java.time.LocalDate;

import javax.validation.constraints.*;

public class BugResponseDto extends BugAssignDto {
	public long bugId;
	public LocalDate dateClose;
	public BugStatus status;
	public OpeningMethod openingMethod;

	public BugResponseDto(long bugId, @NotNull Seriousness seriousness, @NotEmpty String description,
			LocalDate dateOpen, @Min(1) long programmerId, LocalDate dateClose, BugStatus status,
			OpeningMethod openingMethod) {
		super(seriousness, description, dateOpen, programmerId);
		this.dateClose = dateClose;
		this.status = status;
		this.openingMethod = openingMethod;
		this.bugId = bugId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (int) (bugId ^ (bugId >>> 32));
		result = prime * result + ((dateClose == null) ? 0 : dateClose.hashCode());
		result = prime * result + ((openingMethod == null) ? 0 : openingMethod.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
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
		BugResponseDto other = (BugResponseDto) obj;
		if (bugId != other.bugId)
			return false;
		if (dateClose == null) {
			if (other.dateClose != null)
				return false;
		} else if (!dateClose.equals(other.dateClose))
			return false;
		if (openingMethod != other.openingMethod)
			return false;
		if (status != other.status)
			return false;
		return true;
	}

}
