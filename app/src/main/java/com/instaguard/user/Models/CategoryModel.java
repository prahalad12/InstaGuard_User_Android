package com.instaguard.user.Models;

import java.io.Serializable;

public class CategoryModel implements Serializable {

    private String id;
    public String getId(){
        return id;
    }
    public void setId(String cabId){
        this.id = cabId;
    }

    private boolean is_selected;
    public boolean getiIsSelected(){
        return is_selected;
    }
    public void setIsSelected(boolean is_selected){
        this.is_selected = is_selected;
    }

    private String categoryId;
    public String getCategoryId(){
        return categoryId;
    }
    public void setCategoryId(String categoryId){
        this.categoryId = categoryId;
    }

    private String categoryName;
    public String getCategoryName(){
        return categoryName;
    }
    public void setCategoryName(String categoryName){
        this.categoryName = categoryName;
    }

    private String categoryImage;
    public String getCategoryImage(){
        return categoryImage;
    }
    public void setCategoryImage(String categoryImage){
        this.categoryImage = categoryImage;
    }

    private String catId;

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    private String catType;

    public String getcatType() {
        return catType;
    }

    public void setcatType(String catType) {
        this.catType = catType;
    }



    private String levelType;
    public String getLevelType(){
        return levelType;
    }
    public void setLevelType(String levelType){
        this.levelType = levelType;
    }

    private String LevelAmount;
    public String getLevelAmount(){
        return LevelAmount;
    }
    public void setLevelAmount(String LevelAmount){
        this.LevelAmount = LevelAmount;
    }

    private String categoryArName;
    public String getCategoryArName(){
        return categoryArName;
    }
    public void setCategoryArName(String categoryArName){
        this.categoryArName = categoryArName;
    }

    private String categoryEnName;
    public String getCategoryEnName(){
        return categoryEnName;
    }
    public void setCategoryEnName(String categoryEnName){
        this.categoryEnName = categoryEnName;
    }

}
