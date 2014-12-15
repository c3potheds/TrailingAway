package cmsc434.trailingaway;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LandmarkFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LandmarkFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class LandmarkFragment extends Fragment {

    private OnFragmentInteractionListener _listener;

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    public static LandmarkFragment newInstance() {
        LandmarkFragment fragment = new LandmarkFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    public LandmarkFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_landmark, container, false);
        Button save = (Button) v.findViewById(R.id.buttonAddLandmarkSave);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddLandmarkSaveClick(view);
            }
        });
        Button cancel = (Button) v.findViewById(R.id.buttonAddLandmarkCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddLandmarkCancelClick(view);
            }
        });
        Button photo = (Button)v.findViewById(R.id.buttonAddPhoto);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddPhotoClick(view);
            }
        });
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            _listener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        _listener = null;
    }

    public void onAddPhotoClick(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView photoView = (ImageView)getActivity().findViewById(R.id.imageViewPhoto);
            photoView.setImageBitmap(imageBitmap);
        }

    }

    public void onAddLandmarkSaveClick(View view) {
        String name = ((EditText)getActivity().findViewById(
                R.id.editTextName)).getText().toString();
        String description = ((EditText)getActivity().findViewById(
                R.id.editTextDescription)).getText().toString();
        Location location = ((MapView) getActivity().findViewById(R.id.map))
                .getMap().getMyLocation();
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        ImageView viewPhoto = ((ImageView)getActivity().findViewById(R.id.imageViewPhoto));
        viewPhoto.buildDrawingCache();
        Bitmap photo = viewPhoto.getDrawingCache();


        Landmark newLandmark = new Landmark(name, description, photo, latLng);
        _listener.onLandmarkCreated(newLandmark);

        Animation bottomDown = AnimationUtils.loadAnimation(getActivity().getBaseContext(),
                R.anim.bottom_down);
        View hiddenPanel = getActivity().findViewById(R.id.layoutLandmarkPanel);
        hiddenPanel.startAnimation(bottomDown);
        hiddenPanel.setVisibility(View.GONE);

    }

    public void onAddLandmarkCancelClick(View view) {
        Animation bottomDown = AnimationUtils.loadAnimation(getActivity().getBaseContext(),
                R.anim.bottom_down);
        View hiddenPanel = getActivity().findViewById(R.id.layoutLandmarkPanel);
        hiddenPanel.startAnimation(bottomDown);
        hiddenPanel.setVisibility(View.GONE);
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onLandmarkCreated(Landmark landmark);
    }

}
