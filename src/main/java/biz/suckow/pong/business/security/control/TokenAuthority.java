package biz.suckow.pong.business.security.control;

public class TokenAuthority {
    public static final String TOKEN_HEADER_NAME = "X-PONG-TOKEN";

    public boolean isAnIssue(final String token) {
	if ("4711".equals(token)) {
	    return false;
	}
	return true;
    }
}
