package com.habib.habib_user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import es.dmoral.toasty.Toasty;

public class PaymentActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private Spinner spinnerTextSize,spinnerTextSize1,spinnerTextSize2;
    EditText Email_Log;
    String valueFromSpinner;
    String valueFromSpinner1;
    String valueFromSpinner2;
    TextView coinsT1v;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    EditText spinner1,spinner2;
    Button upgrade;
    KProgressHUD kProgressHUD;
    LottieAnimationView empty_cart;


    DocumentReference documentReference;
    RecyclerView recyclerView;
    Payment_Adapter getDataAdapter1;
    List<Payment_request> getList;
    String url;

    FirebaseUser firebaseUser;
    KProgressHUD progressHUD;
    String cus_name;
    String main_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        spinner1=findViewById(R.id.spinner1);
        spinner2=findViewById(R.id.spinner2);
        upgrade=findViewById(R.id.upgrade);
        try {
            cus_name=getIntent().getStringExtra("username");
        }catch (Exception e) {
            cus_name=getIntent().getStringExtra("username");
        }
        // Toast.makeText(this, ""+cus_name, Toast.LENGTH_SHORT).show();
        kProgressHUD=KProgressHUD.create(PaymentActivity.this);

        empty_cart=findViewById(R.id.empty_cart);

        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle("Withdraw");
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(10.0f);
        //
        spinnerTextSize=findViewById(R.id.spinner3);
        spinnerTextSize.setOnItemSelectedListener(this);

        String[] textSizes = getResources().getStringArray(R.array.payment);
        ArrayAdapter adapter = new ArrayAdapter(this,
                R.layout.spinner_row, textSizes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTextSize.setAdapter(adapter);
        coinsT1v=findViewById(R.id.coinsT1v);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();


        //check
        firebaseFirestore = FirebaseFirestore.getInstance();
        //counting
        firebaseFirestore.collection("Payment_Request")
                .document(firebaseAuth.getCurrentUser().getEmail())
                .collection("List")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count = 0;
                            for (DocumentSnapshot document : task.getResult()) {
                                count++;
                            }
                            if (count == 0) {
                                empty_cart.setVisibility(View.VISIBLE);

                            } else {
                                empty_cart.setVisibility(View.INVISIBLE);
                            }
                        }

                    }
                });
        //
        getList = new ArrayList<>();
        getDataAdapter1 = new Payment_Adapter(getList);
        firebaseFirestore = FirebaseFirestore.getInstance();
        documentReference  =  firebaseFirestore.collection("Payment_Request")
                .document(firebaseAuth.getCurrentUser().getEmail())
                .collection("List").document();
        recyclerView =findViewById(R.id.rreeeed);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(PaymentActivity.this));
        recyclerView.setAdapter(getDataAdapter1);
        reciveData();

        //
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
                                    String taka=task.getResult().getString("cashwalet");
                                    coinsT1v.setText(taka);
                                }catch (Exception e) {
                                    String taka=task.getResult().getString("cashwalet");
                                    coinsT1v.setText(taka);
                                }
                            }
                            else {
                                try {
                                    coinsT1v.setText("0");
                                }catch (Exception e) {
                                    coinsT1v.setText("0");
                                }
                            }
                        }
                    }
                });
//Toasty.success(getApplicationContext(),""+main_account,Toasty.LENGTH_SHORT).show();

        upgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(spinner1.getText().toString()) || TextUtils.isEmpty(spinner2.getText().toString()) ||
                        TextUtils.isEmpty(valueFromSpinner)) {
                    Toasty.error(getApplicationContext(), "error", Toast.LENGTH_SHORT, true).show();
                    return;
                } else {
                    // progress_check();
                    int a = Integer.parseInt(coinsT1v.getText().toString());
                    int b = Integer.parseInt(spinner2.getText().toString());

                    if (a<300) {
                        kProgressHUD.dismiss();
                        Toasty.info(getApplicationContext(),"Payment below "+300+" taka is not allowed",Toasty.LENGTH_SHORT,true).show();
                    }
                    else {
                        if (a >= b) {
                            final EditText input = new EditText(PaymentActivity.this);
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT);
                            input.setLayoutParams(lp);
                            input.setHint("Enter Password");
                            new AlertDialog.Builder(PaymentActivity.this)
                                    .setTitle("User Password")
                                    .setPositiveButton("Okey", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();

                                            firebaseFirestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid())
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                if (task.getResult().exists()) {
                                                                    String pas = task.getResult().getString("pass");
                                                                    if (pas.contains(input.getText().toString().toLowerCase())) {
                                                                        final String uuid = UUID.randomUUID().toString();
                                                                        Calendar calendar = Calendar.getInstance();
                                                                        String current = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
                                                                        String current1 = DateFormat.getDateInstance().format(calendar.getTime());
                                                                        final Payment_request payment_request = new Payment_request(firebaseAuth.getCurrentUser().getEmail(),
                                                                                valueFromSpinner, spinner1.getText().toString(), spinner2.getText().toString(), current1, "Pending", uuid, cus_name);
                                                                        firebaseFirestore.collection("Payment_Request")
                                                                                .document(firebaseAuth.getCurrentUser().getEmail())
                                                                                .collection("List")
                                                                                .document(uuid)
                                                                                .set(payment_request)
                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        if (task.isSuccessful()) {
                                                                                            firebaseFirestore.collection("Admin_paymentRequest")
                                                                                                    .document(uuid)
                                                                                                    .set(payment_request)
                                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                                            if (task.isSuccessful()) {
                                                                                                                int main = Integer.parseInt(coinsT1v.getText().toString()) - Integer.parseInt(spinner2.getText().toString());
                                                                                                                firebaseFirestore.collection("Users")
                                                                                                                        .document(firebaseAuth.getCurrentUser().getUid())
                                                                                                                        .collection("Main_Balance")
                                                                                                                        .document(firebaseAuth.getCurrentUser().getEmail())
                                                                                                                        .update("main_balance", String.valueOf(main))
                                                                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                            @Override
                                                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                                                if (task.isSuccessful()) {
                                                                                                                                    kProgressHUD.dismiss();
                                                                                                                                    kProgressHUD.dismiss();
                                                                                                                                    kProgressHUD.dismiss();
                                                                                                                                    Toasty.success(getApplicationContext(), "Money Withdraw Request Successfully Done", Toast.LENGTH_SHORT, true).show();
                                                                                                                                }
                                                                                                                            }
                                                                                                                        });
                                                                                                            }
                                                                                                        }
                                                                                                    });
                                                                                        }
                                                                                    }
                                                                                });
                                                                    } else {
                                                                        kProgressHUD.dismiss();
                                                                        Toasty.error(getApplicationContext(), "Password not match", Toast.LENGTH_SHORT, true).show();
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    });

                                        }
                                    })
                                    .setIcon(R.drawable.habib_user)
                                    .setView(input)
                                    .show();


                        } else {
                            kProgressHUD.dismiss();
                            Toasty.error(getApplicationContext(), "User hav't enough amount", Toast.LENGTH_SHORT, true).show();
                        }

                    }


                }
            }

        });

    }

    private void reciveData() {
        firebaseFirestore.collection("Payment_Request")
                .document(firebaseAuth.getCurrentUser().getEmail())
                .collection("List").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentChange ds : queryDocumentSnapshots.getDocumentChanges()) {
                    if (ds.getType() == DocumentChange.Type.ADDED) {

                 /*String first;
                 first = ds.getDocument().getString("name");
                 Toast.makeText(MainActivity2.this, "" + first, Toast.LENGTH_SHORT).show();*/
                        Payment_request get = ds.getDocument().toObject(Payment_request.class);
                        getList.add(get);
                        getDataAdapter1.notifyDataSetChanged();
                    }

                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.spinner3) {
            valueFromSpinner = parent.getItemAtPosition(position).toString();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private void progress_check() {
        kProgressHUD = KProgressHUD.create(PaymentActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Uploading Data..")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

    }
    @Override
    public boolean onSupportNavigateUp() {
        Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
        intent.putExtra("username",cus_name);
        startActivity(intent);

        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
        intent.putExtra("username",cus_name);
        startActivity(intent);
    }
}