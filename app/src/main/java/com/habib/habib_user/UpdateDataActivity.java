package com.habib.habib_user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class UpdateDataActivity extends AppCompatActivity {

    private Button button;
    private EditText edtname,edtemail,edtmobile;
    CircleImageView primage;
    private TextView namebutton;
    private ImageView changeprofilepic;
    private String name,email,photo,mobile,newemail,userrr;
    private String check;
    boolean IMAGE_STATUS = false;
    ImageButton image_button;
    ImageView imageView;
    private final int PICK_IMAGE_REQUEST = 71;
    private Uri filePath;//Firebase

    DocumentReference documentReference;
    Button floatingActionButton;
    FirebaseStorage storage;
    private static final int CAMERA_REQUEST = 1888;
    Button generate_btn;
    //doctor
    private static final int READCODE = 1;
    private static final int WRITECODE = 2;

    private Uri mainImageUri = null;
    FirebaseAuth firebaseAuth;


    //to get user session data
    private UserSession session;
    private Uri mainImageURI = null;
    private HashMap<String,String> user;
    private Button updateProfileBtn;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;
    private StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_data);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        mAuth=FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(UpdateDataActivity.this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();

        initialize();

        //retrieve session values and display on listviews
        getValues();



        primage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {


                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

            }
        });


        updateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edtname.getText().toString())||
                        TextUtils.isEmpty(edtemail.getText().toString())||TextUtils.isEmpty(edtmobile.getText().toString())) {
                    Toasty.warning(UpdateDataActivity.this,"Incorrect Details Entered", Toast.LENGTH_LONG).show();
                }
                else {
                    final KProgressHUD progressDialog=  KProgressHUD.create(UpdateDataActivity.this)
                            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                            .setLabel("Please wait")
                            .setCancellable(false)
                            .setAnimationSpeed(2)
                            .setDimAmount(0.5f)
                            .show();

                    name = edtname.getText().toString();
                    email = edtemail.getText().toString();
                    mobile = edtmobile.getText().toString();

                    // If Profile Picture is NOT CHANGED
                    if(filePath==null) {
                        // update data only
                        //  Toast.makeText(UpdateDataActivity.this, "Img Not Updated", Toast.LENGTH_SHORT).show();

                        firebaseFirestore.collection("Users").document(mAuth.getCurrentUser().getUid()).update(
                                "name", name, "number", mobile
                                ,"email",email
                        ).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressDialog.dismiss();
                                Toasty.success(UpdateDataActivity.this, "Profile Updated Successfully! Please Login again.", 2000).show();
                                session.createLoginSession(name, email, mobile, photo,userrr);
                                mAuth.signOut();
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toasty.success(UpdateDataActivity.this, "Profile Updated Successfully", 2000).show();
                            }
                        });


                    }
                    else {
                        //Toast.makeText(UpdateDataActivity.this, "Image Updated", Toast.LENGTH_SHORT).show();
                        final StorageReference image_path = storageReference.child("profile_images").child(mAuth.getCurrentUser().getUid()+".jpg");

                        image_path.putFile(filePath).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    //Log.d(TAG+"_TXN_ERR_1","ERROR IN SAVING MAIN IMAGE");
                                    progressDialog.dismiss();
                                    throw task.getException();
                                }

                                // Continue with the task to get the download URL
                                return image_path.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {

                                    //Log.d(TAG+"_TXN","4. MAIN IMAGE SAVED");
                                    final Uri downloadUri = task.getResult();

                                    //compressBitmap(downloadUri,name, mobile,email);
                                    firebaseFirestore.collection("Users").document(mAuth.getCurrentUser().getUid()).update(
                                            "name", name, "number", mobile,"image",String.valueOf(downloadUri)
                                            ,"email",email).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            progressDialog.dismiss();
                                            Toasty.success(UpdateDataActivity.this, "Profile Updated Successfully! Please Login again.", 2000).show();
                                            session.createLoginSession(name, email, mobile, String.valueOf(downloadUri),userrr);
                                            mAuth.signOut();
                                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressDialog.dismiss();
                                            Toasty.success(UpdateDataActivity.this, "Profile Updated Successfully", 2000).show();
                                        }
                                    });


                                } else {
                                    progressDialog.dismiss();
                                    // Log.d(TAG+"_TXN_ERR_2","ERROR IN SAVING MAIN IMAGE");
                                    String errorMessage = task.getException().getMessage();
                                    //Log.d(TAG+"_ERR","UPLOAD Error : "+errorMessage);

                                }
                            }
                        });
                    }

                }


            }
        });
    }
    public void viewCart(View view) {
        //startActivity(new Intent(UpdateData.this,Cart.class));
        //finish();
    }

    public void viewProfile(View view) {
        startActivity(new Intent(UpdateDataActivity.this,ProfileActivity.class));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private boolean validateNumber() {

        check = edtmobile.getText().toString();
        Log.e("inside number",check.length()+" ");
        if (check.length()>11) {
            return false;
        }else if(check.length()<11){
            return false;
        }
        return true;
    }

    private boolean validateEmail() {

        check = edtemail.getText().toString();

        if (check.length() < 4 || check.length() > 40) {
            return false;
        } else if (!check.matches("^[A-za-z0-9.@]+")) {
            return false;
        } else if (!check.contains("@") || !check.contains(".")) {
            return false;
        }

        return true;
    }

    private boolean validateName() {

        check = edtname.getText().toString();

        return !(check.length() < 4 || check.length() > 20);

    }

    //TextWatcher for Name -----------------------------------------------------

    TextWatcher nameWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //none
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //none
        }

        @Override
        public void afterTextChanged(Editable s) {

            check = s.toString();

            if (check.length() < 4 || check.length() > 20) {
                edtname.setError("Name Must consist of 4 to 20 characters");
            }
        }

    };

    //TextWatcher for Email -----------------------------------------------------

    TextWatcher emailWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //none
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //none
        }

        @Override
        public void afterTextChanged(Editable s) {

            check = s.toString();

            if (check.length() < 4 || check.length() > 40) {
                edtemail.setError("Email Must consist of 4 to 20 characters");
            } else if (!check.matches("^[A-za-z0-9.@]+")) {
                edtemail.setError("Only . and @ characters allowed");
            } else if (!check.contains("@") || !check.contains(".")) {
                edtemail.setError("Enter Valid Email");
            }

        }

    };

    //TextWatcher for Mobile -----------------------------------------------------

    TextWatcher numberWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //none
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //none
        }

        @Override
        public void afterTextChanged(Editable s) {

            check = s.toString();

            if (check.length()>11) {
                edtmobile.setError("Number cannot be grated than 11 digits");
            }else if(check.length()<11){
                edtmobile.setError("Number should be 11 digits");
            }
        }

    };

    private void initialize() {

        namebutton =findViewById(R.id.name_button);
        primage = findViewById(R.id.profilepic);
        edtname =findViewById(R.id.name);
        edtemail =findViewById(R.id.email);
        edtmobile =findViewById(R.id.number);
        updateProfileBtn = findViewById(R.id.update);
        changeprofilepic = findViewById(R.id.changeprofilepic);
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        edtname.addTextChangedListener(nameWatcher);
        edtemail.addTextChangedListener(emailWatcher);
        edtmobile.addTextChangedListener(numberWatcher);



    }

    private void bringImagePicker() {

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(UpdateDataActivity.this);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            Toast.makeText(this, ""+filePath, Toast.LENGTH_SHORT).show();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                primage.setImageBitmap(bitmap);

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    private void getValues() {

        //create new session object by passing application context
        session = new UserSession(getApplicationContext());

        //validating session
        session.isLoggedIn();

        //get User details if logged in
        user = session.getUserDetails();

        name = user.get(UserSession.KEY_NAME);
        email = user.get(UserSession.KEY_EMAIL);
        mobile = user.get(UserSession.KEY_MOBiLE);
        photo = user.get(UserSession.KEY_PHOTO);

        //setting values
        edtemail.setText(email);
        edtmobile.setText(mobile);
        edtname.setText(name);
        namebutton.setText(name);
        String image22="https://firebasestorage.googleapis.com/v0/b/cash-money-express-ltd.appspot.com/o/profile_images%2Fo8Dnqf5LFodKSwocGQ4nKB7ZEkW2.jpg?alt=media&token=c22700e2-67ca-4497-8bf1-204ac83b6749";
        try {
            if (photo.contains(image22)) {
                Picasso.get().load(R.drawable.habib_user).into(primage);
            }
            else {
                Picasso.get().load(photo).into(primage);
            }
        }catch (Exception e) {
            e.printStackTrace();
            if (photo.contains(image22)) {
                Picasso.get().load(R.drawable.habib_user).into(primage);
            }
            else {
                Picasso.get().load(photo).into(primage);
            }
        }

        //Glide.with(getBaseContext()).load(photo).into(primage);
    }
}