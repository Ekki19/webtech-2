package de.ehi.wt2.conf.auth;

import java.util.Collection;
import java.util.Collections;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import de.ehi.wt2.entity.DBUser;
import de.ehi.wt2.repository.UserRepository;

public class EHIRealm extends AuthorizingRealm implements Realm {

    final static String REALM = "EHI";

	@Autowired
	private UserRepository userRepository;
    
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken uPToken = (UsernamePasswordToken) token;
		DBUser user = null;
		if(uPToken.getUsername() == null || uPToken.getUsername().isEmpty()) {
			throw new UnknownAccountException("Username is null or empty!");
		} else {
			user = userRepository.findByUsernameAndPassword(uPToken.getUsername(), String.valueOf(uPToken.getPassword()));
			if(user == null) {
				throw new AuthenticationException();
			}
		}
		
		SimpleAccount simpleAccount = new SimpleAccount(user.getUsername(), user.getPassword(), REALM);
		simpleAccount.addRole(user.getRole());
		
		return simpleAccount;
	}
	
	@SuppressWarnings("serial")
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		return new AuthorizationInfo() {
			
            @Override
            public Collection<String> getRoles() {
                return Collections.emptyList();
            }

            @Override
            public Collection<String> getStringPermissions() {
                return Collections.emptyList();
            }

            @Override
            public Collection<Permission> getObjectPermissions() {
                //return Collections.singleton(new ReadNewsItemPermission());
            	return null;
            }
        };
	}

	

}
