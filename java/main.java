package oshin.tasnuva.firebase;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int SELECT_PICTURES = 10 ;
DatabaseReference databaseReference;

    ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
    Uri imageUri;
    int up = 0;
    int k =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("oshin");
    }

    public void CickME(View view) {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent,SELECT_PICTURES);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELECT_PICTURES) {
            if (resultCode == MainActivity.RESULT_OK) {
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    Log.i("count", String.valueOf(count));
                    int currentItem = 0;
                    while (currentItem < count) {
                        imageUri = data.getClipData().getItemAt(currentItem).getUri();


                        Log.i("uri", imageUri.toString());
                        mArrayUri.add(imageUri);
                        currentItem = currentItem + 1;
                    }
                    Log.i("listsize", String.valueOf(mArrayUri.size()));
                    Toast.makeText(this, "You Have Selected"+mArrayUri.size()+" Images", Toast.LENGTH_SHORT).show();
                } else if (data.getData() != null) {
                    String imagePath = data.getData().getPath();

                }
            }
        }
    }


    public void Upload(View view) {

        //folder
        final StorageReference filepath = FirebaseStorage.getInstance().getReference().child("gpic");

        while (up < mArrayUri.size()){
            filepath.child(mArrayUri.get(k).getLastPathSegment()).putFile(mArrayUri.get(k)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(MainActivity.this, "Uploaded", Toast.LENGTH_LONG).show();



                }
            });



            filepath.child(mArrayUri.get(k).getLastPathSegment()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {

                    String url = String.valueOf(uri);
                    databaseReference.push().setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(MainActivity.this, "Done", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            up++;
            k++;

        }


    }

    }


