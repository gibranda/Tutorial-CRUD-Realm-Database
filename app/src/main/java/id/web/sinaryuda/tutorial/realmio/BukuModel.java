package id.web.sinaryuda.tutorial.realmio;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sinaryuda on 6/11/16.
 */
public class BukuModel extends RealmObject {

    // untuk menentukan primary key dengan menggunakan anotasi @PrimaryKey
    @PrimaryKey
    private int id;
    private String penulis;
    private String judul;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPenulis() {
        return penulis;
    }

    public void setPenulis(String penulis) {
        this.penulis = penulis;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }
}
