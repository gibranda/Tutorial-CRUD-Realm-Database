package id.web.sinaryuda.tutorial.realmio;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sinaryuda on 6/14/16.
 */
public class BukuAdapter extends BaseAdapter {

    private ArrayList<BukuModel> listBuku;
    private Context context;
    private LayoutInflater inflater;

    public BukuAdapter(Context context, ArrayList<BukuModel> list){
        this.context = context;
        this.listBuku = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listBuku.size();
    }

    @Override
    public Object getItem(int i) {
        return listBuku.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        TextView tvJudul = null;
        ImageView ivUbah = null, ivDelete = null;

        if (view == null) {
            view = inflater.inflate(R.layout.list_item, null);
            tvJudul = (TextView) view.findViewById(R.id.tvJudulBuku);
            ivUbah = (ImageView) view.findViewById(R.id.ivUbahBuku);
            ivDelete = (ImageView) view.findViewById(R.id.ivHapusBuku);

        } else {

            tvJudul = (TextView) view.findViewById(R.id.tvJudulBuku);
            ivUbah = (ImageView) view.findViewById(R.id.ivUbahBuku);
            ivDelete = (ImageView) view.findViewById(R.id.ivHapusBuku);

            Log.d("DATA", listBuku.get(i).toString());
            tvJudul = (TextView) view.findViewById(R.id.tvJudulBuku);
            tvJudul.setText(listBuku.get(i).getJudul().toString());
        }

        tvJudul.setText(listBuku.get(i).getJudul().toString());
        ivUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BukuModel ubahBuku = MainActivity.getInstance().searchBuku(listBuku.get(i).getId());
                MainActivity.getInstance().TambahUbahBukuDialog(ubahBuku,i);
            }
        });

        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                konfirmasiDialog(context, listBuku.get(i).getId(),i);
            }
        });

        return view;
    }

    public static void konfirmasiDialog(Context context,final int dataId,final int position)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder
                .setMessage("Yakin datanya mau dihapus ?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        MainActivity.getInstance().deleteDataBuku(dataId, position);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
