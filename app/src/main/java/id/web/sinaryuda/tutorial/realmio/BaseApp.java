package id.web.sinaryuda.tutorial.realmio;

import android.app.Application;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 * Created by sinaryuda on 6/11/16.
 */
public class BaseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //konfigurasi Realm
        RealmConfiguration config = new RealmConfiguration.Builder(this)
                .schemaVersion(0) // versi schema database yg digunakan
                .migration(new DataMigration())
                .build();

        Realm.setDefaultConfiguration(config);

    }

    private class DataMigration implements RealmMigration {
        @Override
        public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

            //mengakses schema database untuk tujuan membuat, mengubah atau menghapus kelas dan kolom.
            RealmSchema schema = realm.getSchema();

            //jika versi 0 maka buat schema baru
            if (oldVersion == 0) {
                // buat kelas buku
                schema.create("buku")
                        .addField("id", int.class)
                        .addField("penulis", String.class)
                        .addField("judul", String.class);
                oldVersion++;
            }
        }
    }

}
