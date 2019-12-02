package com.example.vamsikikkuriproject;

public class Images {
    private String link;
    private String description;
    private String category;

    public Images(String link, String description, String category) {
        this.link = link;
        this.description = description;
        this.category = category;
    }

    public Images() {

    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }


}
