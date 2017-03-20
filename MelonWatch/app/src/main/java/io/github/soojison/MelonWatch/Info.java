package io.github.soojison.MelonWatch;

public class Info {
    private String title, blurb;

    public Info() {

    }

    public Info(String title, String blurb) {
        this.title = title;
        this.blurb = blurb;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBlurb() {
        return blurb;
    }

    public void setBlurb(String blurb) {
        this.blurb = blurb;
    }
}
