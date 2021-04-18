package com.usermanage.view.camera

import android.content.Context
import android.graphics.Bitmap
import android.media.ExifInterface
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.usermanage.ConvertUriToBitmapChangedRotation
import com.usermanage.viewModel.dataUser.UpdateAvatar

class ImageFullScreenActivityViewModel : ViewModel() {

    private val firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()
    private val mFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    val context: MutableLiveData<Context> = MutableLiveData()
    val hideProgressBar: MutableLiveData<Boolean> = MutableLiveData(false)
    val changeAvatar: MutableLiveData<String> = MutableLiveData()
    fun getBitmapFromUri(uri: Uri?): Bitmap? {
        val bitmap = ConvertUriToBitmapChangedRotation().getBitmapFromUri(uri, context.getValue())
        return bitmap
    }

    fun updateImageDB(bitmap: Bitmap, uid: String) {
        UpdateAvatar.getInstance().updateAvatarUser(firebaseStorage, bitmap, mFirestore, uid, object : UpdateAvatar.IUpdateAvatar {
            override fun onSuccess(success: String) {
                hideProgressBar.value = true
                changeAvatar.value = success
            }

            override fun onFail(fail: String) {
                Log.d("failChangeImage", "onFail: $fail")
                changeAvatar.value = fail
            }
        })
    }
}