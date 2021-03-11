package telran.logs.bugs.client;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class EmailProviderClient {
	static Logger LOG = LoggerFactory.getLogger(EmailProviderClient.class);
	RestTemplate restTemplate = new RestTemplate();
	@Value("${app-url-assigner-mail:xxxx}")
	String urlAssignerMail;
	@Value("${app-url-programmer-mail:xxxx}")
	String mailByArtifact;

	public String getEmailByArtifact(String artifact) {
		String urlMailProvider = getUrlMailArtifact(artifact);
		String res = getMail(urlMailProvider);
		LOG.debug("Programmer mail is {}", res);
		return res;
	}

	public String getAssignerMail() {
		String urlMailProvider = getUrlAssigner();
		String res = getMail(urlMailProvider);
		LOG.debug("assigner email is {}", res);
		return res;

	}

	private String getMail(String urlMailProvider) {
		String res;
		try {
			ResponseEntity<String> response = restTemplate.exchange(urlMailProvider, HttpMethod.GET, null,
					String.class);
			res = response.getBody();
		} catch (RestClientException e) {
			LOG.error("request to url {} thrown exception {}", urlMailProvider, e.getMessage());
			res = "";
		}
		return res;
	}

	private String getUrlAssigner() {
		String res = urlAssignerMail + "/mail/assigner";
		LOG.debug("URL for getting assigner mail is {}", res);
		return res;
	}

	private String getUrlMailArtifact(String artifact) {
		String res = mailByArtifact + "/email/" + artifact;
		LOG.debug("url for getting email by artifact is {}", res);
		return res;
	}
}
