package com.example.android.mycameraapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CameraFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CameraFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CameraFragment extends Fragment implements View.OnClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

    /*
        Converts screen rotation to JPEG orientation
     */

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final String FRAGMENT_DIALOG = "dialog";

    // This allows us to put data into our static array variable ORIENTATIONS
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);

    }

    /**
     * Tag for the {@link Log}
     */
    private static final String TAG = "CameraFragment";

    /**
     * Camera state that shows Camera Preview
     */
    private static final int STATE_PREVIEW = 0;


    /**
     * Camera state: wait for focus to be locked
     */
    private static final int STATE_WAITING_LOCK = 1;

    /**
     * Camera state: wait for exposure to be precapture state
     */
    private static final int STATE_WAITING_PRECAPTURE = 2;

    /**
     * Camera state: wait for exposure to be something other than precapture
     */
    private static final int STATE_WAITING_NON_PRECAPTURE = 3;

    /**
     * Camera state: Picture was taken
     */
    private static final int STATE_PICTURE_TAKEN = 4;

    /**
     * Max preview width
     */
    private static final int MAX_PREVIEW_WIDTH = 1920;

    /**
     * Max preview height
     */
    private static final int MAX_PREVIEW_HEIGHT = 1080;

    /**
     * TextureView.SurfaceTextureListener is an interface that helps
     * listen for when the SurfaceTexture is available needed for TextureView
     * and helps render the view for our Camera
     */
    private final TextureView.SurfaceTextureListener mSurfaceTextureListener =
            new TextureView.SurfaceTextureListener() {
                @Override
                public void onSurfaceTextureAvailable(SurfaceTexture texture, int width, int height) {
                    openCamera(width, height);

                }

                @Override
                public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {
                    configureTransform(width, height);
                }

                @Override
                public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                    return true;
                }

                @Override
                public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
                }
            };

    private void requestCameraPermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            new ConfirmationDialog().show(getChildFragmentManager(), FRAGMENT_DIALOG);
        } else {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
    }

    /**
     * Opens the camera specified by the CameraFragment
     *
     * @param width
     * @param height
     */

    private void openCamera(int width, int height) {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
            requestCameraPermission();
            return;
        }

    }

    /**
     * Configures the necessary {@link android.graphics.Matrix} transformation to 'mTextureView'.
     * This method should be called after the camera preview size is determined in
     * setUpCameraOutputs and also the size of 'mTextureView' is fixed.
     */

    private void configureTransform(int viewWidth, int viewHeight){

    }

    /**
     * shows the OK/CANCEL Confirmation Dialog for Camera Permission
     */

    public static class ConfirmationDialog extends DialogFragment {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Fragment parent = getParentFragment();
            return new AlertDialog.Builder(getActivity())
                    .setMessage(R.string.request_permission)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            parent.requestPermissions(new String[]{Manifest.permission.CAMERA},
                                    REQUEST_CAMERA_PERMISSION);
                        }
                    })

                    .setNegativeButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            Activity activity = parent.getActivity();
                            if (activity != null) {
                                activity.finish();
                            }

                        }
                    })
                    .create();
        }
    }

}