package casino.account.auth;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.inject.Singleton;
import javax.validation.constraints.NotBlank;

@Singleton
public class BCryptPasswordEncoderImpl implements PasswordEncoder {
    org.springframework.security.crypto.password.PasswordEncoder delegate = new BCryptPasswordEncoder();

    @Override
    public String encode(@NonNull @NotBlank String rawPassword) {

        return delegate.encode(rawPassword);
    }

    @Override
    public boolean matches(@NonNull @NotBlank String rawPassword, @NonNull @NotBlank String encodedPassword) {
        return delegate.matches(rawPassword, encodedPassword);
    }
}
