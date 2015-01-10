package biz.suckow.pong.business.security.entity;

public class User {
    public String token;
    public String username;
    public Role role;
    public String getToken() {
	return this.token;
    }
    public void setToken(final String token) {
	this.token = token;
    }
    public String getUsername() {
	return this.username;
    }
    public void setUsername(final String username) {
	this.username = username;
    }
    public Role getRole() {
	return this.role;
    }
    public void setRole(final Role role) {
	this.role = role;
    }
}
