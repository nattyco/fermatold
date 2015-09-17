package com.bitdubai.sub_app.wallet_store.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_store.enums.InstallationStatus;
import com.wallet_store.bitdubai.R;

import java.util.ArrayList;
import java.util.List;

import static com.bitdubai.fermat_api.layer.dmp_middleware.wallet_store.enums.InstallationStatus.INSTALLED;
import static com.bitdubai.fermat_api.layer.dmp_middleware.wallet_store.enums.InstallationStatus.NOT_UNINSTALLED;
import static com.bitdubai.fermat_api.layer.dmp_middleware.wallet_store.enums.InstallationStatus.UPGRADE_AVAILABLE;

/**
 * Created by nelson on 28/08/15.
 */
public enum UtilsFuncs {
    INSTANCE;

    public int getInstallationStatusStringResource(InstallationStatus installationStatus) {
        if (installationStatus == INSTALLED || installationStatus == NOT_UNINSTALLED)
            return R.string.wallet_status_installed;
        if (installationStatus == UPGRADE_AVAILABLE)
            return R.string.wallet_status_upgrade;

        return R.string.wallet_status_install;
    }

    public ArrayList<Bitmap> getBitmapList(List<byte[]> byteArrayList) {
        ArrayList<Bitmap> bitmapImageList = null;

        if (byteArrayList != null) {
            bitmapImageList = new ArrayList<>();

            for (int i = 0; i < byteArrayList.size(); i++) {
                byte[] previewImgBytes = byteArrayList.get(i);
                Bitmap img = BitmapFactory.decodeByteArray(previewImgBytes, 0, previewImgBytes.length);
                bitmapImageList.add(img);
            }
        }

        return bitmapImageList;
    }
}