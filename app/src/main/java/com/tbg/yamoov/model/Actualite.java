package com.tbg.yamoov.model;

import java.util.Date;

public class Actualite {

    private String titre;
    private String description;
    private String image;
    private String date;

    public Actualite(String titre, String description, String image, String date) {
        this.titre = titre;
        this.description = description;
        this.image = image;
        this.date = date;
    }

    public Actualite() {

    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
