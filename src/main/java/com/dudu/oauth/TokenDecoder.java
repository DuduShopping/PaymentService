package com.dudu.oauth;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;

import static java.nio.charset.StandardCharsets.US_ASCII;

public class TokenDecoder {
    private static final String TOKEN_TYPE = "JWT";
    private static final String TOKEN_ALGO = "RS256";
    private static final String TOKEN_ISSUER = "dudu";

    private RsaVerifier verifier;

    public TokenDecoder(String pubKey) {
        verifier = new RsaVerifier(pubKey);
    }

    /**
     *
     * @param token
     * @return claims
     * @throws Exception token is invalid, token expired, or system error.
     */
    public Claims getClaims(String token) throws Exception {
        var parts = token.split("\\.");
        if (parts.length != 3)
            throw new IllegalArgumentException("Expect token has three parts: header, claim and signature");

        // check on header
        var header = parts[0];
        header = new String(Base64.getDecoder().decode(header), US_ASCII);
        var headerJson = new JSONObject(new JSONTokener(header));
        if (!headerJson.get("typ").equals(TOKEN_TYPE))
            throw new IllegalArgumentException("Support token type: " + TOKEN_TYPE);

        if (!headerJson.get("alg").equals(TOKEN_ALGO))
            throw new IllegalArgumentException("Support token algorithm: " + TOKEN_ALGO);

        // verify signature
        var claims = parts[1];
        var claimsBytes = Base64.getDecoder().decode(claims);

        var signature = parts[2];
        var signatureBytes = Base64.getDecoder().decode(signature);

        verifier.verify(claimsBytes, signatureBytes);

        // verify token time
        var claimsJson = new JSONObject(new JSONTokener(new String(claimsBytes, US_ASCII)));

        if (!claimsJson.get("iss").equals(TOKEN_ISSUER))
            throw new IllegalArgumentException("Expect issuer: " + TOKEN_ISSUER);

        long issuedAtSeconds = claimsJson.getLong("iat");
        long expiredTimeSeconds = claimsJson.getLong("exp");

        var cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        if (issuedAtSeconds < cal.getTimeInMillis()/1000)
            throw new IllegalArgumentException("Issue time is too old");

        cal = Calendar.getInstance();
        if (expiredTimeSeconds < cal.getTimeInMillis()/1000)
            throw new IllegalArgumentException("Token time expired");

        // good token, extract all claims
        var tokenObj = new Claims();
        tokenObj.setIss(claimsJson.getString("iss"));
        tokenObj.setExp(claimsJson.getLong("exp"));
        tokenObj.setIat(claimsJson.getLong("iat"));
        tokenObj.setJti(claimsJson.getLong("jti"));
        if (claimsJson.has("UserId"))
            tokenObj.setUserId(claimsJson.getLong("UserId"));
        if (claimsJson.has("Scopes")) {
            tokenObj.setScopes(new ArrayList<>());
            for (var scope : claimsJson.getString("Scopes").split(","))
                tokenObj.getScopes().add(scope);
        }

        return tokenObj;
    }
}
