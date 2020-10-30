package com.example.simcareer.ui.gallery;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.simcareer.R;
import com.example.simcareer.bean.DbManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;

public class PhotoDialogActivity extends AppCompatDialogFragment {
    ImageView imagePic;
    int imageId;
    FloatingActionButton fabClose;
    FloatingActionButton fabShare;
    View root;
    Dialog dialog;
    int REQUEST_CODE_MY_PICK = 1;

    public PhotoDialogActivity(int imageId) {
        this.imageId = imageId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_gallery_full_photo, container, false);
        imagePic = root.findViewById(R.id.image_full_photo);
        fabClose = root.findViewById(R.id.fab_close);
        fabShare = root.findViewById(R.id.fab_share);
        imagePic.setImageResource(imageId);
        fabClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Uri uri = DbManager.saveDrawableToFiles(requireContext(), imageId, "toShare.png");
                    Intent resultIntent = new Intent("com.example.simcareer.ACTION_RETURN_FILE");
                    resultIntent.addFlags(
                            Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    resultIntent.setDataAndType(uri,
                            getActivity().getContentResolver().getType(uri));
                    doSocialShare(uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return root;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }


    public void doSocialShare(Uri uri) {
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("image/png");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "Share image"));
    }

}