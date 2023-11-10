package studentsinfo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class InMemorySecurityDemo {

	@Bean
	public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
		//System.out.println(passwordEncoder.getClass());

		UserDetails user = User.withUsername("lucky").password(passwordEncoder.encode("lucky")).roles("USER").build();

		UserDetails admin = User.withUsername("kalyan").password(passwordEncoder.encode("kalyan")).roles("USER", "ADMIN")
				.build();

		return new InMemoryUserDetailsManager(user, admin);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		return encoder;
	}
}