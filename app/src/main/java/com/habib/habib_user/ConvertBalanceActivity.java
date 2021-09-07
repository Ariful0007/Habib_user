package com.habib.habib_user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kaopiz.kprogresshud.KProgressHUD;

import es.dmoral.toasty.Toasty;

public class ConvertBalanceActivity extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    TextView no_of_items,total_amount;
    Button upgrade;
    KProgressHUD kProgressHUD;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert_balance);
        try {
            username=getIntent().getStringExtra("username");
        }catch (Exception e) {
            username=getIntent().getStringExtra("username");
        }
        kProgressHUD=KProgressHUD.create(ConvertBalanceActivity.this);
        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle("Wallet");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(10.0f);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        no_of_items=findViewById(R.id.no_of_items);
        total_amount=findViewById(R.id.total_amount);
        firebaseFirestore.collection("Users")
                .document(firebaseAuth.getCurrentUser().getUid())
                .collection("Main_Balance")
                .document(firebaseAuth.getCurrentUser().getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if(task.getResult().exists()) {
                                try {
                                    String taka=task.getResult().getString("main_balance");
                                    no_of_items.setText(taka);
                                }catch (Exception e) {
                                    String taka=task.getResult().getString("main_balance");
                                    no_of_items.setText(taka);
                                }
                            }
                            else {
                                try {
                                    no_of_items.setText("0");
                                }catch (Exception e) {
                                    no_of_items.setText("0");
                                }
                            }
                        }
                    }
                });

        //balance
        firebaseFirestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid())
                .collection("Main_Balance")
                .document(firebaseAuth.getCurrentUser().getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().exists()) {
                                try {
                                    total_amount.setText(task.getResult().getString("purches_balance"));
                                }catch (Exception e) {
                                    total_amount.setText(task.getResult().getString("purches_balance"));
                                }
                                //Toast.makeText(HomeActivity.this, ""+task.getResult().getString("coin"), Toast.LENGTH_SHORT).show();
                            }
                            else {
                                try {
                                    total_amount.setText("0");
                                }catch (Exception e) {
                                    total_amount.setText("0");
                                }
                                // Toast.makeText(HomeActivity.this, ""+firebaseAuth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
        upgrade=findViewById(R.id.upgrade);
        upgrade.setText("Convert");
        upgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(ConvertBalanceActivity.this);
                builder.setTitle("Convert Balance")
                        .setMessage("Are you want to convert balance?")
                        .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        }).setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        progress_check();
                        firebaseFirestore.collection("Users")
                                .document(firebaseAuth.getCurrentUser().getUid())
                                .collection("Main_Balance")
                                .document(firebaseAuth.getCurrentUser().getEmail())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            if (task.getResult().exists()) {
                                                try {
                                                    String taka=task.getResult().getString("main_balance");

                                                    String purches_balance=task.getResult().getString("purches_balance");
                                                    String giving_balance=task.getResult().getString("giving_balance");
                                                    String shoping_balance=task.getResult().getString("shoping_balance");
                                                    if (Integer.parseInt(taka)==0) {
                                                        kProgressHUD.dismiss();
                                                        Toasty.error(getApplicationContext(), "User hav't enough amount for convert", Toast.LENGTH_SHORT, true).show();
                                                    }
                                                    else if(Integer.parseInt(taka)>0){
                                                        int taka_20=(Integer.parseInt(taka)*20)/100;

                                                        int after_sub=Integer.parseInt(taka)-taka_20;
                                                        int shoping_balancesub=Integer.parseInt(shoping_balance)+(taka_20)/2;
                                                        int after_get=Integer.parseInt(purches_balance)+after_sub;

                                                        //Toast.makeText(ConvertBalanceActivity.this, ""+after_sub, Toast.LENGTH_SHORT).show();

                                                        firebaseFirestore.collection("Users")
                                                                .document(firebaseAuth.getCurrentUser().getUid())
                                                                .collection("Main_Balance")
                                                                .document(firebaseAuth.getCurrentUser().getEmail())
                                                                .update("main_balance", "0",
                                                                        "purches_balance", ""+after_get,"shoping_balance",""+shoping_balancesub)
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {

                                                                            kProgressHUD.dismiss();
                                                                            Toasty.success(ConvertBalanceActivity.this, "Done", Toast.LENGTH_SHORT).show();
                                                                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                                                                        }
                                                                    }
                                                                });

                                                    }
                                                }catch (Exception e) {
                                                    String taka=task.getResult().getString("main_balance");

                                                    String purches_balance=task.getResult().getString("purches_balance");
                                                    String giving_balance=task.getResult().getString("giving_balance");
                                                    String shoping_balance=task.getResult().getString("shoping_balance");
                                                    if (Integer.parseInt(taka)==0) {
                                                        kProgressHUD.dismiss();
                                                        Toasty.error(getApplicationContext(), "User hav't enough amount for convert", Toast.LENGTH_SHORT, true).show();
                                                    }
                                                    else if(Integer.parseInt(taka)>0){
                                                        int taka_20=(Integer.parseInt(taka)*20)/100;

                                                        int after_sub=Integer.parseInt(taka)-taka_20;
                                                        int after_get=Integer.parseInt(purches_balance)+after_sub;
                                                        int shoping_balancesub=Integer.parseInt(shoping_balance)+(taka_20)/2;

                                                        // Toast.makeText(ConvertBalanceActivity.this, ""+after_get, Toast.LENGTH_SHORT).show();

                                                        firebaseFirestore.collection("Users")
                                                                .document(firebaseAuth.getCurrentUser().getUid())
                                                                .collection("Main_Balance")
                                                                .document(firebaseAuth.getCurrentUser().getEmail())
                                                                .update("main_balance", "0",
                                                                        "purches_balance", ""+after_sub,"shoping_balance",""+shoping_balancesub)
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {

                                                                            kProgressHUD.dismiss();
                                                                            Toasty.success(ConvertBalanceActivity.this, "Done", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });

                                                    }


                                                }
                                            }
                                        }
                                    }
                                });

                    }
                }).create().show();
            }
        });



    }
    @Override
    public boolean onSupportNavigateUp() {
        Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
        intent.putExtra("username",username);
        startActivity(intent);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
        intent.putExtra("username",username);
        startActivity(intent);
    }
    private void progress_check() {
        kProgressHUD = KProgressHUD.create(ConvertBalanceActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Uploading Data")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

    }
    /*
    upgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(ConvertBalanceActivity.this);
                builder.setTitle("Convert Balance")o convert balance?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                progress_check();
                                firebaseFirestore.collection("Users")
                                        .document(firebaseAuth.getCurrentUser().getUid())
                                        .collection("Main_Balance")
                                        .document(firebaseAuth.getCurrentUser().getEmail())
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                         .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                                if (task.isSuccessful()) {
                                                    if(task.getResult().exists()) {
                                                        try {
                                                            String taka=task.getResult().getString("main_balance");

                                                            String purches_balance=task.getResult().getString("purches_balance");
                                                            String giving_balance=task.getResult().getString("giving_balance");
                                                            int c=Integer.parseInt(taka)+Integer.parseInt(purches_balance);
                                                            int d=Integer.parseInt(taka)+Integer.parseInt(giving_balance);
                                                            if (Integer.parseInt(taka)==0) {
                                                                kProgressHUD.dismiss();
                                                                Toasty.error(getApplicationContext(), "User hav't enough amount for convert", Toast.LENGTH_SHORT, true).show();
                                                            }
                                                            else {
                                                                firebaseFirestore.collection("Users")
                                                                        .document(firebaseAuth.getCurrentUser().getUid())
                                                                        .collection("Main_Balance")
                                                                        .document(firebaseAuth.getCurrentUser().getEmail())
                                                                        .update("main_balance", "0",
                                                                                "purches_balance", String.valueOf(c),
                                                                                "giving_balance", String.valueOf(d))
                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                if (task.isSuccessful()) {
                                                                                    kProgressHUD.dismiss();
                                                                                    Toasty.success(ConvertBalanceActivity.this, "Done", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            }
                                                                        });
                                                            }



                                                        }catch (Exception e) {

                                                            String taka=task.getResult().getString("main_balance");
                                                            String purches_balance=task.getResult().getString("purches_balance");
                                                            String giving_balance=task.getResult().getString("giving_balance");
                                                            int c=Integer.parseInt(taka)+Integer.parseInt(purches_balance);
                                                            int d=Integer.parseInt(taka)+Integer.parseInt(giving_balance);
                                                            if (Integer.parseInt(taka)==0) {
                                                                kProgressHUD.dismiss();
                                                                Toasty.error(getApplicationContext(), "User hav't enough amount for convert", Toast.LENGTH_SHORT, true).show();
                                                            }
                                                            else if(Integer.parseInt(taka)<100) {
                                                                kProgressHUD.dismiss();
                                                                Toasty.error(getApplicationContext(), "User hav't enough amount for convert", Toast.LENGTH_SHORT, true).show();
                                                            }
                                                            else if(Integer.parseInt(taka)>=100){
                                                                firebaseFirestore.collection("Users")
                                                                        .document(firebaseAuth.getCurrentUser().getUid())
                                                                        .collection("Main_Balance")
                                                                        .document(firebaseAuth.getCurrentUser().getEmail())
                                                                        .update("main_balance", "0",
                                                                                "purches_balance", String.valueOf(c),
                                                                                "giving_balance", String.valueOf(d))
                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                if (task.isSuccessful()) {
                                                                                    kProgressHUD.dismiss();
                                                                                    Toasty.success(ConvertBalanceActivity.this, "Done", Toast.LENGTH_SHORT).show();
                                                                                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                                                                    finish();
                                                                                }
                                                                            }
                                                                        });
                                                            }


                                                        }
                                                    }
                                                    else {

                                                        Toasty.error(getApplicationContext(),"No Data Found", Toast.LENGTH_SHORT,true).show();

                                                    }
                                                }
                                            }
                                        });

                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                }).create().show();


            }
        });
     */
}


