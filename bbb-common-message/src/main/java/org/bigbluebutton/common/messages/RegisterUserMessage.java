package org.bigbluebutton.common.messages;

import java.util.HashMap;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class RegisterUserMessage implements IBigBlueButtonMessage {
	public static final String REGISTER_USER = "register_user_request";
	public final String VERSION = "0.0.1";

	private static final String GUEST = "guest";
	private static final String AUTHENTICATED = "authenticated";


	public final String meetingID;
	public final String internalUserId;
	public final String fullname;
	public final String role;
	public final String externUserID;
	public final String authToken;
	public final String avatarURL;
	public final Boolean guest;
	public final Boolean authenticated;

	public RegisterUserMessage(String meetingID, String internalUserId, String fullname, String role,
							   String externUserID, String authToken, String avatarURL,
							   Boolean guest, Boolean authenticated) {
		this.meetingID = meetingID;
		this.internalUserId = internalUserId;
		this.fullname = fullname;
		this.role = role;
		this.externUserID = externUserID;
		this.authToken = authToken;
		this.avatarURL = avatarURL;
		this.guest = guest;
		this.authenticated = authenticated;
	}

	public String toJson() {
		HashMap<String, Object> payload = new HashMap<String, Object>();

		payload.put(Constants.MEETING_ID, meetingID);
		payload.put(Constants.INTERNAL_USER_ID, internalUserId);
		payload.put(Constants.NAME, fullname);
		payload.put(Constants.ROLE, role);
		payload.put(Constants.EXT_USER_ID, externUserID);
		payload.put(Constants.AUTH_TOKEN, authToken);
		payload.put(Constants.AVATAR_URL, avatarURL);
		payload.put(GUEST, guest);
		payload.put(AUTHENTICATED, authenticated);

		java.util.HashMap<String, Object> header = MessageBuilder.buildHeader(REGISTER_USER, VERSION, null);

		return MessageBuilder.buildJson(header, payload);				
	}
	public static RegisterUserMessage fromJson(String message) {
		JsonParser parser = new JsonParser();
		JsonObject obj = (JsonObject) parser.parse(message);

		if (obj.has("header") && obj.has("payload")) {
			JsonObject header = (JsonObject) obj.get("header");
			JsonObject payload = (JsonObject) obj.get("payload");

			if (header.has("name")) {
				String messageName = header.get("name").getAsString();
				if (REGISTER_USER.equals(messageName)) {
					if (payload.has(Constants.MEETING_ID)
							&& payload.has(Constants.NAME)
							&& payload.has(Constants.ROLE)
							&& payload.has(Constants.EXT_USER_ID)
							&& payload.has(Constants.AUTH_TOKEN)
							&& payload.has(GUEST)
							&& payload.has(AUTHENTICATED)) {

						String meetingID = payload.get(Constants.MEETING_ID).getAsString();
						String fullname = payload.get(Constants.NAME).getAsString();
						String role = payload.get(Constants.ROLE).getAsString();
						String externUserID = payload.get(Constants.EXT_USER_ID).getAsString();
						String authToken = payload.get(Constants.AUTH_TOKEN).getAsString();
						String avatarURL = payload.get(Constants.AVATAR_URL).getAsString();
						Boolean guest = payload.get(GUEST).getAsBoolean();
						Boolean authenticated = payload.get(AUTHENTICATED).getAsBoolean();

						//use externalUserId twice - once for external, once for internal
						return new RegisterUserMessage(meetingID, externUserID, fullname,
								role, externUserID, authToken, avatarURL, guest, authenticated);
					}
				}
			}
		}

		return null;
	}
}
