package id.web.sinaryuda.tutorial.realmio;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class DetailBukuActivity extends AppCompatActivity {

    private TextView tvNamaPenulis, tvJudul;
    private BukuModel bukuModel =  new BukuModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_buku);

        tvNamaPenulis = (TextView) findViewById(R.id.tvNamaPenulis);
        tvJudul = (TextView) findViewById(R.id.tvJudul);

        int bukuID = getIntent().getIntExtra("BukuID", -1);
        bukuModel= MainActivity.getInstance().searchBuku(bukuID);

        tvNamaPenulis.setText(getString(R.string.buku_nama_penulis, bukuModel.getPenulis()));
        tvJudul.setText(getString(R.string.buku_judul,bukuModel.getJudul()));
    }
}
