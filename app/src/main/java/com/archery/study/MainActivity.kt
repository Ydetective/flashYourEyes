package com.archery.study

import android.graphics.Color
import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import android.widget.TextView
import com.talkingdata.study.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() , View.OnClickListener{
    val content: Array<String> = arrayOf("临", "兵", "斗", "者", "皆", "阵", "列", "在", "前")
    lateinit var typeface : Typeface
    lateinit var handler: MyHandler
    var isPlaying = false

    class MyHandler : Handler(){
        override fun handleMessage(msg: Message?) {
            //do something
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initConf()
        initView()
    }

    private fun initConf(){
        handler = MyHandler()
    }

    private fun initView(){
        typeface = Typeface.createFromAsset(assets, "test.ttf")
        btn_play.setOnClickListener(this)
        for(text in content){
            val view = TextView(this)
            val lp = RelativeLayout.LayoutParams(ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))
            view.layoutParams = lp
            view.text = text
            view.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
            view.textSize = 400f
            view.typeface = typeface
            view.setTextColor(Color.BLACK)
            view.visibility = View.INVISIBLE
            rl_show.addView(view)
        }
    }



    private fun loadAnimations(view: View){
        if(view.animation == null){
            val alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.test_anim)
            val scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.test_scale)
            val animationSet = AnimationSet(true)
            animationSet.duration = 500
            animationSet.addAnimation(alphaAnimation)
            animationSet.addAnimation(scaleAnimation)

            view.animation = animationSet
        }

        view.animation.start()
        view.animation.fillAfter = true
    }


    override fun onClick(v: View?) {
        if(v != null && v.id == R.id.btn_play){
            if(!isPlaying){
                isPlaying = true
                Log.i("TAG", "Ready to play animation")
                playAnimation()
            }
        }
    }

    private fun playAnimation(){
        var i = 0
        handler.postDelayed(object: Runnable{
            override fun run() {
                val child = rl_show.getChildAt(i)
                child.visibility = View.VISIBLE
                loadAnimations(child)
                if(++i < rl_show.childCount){
                    handler.postDelayed(this, 300)
                }else{
                    resetAnimation()
                    isPlaying = false
                }
            }
        }, 300)
    }


    private fun resetAnimation(){
        Log.i("TAG", "Ready to resetAnimation")
        var i = 0
        while (i < rl_show.childCount){
            val child = rl_show.getChildAt(i)
            child.animation.reset()
            child.invalidate()
            child.visibility = View.INVISIBLE
            i++
        }
    }


    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}
