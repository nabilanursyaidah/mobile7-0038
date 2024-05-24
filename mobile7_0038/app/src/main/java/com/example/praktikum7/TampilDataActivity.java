package com.example.praktikum7;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class TampilDataActivity extends AppCompatActivity {

    private TableLayout tblMhs;
    private TableRow tr;
    private TextView coll, col2, col3;
    private RestHelper restHelper;
    private String stb, nama, angkatan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tampil_data);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inisialisasi restHelper dan tblMhs, kemudian panggil tampilData()
        restHelper = new RestHelper(this);
        tblMhs = findViewById(R.id.tbl_mhs);
        tampilData();
    }

    private void tampilData() {
        // Memanggil restHelper untuk mendapatkan data mahasiswa
        restHelper.getDataMhs(new RestCallbackMahasiswa() {
            @Override
            public void requestDataMhsSuccess(ArrayList<Mahasiswa> arrayList) {
                // Setelah mendapatkan data, tampilkan ke dalam tabel
                tampilTblMhs(arrayList);
            }
        });
    }

    private void tampilTblMhs(ArrayList<Mahasiswa> arrListMhs) {
        tblMhs.removeAllViews();

        tr = new TableRow(this);
        coll = new TextView(this);
        col2 = new TextView(this);
        col3 = new TextView(this);

        coll.setText("Stambuk");
        col2.setText("Nama Mahasiswa");
        col3.setText("Angkatan");

        coll.setWidth(200);
        col2.setWidth(300);
        col3.setWidth(150);

        tr.addView(coll);
        tr.addView(col2);
        tr.addView(col3);

        tblMhs.addView(tr);

        for (final Mahasiswa mhs : arrListMhs) {
            tr = new TableRow(this);
            coll = new TextView(this);
            col2 = new TextView(this);
            col3 = new TextView(this);

            coll.setText(mhs.getStb());
            col2.setText(mhs.getNama());
            col3.setText(String.valueOf(mhs.getAngkatan()));

            coll.setWidth(200);
            col2.setWidth(300);
            col3.setWidth(150);

            tr.addView(coll);
            tr.addView(col2);
            tr.addView(col3);

            tblMhs.addView(tr);

            tr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int i = 0; i < tblMhs.getChildCount(); i++) {
                        if (tblMhs.getChildAt(i) == view) {
                            stb = mhs.getStb();
                            nama = mhs.getNama();
                            angkatan = String.valueOf(mhs.getAngkatan());
                            if (tblMhs.getChildAt(i) == view)
                                tblMhs.getChildAt(i).setBackgroundColor(Color.LTGRAY);
                            else tblMhs.getChildAt(i).setBackgroundColor(Color.WHITE);
                        }
                    }
                }
            });
        }
    }

    public void btnEditClick(View view) {
        Intent intent = new Intent();
        intent.putExtra("stb", stb);
        intent.putExtra("nama", nama);
        intent.putExtra("angkatan", angkatan);
        setResult(1, intent);
        finish();
    }

    public void btnHapusClick(View view) {
        if (stb == null) return;
        restHelper.hapusData(stb, new RestCallbackMahasiswa() {
            @Override
            public void requestDataMhsSuccess(ArrayList<Mahasiswa> arrayList) {
                tampilTblMhs(arrayList);
            }
        });
    }
}