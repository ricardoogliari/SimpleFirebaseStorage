package padroes.estudo.com.simplefirebasestorage;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<String> urls = new ArrayList<>();
        urls.add("gs://amigo-pet-35284.appspot.com/luppy.jpg");
        urls.add("gs://amigo-pet-35284.appspot.com/luppy2.jpg");
        urls.add("gs://amigo-pet-35284.appspot.com/luppy3.JPG");
        urls.add("gs://amigo-pet-35284.appspot.com/luppy4.jpg");
        urls.add("gs://amigo-pet-35284.appspot.com/tigra.jpg");
        urls.add("gs://amigo-pet-35284.appspot.com/tigra2.jpg");

        final ImageView[] imgViews = new ImageView[7];
        imgViews[0] = (ImageView) findViewById(R.id.img1);
        imgViews[1] = (ImageView) findViewById(R.id.img2);
        imgViews[2] = (ImageView) findViewById(R.id.img3);
        imgViews[3] = (ImageView) findViewById(R.id.img4);
        imgViews[4] = (ImageView) findViewById(R.id.img5);
        imgViews[5] = (ImageView) findViewById(R.id.img6);

        for (String url : urls) {
            StorageReference gsReference = Core.storage.getReferenceFromUrl(url);
            final long ONE_MEGABYTE = 1024 * 1024;
            gsReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Log.e("STORAGE", "SUCESSO!!!!: " + bytes.length);
                    imgViews[index].setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                    index++;

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.e("STORAGE", "falha!!!! = " + exception.getLocalizedMessage());
                }
            });


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), 10);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 10) {
                Uri selectedImageUri = data.getData();

                UploadTask task = Core.storage.getReference("teste.png").putFile(selectedImageUri);
                task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.e("STORAGE", "onSucess no envio");
                    }
                });
            }

        }

    }

}
