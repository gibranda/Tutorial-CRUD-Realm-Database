package id.web.sinaryuda.tutorial.realmio;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private static MainActivity mInstance = null;
    private FloatingActionButton fabAddBuku;
    private ListView lvDaftarBuku;
    private AlertDialog.Builder dialogHandler;
    private Realm dbRealm;
    private ArrayList<BukuModel> BukuArrayList = new ArrayList<BukuModel>();
    private BukuAdapter listBukuAdapter;

    public static MainActivity getInstance() {
        return mInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInstance = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lvDaftarBuku = (ListView) findViewById(R.id.lvBuku);

        dbRealm = Realm.getInstance(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TambahUbahBukuDialog(null, -1);
            }
        });

        lvDaftarBuku.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(MainActivity.this,DetailBukuActivity.class);
                intent.putExtra("BukuID", BukuArrayList.get(i).getId());
                startActivity(intent);
            }
        });

        listBukuAdapter = new BukuAdapter(this,BukuArrayList);
        lvDaftarBuku.setAdapter(listBukuAdapter);

        readDataBuku();
    }

    public void TambahUbahBukuDialog(final BukuModel model, final int posisiIndex) {

        LayoutInflater li = LayoutInflater.from(MainActivity.this);
        View promptsView = li.inflate(R.layout.buku_dialog, null);
        AlertDialog.Builder mainDialog = new AlertDialog.Builder(MainActivity.this);
        mainDialog.setView(promptsView);

        final EditText etJudulBuku = (EditText) promptsView.findViewById(R.id.etJudulBuku);
        final EditText etNamaPenulis = (EditText) promptsView.findViewById(R.id.etNamaPenulis);

        if (model != null) {
            etNamaPenulis.setText(model.getPenulis());
            etJudulBuku.setText(model.getJudul());
        }

        mainDialog.setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!etJudulBuku.getText().toString().trim().equals("") && !etNamaPenulis.getText().toString().trim().equals("")) {

                            BukuModel bukumodel = new BukuModel();

                            bukumodel.setPenulis(etNamaPenulis.getText().toString());
                            bukumodel.setJudul(etJudulBuku.getText().toString());

                            if (model == null) {
                                createDataBuku(bukumodel);
                            } else {
                                updateDataBuku(bukumodel, posisiIndex, model.getId());
                            }

                            dialog.cancel();
                        } else {
                            dialogHandler = new AlertDialog.Builder(MainActivity.this)
                                    .setMessage("Silahkan isi datanya!")
                                    .setCancelable(false)
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                            dialogHandler.show();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        final AlertDialog dialog = mainDialog.create();
        dialog.show();
    }

    private void createDataBuku(BukuModel model) {
        dbRealm.beginTransaction();
        BukuModel bukuModel = dbRealm.createObject(BukuModel.class);
        int id = dbRealm.where(BukuModel.class).max("id").intValue() + 1;
        bukuModel.setId(id);
        bukuModel.setPenulis(model.getPenulis());
        bukuModel.setJudul(model.getJudul());
        dbRealm.commitTransaction();
        BukuArrayList.add(bukuModel);
        listBukuAdapter.notifyDataSetChanged();
    }

    private void readDataBuku() {
        RealmResults<BukuModel> hasil = dbRealm.where(BukuModel.class).findAll();
        dbRealm.beginTransaction();
        if(!hasil.isEmpty()) {
            BukuArrayList.addAll(hasil);
            listBukuAdapter.notifyDataSetChanged();
        }
        dbRealm.commitTransaction();

    }

    private void updateDataBuku(BukuModel model, int posisiIndex, int id) {
        BukuModel editBukuModel = dbRealm.where(BukuModel.class).equalTo("id", id).findFirst();
        dbRealm.beginTransaction();
        editBukuModel.setPenulis(model.getPenulis());
        editBukuModel.setJudul(model.getJudul());
        dbRealm.commitTransaction();
        BukuArrayList.set(posisiIndex,editBukuModel);
        listBukuAdapter.notifyDataSetChanged();
    }

    public void deleteDataBuku(int dataID, int posisiIndex) {
        RealmResults<BukuModel> hasil = dbRealm.where(BukuModel.class).equalTo("id", dataID).findAll();
        dbRealm.beginTransaction();
        hasil.remove(0);
        dbRealm.commitTransaction();
        BukuArrayList.remove(posisiIndex);
        listBukuAdapter.notifyDataSetChanged();
    }

    public BukuModel searchBuku(int id) {
        RealmResults<BukuModel> hasil = dbRealm.where(BukuModel.class).equalTo("id", id).findAll();
        dbRealm.beginTransaction();
        dbRealm.commitTransaction();
        return hasil.first();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbRealm.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
