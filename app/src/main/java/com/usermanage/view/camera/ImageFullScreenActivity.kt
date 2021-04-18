package com.usermanage.view.camera

import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.usermanage.R
import com.usermanage.TransparentStatusBar
import com.usermanage.dao.user.UserDatabase
import com.usermanage.view.main.MainActivity
import com.usermanage.viewModel.dataUser.GetUid
import kotlinx.android.synthetic.main.activity_image_full_screen.*


class ImageFullScreenActivity : AppCompatActivity() {
    private lateinit var mViewModel: ImageFullScreenActivityViewModel
    private lateinit var uriIntent: String
    private lateinit var bitmap: Bitmap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_full_screen)
        initUi()
        initData()
    }

    private fun initData() {
        uriIntent = intent.getStringExtra("uri").toString()
        val uri: Uri = Uri.parse(uriIntent)
        bitmap = mViewModel.getBitmapFromUri(uri)!!
        Glide.with(this).load(bitmap).into(im_avatar)
    }

    private fun initUi() {
        TransparentStatusBar(this)
        mViewModel = ViewModelProvider(this).get(ImageFullScreenActivityViewModel::class.java)
        mViewModel.context.value = this
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mViewModel.hideProgressBar.observe(this, Observer {
            if (it) {
                progress_bar.visibility = View.GONE
            }
        })
        bt_cancle.setOnClickListener { finish() }
        mViewModel.changeAvatar.observe(this, Observer {
            if (it == "Insert Success") {
                Toast.makeText(this, "Thay đổi ảnh thành công", Toast.LENGTH_LONG).show()
                val userDatabase = UserDatabase.getInsance(this)
                userDatabase.daoQuery().updateAvatar(uriIntent)
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Lỗi $it", Toast.LENGTH_LONG).show()
            }
        })
        val uid = GetUid.getInstance().get(this)
        bt_accept.setOnClickListener {
            progress_bar.visibility = View.VISIBLE
            val thread = Thread {
                mViewModel.updateImageDB(bitmap!!, uid)
            }
            thread.start()
        }
    }
}