package com.tbg.yamoov;

import com.nightonke.boommenu.BoomButtons.HamButton;

class BuilderManager {

    private static int[] imageResources = new int[]{
            R.drawable.ic_pages_black_24dp,
            R.drawable.ic_perm_device_information_black_24dp,
            R.drawable.ic_insert_invitation_black_24dp,
            R.drawable.ic_person_outline_black_24dp,
            R.drawable.ic_save_black_24dp
    };

    private static int imageResourceIndex = 0;

    private static int getImageResource() {
        if (imageResourceIndex >= imageResources.length) imageResourceIndex = 0;
        return imageResources[imageResourceIndex++];
    }

    static HamButton.Builder getHamButtonBuilder(String text, String subText) {
        return new HamButton.Builder()
                .normalImageRes(getImageResource())
                .normalText(text)
                .subNormalText(subText);
    }

}
