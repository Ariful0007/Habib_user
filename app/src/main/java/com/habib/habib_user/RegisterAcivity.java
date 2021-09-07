package com.habib.habib_user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class RegisterAcivity extends AppCompatActivity {

    private EditText edtname, edtemail, edtpass, edtcnfpass, edtnumber,username;
    private String check, name, email, password, mobile, profile;
    CircleImageView image;
    ImageView upload;
    private Uri mainImageURI = null;
    boolean IMAGE_STATUS = false;
    Bitmap profilePicture;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private final String TAG = this.getClass().getSimpleName();
    private String userId;

    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;

    private Bitmap compressedImageFile;
    private KProgressHUD progressDialog;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_acivity);
        firebaseAuth = FirebaseAuth.getInstance();

        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();

        // Typeface typeface = ResourcesCompat.getFont(this, R.font.blacklist);
        // TextView appname = findViewById(R.id.appname);
        //appname.setTypeface(typeface);
        progressDialog=KProgressHUD.create(RegisterAcivity.this);

        FirebaseApp.initializeApp(RegisterAcivity.this);
        username=findViewById(R.id.username);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();


        upload = findViewById(R.id.uploadpic);
        image = findViewById(R.id.profilepic);
        edtname = findViewById(R.id.name);
        edtemail = findViewById(R.id.email);
        edtpass = findViewById(R.id.password);
        edtcnfpass = findViewById(R.id.confirmpassword);
        edtnumber = findViewById(R.id.number);

        edtname.addTextChangedListener(nameWatcher);
        username.addTextChangedListener(nameWatcher1);
        edtemail.addTextChangedListener(emailWatcher);
        edtpass.addTextChangedListener(passWatcher);
        edtcnfpass.addTextChangedListener(cnfpassWatcher);
        edtnumber.addTextChangedListener(numberWatcher);
        //validate user details and register user

        Button register_button = findViewById(R.id.register);

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edtname.getText().toString()) || TextUtils.isEmpty(edtemail.getText().toString())
                        || TextUtils.isEmpty(edtpass.getText().toString()) || TextUtils.isEmpty(edtcnfpass.getText().toString())
                        || TextUtils.isEmpty(edtnumber.getText().toString())||TextUtils.isEmpty(username.getText().toString())) {
                    Toasty.error(getApplicationContext(), "Error give right information", Toast.LENGTH_SHORT, true).show();
                    return;
                } else {
                    progress_check();
                    firebaseFirestore.collection("BlockList")
                            .document(username.getText().toString().toLowerCase().toString()+"@gmail.com")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if (task.getResult().exists()) {
                                            progressDialog.dismiss();
                                            Toasty.error(getApplicationContext(),"You  are in blook list.", Toast.LENGTH_SHORT,true).show();
                                        }
                                        else {
                                            firebaseFirestore.collection("Name_Exsiting")
                                                    .document(username.getText().toString().toLowerCase())
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull  Task<DocumentSnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                if (task.getResult().exists()) {
                                                                    progressDialog.dismiss();
                                                                    Toasty.error(getApplicationContext(),"This username already taken..",Toast.LENGTH_SHORT,true).show();
                                                                    return;
                                                                }
                                                                else {
                                                                    final EditText input = new EditText(RegisterAcivity.this);
                                                                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                                                            LinearLayout.LayoutParams.MATCH_PARENT);
                                                                    input.setLayoutParams(lp);
                                                                    input.setHint("Enter referral name ");
                                                                    new AlertDialog.Builder(RegisterAcivity.this)
                                                                            .setTitle("Referal name")
                                                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                                                public void onClick(DialogInterface dialog, int which) {
                                                                                    if (TextUtils.isEmpty(input.getText().toString())) {
                                                                                        progressDialog.dismiss();
                                                                                        Toasty.error(getApplicationContext(),"Given a referal name",Toast.LENGTH_SHORT,true).show();
                                                                                        return;
                                                                                    }
                                                                                    else {
                                                                                        firebaseFirestore.collection("Old_User")
                                                                                                .document(input.getText().toString().toLowerCase().toString())
                                                                                                .get()
                                                                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                        if (task.isSuccessful()) {
                                                                                                            if (task.getResult().exists()) {
                                                                                                                name=edtname.getText().toString();
                                                                                                                email=edtemail.getText().toString();
                                                                                                                password=edtcnfpass.getText().toString();
                                                                                                                mobile=edtnumber.getText().toString();
                                                                                                                mAuth.createUserWithEmailAndPassword(username.getText().toString().toLowerCase().toString()+"@gmail.com","123456")
                                                                                                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                                                                                            @Override
                                                                                                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                                                                                                if (task.isSuccessful()) {
                                                                                                                                    final Map<String, String> userMap = new HashMap<>();
                                                                                                                                    userMap.put("name",username.getText().toString().toLowerCase().toString());
                                                                                                                                    firebaseFirestore.collection("ReferalPersols").document(input.getText().toString().toLowerCase())
                                                                                                                                            .collection("ReferalPersons")
                                                                                                                                            .document(UUID.randomUUID().toString())
                                                                                                                                            .set(userMap)
                                                                                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                                @Override
                                                                                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                                                                                    if (task.isSuccessful()) {
                                                                                                                                                        Name_Exsiting name_exsiting=new Name_Exsiting(username.getText().toString().toLowerCase(),
                                                                                                                                                                username.getText().toString());
                                                                                                                                                        firebaseFirestore.collection("Name_Exsiting")
                                                                                                                                                                .document(username.getText().toString().toLowerCase())
                                                                                                                                                                .set(name_exsiting)
                                                                                                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                                                    @Override
                                                                                                                                                                    public void onComplete(@NonNull  Task<Void> task) {
                                                                                                                                                                        if (task.isSuccessful()) {
                                                                                                                                                                            final Map<String, String> userMap9 = new HashMap<>();
                                                                                                                                                                            userMap9.put("refername",input.getText().toString().toLowerCase().toString());
                                                                                                                                                                            firebaseFirestore.collection("MyUpline")
                                                                                                                                                                                    .document(mAuth.getCurrentUser().getEmail())
                                                                                                                                                                                    .set(userMap9)
                                                                                                                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                                                                        @Override
                                                                                                                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                                                                                                                            if (task.isSuccessful()) {
                                                                                                                                                                                                final Map<String, String> userMap19 = new HashMap<>();
                                                                                                                                                                                                userMap19.put("refername",input.getText().toString().toLowerCase().toString());
                                                                                                                                                                                                userMap19.put("refername_email",input.getText().toString().toLowerCase().toString()+"@gmail.com");
                                                                                                                                                                                                userMap19.put("user_id",firebaseAuth.getCurrentUser().getUid());
                                                                                                                                                                                                userMap19.put("user_name",username.getText().toString().toLowerCase().toString());
                                                                                                                                                                                                firebaseFirestore.collection("ALL_GET")
                                                                                                                                                                                                        .document(firebaseAuth.getCurrentUser().getEmail())
                                                                                                                                                                                                        .set(userMap19)
                                                                                                                                                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                                                                                            @Override
                                                                                                                                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                                                                                                                                if (task.isSuccessful()) {
                                                                                                                                                                                                                    String uuid=UUID.randomUUID().toString();
                                                                                                                                                                                                                    final Map<String, String> userMap191 = new HashMap<>();
                                                                                                                                                                                                                    userMap191.put("refername",input.getText().toString().toLowerCase().toString());
                                                                                                                                                                                                                    userMap191.put("refername_email",input.getText().toString().toLowerCase().toString()+"@gmail.com");
                                                                                                                                                                                                                    userMap191.put("user_id",firebaseAuth.getCurrentUser().getUid());
                                                                                                                                                                                                                    userMap191.put("user_name",username.getText().toString().toLowerCase().toString());
                                                                                                                                                                                                                    userMap191.put("uuid",uuid);
                                                                                                                                                                                                                    userMap191.put("user_email",username.getText().toString().toLowerCase().toString()+"@gmail.com");
                                                                                                                                                                                                                    firebaseFirestore.collection("MyIndiREfer")
                                                                                                                                                                                                                            .document(input.getText().toString().toLowerCase().toString()+"@gmail.com")
                                                                                                                                                                                                                            .collection("1st_Level")
                                                                                                                                                                                                                            .document(input.getText().toString().toLowerCase().toString()+"@gmail.com")
                                                                                                                                                                                                                            .collection("List")
                                                                                                                                                                                                                            .document(uuid)
                                                                                                                                                                                                                            .set(userMap191)
                                                                                                                                                                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                                                                                                                @Override
                                                                                                                                                                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                                                                                                                                                                    if (task.isSuccessful()) {
                                                                                                                                                                                                                                        firebaseFirestore.collection("IndiMyAllRefer")
                                                                                                                                                                                                                                                .document(input.getText().toString().toLowerCase().toString()+"@gmail.com")
                                                                                                                                                                                                                                                .collection("List")
                                                                                                                                                                                                                                                .add(userMap191)
                                                                                                                                                                                                                                                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                                                                                                                                                                                                                    @Override
                                                                                                                                                                                                                                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                                                                                                                                                                                                                                        if (task.isSuccessful()) {
                                                                                                                                                                                                                                                            progressDialog.dismiss();
                                                                                                                                                                                                                                                            user = mAuth.getCurrentUser();
                                                                                                                                                                                                                                                            gto_oCount(mAuth.getCurrentUser().getEmail(),mAuth.getCurrentUser().getUid());
                                                                                                                                                                                                                                                            userId = mAuth.getCurrentUser().getUid();
                                                                                                                                                                                                                                                            uploadImage();
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

                                                                                                                                                    }
                                                                                                                                                }
                                                                                                                                            });

                                                                                                                                }
                                                                                                                            }
                                                                                                                        }).addOnFailureListener(new OnFailureListener() {
                                                                                                                    @Override
                                                                                                                    public void onFailure(@NonNull Exception e) {
                                                                                                                        progressDialog.dismiss();
                                                                                                                        Toasty.error(getApplicationContext(),"Enter another username",Toast.LENGTH_SHORT,true).show();
                                                                                                                        return;

                                                                                                                    }
                                                                                                                });

                                                                                                            }
                                                                                                            else {
                                                                                                                progressDialog.dismiss();
                                                                                                                Toasty.error(getApplicationContext(),"No User Found .",Toast.LENGTH_SHORT,true).show();
                                                                                                                return;
                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                });

                                                                                    }
                                                                                    dialog.dismiss();

                                                                                }
                                                                            })
                                                                            .setIcon(R.drawable.habib_user)
                                                                            .setView(input)
                                                                            .show();


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

        //Take already registered user to login page

        final TextView loginuser = findViewById(R.id.login_now);
        loginuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterAcivity.this, LoginActivity.class));
                finish();
            }
        });

        //take user to reset password

        final TextView forgotpass = findViewById(R.id.forgot_pass);
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });


    }

    private void uploadImage() {
        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference image_path = storageReference.child("profile_images").child(userId + ".jpg");
            image_path.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful());
                            final Uri downloadUri=uriTask.getResult();



                            if (uriTask.isSuccessful()) {
                               /* HashMap<String,Object> hashMap=new HashMap<>();

                                hashMap.put("image",downloadUri.toString());
                                hashMap.put("product_name",product_name.getText().toString());
                                hashMap.put("product_price",product_price.getText().toString());
                                hashMap.put("old_price",old_price.getText().toString());
                                hashMap.put("stock",stock.getText().toString());*/
                                name=edtname.getText().toString();
                                email=edtemail.getText().toString();
                                password=edtcnfpass.getText().toString();
                                mobile=edtnumber.getText().toString();

                                storeToFirestore(name, edtemail.getText().toString(), Uri.parse(downloadUri.toString()), email);



                            }
                            else {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
        else {
            progress_check();
            name=edtname.getText().toString();
            email=edtemail.getText().toString();
            password=edtcnfpass.getText().toString();
            mobile=edtnumber.getText().toString();
            String image22="https://firebasestorage.googleapis.com/v0/b/cash-money-express-ltd.appspot.com/o/profile_images%2Fo8Dnqf5LFodKSwocGQ4nKB7ZEkW2.jpg?alt=media&token=c22700e2-67ca-4497-8bf1-204ac83b6749";
            storeToFirestore1(name, edtemail.getText().toString(),image22, email);
        }

    }

    private void storeToFirestore1(String name, String toString, String image22, String email) {
        Log.d(TAG+"_TXN","8. SAVING DATA TO FIRESTORE");
        // Storing data on Firestore...

        final Map<String, String> userMap = new HashMap<>();
        userMap.put("name",name);
        userMap.put("image",image22.toString());
        userMap.put("number", edtnumber.getText().toString());
        userMap.put("email",email);
        userMap.put("pass",edtpass.getText().toString().toLowerCase());
        userMap.put("username",username.getText().toString().toLowerCase().toString());



        firebaseFirestore.collection("Users").document(userId).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {


                if(task.isSuccessful())
                {

                    // Sending Verification Link via Email
                    Log.d(TAG+"_TXN","9. DATA SAVED");
                    Log.d(TAG+"_TXN","10. SENDING EMAIL");
                    firebaseFirestore.collection("User2").document(firebaseAuth.getCurrentUser().getEmail())
                            .set(userMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        User_coun user_coun=new User_coun( name,toString,email,"0");
                                        firebaseFirestore.collection("Users")
                                                .document(userId).collection("Coins")
                                                .document(mAuth.getCurrentUser().getEmail())
                                                .set(user_coun)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            MainCoin mainCoin=new MainCoin("0");
                                                            firebaseFirestore.collection("Users").document(userId)
                                                                    .collection("MainCoin")
                                                                    .document(mAuth.getCurrentUser().getEmail().toString().toLowerCase().toString())
                                                                    .set(mainCoin)
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()) {
                                                                                Map<String, String> userMap1 = new HashMap<>();

                                                                                userMap1.put("username",username.getText().toString().toLowerCase().toString());
                                                                                firebaseFirestore.collection("Old_User")
                                                                                        .document(username.getText().toString().toLowerCase().toString())
                                                                                        .set(userMap1)
                                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                if (task.isSuccessful()) {
                                                                                                    Map<String, String> userMap7 = new HashMap<>();

                                                                                                    userMap7.put("uuid",userId);
                                                                                                    firebaseFirestore.collection("All_UserID")
                                                                                                            .document(mAuth.getCurrentUser().getEmail())
                                                                                                            .set(userMap7)
                                                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                @Override
                                                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                                                    if (task.isSuccessful()) {
                                                                                                                        Map<String, String> paSS = new HashMap<>();

                                                                                                                        paSS.put("uuid",edtpass.getText().toString().toLowerCase());
                                                                                                                        firebaseFirestore.collection("Password")
                                                                                                                                .document(firebaseAuth.getCurrentUser().getEmail())
                                                                                                                                .set(paSS)
                                                                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                    @Override
                                                                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                                                                        if (task.isSuccessful()) {
                                                                                                                                            progressDialog.dismiss();

                                                                                                                                            mAuth.signOut();
                                                                                                                                            Log.d(TAG+"_TXN","11. EMAIL SENT");
                                                                                                                                            startActivity(new Intent(RegisterAcivity.this,LoginActivity.class));
                                                                                                                                            finish();
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
                                    }
                                }
                            });





                } else {
                    Log.d(TAG+"_ERR5","ERROR IN SAVING DATA");
                    progressDialog.dismiss();
                    String errorMessage = task.getException().getMessage();
                    Toast.makeText(RegisterAcivity.this,"FIRESTORE Error : "+errorMessage,Toast.LENGTH_LONG).show();

                }

            }
        });

    }

    private void progress_check() {
        progressDialog = KProgressHUD.create(RegisterAcivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

    }


    private void bringImagePicker() {

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(RegisterAcivity.this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                image.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /*if (requestCode == 1000 && resultCode == Activity.RESULT_OK && data != null) {
            //Image Successfully Selected
            try {
                //parsing the Intent data and displaying it in the imageview
                Uri imageUri = data.getData();//Geting uri of the data
                InputStream imageStream = getContentResolver().openInputStream(imageUri);//creating an imputstrea
                profilePicture = BitmapFactory.decodeStream(imageStream);//decoding the input stream to bitmap
                image.setImageBitmap(profilePicture);
                IMAGE_STATUS = true;//setting the flag
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }*/
    }

    private void convertBitmapToString(Bitmap profilePicture) {
            /*
                Base64 encoding requires a byte array, the bitmap image cannot be converted directly into a byte array.
                so first convert the bitmap image into a ByteArrayOutputStream and then convert this stream into a byte array.
            */
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        profilePicture.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
        byte[] array = byteArrayOutputStream.toByteArray();
        profile = Base64.encodeToString(array, Base64.DEFAULT);
    }


    private boolean validateNumber() {

        check = edtnumber.getText().toString();
        Log.e("inside number", check.length() + " ");
        if (check.length() > 10) {
            return false;
        } else if (check.length() < 10) {
            return false;
        }
        return true;
    }

    private boolean validateCnfPass() {

        check = edtcnfpass.getText().toString();

        return check.equals(edtpass.getText().toString());
    }

    private boolean validatePass() {


        check = edtpass.getText().toString();

        if (check.length() < 4 || check.length() > 20) {
            return false;
        } else if (!check.matches("^[A-za-z0-9@]+")) {
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

    private boolean validateProfile() {
        if (!IMAGE_STATUS)
            Toasty.info(RegisterAcivity.this, "Select A Profile Picture", Toast.LENGTH_LONG).show();
        return IMAGE_STATUS;
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
//username
    //TextWatcher for Name -----------------------------------------------------

    TextWatcher nameWatcher1 = new TextWatcher() {
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

            if (check.length() < 4 || check.length() > 10) {
                username.setError("Name Must consist of 4 to 10 characters");
            }
        }

    };

    //TextWatcher for pass -----------------------------------------------------

    TextWatcher passWatcher = new TextWatcher() {
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
                edtpass.setError("Password Must consist of 4 to 20 characters");
            } else if (!check.matches("^[A-za-z0-9@]+")) {
                edtemail.setError("Only @ special character allowed");
            }
        }

    };

    //TextWatcher for repeat Password -----------------------------------------------------

    TextWatcher cnfpassWatcher = new TextWatcher() {
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

            if (!check.equals(edtpass.getText().toString())) {
                edtcnfpass.setError("Both the passwords do not match");
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

            if (check.length() > 11) {
                edtnumber.setError("Number cannot be grated than 11 digits");
            } else if (check.length() < 11) {
                edtnumber.setError("Number should be 11 digits");
            }
        }

    };

    @Override
    protected void onResume() {
        super.onResume();
        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();
    }


    private void storeToFirestore(String user_name, String user_number,  Uri downloadUri, String user_email) {


        Log.d(TAG+"_TXN","8. SAVING DATA TO FIRESTORE");
        // Storing data on Firestore...

        Map<String, String> userMap = new HashMap<>();
        userMap.put("name",user_name);
        userMap.put("image",downloadUri.toString());
        userMap.put("number", edtnumber.getText().toString());
        userMap.put("email",user_email);
        userMap.put("pass",edtpass.getText().toString().toLowerCase());
        userMap.put("username",username.getText().toString().toLowerCase().toString());



        firebaseFirestore.collection("Users").document(userId).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {


                if(task.isSuccessful())
                {

                    // Sending Verification Link via Email
                    Log.d(TAG+"_TXN","9. DATA SAVED");
                    Log.d(TAG+"_TXN","10. SENDING EMAIL");
                    User_coun user_coun=new User_coun( name,user_number,email,"0");
                    firebaseFirestore.collection("Users")
                            .document(userId).collection("Coins")
                            .document(mAuth.getCurrentUser().getEmail())
                            .set(user_coun)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        MainCoin mainCoin=new MainCoin("0");
                                        firebaseFirestore.collection("Users").document(userId)
                                                .collection("MainCoin")
                                                .document(mAuth.getCurrentUser().getEmail().toString().toLowerCase().toString())
                                                .set(mainCoin)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Map<String, String> userMap1 = new HashMap<>();

                                                            userMap1.put("username",username.getText().toString().toLowerCase().toString());
                                                            firebaseFirestore.collection("Old_User")
                                                                    .document(username.getText().toString().toLowerCase().toString())
                                                                    .set(userMap1)
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()) {
                                                                                Map<String, String> userMap7 = new HashMap<>();

                                                                                userMap7.put("uuid",userId);
                                                                                firebaseFirestore.collection("All_UserID")
                                                                                        .document(mAuth.getCurrentUser().getEmail())
                                                                                        .set(userMap7)
                                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                if (task.isSuccessful()) {
                                                                                                    Map<String, String> paSS = new HashMap<>();

                                                                                                    paSS.put("uuid",edtpass.getText().toString().toLowerCase());
                                                                                                    firebaseFirestore.collection("Password")
                                                                                                            .document(firebaseAuth.getCurrentUser().getEmail())
                                                                                                            .set(paSS)
                                                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                @Override
                                                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                                                    if (task.isSuccessful()) {
                                                                                                                        progressDialog.dismiss();

                                                                                                                        mAuth.signOut();
                                                                                                                        Log.d(TAG+"_TXN","11. EMAIL SENT");
                                                                                                                        startActivity(new Intent(RegisterAcivity.this,LoginActivity.class));
                                                                                                                        finish();
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






                } else {
                    Log.d(TAG+"_ERR5","ERROR IN SAVING DATA");
                    progressDialog.dismiss();
                    String errorMessage = task.getException().getMessage();
                    Toast.makeText(RegisterAcivity.this,"FIRESTORE Error : "+errorMessage,Toast.LENGTH_LONG).show();

                }

            }
        });



    }
    private void gto_oCount(String email, String uid) {
        Map<String, String> userMap2 = new HashMap<>();

        userMap2.put("main_balance", "0");
        userMap2.put("purches_balance", "0");
        userMap2.put("giving_balance", "0");
        userMap2.put("self_income", "0");
        userMap2.put("sponsor_income", "0");
        userMap2.put("first_level", "0");
        userMap2.put("second_level", "0");
        userMap2.put("third_level", "0");
        userMap2.put("forth_level", "0");
        userMap2.put("fifth_level", "0");
        userMap2.put("shoping_balance", "0");
        userMap2.put("cashwalet", "0");
        userMap2.put("monthly_income", "0");
        userMap2.put("leader_club", "0");

        firebaseFirestore.collection("Users").document(uid)
                .collection("Main_Balance")
                .document(email)
                .set(userMap2)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                        }
                    }
                });
    }
    }






