package model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity
public class DogBreed {

    @ColumnInfo(name="breed_id")
    @SerializedName("id")
    public String breedId;

    @ColumnInfo(name="dog_name")
    @SerializedName("name")
    public String dogBreed;

    @ColumnInfo(name = "life_span")
    @SerializedName("life_span")
    public String lifeSpan;

    @ColumnInfo(name = "breed_group")       //not necessarily to have Column info if u want the column to hame same name as the name of variable
    @SerializedName("breed_group")
    public String breedGroup;

    @ColumnInfo(name="bred_for")
    @SerializedName("bred_for")
    public String bredFor;


    public String temperament;  //if filed names is exactly like the one from backend then it does not require @, else it requires

    @ColumnInfo(name="dog_url")
    @SerializedName("url")
    public String imageUrl;

    @PrimaryKey(autoGenerate = true)
    public int uuid;

    public DogBreed(String breedId, String dogBreed, String lifeSpan, String breedGroup, String bredFor, String temperament, String imageUrl) {
        this.breedId = breedId;
        this.dogBreed = dogBreed;
        this.lifeSpan = lifeSpan;
        this.breedGroup = breedGroup;
        this.bredFor = bredFor;
        this.temperament = temperament;
        this.imageUrl = imageUrl;
    }
}

