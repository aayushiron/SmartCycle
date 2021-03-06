package com.clarifai.android.starter.api.v2.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import butterknife.BindView;
import butterknife.OnClick;
import clarifai2.api.ClarifaiResponse;
import clarifai2.dto.input.ClarifaiImage;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.model.ConceptModel;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;
import com.clarifai.android.starter.api.v2.App;
import com.clarifai.android.starter.api.v2.ClarifaiUtil;
import com.clarifai.android.starter.api.v2.IntermediateryRecycle;
import com.clarifai.android.starter.api.v2.IntermediateryTrash;
import com.clarifai.android.starter.api.v2.R;
import com.clarifai.android.starter.api.v2.adapter.RecognizeConceptsAdapter;

import org.w3c.dom.Text;

import java.util.Collections;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public final class RecognizeConceptsActivity extends BaseActivity {

  public static final int PICK_IMAGE = 100;
  public static final String EXTRA_MESSAGE = "oof";

  // the list of results that were returned from the API
  @BindView(R.id.resultsList) RecyclerView resultsList;

  // the view where the image the user selected is displayed
  @BindView(R.id.image) ImageView imageView;

  // switches between the text prompting the user to hit the FAB, and the loading spinner
  @BindView(R.id.switcher) ViewSwitcher switcher;

  // the FAB that the user clicks to select an image
  @BindView(R.id.fab) View fab;

  @NonNull private final RecognizeConceptsAdapter adapter = new RecognizeConceptsAdapter();

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Button btnOld = (Button)findViewById(R.id.btnProcess);


  }

  @Override protected void onStart() {
    super.onStart();

    resultsList.setLayoutManager(new LinearLayoutManager(this));
    resultsList.setAdapter(adapter);
  }

  @OnClick(R.id.fab)
  void pickImage() {
    startActivityForResult(new Intent(Intent.ACTION_PICK).setType("image/*"), PICK_IMAGE);
    TextView t = (TextView)findViewById(R.id.app);
    t.setText("");


  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode != RESULT_OK) {
      return;
    }
    switch(requestCode) {
      case PICK_IMAGE:
        final byte[] imageBytes = ClarifaiUtil.retrieveSelectedImage(this, data);
        if (imageBytes != null) {
          onImagePicked(imageBytes);
        }
        break;
    }
  }

  private void onImagePicked(@NonNull final byte[] imageBytes) {
    // Now we will upload our image to the Clarifai API
    setBusy(true);

    // Make sure we don't show a list of old concepts while the image is being uploaded
    adapter.setData(Collections.<Concept>emptyList());

    new AsyncTask<Void, Void, ClarifaiResponse<List<ClarifaiOutput<Concept>>>>() {
      @Override protected ClarifaiResponse<List<ClarifaiOutput<Concept>>> doInBackground(Void... params) {
        // The default Clarifai model that identifies concepts in images
        final ConceptModel generalModel = App.get().clarifaiClient().getDefaultModels().generalModel();

        // Use this model to predict, with the image that the user just selected as the input
        return generalModel.predict()
            .withInputs(ClarifaiInput.forImage(ClarifaiImage.of(imageBytes)))
            .executeSync();
      }

      @Override protected void onPostExecute(ClarifaiResponse<List<ClarifaiOutput<Concept>>> response) {
        setBusy(false);
        if (!response.isSuccessful()) {
          showErrorSnackbar(R.string.error_while_contacting_api);
          return;
        }
        final List<ClarifaiOutput<Concept>> predictions = response.get();
        if (predictions.isEmpty()) {
          showErrorSnackbar(R.string.no_results_from_api);
          return;
        }
        ClarifaiOutput<Concept> clarifaiOutput = predictions.get(0);
        List<Concept> concepts = clarifaiOutput.data();

        final String m = concepts.get(0).name();
        Button btn = (Button)findViewById(R.id.btnProcess);
        for (int i = 0; i < concepts.size(); i++) {
          if (concepts.get(i).name().equals("plastic") || concepts.get(i).name().equals("bottle")) {
            Intent bois = new Intent(getApplicationContext(), IntermediateryRecycle.class);
            startActivity(bois);
          } else {
            Intent bois = new Intent(getApplicationContext(), IntermediateryTrash.class);
            startActivity(bois);
          }
        }
        //btn.setText("Proceed to Allergies");
        /*btn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("https://www.google.com/maps/dir/37.3875089,-121.9639485/J+%26+B+Enterprises,+1650+Russell+Ave,+Santa+Clara,+CA+95054/@37.3873844,-121.9626202,17z/data=!3m1!4b1!4m16!1m6!3m5!1s0x0:0xab871ec150ff24d8!2sJ+%26+B+Enterprises!8m2!3d37.3868418!4d-121.9566804!4m8!1m1!4e1!1m5!1m1!1s0x808fc99ace703ce5:0xab871ec150ff24d8!2m2!1d-121.956681!2d37.38684"));
            startActivity(intent);

          }
        });*/
        adapter.setData(predictions.get(0).data());
        imageView.setImageBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));
      }

      private void showErrorSnackbar(@StringRes int errorString) {
        Snackbar.make(
            root,
            errorString,
            Snackbar.LENGTH_INDEFINITE
        ).show();
      }
    }.execute();
  }


  @Override protected int layoutRes() { return R.layout.activity_recognize; }

  private void setBusy(final boolean busy) {
    runOnUiThread(new Runnable() {
      @Override public void run() {
        switcher.setDisplayedChild(busy ? 1 : 0);
        imageView.setVisibility(busy ? GONE : VISIBLE);
        fab.setEnabled(!busy);
      }
    });
  }

}
