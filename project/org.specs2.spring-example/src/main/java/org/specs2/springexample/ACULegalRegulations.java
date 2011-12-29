package org.specs2.springexample;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * @author janmachacek
 */
@Component
@Profile("ACU")
public class ACULegalRegulations implements LegalRegulations {

	@Override
	public boolean hasDoped(Rider rider) {
		return false;
	}
}
