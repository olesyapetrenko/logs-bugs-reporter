package telran.logs.bugs.interfaces;

import reactor.core.publisher.Flux;
import telran.logs.bugs.dto.LogDto;
import telran.logs.bugs.dto.LogType;

public interface LogsInfo {
	Flux<LogDto> getAllLogs();
	Flux<LogDto> getAllExceptions();
	Flux<LogDto> getLogsType(LogType logType);
	
	
	

}
