package com.habib.habib_user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flatdialoglibrary.dialog.FlatDialog;
import com.geniusforapp.fancydialog.FancyAlertDialog;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.mikepenz.crossfadedrawerlayout.view.CrossfadeDrawerLayout;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.MiniDrawer;
import com.mikepenz.materialdrawer.interfaces.ICrossfader;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;
import com.mikepenz.materialize.util.UIUtils;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import es.dmoral.toasty.Toasty;

public class HomeActivity extends AppCompatActivity {

    private UserSession session;
    private HashMap<String, String> user;
    private String name, email, photo, mobile,username;
    private Drawer result;
    private CrossfadeDrawerLayout crossfadeDrawerLayout = null;
    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String user1;

    IProfile profile;
    TextView nameTv,emailTv;
    ImageView profileImage;
    TextView coinsT1v;
    CardView dailyCheckCard,luckySpinCard,aboutCard1,aboutCard,redeemCard,referCard,taskCard;
    FirebaseFirestore firebaseFirestore;
    String sessionname,sessionmobile,sessionphoto,sessionemail,sessionusername;
    int count,count1,count2,count3;
    String package_actove;
    String daily_bonus;
    String incomeType="Daily Task";
    int main_account;
    int count12,count123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FirebaseApp.initializeApp(HomeActivity.this);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        firebaseFirestore=FirebaseFirestore.getInstance();
        user1=firebaseUser.getEmail();
        nameTv=findViewById(R.id.nameTv);
        emailTv=findViewById(R.id.emailTv);
        profileImage=findViewById(R.id.profileImage);
        taskCard=findViewById(R.id.taskCard);
        my_detector();
        royal_ckud();
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);



       /*
        Map<String, String> userMap2 = new HashMap<>();

        userMap2.put("main_balance","2586");
        userMap2.put("purches_balance","10000000");
        userMap2.put("giving_balance","0");
        userMap2.put("self_income","0");
        userMap2.put("sponsor_income","0");
        userMap2.put("first_level","2586");
        userMap2.put("second_level","0");
        userMap2.put("third_level","0");
        userMap2.put("forth_level","0");
        userMap2.put("fifth_level","0");
        userMap2.put("shoping_balance","0");
        userMap2.put("cashwalet","0");
        firebaseFirestore.collection("Users")
                .document(firebaseAuth.getCurrentUser().getUid())
                .collection("Main_Balance")
                .document(firebaseAuth.getCurrentUser().getEmail())
                .set(userMap2)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                        }
                    }
                });
        Calendar date = Calendar.getInstance();
        String dayToday = android.text.format.DateFormat.format("EEEE", date).toString();
        Toast.makeText(this, ""+dayToday, Toast.LENGTH_SHORT).show();
        */
       /*
        Map<String, String> userMap7 = new HashMap<>();

        userMap7.put("uuid",firebaseAuth.getCurrentUser().getUid());
        firebaseFirestore.collection("All_UserID")
                .document(firebaseAuth.getCurrentUser().getEmail())
                .set(userMap7)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                        }
                    }
                });
        */

        taskCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),PaymentActivity.class);
                intent.putExtra("name",username);
                startActivity(intent);
            }
        });
        referCard=findViewById(R.id.referCard);
        referCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        dailyCheckCard=findViewById(R.id.dailyCheckCard);
        dailyCheckCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Toasty.success(getApplicationContext(),"You are home now",Toasty.LENGTH_SHORT,true).show();
            }
        });
        luckySpinCard=findViewById(R.id.luckySpinCard);
        luckySpinCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),ConvertBalanceActivity.class);
                intent.putExtra("name",username);
                startActivity(intent);


            }
        });
        CardView watchCard=findViewById(R.id.watchCard);
        watchCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),MYTEAM.class);
             //   intent.putExtra("name",username);
                startActivity(intent);

            }
        });
       /*
        CardView aboutCard3=findViewById(R.id.aboutCard3);
        aboutCard3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ShoesActivity.class));
            }
        });
          CardView aboutCard21=findViewById(R.id.aboutCard21);
        aboutCard21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), HelpLineActivity.class));
            }
        });
           aboutCard1=findViewById(R.id.aboutCard1);
        aboutCard1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MYTeam2Activity.class));
            }
        });
        //card
        aboutCard=findViewById(R.id.aboutCard);
        aboutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HistoryActivity.class));
            }
        });

        */

        //


        //paymentreport
        redeemCard=findViewById(R.id.redeemCard);
        redeemCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent=new Intent(getApplicationContext(),ChatActivity.class);
                intent.putExtra("name",username);
                startActivity(intent);

            }
        });
        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();

        session = new UserSession(getApplicationContext());
        Log.d("xlr8_wlv", String.valueOf(session.getWishlistValue()));

        Typeface typeface = ResourcesCompat.getFont(this, R.font.blacklist);
        TextView appname = findViewById(R.id.appname);
        appname.setTypeface(typeface);
        coinsT1v=findViewById(R.id.coinsT1v);
        firebaseFirestore=FirebaseFirestore.getInstance();



        //retrieve session values and display on listviews
        getValues();

        inflateNavDrawer();
        nameTv.setText(username);
        emailTv.setText(email);



       /*
        try {
            String image22="https://firebasestorage.googleapis.com/v0/b/cash-money-express-ltd.appspot.com/o/profile_images%2Fo8Dnqf5LFodKSwocGQ4nKB7ZEkW2.jpg?alt=media&token=c22700e2-67ca-4497-8bf1-204ac83b6749";
            if (photo.contains(image22)) {
                Picasso.get().load(R.drawable.salary).into(profileImage);
            }
            else {
                Picasso.get().load(photo).into(profileImage);
            }
        }catch (Exception e) {
            e.printStackTrace();
            String image22="https://firebasestorage.googleapis.com/v0/b/cash-money-express-ltd.appspot.com/o/profile_images%2Fo8Dnqf5LFodKSwocGQ4nKB7ZEkW2.jpg?alt=media&token=c22700e2-67ca-4497-8bf1-204ac83b6749";
            if (photo.contains(image22)) {
                Picasso.get().load(R.drawable.salary).into(profileImage);
            }
            else {
                Picasso.get().load(photo).into(profileImage);
            }
          //  Picasso.get().load(photo).placeholder(R.drawable.profile_image).into(profileImage);
        }
        */




        // initializing our slider view and
        // firebase firestore instance.
        db = FirebaseFirestore.getInstance();
        db.collection("Users").document(firebaseAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().exists()) {
                                String imagee=task.getResult().getString("image");
                                String image22="https://firebasestorage.googleapis.com/v0/b/cash-money-express-ltd.appspot.com/o/profile_images%2Fo8Dnqf5LFodKSwocGQ4nKB7ZEkW2.jpg?alt=media&token=c22700e2-67ca-4497-8bf1-204ac83b6749";
                                try {
                                    if (photo.contains(image22)) {
                                        Picasso.get().load(R.drawable.fuking_logo).into(profileImage);
                                    }
                                    else {
                                        Picasso.get().load(photo).into(profileImage);
                                    }
                                }catch (Exception e) {
                                    e.printStackTrace();
                                    if (photo.contains(image22)) {
                                        Picasso.get().load(R.drawable.fuking_logo).into(profileImage);
                                    }
                                    else {
                                        Picasso.get().load(photo).into(profileImage);
                                    }
                                }

                                /*
                                try {
                                    Picasso.get().load(imagee).into(profileImage);
                                }catch (Exception e) {
                                    e.printStackTrace();
                                    Picasso.get().load(imagee).into(profileImage);
                                    //  Picasso.get().load(photo).placeholder(R.drawable.profile_image).into(profileImage);
                                }
                                 */
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
                                                            coinsT1v.setText(taka);
                                                        }catch (Exception e) {
                                                            String taka=task.getResult().getString("main_balance");
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


                                db.collection("Users").document(firebaseAuth.getCurrentUser().getUid())
                                        .collection("Coins")
                                        .document(firebaseAuth.getCurrentUser().getEmail())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    if (task.getResult().exists()) {
                                                        //coinsT1v.setText(task.getResult().getString("coin"));
                                                        //Toast.makeText(HomeActivity.this, ""+task.getResult().getString("coin"), Toast.LENGTH_SHORT).show();
                                                    }
                                                    else {
                                                        // Toast.makeText(HomeActivity.this, ""+firebaseAuth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                        });

                            }
                        }
                    }
                });



        if (session.getFirstTime()) {
            //tap target view
            tapview();
            session.setFirstTime(false);
        }


    }

    private void royal_ckud() {
        firebaseFirestore.collection("Users")
                .document(firebaseAuth.getCurrentUser().getUid())
                .collection("Main_Balance")
                .document(firebaseAuth.getCurrentUser().getEmail())
                .update("monthly_income","0","leader_club","0")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                        }
                    }
                });
    }







    private void my_detector() {
        firebaseFirestore.collection("ALL_GET")
                .document(firebaseAuth.getCurrentUser().getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().exists()) {
                                String refername=task.getResult().getString("refername");
                                String referemail=task.getResult().getString("refername_email");

                                firebaseFirestore.collection("ALL_GET")
                                        .document(referemail)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    if (task.getResult().exists()) {
                                                        String refername1=task.getResult().getString("refername");
                                                        final String referemail1=task.getResult().getString("refername_email");
                                                        firebaseFirestore.collection("All_UserID")
                                                                .document(referemail1)
                                                                .get()
                                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                        if (task.isSuccessful()) {
                                                                            if (task.getResult().exists()) {
                                                                                String uuid=task.getResult().getString("uuid");
                                                                                firebaseFirestore.collection("Users")
                                                                                        .document(uuid)
                                                                                        .collection("Main_Balance")
                                                                                        .document(referemail1)
                                                                                        .get()
                                                                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                if (task.isSuccessful()) {
                                                                                                    if (task.getResult().exists()) {
                                                                                                        String mainbanalce=task.getResult().getString("main_balance");
                                                                                                        //Toast.makeText(HomeActivity.this, ""+((4*600)/100), Toast.LENGTH_SHORT).show();
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        });
                                                                            }
                                                                        }
                                                                    }
                                                                });

                                                    }
                                                }
                                            }
                                        });


                            }
                        }
                    }
                });
    }

    /*
    private void go_toCount() {
        firebaseFirestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid())
                .collection("Counter")
                .document(firebaseAuth.getCurrentUser().getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            if (task.getResult().exists()) {


                            }
                            else {

                                Map<String, String> userMap1 = new HashMap<>();

                                userMap1.put("counter","1");
                                firebaseFirestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid())
                                        .collection("Counter")
                                        .document( firebaseAuth.getCurrentUser().getEmail())
                                        .set(userMap1)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Map<String, String> userMap2 = new HashMap<>();

                                                    userMap2.put("main_balance","0");
                                                    userMap2.put("purches_balance","0");
                                                    userMap2.put("giving_balance","0");
                                                    userMap2.put("self_income","0");
                                                    userMap2.put("sponsor_income","0");
                                                    userMap2.put("first_level","0");
                                                    userMap2.put("second_level","0");
                                                    userMap2.put("third_level","0");
                                                    userMap2.put("forth_level","0");
                                                    userMap2.put("fifth_level","0");

                                                    firebaseFirestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid())
                                                            .collection("Main_Balance")
                                                            .document( firebaseAuth.getCurrentUser().getEmail())
                                                            .set(userMap2)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        Map<String, String> userMap3 = new HashMap<>();

                                                                        userMap3.put("rating","0");
                                                                        firebaseFirestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid())
                                                                                .collection("Rating")
                                                                                .document( firebaseAuth.getCurrentUser().getEmail())
                                                                                .set(userMap3)
                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        if (task.isSuccessful()) {
                                                                                            Map<String, String> userMap4 = new HashMap<>();

                                                                                            userMap4.put("package","Inactive");
                                                                                            firebaseFirestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid())
                                                                                                    .collection("Package")
                                                                                                    .document( firebaseAuth.getCurrentUser().getEmail())
                                                                                                    .set(userMap4)
                                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                                            if (task.isSuccessful()) {
                                                                                                                Map<String, String> userMap5= new HashMap<>();

                                                                                                                userMap5.put("ammount","0");
                                                                                                                firebaseFirestore.collection("SendMoney").document(firebaseAuth.getCurrentUser().getEmail())
                                                                                                                        .set(userMap5)
                                                                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                            @Override
                                                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                                                if (task.isSuccessful()) {
                                                                                                                                }
                                                                                                                            }
                                                                                                                        });
                                                                                                            }
                                                                                                        }
                                                                                                    });
                                                                                        }
                                                                                    }
                                                                                });


                                                                    }
                                                                }
                                                            });


                                                }
                                            }
                                        });



                            }////
                        }
                    }
                });

    }
     */

    private void tapview() {
        new TapTargetSequence(this)
                .targets(
                        TapTarget.forView(findViewById(R.id.notifintro), "Notifications", "Latest offers will be available here !")
                                .targetCircleColor(R.color.white)
                                .titleTextColor(R.color.white)
                                .titleTextSize(25)
                                .descriptionTextSize(15)
                                .descriptionTextColor(R.color.white)
                                .drawShadow(true)                   // Whether to draw a drop shadow or not
                                .cancelable(true)// Whether tapping outside the outer circle dismisses the view
                                .tintTarget(true)
                                .transparentTarget(true)
                                .outerCircleColor(R.color.first),
                        TapTarget.forView(findViewById(R.id.view_profile), "Profile", "You can view and edit your profile here !")
                                .targetCircleColor(R.color.white)
                                .titleTextColor(R.color.white)
                                .titleTextSize(25)
                                .descriptionTextSize(15)
                                .descriptionTextColor(R.color.white)
                                .drawShadow(true)                   // Whether to draw a drop shadow or not
                                .cancelable(true)                  // Whether tapping outside the outer circle dismisses the view
                                .tintTarget(true)
                                .transparentTarget(true)
                                .outerCircleColor(R.color.third),
                        TapTarget.forView(findViewById(R.id.dailyCheckCard), "Home", "You  are home now.")
                                .targetCircleColor(R.color.white)
                                .titleTextColor(R.color.white)
                                .titleTextSize(25)
                                .descriptionTextSize(15)
                                .descriptionTextColor(R.color.white)
                                .drawShadow(true)
                                .cancelable(true)// Whether tapping outside the outer circle dismisses the view
                                .tintTarget(true)
                                .transparentTarget(true)
                                .outerCircleColor(R.color.second),
                        TapTarget.forView(findViewById(R.id.luckySpinCard), "Transcation ", "Your all transcation will be here")
                                .targetCircleColor(R.color.white)
                                .titleTextColor(R.color.white)
                                .titleTextSize(25)
                                .descriptionTextSize(15)
                                .descriptionTextColor(R.color.white)
                                .drawShadow(true)
                                .cancelable(true)// Whether tapping outside the outer circle dismisses the view
                                .tintTarget(true)
                                .transparentTarget(true)
                                .outerCircleColor(R.color.fourth))
                .listener(new TapTargetSequence.Listener() {
                    // This listener will tell us when interesting(tm) events happen in regards
                    // to the sequence
                    @Override
                    public void onSequenceFinish() {
                        //session.setFirstTime(false);
                        Toasty.success(HomeActivity.this, " You are ready to go !", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {
                        // Boo
                    }
                }).start();
    }

    private void getValues() {
        //validating session
        session.isLoggedIn();

        //get User details if logged in
        user = session.getUserDetails();

        name = user.get(UserSession.KEY_NAME);
        email = user.get(UserSession.KEY_EMAIL);
        mobile = user.get(UserSession.KEY_MOBiLE);
        photo = user.get(UserSession.KEY_PHOTO);
        username=user.get(UserSession.Username);
        //Toast.makeText(this, ""+username, Toast.LENGTH_SHORT).show();
    }

    private void inflateNavDrawer() {

        //set Custom toolbar to activity -----------------------------------------------------------
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the AccountHeader ----------------------------------------------------------------

        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                Picasso.get().load(uri).placeholder(placeholder).into(imageView);
            }
            @Override
            public void cancel(ImageView imageView) {
                Picasso.get().cancelRequest(imageView);
            }
        });


        //Profile Making
       /*
        IProfile profile = new ProfileDrawerItem()
                .withName(name)
                .withEmail(email)
                .withIcon(photo);
        */
        String image22="https://firebasestorage.googleapis.com/v0/b/cash-money-express-ltd.appspot.com/o/profile_images%2Fo8Dnqf5LFodKSwocGQ4nKB7ZEkW2.jpg?alt=media&token=c22700e2-67ca-4497-8bf1-204ac83b6749";

        //String image22="https://firebasestorage.googleapis.com/v0/b/mydex-91780.appspot.com/o/profile_images%2FNt4rITrPbqc9e1AHPKPaLb4IcFI3.jpg?alt=media&token=d2da7b65-ed6a-41c9-8d1c-ade16adddc3a";
        if (photo.contains(image22)) {
            profile = new ProfileDrawerItem()
                    .withName(name)
                    .withEmail(email)
                    .withIcon(R.drawable.profile_image);

        }
        else {
            profile = new ProfileDrawerItem()
                    .withName(name)
                    .withEmail(email)
                    .withIcon(photo);
        }

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.gradient_background)
                .addProfiles(profile)
                .withCompactStyle(true)
                .build();

        //Adding nav drawer items ------------------------------------------------------------------
        //Adding nav drawer items ------------------------------------------------------------------
        //Adding nav drawer items ------------------------------------------------------------------
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.home).withIcon(R.drawable.home);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(2).withName(R.string.myprofile).withIcon(R.drawable.profile);
        PrimaryDrawerItem item5 = new PrimaryDrawerItem().withIdentifier(5).withName(R.string.logout).withIcon(R.drawable.logout);

        PrimaryDrawerItem item7 = new PrimaryDrawerItem().withIdentifier(7).withName("Income History").withIcon(R.drawable.hh);
        PrimaryDrawerItem item8 = new PrimaryDrawerItem().withIdentifier(8).withName("Change Password").withIcon(R.drawable.passa);

        //creating navbar and adding to the toolbar ------------------------------------------------
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHasStableIds(true)
                .withDrawerLayout(R.layout.crossfade_drawer)
                .withAccountHeader(headerResult)
                .withDrawerWidthDp(72)
                .withGenerateMiniDrawer(true)
                .withTranslucentStatusBar(true)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(item1,item2,item5, new DividerDrawerItem(), item7, item8,new DividerDrawerItem()).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        switch (position){

                            case 1:
                                if (result != null && result.isDrawerOpen()) {
                                    result.closeDrawer();
                                }
                                break;
                            case 11:
                                result.closeDrawer();
                              //  startActivity(new Intent(HomeActivity.this, HelpLineActivity.class));
                                break;
                            case 2:
                                result.closeDrawer();
                              startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                                break;



                            case 3:
                                AlertDialog.Builder warning = new AlertDialog.Builder(HomeActivity.this)
                                        .setTitle("Logout")
                                        .setMessage("Are you want to logout?")
                                        .setPositiveButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();



                                            }
                                        }).setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // ToDO: delete all the notes created by the Anon user

                                                // TODO: delete the anon user
                                                firebaseAuth.signOut();
                                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                                finish();


                                            }
                                        });

                                warning.show();

                                result.closeDrawer();
                                break;
                            case 4:
                              startActivity(new Intent(HomeActivity.this, HistoryActivity.class));
                                result.closeDrawer();
                                break;
                            case 9:
                                final FlatDialog flatDialog1 = new FlatDialog(HomeActivity.this);
                                flatDialog1.setTitle("Change Password")
                                        .setSubtitle("User forget his/her password.Now as a admin you change it.")
                                        .setSecondTextFieldHint("password")
                                        .setFirstButtonText("Change")
                                        .setSecondButtonText("Cancel")
                                        .withFirstButtonListner(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                flatDialog1.dismiss();
                                                final KProgressHUD progressDialog=  KProgressHUD.create(HomeActivity.this)
                                                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                                                        .setLabel("Please wait")
                                                        .setCancellable(false)
                                                        .setAnimationSpeed(2)
                                                        .setDimAmount(0.5f)
                                                        .show();
                                                firebaseFirestore.collection("Password")
                                                        .document(firebaseAuth.getCurrentUser().getEmail())
                                                        .get()
                                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                if (task.isSuccessful()) {
                                                                    if (task.getResult().exists()) {
                                                                        firebaseFirestore.collection("Password")
                                                                                .document(firebaseAuth.getCurrentUser().getEmail())
                                                                                .update("uuid",flatDialog1.getSecondTextField().toLowerCase().toString())
                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        if (task.isSuccessful()) {
                                                                                            firebaseFirestore.collection("All_UserID")
                                                                                                    .document(firebaseAuth.getCurrentUser().getEmail())
                                                                                                    .get()
                                                                                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                        @Override
                                                                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                            if (task.isSuccessful()) {
                                                                                                                if (task.getResult().exists()) {
                                                                                                                    String uuid=task.getResult().getString("uuid");
                                                                                                                    firebaseFirestore.collection("Users")
                                                                                                                            .document(uuid)
                                                                                                                            .get()
                                                                                                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                                                @Override
                                                                                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                                                    if (task.isSuccessful()) {
                                                                                                                                        if (task.getResult().exists()) {
                                                                                                                                            firebaseFirestore.collection("Users")
                                                                                                                                                    .document(uuid)
                                                                                                                                                    .update("pass",flatDialog1
                                                                                                                                                            .getSecondTextField().toLowerCase().toString())
                                                                                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                                        @Override
                                                                                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                                                                                            if (task.isSuccessful()) {
                                                                                                                                                                progressDialog.dismiss();
                                                                                                                                                                Toasty.success(getApplicationContext(),"Done",Toasty.LENGTH_SHORT,true).show();
                                                                                                                                                            }
                                                                                                                                                        }
                                                                                                                                                    });
                                                                                                                                        }
                                                                                                                                    }
                                                                                                                                }
                                                                                                                            });
                                                                                                                }
                                                                                                            }
                                                                                                        }
                                                                                                    });


                                                                                        }
                                                                                    }
                                                                                });

                                                                    }
                                                                    else {
                                                                        progressDialog.dismiss();
                                                                        Toasty.error(getApplicationContext(),"No user found",Toasty.LENGTH_SHORT,true).show();
                                                                    }
                                                                }
                                                            }
                                                        });
                                            }
                                        })
                                        .withSecondButtonListner(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                flatDialog1.dismiss();
                                            }
                                        })
                                        .show();
                                result.closeDrawer();
                                break;



                            case 10:
                                final FlatDialog flatDialog = new FlatDialog(HomeActivity.this);
                                flatDialog.setCancelable(false);
                                flatDialog.setTitle("Send a message")

                                        .setTitleColor(Color.parseColor("#000000"))
                                        .setBackgroundColor(Color.parseColor("#f5f0e3"))
                                        .setLargeTextFieldHint("write your text here ...")
                                        .setLargeTextFieldHintColor(Color.parseColor("#000000"))
                                        .setLargeTextFieldBorderColor(Color.parseColor("#000000"))
                                        .setLargeTextFieldTextColor(Color.parseColor("#000000"))
                                        .setFirstButtonColor(Color.parseColor("#fda77f"))
                                        .setFirstButtonTextColor(Color.parseColor("#000000"))
                                        .setFirstButtonText("Done")

                                        .  setSecondButtonText("Cancel")
                                        .withFirstButtonListner(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                flatDialog.dismiss();
                                                Map<String, String> userMap41 = new HashMap<>();
                                                userMap41.put("username",username);

                                                userMap41.put("feedback",flatDialog.getLargeTextField().toLowerCase().toString());
                                                firebaseFirestore=FirebaseFirestore.getInstance();
                                                firebaseFirestore.collection("Customer_feedback")
                                                        .add(userMap41)
                                                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                                                if (task.isSuccessful()) {
                                                                    Toasty.success(getApplicationContext(),"Feedback Send",Toast.LENGTH_SHORT,true).show();
                                                                }
                                                            }
                                                        });


                                            }
                                        })
                                        .withSecondButtonListner(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                flatDialog.dismiss();
                                            }
                                        })
                                        .show();
                                result.closeDrawer();
                                break;



                            case 13:
                                session.setFirstTimeLaunch(true);
                                result.closeDrawer();
                                startActivity(new Intent(HomeActivity.this, MainActivity.class));
                                finish();
                                break;

                            case 14:
                                if (result != null && result.isDrawerOpen()) {
                                    result.closeDrawer();
                                }
                                tapview();
                                break;
                            default:
                                Toast.makeText(HomeActivity.this, "Default", Toast.LENGTH_LONG).show();
                        }

                        return true;
                    }
                }).build();


      /*
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.home).withIcon(R.drawable.home);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(2).withName(R.string.myprofile).withIcon(R.drawable.profile);
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIdentifier(4).withName("History").withIcon(R.drawable.hh);
        PrimaryDrawerItem item7 = new PrimaryDrawerItem().withIdentifier(6).withName("Change Password").withIcon(R.drawable.passa);
        //PrimaryDrawerItem item6 = new PrimaryDrawerItem().withIdentifier(6).withName(R.string.order_history).withIcon(R.drawable.order_hist_icon);

        PrimaryDrawerItem item5 = new PrimaryDrawerItem().withIdentifier(5).withName(R.string.logout).withIcon(R.drawable.logout);

        SecondaryDrawerItem item9 = new SecondaryDrawerItem().withIdentifier(9).withName(R.string.feedback).withIcon(R.drawable.feedback);
        PrimaryDrawerItem item13 = new PrimaryDrawerItem().withIdentifier(3).withName(R.string.wishlist).withIcon(R.drawable.wishlist);
        PrimaryDrawerItem item114 = new PrimaryDrawerItem().withIdentifier(4).withName(R.string.cart).withIcon(R.drawable.cart);
        PrimaryDrawerItem item6 = new PrimaryDrawerItem().withIdentifier(15).withName(R.string.order_history).withIcon(R.drawable.order_hist_icon);

       */


        //..  SecondaryDrawerItem item10 = new SecondaryDrawerItem().withIdentifier(10).withName("Change Password").withIcon(R.drawable.customer);

        //  SecondaryDrawerItem item12 = new SecondaryDrawerItem().withIdentifier(12).withName("App Tour").withIcon(R.drawable.tour);
        // SecondaryDrawerItem item13 = new SecondaryDrawerItem().withIdentifier(13).withName("Explore").withIcon(R.drawable.explore);

        // PrimaryDrawerItem item14 = new PrimaryDrawerItem().withIdentifier(14).withName("Sub Account").withIcon(R.drawable.subaccount);
        //  PrimaryDrawerItem item13 = new PrimaryDrawerItem().withIdentifier(3).withName(R.string.wishlist).withIcon(R.drawable.wishlist);
        // PrimaryDrawerItem item114 = new PrimaryDrawerItem().withIdentifier(4).withName(R.string.cart).withIcon(R.drawable.cart);
        // PrimaryDrawerItem item6 = new PrimaryDrawerItem().withIdentifier(5).withName(R.string.order_history).withIcon(R.drawable.order_hist_icon);
        //creating navbar and adding to the toolbar ------------------------------------------------
     /*
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHasStableIds(true)
                .withDrawerLayout(R.layout.crossfade_drawer)
                .withAccountHeader(headerResult)
                .withDrawerWidthDp(72)
                .withGenerateMiniDrawer(true)
                .withTranslucentStatusBar(true)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(item1,item2,item4,item7,item5,item9,item13,item114,item6,new DividerDrawerItem(),new DividerDrawerItem()).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        switch (position){

                            case 0:
                                if (result != null && result.isDrawerOpen()) {
                                    result.closeDrawer();
                                }
                                break;
                            case 15:
                                Toast.makeText(HomeActivity.this, "", Toast.LENGTH_SHORT).show();
                                result.closeDrawer();
                                break;

                            case 2:
                                startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                                result.closeDrawer();
                                break;

                            case 3:

                                startActivity(new Intent(getApplicationContext(),HistoryActivity.class));
                                result.closeDrawer();
                                break;
                            case 4:
                                final FlatDialog flatDialog1 = new FlatDialog(HomeActivity.this);
                                flatDialog1.setTitle("Change Password")
                                        .setSubtitle("User forget his/her password.Now as a admin you change it.")
                                        .setSecondTextFieldHint("password")
                                        .setFirstButtonText("Change")
                                        .setSecondButtonText("Cancel")
                                        .withFirstButtonListner(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                flatDialog1.dismiss();
                                                final KProgressHUD progressDialog=  KProgressHUD.create(HomeActivity.this)
                                                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                                                        .setLabel("Please wait")
                                                        .setCancellable(false)
                                                        .setAnimationSpeed(2)
                                                        .setDimAmount(0.5f)
                                                        .show();
                                                firebaseFirestore.collection("Password")
                                                        .document(firebaseAuth.getCurrentUser().getEmail())
                                                        .get()
                                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                if (task.isSuccessful()) {
                                                                    if (task.getResult().exists()) {
                                                                        firebaseFirestore.collection("Password")
                                                                                .document(firebaseAuth.getCurrentUser().getEmail())
                                                                                .update("uuid",flatDialog1.getSecondTextField().toLowerCase().toString())
                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        if (task.isSuccessful()) {
                                                                                            firebaseFirestore.collection("All_UserID")
                                                                                                    .document(firebaseAuth.getCurrentUser().getEmail())
                                                                                                    .get()
                                                                                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                        @Override
                                                                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                            if (task.isSuccessful()) {
                                                                                                                if (task.getResult().exists()) {
                                                                                                                    String uuid=task.getResult().getString("uuid");
                                                                                                                    firebaseFirestore.collection("Users")
                                                                                                                            .document(uuid)
                                                                                                                            .get()
                                                                                                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                                                @Override
                                                                                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                                                    if (task.isSuccessful()) {
                                                                                                                                        if (task.getResult().exists()) {
                                                                                                                                            firebaseFirestore.collection("Users")
                                                                                                                                                    .document(uuid)
                                                                                                                                                    .update("pass",flatDialog1
                                                                                                                                                            .getSecondTextField().toLowerCase().toString())
                                                                                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                                        @Override
                                                                                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                                                                                            if (task.isSuccessful()) {
                                                                                                                                                                progressDialog.dismiss();
                                                                                                                                                                Toasty.success(getApplicationContext(),"Done",Toasty.LENGTH_SHORT,true).show();
                                                                                                                                                            }
                                                                                                                                                        }
                                                                                                                                                    });
                                                                                                                                        }
                                                                                                                                    }
                                                                                                                                }
                                                                                                                            });
                                                                                                                }
                                                                                                            }
                                                                                                        }
                                                                                                    });


                                                                                        }
                                                                                    }
                                                                                });

                                                                    }
                                                                    else {
                                                                        progressDialog.dismiss();
                                                                        Toasty.error(getApplicationContext(),"No user found",Toasty.LENGTH_SHORT,true).show();
                                                                    }
                                                                }
                                                            }
                                                        });
                                            }
                                        })
                                        .withSecondButtonListner(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                flatDialog1.dismiss();
                                            }
                                        })
                                        .show();
                                result.closeDrawer();
                                break;

                            case 5:
                                AlertDialog.Builder warning = new AlertDialog.Builder(HomeActivity.this)
                                        .setTitle("Logout")
                                        .setMessage("Are you want to logout?")
                                        .setPositiveButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();



                                            }
                                        }).setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // ToDO: delete all the notes created by the Anon user

                                                // TODO: delete the anon user
                                                firebaseAuth.signOut();
                                               startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                               finish();


                                            }
                                        });

                                warning.show();

                                result.closeDrawer();
                                break;
                            case 7:

                                new EasyFeedback.Builder(HomeActivity.this)
                                        .withEmail("sinhashashank.98@gmail.com")
                                        .withSystemInfo()
                                        .build()
                                        .start();

                            final FlatDialog flatDialog = new FlatDialog(HomeActivity.this);
                            flatDialog.setCancelable(false);
                            flatDialog.setTitle("Send a message")

                                    .setTitleColor(Color.parseColor("#000000"))
                                    .setBackgroundColor(Color.parseColor("#f5f0e3"))
                                    .setLargeTextFieldHint("write your text here ...")
                                    .setLargeTextFieldHintColor(Color.parseColor("#000000"))
                                    .setLargeTextFieldBorderColor(Color.parseColor("#000000"))
                                    .setLargeTextFieldTextColor(Color.parseColor("#000000"))
                                    .setFirstButtonColor(Color.parseColor("#fda77f"))
                                    .setFirstButtonTextColor(Color.parseColor("#000000"))
                                    .setFirstButtonText("Done")

                                    .  setSecondButtonText("Cancel")
                                    .withFirstButtonListner(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            flatDialog.dismiss();
                                            Map<String, String> userMap41 = new HashMap<>();
                                            userMap41.put("username",username);

                                            userMap41.put("feedback",flatDialog.getLargeTextField().toLowerCase().toString());
                                            firebaseFirestore=FirebaseFirestore.getInstance();
                                            firebaseFirestore.collection("Customer_feedback")
                                                    .add(userMap41)
                                                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                                            if (task.isSuccessful()) {
                                                                Toasty.success(getApplicationContext(),"Feedback Send",Toast.LENGTH_SHORT,true).show();
                                                            }
                                                        }
                                                    });


                                        }
                                    })
                                    .withSecondButtonListner(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            flatDialog.dismiss();
                                        }
                                    })
                                    .show();
                            result.closeDrawer();
                            break;

                            case 10:
                                startActivity(new Intent(HomeActivity.this, WishlistActivity.class));
                                result.closeDrawer();
                                break;
                            case 8:
                                startActivity(new Intent(HomeActivity.this, CartActivity.class));
                                result.closeDrawer();
                                break;

                            case 9:
                                startActivity(new Intent(HomeActivity.this, OrderHistoryActivity.class));
                                result.closeDrawer();
                                break;



                            case 7:
                                if (result != null && result.isDrawerOpen()) {
                                    result.closeDrawer();
                                }
                               tapview();
                                break;


                          startActivity(new Intent(HomeActivity.this, WishlistActivity.class));
                                result.closeDrawer();
                                break;
                            case 9:
                                startActivity(new Intent(HomeActivity.this, CartActivity.class));
                                result.closeDrawer();
                                break;

                            case 10:
                                startActivity(new Intent(HomeActivity.this, OrderHistoryActivity.class));
                                result.closeDrawer();
                                break;



        default:
        Toast.makeText(HomeActivity.this, "Default", Toast.LENGTH_LONG).show();
    }

                        return true;
}
                }).build();

                        */
        //Setting crossfader drawer------------------------------------------------------------

        crossfadeDrawerLayout = (CrossfadeDrawerLayout) result.getDrawerLayout();

        //define maxDrawerWidth
        crossfadeDrawerLayout.setMaxWidthPx(DrawerUIUtils.getOptimalDrawerWidth(this));

        //add second view (which is the miniDrawer)
        final MiniDrawer miniResult = result.getMiniDrawer();

        //build the view for the MiniDrawer
        View view = miniResult.build(this);

        //set the background of the MiniDrawer as this would be transparent
        view.setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(this, com.mikepenz.materialdrawer.R.attr.material_drawer_background, com.mikepenz.materialdrawer.R.color.material_drawer_background));

        //we do not have the MiniDrawer view during CrossfadeDrawerLayout creation so we will add it here
        crossfadeDrawerLayout.getSmallView().addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        //define the crossfader to be used with the miniDrawer. This is required to be able to automatically toggle open / close
        miniResult.withCrossFader(new ICrossfader() {
            @Override
            public void crossfade() {
                boolean isFaded = isCrossfaded();
                crossfadeDrawerLayout.crossfade(400);

                //only close the drawer if we were already faded and want to close it now
                if (isFaded) {
                    result.getDrawerLayout().closeDrawer(GravityCompat.START);
                }
            }

            @Override
            public boolean isCrossfaded() {
                return crossfadeDrawerLayout.isCrossfaded();
            }
        });
    }

    @Override
    public void onBackPressed() {
        final FancyAlertDialog.Builder alert = new FancyAlertDialog.Builder(HomeActivity.this)
                .setBackgroundColor(R.color.white)
                .setTextTitle("Exit")
                .setCancelable(false)
                .setTextSubTitle("Are you want to exit")
                .setBody("User is not stay at app when user click exit button.")
                .setPositiveButtonText("No")
                .setPositiveColor(R.color.toolbar)
                .setOnPositiveClicked(new FancyAlertDialog.OnPositiveClicked() {
                    @Override
                    public void OnClick(View view, Dialog dialog) {
                        dialog.dismiss();

                    }
                })
                .setNegativeButtonText("Exit")
                .setNegativeColor(R.color.colorPrimaryDark)
                .setOnNegativeClicked(new FancyAlertDialog.OnNegativeClicked() {
                    @Override
                    public void OnClick(View view, Dialog dialog) {
                        dialog.dismiss();
                        finishAffinity();

                    }
                })
                .setBodyGravity(FancyAlertDialog.TextGravity.CENTER)
                .setTitleGravity(FancyAlertDialog.TextGravity.CENTER)
                .setSubtitleGravity(FancyAlertDialog.TextGravity.CENTER)
                .setCancelable(false)
                .build();
        alert.show();

    }


    public void Notifications(View view) {
     //   startActivity(new Intent(getApplicationContext(),NotificationActivity.class));
    }

    public void viewProfile(View view) {
     //   startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
    }

}