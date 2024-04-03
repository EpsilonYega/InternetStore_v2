package api.Service;

import api.Models.User;
import api.Repositories.DataAccessLayer;
import api.Security.UserDetailsImpl;
import api.dto.SignupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class UserService implements UserDetailsService {
    private final DataAccessLayer dataAccessLayer;
    @Autowired
    public UserService(DataAccessLayer dataAccessLayer) {
        this.dataAccessLayer = dataAccessLayer;
    }
    public String newUser(SignupRequest signupRequest) {
        User user = new User();
        user.setUsername(signupRequest.getUserName());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(signupRequest.getPassword());
        return dataAccessLayer.newUserToDatabase(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = dataAccessLayer.getUserFromDatabaseByUsername(username);
        if (user == null) return null;
        return UserDetailsImpl.build(user);
    }
    public User loadUserEntityByUsername(String username) throws UsernameNotFoundException {
        return dataAccessLayer.getUserFromDatabaseByUsername(username);
    }

    public String hashUserPassword(String password) {
        HashPassword hashPassword = new HashPassword();
        return hashPassword.hashUserPassword(password);
    }
}
