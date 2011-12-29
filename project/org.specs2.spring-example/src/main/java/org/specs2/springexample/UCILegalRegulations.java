package org.specs2.springexample;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * @author janmachacek
 */
@Component
@Profile("UCI")
public class UCILegalRegulations implements LegalRegulations {

	@Override
	public boolean hasDoped(Rider rider) {
		return true;
	}
}
