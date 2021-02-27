package telran.logs.bugs.repo;

import reactor.core.publisher.Flux;
import telran.logs.bugs.dto.ArtifactCount;
import telran.logs.bugs.dto.LogTypeCount;

public interface LogStatistics {
	Flux<LogTypeCount> getLogTypeCounts();
	Flux<LogTypeCount> getMostEncounteredExceptions(int nExceptions);
	Flux<ArtifactCount> getArtifactCounts();
	Flux<ArtifactCount> getMostEncounteredArtifacts(int nArtifacts);
	

}
