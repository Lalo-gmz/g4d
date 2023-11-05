package mx.lania.g4d.service.mapper;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Author {

    private long id;
    private String username;
    private String name;
    private String state;
    private String avatarURL;
    private String webURL;

    @JsonProperty("id")
    public long getID() {
        return id;
    }

    @JsonProperty("id")
    public void setID(long value) {
        this.id = value;
    }

    @JsonProperty("username")
    public String getUsername() {
        return username;
    }

    @JsonProperty("username")
    public void setUsername(String value) {
        this.username = value;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String value) {
        this.name = value;
    }

    @JsonProperty("state")
    public String getState() {
        return state;
    }

    @JsonProperty("state")
    public void setState(String value) {
        this.state = value;
    }

    @JsonProperty("avatar_url")
    public String getAvatarURL() {
        return avatarURL;
    }

    @JsonProperty("avatar_url")
    public void setAvatarURL(String value) {
        this.avatarURL = value;
    }

    @JsonProperty("web_url")
    public String getWebURL() {
        return webURL;
    }

    @JsonProperty("web_url")
    public void setWebURL(String value) {
        this.webURL = value;
    }
}
