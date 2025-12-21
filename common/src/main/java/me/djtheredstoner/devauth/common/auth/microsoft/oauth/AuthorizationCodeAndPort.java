package me.djtheredstoner.devauth.common.auth.microsoft.oauth;

public class AuthorizationCodeAndPort {
	private final String code;
	private final int port;

	public AuthorizationCodeAndPort(String code, int port) {
		this.code = code;
		this.port = port;
	}
	
	public String getCode() {
		return code;
	}
	
	public int getPort() {
		return port;
	}
}
