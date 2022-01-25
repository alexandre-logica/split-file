package com.bigid.domain;

import java.util.Objects;

public class Location {

    private Integer lineOffset;
    private Integer charOffset;

    public Location(){
    }

    public Location(Integer lineOffset, Integer charOffset) {
        this.lineOffset = lineOffset;
        this.charOffset = charOffset;
    }

    public Integer getLineOffset() {
        return lineOffset;
    }

    public void setLineOffset(Integer lineOffset) {
        this.lineOffset = lineOffset;
    }

    public Integer getCharOffset() {
        return charOffset;
    }

    public void setCharOffset(Integer charOffset) {
        this.charOffset = charOffset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location)) return false;
        Location location = (Location) o;
        return getLineOffset().equals(location.getLineOffset()) && getCharOffset().equals(location.getCharOffset());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLineOffset(), getCharOffset());
    }

    @Override
    public String toString() {
        return "[" +
                "lineOffset=" + lineOffset +
                ", charOffset=" + charOffset +
                ']';
    }
}
