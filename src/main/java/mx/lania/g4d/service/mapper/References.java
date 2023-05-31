package mx.lania.g4d.service.mapper;

import com.fasterxml.jackson.annotation.*;

public class References {

    private String referencesShort;
    private String relative;
    private String full;

    @JsonProperty("short")
    public String getReferencesShort() {
        return referencesShort;
    }

    @JsonProperty("short")
    public void setReferencesShort(String value) {
        this.referencesShort = value;
    }

    @JsonProperty("relative")
    public String getRelative() {
        return relative;
    }

    @JsonProperty("relative")
    public void setRelative(String value) {
        this.relative = value;
    }

    @JsonProperty("full")
    public String getFull() {
        return full;
    }

    @JsonProperty("full")
    public void setFull(String value) {
        this.full = value;
    }
}
