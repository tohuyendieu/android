package fptu.ninhtbm.thebookshop.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import fptu.ninhtbm.thebookshop.R;
import fptu.ninhtbm.thebookshop.ui.models.Account;

public class FirestoreActivity extends AppCompatActivity {
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firestore);

        db = FirebaseFirestore.getInstance();
    }

    public void onTestFirestore(View view) {
        Toast.makeText(this, "work", Toast.LENGTH_SHORT).show();
        testFirestore();
    }

    private void testFirestore() {
        DocumentReference docRef = db.collection("Account").document("5jDFIM3oQMmfsQIWFBOa");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Account author = documentSnapshot.toObject(Account.class);
                Log.i("Firestore", author.getUsername());
                Log.i("Firestore", documentSnapshot.getId());
                Log.i("Firestore", author.getPassword());
                Log.i("Firestore", author.getCreatedAt().toString());
                Log.i("Firestore", author.isStatus() + "");
            }
        });
    }
}